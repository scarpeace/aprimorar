#!/usr/bin/env python3
"""Local sprint synchronizer for GitHub issues and Projects.

Workflow:
1) Parse sprint.md and extract exactly one fenced JSON block.
2) Validate schema and normalize data.
3) validate: local schema checks only.
4) sync: strict checks + issue upsert + project field sync.

Notes:
- Strict mode (always on): missing labels/fields/options fail fast.
- Source of truth: when ticket_key already exists, issue is overwritten.
"""

from __future__ import annotations

import argparse
import json
import re
import subprocess
import sys
from dataclasses import dataclass
from datetime import date
from pathlib import Path
from typing import Any


DEFAULT_FIELD_MAP: dict[str, str] = {
    "status": "Status",
    "priority": "Priority",
    "iteration": "Iteration",
    "roadmap": "Roadmap",
    "estimate": "Estimate",
    "target_date": "Target date",
}

SCALAR_KEYS = {
    "status",
    "priority",
    "iteration",
    "roadmap",
    "estimate",
    "target_date",
}

MARKER_TEMPLATE = "<!-- ticket_key:{ticket_key} -->"


class ValidationError(Exception):
    def __init__(self, errors: list[str]) -> None:
        super().__init__("\n".join(errors))
        self.errors = errors


class SyncError(Exception):
    pass


class GhCommandError(Exception):
    def __init__(
        self, cmd: list[str], stdout: str, stderr: str, returncode: int
    ) -> None:
        self.cmd = cmd
        self.stdout = stdout
        self.stderr = stderr
        self.returncode = returncode
        message = stderr.strip() or stdout.strip() or "unknown gh error"
        super().__init__(message)


@dataclass
class FieldMeta:
    field_id: str
    name: str
    kind: str
    options: dict[str, str]
    iterations: dict[str, str]
    iteration_labels: list[str]


@dataclass
class PreflightItem:
    item: dict[str, Any]
    effective: dict[str, Any]
    existing_issue: dict[str, Any] | None
    project_updates: list[dict[str, Any]]


def run_gh(args: list[str], input_text: str | None = None) -> str:
    cmd = ["gh", *args]
    proc = subprocess.run(
        cmd,
        input=input_text,
        text=True,
        capture_output=True,
        check=False,
    )
    if proc.returncode != 0:
        raise GhCommandError(
            cmd=cmd, stdout=proc.stdout, stderr=proc.stderr, returncode=proc.returncode
        )
    return proc.stdout


def run_gh_json(args: list[str], input_text: str | None = None) -> Any:
    raw = run_gh(args, input_text=input_text)
    try:
        return json.loads(raw)
    except json.JSONDecodeError as exc:
        raise SyncError(f"Falha ao ler JSON do gh ({' '.join(args)}): {exc}") from exc


def run_graphql(query: str, variables: dict[str, Any]) -> dict[str, Any]:
    args: list[str] = ["api", "graphql", "-f", f"query={query}"]
    for key, value in variables.items():
        if isinstance(value, int):
            args.extend(["-F", f"{key}={value}"])
        else:
            args.extend(["-f", f"{key}={value}"])

    payload = run_gh_json(args)
    if isinstance(payload, dict) and payload.get("errors"):
        raise SyncError(
            f"Erro GraphQL: {json.dumps(payload['errors'], ensure_ascii=True)}"
        )
    if not isinstance(payload, dict):
        raise SyncError("Resposta GraphQL invalida")
    return payload


def extract_json_block(markdown_text: str) -> dict[str, Any]:
    blocks = re.findall(
        r"```json\s*(.*?)\s*```", markdown_text, flags=re.IGNORECASE | re.DOTALL
    )
    if not blocks:
        raise ValidationError(
            [
                "Nao foi encontrado bloco JSON. Adicione um bloco com ```json ... ``` em sprint.md"
            ]
        )
    if len(blocks) > 1:
        raise ValidationError(
            [
                "Foram encontrados multiplos blocos JSON. Mantenha apenas um bloco em sprint.md"
            ]
        )

    block = blocks[0]
    try:
        parsed = json.loads(block)
    except json.JSONDecodeError as exc:
        raise ValidationError([f"JSON invalido no bloco de sprint.md: {exc}"]) from exc

    if not isinstance(parsed, dict):
        raise ValidationError(["O bloco JSON precisa ser um objeto no nivel raiz"])
    return parsed


def _as_non_empty_string(
    value: Any, path: str, errors: list[str], allow_empty: bool = False
) -> str:
    if not isinstance(value, str):
        errors.append(f"{path} deve ser string")
        return ""
    clean = value.strip()
    if not clean and not allow_empty:
        errors.append(f"{path} nao pode ser vazio")
    return clean


def _as_string_list(value: Any, path: str, errors: list[str]) -> list[str]:
    if value is None:
        return []
    if not isinstance(value, list):
        errors.append(f"{path} deve ser lista de strings")
        return []

    out: list[str] = []
    for idx, item in enumerate(value, start=1):
        if not isinstance(item, str):
            errors.append(f"{path}[{idx}] deve ser string")
            continue
        token = item.strip()
        if not token:
            errors.append(f"{path}[{idx}] nao pode ser vazio")
            continue
        out.append(token)
    return unique_preserve_order(out)


def _as_dict(value: Any, path: str, errors: list[str]) -> dict[str, Any]:
    if value is None:
        return {}
    if not isinstance(value, dict):
        errors.append(f"{path} deve ser objeto")
        return {}
    return value


def unique_preserve_order(values: list[str]) -> list[str]:
    seen: set[str] = set()
    out: list[str] = []
    for value in values:
        if value in seen:
            continue
        seen.add(value)
        out.append(value)
    return out


def is_iso_date(value: str) -> bool:
    try:
        date.fromisoformat(value)
        return True
    except ValueError:
        return False


def normalize_spec(raw: dict[str, Any]) -> dict[str, Any]:
    errors: list[str] = []

    version = raw.get("version", "1.0")
    if not isinstance(version, str):
        errors.append("version deve ser string")
        version = "1.0"
    version = version.strip() or "1.0"

    repo = _as_non_empty_string(raw.get("repo"), "repo", errors)

    project = _as_dict(raw.get("project"), "project", errors)
    owner = _as_non_empty_string(project.get("owner"), "project.owner", errors)
    number_raw = project.get("number")
    project_number = -1
    if isinstance(number_raw, int):
        project_number = number_raw
    elif isinstance(number_raw, str) and number_raw.strip().isdigit():
        project_number = int(number_raw.strip())
    else:
        errors.append("project.number deve ser inteiro")

    sprint = _as_dict(raw.get("sprint"), "sprint", errors)
    sprint_key = _as_non_empty_string(sprint.get("key"), "sprint.key", errors)
    sprint_name = _as_non_empty_string(sprint.get("name"), "sprint.name", errors)
    sprint_start = sprint.get("start")
    sprint_end = sprint.get("end")
    if sprint_start is not None:
        sprint_start = _as_non_empty_string(sprint_start, "sprint.start", errors)
        if sprint_start and not is_iso_date(sprint_start):
            errors.append("sprint.start deve estar em formato YYYY-MM-DD")
    if sprint_end is not None:
        sprint_end = _as_non_empty_string(sprint_end, "sprint.end", errors)
        if sprint_end and not is_iso_date(sprint_end):
            errors.append("sprint.end deve estar em formato YYYY-MM-DD")

    defaults = _as_dict(raw.get("defaults"), "defaults", errors)
    defaults_labels = _as_string_list(
        defaults.get("labels", []), "defaults.labels", errors
    )
    defaults_assignees = _as_string_list(
        defaults.get("assignees", []), "defaults.assignees", errors
    )

    normalized_defaults: dict[str, Any] = {
        "labels": defaults_labels,
        "assignees": defaults_assignees,
    }
    for key in SCALAR_KEYS:
        value = defaults.get(key)
        if value is None:
            continue
        if isinstance(value, str):
            value = value.strip()
            if not value:
                value = None
        normalized_defaults[key] = value

    for key, value in defaults.items():
        if key in normalized_defaults or key in {"labels", "assignees"}:
            continue
        if isinstance(value, str):
            value = value.strip()
            if not value:
                value = None
        normalized_defaults[key] = value

    field_map = dict(DEFAULT_FIELD_MAP)

    top_field_map = _as_dict(raw.get("field_map"), "field_map", errors)
    project_field_map = _as_dict(project.get("field_map"), "project.field_map", errors)

    for source_name, source_map in (
        ("field_map", top_field_map),
        ("project.field_map", project_field_map),
    ):
        for key, value in source_map.items():
            if not isinstance(key, str) or not key.strip():
                errors.append(
                    f"{source_name}: todas as chaves devem ser strings nao vazias"
                )
                continue
            if not isinstance(value, str) or not value.strip():
                errors.append(
                    f"{source_name}.{key} deve apontar para nome de campo nao vazio"
                )
                continue
            field_map[key.strip()] = value.strip()

    items_raw = raw.get("items")
    if not isinstance(items_raw, list):
        errors.append("items deve ser lista")
        items_raw = []

    normalized_items: list[dict[str, Any]] = []
    seen_ticket_keys: set[str] = set()

    for idx, original_item in enumerate(items_raw, start=1):
        path = f"items[{idx}]"
        if not isinstance(original_item, dict):
            errors.append(f"{path} deve ser objeto")
            continue

        item = dict(original_item)

        ticket_key = _as_non_empty_string(
            item.get("ticket_key"), f"{path}.ticket_key", errors
        )
        if ticket_key:
            if ticket_key in seen_ticket_keys:
                errors.append(f"ticket_key duplicado: {ticket_key}")
            seen_ticket_keys.add(ticket_key)
            item["ticket_key"] = ticket_key

        item_type = _as_non_empty_string(item.get("type"), f"{path}.type", errors)
        if item_type:
            item["type"] = item_type.lower()

        title = _as_non_empty_string(item.get("title"), f"{path}.title", errors)
        if title:
            item["title"] = title

        if "summary" in item and item["summary"] is not None:
            item["summary"] = _as_non_empty_string(
                item["summary"], f"{path}.summary", errors, allow_empty=True
            )
        else:
            item["summary"] = ""

        if "body" in item and item["body"] is not None:
            item["body"] = _as_non_empty_string(
                item["body"], f"{path}.body", errors, allow_empty=True
            )
        else:
            item["body"] = ""

        if not item["summary"] and not item["body"]:
            errors.append(f"{path} precisa de summary ou body")

        item["labels"] = _as_string_list(
            item.get("labels", []), f"{path}.labels", errors
        )
        item["assignees"] = _as_string_list(
            item.get("assignees", []), f"{path}.assignees", errors
        )

        acceptance = item.get("acceptance_criteria", [])
        item["acceptance_criteria"] = _as_string_list(
            acceptance, f"{path}.acceptance_criteria", errors
        )

        if "target_date" in item and item["target_date"] is not None:
            if not isinstance(item["target_date"], str):
                errors.append(f"{path}.target_date deve ser string YYYY-MM-DD")
            else:
                item["target_date"] = item["target_date"].strip()
                if item["target_date"] and not is_iso_date(item["target_date"]):
                    errors.append(
                        f"{path}.target_date deve estar em formato YYYY-MM-DD"
                    )

        estimate = item.get("estimate")
        if estimate is not None and not isinstance(estimate, (int, float, str)):
            errors.append(f"{path}.estimate deve ser numero ou string numerica")

        for key in ("status", "priority", "iteration", "roadmap"):
            if key not in item or item[key] is None:
                continue
            if not isinstance(item[key], str):
                errors.append(f"{path}.{key} deve ser string")
                continue
            item[key] = item[key].strip()

        normalized_items.append(item)

    if not normalized_items:
        errors.append("items nao pode ser vazio")

    if errors:
        raise ValidationError(errors)

    return {
        "version": version,
        "repo": repo,
        "project": {
            "owner": owner,
            "number": project_number,
        },
        "sprint": {
            "key": sprint_key,
            "name": sprint_name,
            "start": sprint_start,
            "end": sprint_end,
        },
        "defaults": normalized_defaults,
        "field_map": field_map,
        "items": normalized_items,
    }


def read_spec_from_file(file_path: Path) -> dict[str, Any]:
    if not file_path.exists():
        raise ValidationError([f"Arquivo nao encontrado: {file_path}"])
    if not file_path.is_file():
        raise ValidationError([f"Caminho invalido (nao e arquivo): {file_path}"])

    markdown = file_path.read_text(encoding="utf-8")
    raw = extract_json_block(markdown)
    return normalize_spec(raw)


def parse_scopes_from_auth_status(auth_status_output: str) -> set[str]:
    match = re.search(r"Token scopes:\s*(.+)", auth_status_output)
    if not match:
        return set()

    raw = match.group(1).strip()
    parts = [token.strip().strip("'\"") for token in raw.split(",")]
    return {token for token in parts if token}


def ensure_scopes(apply_mode: bool) -> None:
    output = run_gh(["auth", "status"])
    scopes = parse_scopes_from_auth_status(output)

    required = {"repo", "read:project"}
    if apply_mode:
        required.add("project")

    missing = sorted(required - scopes)
    if missing:
        refresh_parts = []
        for scope in missing:
            refresh_parts.append(f"-s {scope}")
        refresh_cmd = "gh auth refresh " + " ".join(refresh_parts)
        missing_text = ", ".join(missing)
        raise SyncError(
            f"Token do gh sem escopos obrigatorios: {missing_text}. Execute: {refresh_cmd}"
        )


def get_current_login() -> str:
    payload = run_gh(["api", "user", "--jq", ".login"]).strip()
    if not payload:
        raise SyncError("Nao foi possivel descobrir usuario atual no GitHub")
    return payload


def normalize_assignee(value: str, me_login: str) -> str:
    token = value.strip()
    if token == "@me":
        return me_login
    if token.startswith("@"):
        token = token[1:]
    if not token:
        raise SyncError("Assignee invalido (vazio)")
    return token


def merge_item_with_defaults(
    item: dict[str, Any], defaults: dict[str, Any], me_login: str
) -> dict[str, Any]:
    merged = dict(item)

    merged_labels = unique_preserve_order(
        [*defaults.get("labels", []), *item.get("labels", [])]
    )
    merged_assignees_raw = unique_preserve_order(
        [*defaults.get("assignees", []), *item.get("assignees", [])]
    )
    merged_assignees = [
        normalize_assignee(value, me_login) for value in merged_assignees_raw
    ]

    merged["labels"] = merged_labels
    merged["assignees"] = merged_assignees

    for key in SCALAR_KEYS:
        if merged.get(key) is None:
            merged[key] = defaults.get(key)

    merged["body"] = build_issue_body(merged)
    return merged


def build_issue_body(item: dict[str, Any]) -> str:
    ticket_key = item["ticket_key"]
    marker = MARKER_TEMPLATE.format(ticket_key=ticket_key)

    body_from_input = item.get("body", "")
    if isinstance(body_from_input, str) and body_from_input.strip():
        body = body_from_input.strip()
    else:
        body = build_body_from_template(item)

    if marker in body:
        clean = body
    else:
        clean = body.rstrip() + "\n\n" + marker
    return clean


def build_body_from_template(item: dict[str, Any]) -> str:
    ticket_type = str(item.get("type", "task")).lower()
    summary = str(item.get("summary", "")).strip()
    acceptance = item.get("acceptance_criteria", [])
    acceptance_lines = (
        "\n".join([f"- [ ] {line}" for line in acceptance])
        or "- [ ] Definir criterio de aceite"
    )

    if ticket_type == "bug":
        return (
            "## Resumo\n"
            f"{summary or 'Descrever problema observado'}\n\n"
            "## Comportamento atual\n"
            "Descrever o comportamento atual.\n\n"
            "## Comportamento esperado\n"
            "Descrever o comportamento esperado.\n\n"
            "## Passos para reproduzir\n"
            "1. ...\n2. ...\n3. ...\n\n"
            "## Criterios de pronto\n"
            f"{acceptance_lines}"
        )

    if ticket_type == "chore":
        return (
            "## Objetivo tecnico\n"
            f"{summary or 'Descrever objetivo tecnico'}\n\n"
            "## Escopo\n"
            "Descrever escopo tecnico.\n\n"
            "## Riscos\n"
            "Listar riscos tecnicos conhecidos.\n\n"
            "## Definicao de pronto\n"
            f"{acceptance_lines}"
        )

    return (
        "## Problema\n"
        f"{summary or 'Descrever problema de negocio'}\n\n"
        "## Proposta\n"
        "Descrever proposta de implementacao.\n\n"
        "## Criterios de aceitacao\n"
        f"{acceptance_lines}\n\n"
        "## Fora de escopo\n"
        "Listar itens que nao fazem parte deste ticket."
    )


def get_repo_labels(repo: str) -> set[str]:
    labels = run_gh_json(
        ["label", "list", "--repo", repo, "--limit", "1000", "--json", "name"]
    )
    if not isinstance(labels, list):
        raise SyncError("Resposta invalida ao listar labels")

    names: set[str] = set()
    for label in labels:
        if isinstance(label, dict) and isinstance(label.get("name"), str):
            names.add(label["name"])
    return names


def ensure_assignees_assignable(repo: str, assignees: set[str]) -> None:
    invalid: list[str] = []
    for login in sorted(assignees):
        try:
            run_gh(["api", f"repos/{repo}/assignees/{login}"])
        except GhCommandError:
            invalid.append(login)

    if invalid:
        raise SyncError("Assignees sem permissao no repositorio: " + ", ".join(invalid))


PROJECT_QUERY = """
query($owner: String!, $number: Int!) {
  user(login: $owner) {
    projectV2(number: $number) {
      id
      title
      fields(first: 100) {
        nodes {
          __typename
          ... on ProjectV2Field {
            id
            name
          }
          ... on ProjectV2SingleSelectField {
            id
            name
            options {
              id
              name
            }
          }
          ... on ProjectV2IterationField {
            id
            name
            configuration {
              iterations {
                id
                title
                startDate
              }
            }
          }
        }
      }
    }
  }
  organization(login: $owner) {
    projectV2(number: $number) {
      id
      title
      fields(first: 100) {
        nodes {
          __typename
          ... on ProjectV2Field {
            id
            name
          }
          ... on ProjectV2SingleSelectField {
            id
            name
            options {
              id
              name
            }
          }
          ... on ProjectV2IterationField {
            id
            name
            configuration {
              iterations {
                id
                title
                startDate
              }
            }
          }
        }
      }
    }
  }
}
"""


def get_project_metadata(owner: str, number: int) -> tuple[str, dict[str, FieldMeta]]:
    payload = run_graphql(PROJECT_QUERY, {"owner": owner, "number": number})
    data = payload.get("data")
    if not isinstance(data, dict):
        raise SyncError("Resposta GraphQL sem campo data")

    project = None
    user_block = data.get("user")
    if isinstance(user_block, dict):
        project = user_block.get("projectV2")

    if project is None:
        org_block = data.get("organization")
        if isinstance(org_block, dict):
            project = org_block.get("projectV2")

    if not isinstance(project, dict):
        raise SyncError(
            f"Projeto nao encontrado: owner={owner}, number={number}."
            " Verifique owner/number e escopo read:project"
        )

    project_id = project.get("id")
    if not isinstance(project_id, str) or not project_id.strip():
        raise SyncError("Project ID invalido retornado pela API")

    fields_block = project.get("fields", {})
    nodes = []
    if isinstance(fields_block, dict) and isinstance(fields_block.get("nodes"), list):
        nodes = fields_block["nodes"]

    fields: dict[str, FieldMeta] = {}
    for node in nodes:
        if not isinstance(node, dict):
            continue
        field_id = node.get("id")
        name = node.get("name")
        if not isinstance(field_id, str) or not field_id:
            continue
        if not isinstance(name, str) or not name.strip():
            continue

        typename = node.get("__typename")
        kind = "generic"
        options: dict[str, str] = {}
        iterations: dict[str, str] = {}
        iteration_labels: list[str] = []

        if typename == "ProjectV2SingleSelectField":
            kind = "single_select"
            raw_options = node.get("options")
            if isinstance(raw_options, list):
                for option in raw_options:
                    if not isinstance(option, dict):
                        continue
                    option_name = option.get("name")
                    option_id = option.get("id")
                    if isinstance(option_name, str) and isinstance(option_id, str):
                        options[option_name] = option_id

        if typename == "ProjectV2IterationField":
            kind = "iteration"
            config = node.get("configuration")
            raw_iters: list[Any] = []
            if isinstance(config, dict) and isinstance(config.get("iterations"), list):
                raw_iters = config["iterations"]

            for iteration in raw_iters:
                if not isinstance(iteration, dict):
                    continue
                iteration_id = iteration.get("id")
                if not isinstance(iteration_id, str) or not iteration_id:
                    continue

                title = iteration.get("title")
                start_date = iteration.get("startDate")

                if isinstance(title, str) and title:
                    iterations[title] = iteration_id
                    iteration_labels.append(title)
                if isinstance(start_date, str) and start_date:
                    iterations[start_date] = iteration_id
                    iteration_labels.append(start_date)
                iterations[iteration_id] = iteration_id

        fields[name] = FieldMeta(
            field_id=field_id,
            name=name,
            kind=kind,
            options=options,
            iterations=iterations,
            iteration_labels=unique_preserve_order(iteration_labels),
        )

    return project_id, fields


def build_project_updates(
    effective: dict[str, Any],
    field_map: dict[str, str],
    fields_by_name: dict[str, FieldMeta],
) -> list[dict[str, Any]]:
    updates: list[dict[str, Any]] = []

    for source_key, field_name in field_map.items():
        value = effective.get(source_key)
        if value is None:
            continue

        field = fields_by_name.get(field_name)
        if field is None:
            raise SyncError(
                f"Campo do Project nao encontrado para '{source_key}': '{field_name}'"
            )

        if field.kind == "single_select":
            if not isinstance(value, str) or not value.strip():
                raise SyncError(
                    f"Valor invalido para campo single-select '{field_name}' no ticket {effective['ticket_key']}"
                )
            option_id = field.options.get(value)
            if not option_id:
                available = ", ".join(sorted(field.options.keys())) or "(sem opcoes)"
                raise SyncError(
                    f"Opcao inexistente em '{field_name}': '{value}'. Opcoes: {available}"
                )
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "single_select",
                    "value": option_id,
                    "display": value,
                }
            )
            continue

        if field.kind == "iteration":
            if not isinstance(value, str) or not value.strip():
                raise SyncError(
                    f"Valor invalido para campo iteration '{field_name}' no ticket {effective['ticket_key']}"
                )
            iteration_id = field.iterations.get(value)
            if not iteration_id:
                available = (
                    ", ".join(field.iteration_labels)
                    or "(use title/startDate/id existente)"
                )
                raise SyncError(
                    f"Iteracao inexistente em '{field_name}': '{value}'. Valores aceitos: {available}"
                )
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "iteration",
                    "value": iteration_id,
                    "display": value,
                }
            )
            continue

        if source_key == "target_date":
            if not isinstance(value, str) or not is_iso_date(value):
                raise SyncError(
                    f"target_date invalido no ticket {effective['ticket_key']}: '{value}'"
                )
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "date",
                    "value": value,
                    "display": value,
                }
            )
            continue

        if source_key == "estimate":
            number_value: float
            if isinstance(value, (int, float)):
                number_value = float(value)
            elif isinstance(value, str):
                try:
                    number_value = float(value.strip())
                except ValueError as exc:
                    raise SyncError(
                        f"estimate invalido no ticket {effective['ticket_key']}: '{value}'"
                    ) from exc
            else:
                raise SyncError(
                    f"estimate invalido no ticket {effective['ticket_key']}: '{value}'"
                )
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "number",
                    "value": number_value,
                    "display": str(number_value),
                }
            )
            continue

        if isinstance(value, bool):
            text_value = "true" if value else "false"
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "text",
                    "value": text_value,
                    "display": text_value,
                }
            )
            continue

        if isinstance(value, (int, float)):
            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "number",
                    "value": float(value),
                    "display": str(value),
                }
            )
            continue

        if isinstance(value, str):
            if source_key.endswith("_date"):
                if not is_iso_date(value):
                    raise SyncError(
                        f"Data invalida para '{source_key}' no ticket {effective['ticket_key']}: '{value}'"
                    )
                updates.append(
                    {
                        "field_id": field.field_id,
                        "field_name": field_name,
                        "mode": "date",
                        "value": value,
                        "display": value,
                    }
                )
                continue

            updates.append(
                {
                    "field_id": field.field_id,
                    "field_name": field_name,
                    "mode": "text",
                    "value": value,
                    "display": value,
                }
            )
            continue

        raise SyncError(
            f"Valor nao suportado para '{source_key}' no ticket {effective['ticket_key']}: {value}"
        )

    return updates


def find_issue_by_ticket_key(repo: str, ticket_key: str) -> dict[str, Any] | None:
    token = f"ticket_key:{ticket_key}"
    search = f'"{token}" in:body'
    issues = run_gh_json(
        [
            "issue",
            "list",
            "--repo",
            repo,
            "--state",
            "all",
            "--search",
            search,
            "--limit",
            "20",
            "--json",
            "number,id,title,url,body,labels,assignees,state",
        ]
    )

    if not isinstance(issues, list):
        raise SyncError("Resposta invalida ao buscar issues")

    marker = MARKER_TEMPLATE.format(ticket_key=ticket_key)
    filtered: list[dict[str, Any]] = []
    for issue in issues:
        if not isinstance(issue, dict):
            continue
        body = issue.get("body")
        if isinstance(body, str) and marker in body:
            filtered.append(issue)

    if len(filtered) > 1:
        urls = [str(issue.get("url", "")) for issue in filtered]
        raise SyncError(
            f"ticket_key duplicado em multiplas issues ({ticket_key}): "
            + ", ".join(urls)
        )
    if not filtered:
        return None
    return filtered[0]


def create_issue(
    repo: str, title: str, body: str, labels: list[str], assignees: list[str]
) -> str:
    args = [
        "issue",
        "create",
        "--repo",
        repo,
        "--title",
        title,
        "--body",
        body,
    ]
    for label in labels:
        args.extend(["--label", label])
    for assignee in assignees:
        args.extend(["--assignee", assignee])

    output = run_gh(args).strip()
    if not output:
        raise SyncError("gh issue create nao retornou URL")

    lines = [line.strip() for line in output.splitlines() if line.strip()]
    url = lines[-1]
    if not url.startswith("http"):
        raise SyncError(f"URL invalida retornada por gh issue create: {url}")
    return url


def update_issue(
    repo: str,
    number: int,
    title: str,
    body: str,
    labels: list[str],
    assignees: list[str],
) -> None:
    payload = {
        "title": title,
        "body": body,
        "labels": labels,
        "assignees": assignees,
    }
    run_gh_json(
        [
            "api",
            f"repos/{repo}/issues/{number}",
            "--method",
            "PATCH",
            "--input",
            "-",
        ],
        input_text=json.dumps(payload, ensure_ascii=True),
    )


def get_issue(repo: str, issue_ref: str) -> dict[str, Any]:
    issue = run_gh_json(
        [
            "issue",
            "view",
            issue_ref,
            "--repo",
            repo,
            "--json",
            "number,id,url,title,body,labels,assignees,state",
        ]
    )
    if not isinstance(issue, dict):
        raise SyncError(f"Resposta invalida ao carregar issue {issue_ref}")
    if not isinstance(issue.get("id"), str):
        raise SyncError(f"Issue {issue_ref} sem node id")
    return issue


ADD_TO_PROJECT_MUTATION = """
mutation($projectId: ID!, $contentId: ID!) {
  addProjectV2ItemById(input: {projectId: $projectId, contentId: $contentId}) {
    item {
      id
    }
  }
}
"""


def add_issue_to_project(project_id: str, issue_node_id: str) -> str:
    payload = run_graphql(
        ADD_TO_PROJECT_MUTATION,
        {
            "projectId": project_id,
            "contentId": issue_node_id,
        },
    )
    data = payload.get("data", {})
    container = data.get("addProjectV2ItemById", {})
    item = container.get("item", {}) if isinstance(container, dict) else {}
    item_id = item.get("id") if isinstance(item, dict) else None
    if not isinstance(item_id, str) or not item_id:
        raise SyncError("Nao foi possivel obter project item id ao adicionar issue")
    return item_id


def apply_project_updates(
    project_id: str, item_id: str, updates: list[dict[str, Any]]
) -> None:
    for update in updates:
        args = [
            "project",
            "item-edit",
            "--id",
            item_id,
            "--project-id",
            project_id,
            "--field-id",
            update["field_id"],
        ]

        mode = update["mode"]
        if mode == "single_select":
            args.extend(["--single-select-option-id", str(update["value"])])
        elif mode == "iteration":
            args.extend(["--iteration-id", str(update["value"])])
        elif mode == "number":
            args.extend(["--number", str(update["value"])])
        elif mode == "date":
            args.extend(["--date", str(update["value"])])
        else:
            args.extend(["--text", str(update["value"])])

        run_gh(args)


def preflight(
    spec: dict[str, Any],
    me_login: str,
    fields_by_name: dict[str, FieldMeta],
) -> list[PreflightItem]:
    repo = spec["repo"]
    labels_available = get_repo_labels(repo)

    entries: list[PreflightItem] = []
    missing_labels: set[str] = set()
    all_assignees: set[str] = set()

    for item in spec["items"]:
        effective = merge_item_with_defaults(item, spec["defaults"], me_login)

        for label in effective["labels"]:
            if label not in labels_available:
                missing_labels.add(label)
        for assignee in effective["assignees"]:
            all_assignees.add(assignee)

        updates = build_project_updates(
            effective=effective,
            field_map=spec["field_map"],
            fields_by_name=fields_by_name,
        )

        existing = find_issue_by_ticket_key(
            repo=repo, ticket_key=effective["ticket_key"]
        )
        entries.append(
            PreflightItem(
                item=item,
                effective=effective,
                existing_issue=existing,
                project_updates=updates,
            )
        )

    if missing_labels:
        labels = ", ".join(sorted(missing_labels))
        raise SyncError(
            "Modo estrito: labels inexistentes no repositorio: "
            + labels
            + ". Crie as labels antes de sincronizar."
        )

    ensure_assignees_assignable(repo=repo, assignees=all_assignees)
    return entries


def print_validate_result(spec: dict[str, Any], file_path: Path) -> None:
    print(f"OK: {file_path} valido")
    print(f"- repo: {spec['repo']}")
    print(f"- sprint: {spec['sprint']['key']} ({spec['sprint']['name']})")
    print(f"- itens: {len(spec['items'])}")
    print(f"- field_map: {len(spec['field_map'])} chaves")


def print_dry_run_report(entries: list[PreflightItem]) -> None:
    print("DRY-RUN: nenhuma alteracao foi aplicada")
    for entry in entries:
        effective = entry.effective
        ticket_key = effective["ticket_key"]
        action = "update" if entry.existing_issue else "create"
        target = (
            entry.existing_issue.get("url") if entry.existing_issue else "(nova issue)"
        )
        print(f"- {ticket_key}: {action} -> {target}")
        labels = ", ".join(effective["labels"]) or "(sem labels)"
        assignees = ", ".join(effective["assignees"]) or "(sem assignees)"
        print(f"  labels: {labels}")
        print(f"  assignees: {assignees}")
        if entry.project_updates:
            projection = ", ".join(
                [
                    f"{update['field_name']}={update['display']}"
                    for update in entry.project_updates
                ]
            )
            print(f"  project: {projection}")
        else:
            print("  project: (sem atualizacoes)")


def run_sync(spec: dict[str, Any], dry_run: bool) -> None:
    ensure_scopes(apply_mode=not dry_run)

    me_login = get_current_login()
    project_owner = spec["project"]["owner"]
    project_number = spec["project"]["number"]

    project_id, fields_by_name = get_project_metadata(project_owner, project_number)

    entries = preflight(spec=spec, me_login=me_login, fields_by_name=fields_by_name)
    if dry_run:
        print_dry_run_report(entries)
        return

    created = 0
    updated = 0
    print("SYNC: aplicando alteracoes")

    for entry in entries:
        effective = entry.effective
        ticket_key = effective["ticket_key"]

        if entry.existing_issue:
            issue_number = entry.existing_issue.get("number")
            if not isinstance(issue_number, int):
                raise SyncError(f"Issue existente sem numero para {ticket_key}")
            update_issue(
                repo=spec["repo"],
                number=issue_number,
                title=effective["title"],
                body=effective["body"],
                labels=effective["labels"],
                assignees=effective["assignees"],
            )
            issue = get_issue(spec["repo"], str(issue_number))
            updated += 1
            action = "updated"
        else:
            issue_url = create_issue(
                repo=spec["repo"],
                title=effective["title"],
                body=effective["body"],
                labels=effective["labels"],
                assignees=effective["assignees"],
            )
            issue = get_issue(spec["repo"], issue_url)
            created += 1
            action = "created"

        issue_id = issue.get("id")
        issue_url = issue.get("url")
        issue_number = issue.get("number")
        if (
            not isinstance(issue_id, str)
            or not isinstance(issue_url, str)
            or not isinstance(issue_number, int)
        ):
            raise SyncError(f"Dados invalidos da issue sincronizada para {ticket_key}")

        project_item_id = add_issue_to_project(
            project_id=project_id, issue_node_id=issue_id
        )
        apply_project_updates(
            project_id=project_id,
            item_id=project_item_id,
            updates=entry.project_updates,
        )

        print(
            f"- {ticket_key}: {action} issue #{issue_number} ({issue_url})"
            f" | project_item={project_item_id} | campos={len(entry.project_updates)}"
        )

    print(f"SYNC concluido: created={created}, updated={updated}, total={len(entries)}")


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        prog="sprint.py",
        description="Sincroniza sprint.md (JSON) com issues e GitHub Project",
    )

    subparsers = parser.add_subparsers(dest="command", required=True)

    validate_parser = subparsers.add_parser("validate", help="Valida arquivo sprint.md")
    validate_parser.add_argument(
        "--file", default="sprint.md", help="Caminho do arquivo markdown"
    )

    sync_parser = subparsers.add_parser("sync", help="Sincroniza com GitHub")
    sync_parser.add_argument(
        "--file", default="sprint.md", help="Caminho do arquivo markdown"
    )
    mode_group = sync_parser.add_mutually_exclusive_group()
    mode_group.add_argument(
        "--apply", action="store_true", help="Aplica alteracoes no GitHub"
    )
    mode_group.add_argument(
        "--dry-run", action="store_true", help="Simula sem alterar GitHub"
    )

    return parser.parse_args(argv)


def main(argv: list[str]) -> int:
    try:
        args = parse_args(argv)
        file_path = Path(args.file)
        spec = read_spec_from_file(file_path)

        if args.command == "validate":
            print_validate_result(spec, file_path)
            return 0

        if args.command == "sync":
            dry_run = not args.apply
            run_sync(spec=spec, dry_run=dry_run)
            return 0

        raise ValidationError([f"Comando desconhecido: {args.command}"])

    except ValidationError as exc:
        print("ERRO de validacao:", file=sys.stderr)
        for error in exc.errors:
            print(f"- {error}", file=sys.stderr)
        return 1
    except GhCommandError as exc:
        stderr = exc.stderr.strip()
        stdout = exc.stdout.strip()
        message = stderr or stdout or str(exc)

        if "missing required scopes" in message:
            message += (
                "\nDica: rode `gh auth refresh -s read:project -s project -s repo`"
            )

        print("ERRO do GitHub CLI:", file=sys.stderr)
        print(f"- comando: {' '.join(exc.cmd)}", file=sys.stderr)
        print(f"- detalhe: {message}", file=sys.stderr)
        return 2
    except SyncError as exc:
        print("ERRO de sincronizacao:", file=sys.stderr)
        print(f"- {exc}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
