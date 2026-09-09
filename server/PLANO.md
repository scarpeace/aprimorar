# Plano — Responsável embutido no aluno

## Objetivo

Remover o relacionamento entre `alunos` e `responsaveis` e armazenar os dados
do responsável diretamente na tabela `alunos`, usando um value object JPA
`@Embeddable`.

O responsável não terá acesso ao sistema, identidade própria ou ciclo de vida
independente. Ele será apenas um conjunto de dados pertencente ao aluno.

## Contrato HTTP desejado

O aluno receberá os dados do responsável aninhados:

```json
{
  "nome": "Carlos Eduardo Ramos",
  "dataNascimento": "1981-02-12",
  "cpf": "204.681.357-07",
  "telefone": "11980000001",
  "email": "carlos.ramos@example.com"
}
```

`responsavelId` deixará de fazer parte do request do aluno.

## Estrutura desejada

```text
alunos
├── responsavel_nome
├── responsavel_data_nascimento
├── responsavel_cpf
├── responsavel_telefone
└── responsavel_email
```

No Java, o value object ficará sob o domínio de aluno:

```text
pessoas/aluno/
├── domain/Responsavel.java
└── web/dto/
    ├── ResponsavelRequestDTO.java
    └── ResponsavelResponseDTO.java
```

## Fases

### Fase 1 — Value object e contrato

- Criar `Responsavel` como `@Embeddable` em `pessoas.aluno.domain`.
- Remover do modelo novo `id`, `userId` e comportamento de autenticação.
- Criar os DTOs aninhados de request e response dentro de `pessoas.aluno.web.dto`.
- Alterar `AlunoRequestDTO` para receber `ResponsavelRequestDTO`.
- Manter o formato aninhado em `AlunoResponseDTO`.

### Fase 2 — Migration de expansão

- Criar a próxima migration livre (`V8`).
- Adicionar as colunas `responsavel_*` em `alunos`.
- Copiar os dados de `responsaveis` para `alunos` usando `responsavel_id`.
- Aplicar `NOT NULL` aos campos obrigatórios após o backfill.
- Manter temporariamente `responsavel_id` e a tabela `responsaveis`.
- Não criar unicidade global para CPF ou e-mail do responsável, pois vários
  alunos podem compartilhar o mesmo responsável.

### Fase 3 — Troca do mapeamento JPA

- Substituir `@ManyToOne ResponsavelEntity` por `@Embedded Responsavel` em
  `AlunoEntity`.
- Ajustar construtores, método de atualização e mapeamentos.
- Remover a busca por `ResponsavelService` no fluxo de aluno.
- Criar e atualizar os dados do responsável junto com o aluno.

### Fase 4 — Transição do schema e seed

- Atualizar `data.sql` para usar as colunas `responsavel_*`.
- Criar uma migration intermediária (`V9`) tornando `responsavel_id` anulável.
- Garantir que novos alunos não dependam mais da tabela legada.

### Fase 5 — Limpeza do schema

- Criar uma migration posterior (`V10`).
- Remover a FK e a coluna `responsavel_id`.
- Remover a tabela `responsaveis`.
- Não editar migrations já aplicadas.

### Fase 6 — Limpeza de código

- Remover `ResponsavelEntity`.
- Remover `ResponsavelRepository`.
- Remover `ResponsavelService` e sua implementação.
- Remover controller, contratos públicos e exceções exclusivas do responsável,
  caso não tenham mais consumidores.
- Remover referências residuais e diretórios vazios.

### Fase 7 — Validação

- Executar `compile` e `test-compile`.
- Atualizar testes que dependam de `responsavelId` ou `ResponsavelEntity`.
- Verificar que não restaram referências a `responsavel_id` no código ativo.
- Conferir o backfill e a integridade dos dados antes da limpeza definitiva.

## Observação de modelagem

O modelo embutido duplica os dados quando vários alunos possuem o mesmo
responsável. Alterar os dados em um aluno não atualizará automaticamente os
demais. Isso é aceitável enquanto o responsável for apenas um dado cadastral
do aluno, sem identidade ou operações próprias.

## Fora deste plano

- Criação de acesso para responsáveis.
- Compartilhamento de responsáveis como entidade independente.
- Alterações no frontend nesta etapa.
