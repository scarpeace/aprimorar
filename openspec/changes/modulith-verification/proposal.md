## Why

Após as 4 fases anteriores, a arquitetura modular está implementada. Esta fase finaliza a migração com testes formais de módulo, verificação automatizada de boundaries no CI, e documentação da arquitetura gerada automaticamente pelo Spring Modulith.

## What Changes

- Criar `@ModuleTest` para cada módulo (testes de integração com contexto Spring reduzido ao módulo + dependentes)
- Configurar `@ApplicationModuleTest` com `verify()` para garantir que regras de módulo são seguidas
- Remover `allowedDependencies` temporários adicionados na Fase 1 (se houver)
- Gerar documentação de módulos via `spring-modulith-docs`
- Adicionar verificação de módulos ao CI (como parte do build Maven ou script separado)
- Atualizar `server-architecture` spec para refletir o estado final

## Capabilities

### New Capabilities
- Nenhuma

### Modified Capabilities
- `server-architecture`: Modificar requirement sobre verification (adicionar regras finais de teste, CI, docs)

## Impact

- Testes: novos `@ModuleTest` e `@ApplicationModuleTest`
- CI: nova etapa de verificação de módulos
- Docs: documentação gerada em `target/generated-docs/` ou similar
- Nenhuma mudança de código de produção — apenas testes e configuração
