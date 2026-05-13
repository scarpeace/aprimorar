from datetime import datetime, timedelta, timezone
from pathlib import Path
from uuid import NAMESPACE_DNS, uuid5


OUTPUT = Path(__file__).parent / "src/main/resources/data.sql"

PARENT_ID = uuid5(NAMESPACE_DNS, "aprimorar-seed-parent")
STUDENT_ID = uuid5(NAMESPACE_DNS, "aprimorar-seed-student")
STUDENT_NAME = "Marina Alves"

EMPLOYEES = [
    {
        "key": "teacher-gustavo",
        "name": "Gustavo Scarpellini",
        "birthdate": "1990-01-01",
        "cpf": "30000000003",
        "contact": "11999990003",
        "email": "gustavo.scarpellini@example.com",
        "pix": "11999990003",
        "duty": "TEACHER",
    },
    {
        "key": "teacher-ana",
        "name": "Ana Beatriz Lima",
        "birthdate": "1988-03-14",
        "cpf": "30000000004",
        "contact": "11999990004",
        "email": "ana.lima@example.com",
        "pix": "11999990004",
        "duty": "TEACHER",
    },
    {
        "key": "teacher-rodrigo",
        "name": "Rodrigo Martins",
        "birthdate": "1985-06-22",
        "cpf": "30000000005",
        "contact": "11999990005",
        "email": "rodrigo.martins@example.com",
        "pix": "11999990005",
        "duty": "TEACHER",
    },
    {
        "key": "teacher-julia",
        "name": "Julia Fernandes",
        "birthdate": "1992-11-09",
        "cpf": "30000000006",
        "contact": "11999990006",
        "email": "julia.fernandes@example.com",
        "pix": "11999990006",
        "duty": "TEACHER",
    },
    {
        "key": "adm-marcos",
        "name": "Marcos Pereira",
        "birthdate": "1987-02-18",
        "cpf": "30000000007",
        "contact": "11999990007",
        "email": "marcos.pereira@example.com",
        "pix": "11999990007",
        "duty": "ADM",
    },
    {
        "key": "therapist-carolina",
        "name": "Carolina Souza",
        "birthdate": "1991-09-25",
        "cpf": "30000000008",
        "contact": "11999990008",
        "email": "carolina.souza@example.com",
        "pix": "11999990008",
        "duty": "THERAPIST",
    },
    {
        "key": "mentor-paulo",
        "name": "Paulo Henrique Costa",
        "birthdate": "1984-12-03",
        "cpf": "30000000009",
        "contact": "11999990009",
        "email": "paulo.costa@example.com",
        "pix": "11999990009",
        "duty": "MENTOR",
    },
]

for employee in EMPLOYEES:
    employee["id"] = str(uuid5(NAMESPACE_DNS, f"aprimorar-seed-employee-{employee['key']}"))

TEACHER_EMPLOYEES = [employee for employee in EMPLOYEES if employee["duty"] == "TEACHER"]
OTHER_EMPLOYEES = [employee for employee in EMPLOYEES if employee["duty"] != "TEACHER"]

CONTENTS = [
    "AULA",
    "ENEM",
    "PAS",
    "MENTORIA",
    "ORIENTACAO_VOCACIONAL",
]


def sql_timestamp(value: datetime) -> str:
    return value.astimezone(timezone.utc).strftime("%Y-%m-%d %H:%M:%S")


def event_values(index: int) -> tuple[str, str, datetime, datetime, str, int, int]:
    event_id = uuid5(NAMESPACE_DNS, f"aprimorar-seed-event-{index:03d}")
    week = index // 2
    day_offset = 1 if index % 2 == 0 else 3
    start_date = datetime(2027, 3, 1, 14, 0, tzinfo=timezone.utc) + timedelta(
        weeks=week,
        days=day_offset,
    )
    end_date = start_date + timedelta(hours=1, minutes=30)
    content = CONTENTS[index % len(CONTENTS)]
    price = 100 + ((index * 7) % 101)
    payment = 50 + ((index * 3) % 31)

    return str(event_id), content, start_date, end_date, content, price, payment


def employee_for_event(index: int) -> dict[str, str]:
    if index < 85:
        return TEACHER_EMPLOYEES[index % len(TEACHER_EMPLOYEES)]

    other_index = index - 85
    return OTHER_EMPLOYEES[other_index % len(OTHER_EMPLOYEES)]


def write_person_seed(lines: list[str]) -> None:
    lines.extend(
        [
            "-- Base participants",
            (
                "INSERT INTO tb_parent "
                "(id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at) "
                f"VALUES ('{PARENT_ID}', 'Carlos Alves', '1980-04-12', '10000000001', "
                "'11988880001', 'carlos.alves@example.com', '11988880001', 2, true, "
                "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);"
            ),
            (
                "INSERT INTO tb_students "
                "(id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at, "
                "school, parent_id, street, district, city, state, zip, complement) "
                f"VALUES ('{STUDENT_ID}', '{STUDENT_NAME}', '2010-08-20', '20000000002', "
                "'11977770002', 'marina.alves@example.com', '11977770002', 0, true, "
                "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Escola Estadual Aprimorar', "
                f"'{PARENT_ID}', 'Rua das Aulas', 'Centro', 'Sao Paulo', 'SP', '01000000', 'Casa 2');"
            ),
        ]
    )

    for employee in EMPLOYEES:
        lines.append(
            "INSERT INTO tb_employees "
            "(id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at, duty) "
            f"VALUES ('{employee['id']}', '{employee['name']}', '{employee['birthdate']}', '{employee['cpf']}', "
            f"'{employee['contact']}', '{employee['email']}', '{employee['pix']}', 1, true, "
            f"CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{employee['duty']}');"
        )

    lines.append("")


def write_event_seed(lines: list[str]) -> None:
    lines.append("-- Events and financial transactions")

    for index in range(100):
        event_id, label, start_date, end_date, content, price, payment = event_values(index)
        employee = employee_for_event(index)
        title = f"{content} - Col: {employee['name']} - {STUDENT_NAME}"
        description = f"Atendimento {index + 1:03d} - {label}"
        charge_transaction_id = uuid5(NAMESPACE_DNS, f"aprimorar-seed-event-{index:03d}-student-charge")
        payment_transaction_id = uuid5(NAMESPACE_DNS, f"aprimorar-seed-event-{index:03d}-employee-payment")

        lines.extend(
            [
                (
                    "INSERT INTO tb_events "
                    "(id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at, "
                    "title, description, start_date, end_date, payment, price, content, "
                    "employee_payment_date, student_charge_date, student_id, student_name, employee_id, employee_name) "
                    f"VALUES ('{event_id}', 'Atendimento {index + 1:03d}', '2027-01-01', '900{index:08d}', "
                    "'11900000000', 'evento.seed@example.com', '11900000000', 1, true, "
                    f"CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{title}', '{description}', "
                    f"TIMESTAMP '{sql_timestamp(start_date)}', TIMESTAMP '{sql_timestamp(end_date)}', "
                    f"{payment}.00, {price}.00, '{content}', NULL, NULL, "
                    f"'{STUDENT_ID}', '{STUDENT_NAME}', '{employee['id']}', '{employee['name']}');"
                ),
                (
                    "INSERT INTO tb_transactions "
                    "(id, type, status, amount, origin, origin_id, settled_at, category) "
                    f"VALUES ('{charge_transaction_id}', 'IN', 'PENDING', {price}.00, "
                    f"'EVENT_STUDENT_CHARGE', '{event_id}', NULL, 'COBRANCA_ALUNO');"
                ),
                (
                    "INSERT INTO tb_transactions "
                    "(id, type, status, amount, origin, origin_id, settled_at, category) "
                    f"VALUES ('{payment_transaction_id}', 'OUT', 'PENDING', {payment}.00, "
                    f"'EVENT_EMPLOYEE_PAYMENT', '{event_id}', NULL, 'PAGAMENTO_COLABORADOR');"
                ),
            ]
        )


def main() -> None:
    lines = [
        "-- Deterministic development seed for Aprimorar.",
        "-- Generated by server/generate_seed.py.",
        "TRUNCATE TABLE tb_transactions, tb_events, tb_students, tb_parent, tb_employees CASCADE;",
        "",
    ]

    write_person_seed(lines)
    write_event_seed(lines)

    OUTPUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Generated {OUTPUT}")


if __name__ == "__main__":
    main()
