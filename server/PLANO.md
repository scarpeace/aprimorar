# Plano — Endereço como value object

## Objetivo

Remover a entidade/tabela `enderecos` e tratar o endereço como um dado composto
do proprietário (`aluno` ou `colaborador`). O endereço continuará sendo
representado por um tipo Java compartilhado, mas será persistido como colunas
da tabela principal.

Não usaremos JSON/JSONB: os campos são conhecidos, possuem validações próprias
e podem precisar de consultas ou restrições no banco.

## Estado atual

- `Endereco` é uma entidade JPA com tabela própria.
- `alunos.endereco_id` aponta para `enderecos.id`.
- `colaboradores.endereco_id` aponta para `enderecos.id`.
- Os dois relacionamentos são `@OneToOne` com chave estrangeira única.
- Os DTOs de endereço já são aninhados nos DTOs de aluno e colaborador.

## Estrutura desejada

```text
alunos
├── endereco_rua
├── endereco_numero
├── endereco_bairro
├── endereco_cidade
├── endereco_estado
├── endereco_cep
└── endereco_complemento

colaboradores
├── endereco_rua
├── endereco_numero
├── endereco_bairro
├── endereco_cidade
├── endereco_estado
├── endereco_cep
└── endereco_complemento
```

No Java, `Endereco` será um `@Embeddable`, e as entidades usarão
`@Embedded`. O pacote `pessoas.endereco` continua sendo apenas um tipo interno
compartilhado; não haverá módulo ou ciclo de vida próprio para endereço.

## Fases

### Fase 1 — Migration de expansão

- Criar a próxima migration Flyway (`V4`).
- Adicionar as colunas `endereco_*` em `alunos` e `colaboradores`.
- Copiar os dados de `enderecos` para as respectivas tabelas proprietárias.
- Manter temporariamente `endereco_id` e a tabela `enderecos` para permitir a
  transição sem perda de dados.
- Tornar `NOT NULL` os campos obrigatórios depois do backfill.

### Fase 2 — Troca do mapeamento JPA

- Remover `@Entity`, `@Id` e geração de identificador de `Endereco`.
- Tornar `Endereco` um `@Embeddable`.
- Substituir os relacionamentos `@OneToOne` por `@Embedded` em
  `AlunoEntity` e `ColaboradorEntity`.
- Ajustar construtores, métodos de atualização e mapeamentos somente onde
  necessário.
- Manter os DTOs HTTP de endereço, sem alterar o formato externo neste passo.

### Fase 3 — Limpeza do schema

- Criar uma migration posterior (`V5`) após o código estar usando as novas
  colunas.
- Remover as chaves estrangeiras e as colunas `endereco_id`.
- Remover a tabela `enderecos`.
- Não editar migrations já aplicadas, especialmente `V1`.

### Fase 4 — Limpeza de código

- Remover qualquer repositório ou referência residual à tabela `enderecos`.
- Remover diretórios vazios.
- Preservar apenas `Endereco` como value object e os DTOs necessários.

### Fase 5 — Validação

- Executar `compile` e `test-compile`.
- Atualizar testes de entidade/DTO que dependam do ID de endereço ou do
  relacionamento JPA.
- Verificar que não restaram referências a `endereco_id`, `@OneToOne` ou à
  tabela `enderecos`.
- Conferir o diff da migration e a integridade do backfill.

## Fora deste plano

- Incorporação dos dados de responsável em `alunos`.
- Alteração dos contratos HTTP do frontend.
- Migração para JSON/JSONB.
