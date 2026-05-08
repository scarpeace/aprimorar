## Why

Com os módulos configurados e a verificação rodando, o próximo passo é definir formalmente a superfície de API pública de cada módulo. Sem isso, toda classe fica visível para todos os módulos, e a verificação do Modulith não consegue distinguir o que é intencional do que é vazamento.

## What Changes

- Para cada módulo (auth, student, parent, employee, event, finance, dashboard, address), definir um pacote `api/` que contém a API pública
- Mover services, DTOs e interfaces públicas para `api/`
- Manter controllers, entities, repositories, mappers em `internal/`
- Subir exceções específicas para o `api/` quando necessário (para que módulos dependentes possam capturá-las)
- Atualizar imports em todos os consumidores

## Capabilities

### New Capabilities
- Nenhuma

### Modified Capabilities
- `server-architecture`: Modificar requirement sobre API surface de cada módulo (definir o que é público vs interno)

## Impact

- Todos os módulos: novo pacote `api/` e `internal/`
- Todos os imports cross-module precisam ser atualizados para apontar para `api/`
- Sem mudança de lógica de negócio — apenas reorganização de pacotes
