## 1. Modelo financeiro e persistência

- [x] 1.1 Criar a entidade `Transaction` com os campos mínimos, enums e repositório no módulo `finance`
- [x] 1.2 Criar a migration para a tabela de transactions e remover a persistência separada de `GeneralExpense`
- [x] 1.3 Criar a migration de dados para materializar transactions a partir dos eventos e despesas gerais já existentes

## 2. Integração com eventos

- [x] 2.1 Atualizar a criação de eventos para gerar uma transaction `IN` pendente para cobrança do aluno e uma transaction `OUT` pendente para pagamento do colaborador
- [x] 2.2 Atualizar os fluxos de toggle de cobrança e pagamento no evento para liquidar a transaction correspondente e manter as datas do evento sincronizadas
- [x] 2.3 Garantir cobertura de testes para criação e liquidação das transactions ligadas a eventos

## 3. Leituras e APIs do módulo financeiro

- [x] 3.1 Refatorar os resumos financeiros globais para usar transactions como fonte de cálculo
- [x] 3.2 Criar os endpoints de resumo financeiro do aluno e do colaborador em `finance`
- [x] 3.3 Substituir os endpoints de despesas gerais por operações sobre transactions com `origin = GENERAL_EXPENSE`
- [x] 3.4 Implementar filtros de consulta de transactions por categoria, tipo, status e data

## 4. Frontend e contratos

- [x] 4.1 Atualizar o client gerado e os hooks para consumir os novos endpoints financeiros
- [x] 4.2 Ajustar as telas de aluno e colaborador para usar os resumos fornecidos por `finance`
- [x] 4.3 Ajustar a tela de despesas gerais para operar sobre transactions
- [x] 4.4 Validar que o dashboard continua separado entre métricas operacionais de `event` e métricas financeiras de `finance`
