## 1. Module tests por módulo

- [ ] 1.1 Auth: criar `AuthModuleTest.java` com `@ModuleTest(AuthModule.class)`
- [ ] 1.2 Student: criar `StudentModuleTest.java` com `@ModuleTest(StudentModule.class)`
- [ ] 1.3 Parent: criar `ParentModuleTest.java` com `@ModuleTest(ParentModule.class)`
- [ ] 1.4 Employee: criar `EmployeeModuleTest.java`
- [ ] 1.5 Event: criar `EventModuleTest.java`
- [ ] 1.6 Finance: criar `FinanceModuleTest.java`
- [ ] 1.7 Dashboard: criar `DashboardModuleTest.java`
- [ ] 1.8 Address: criar `AddressModuleTest.java`

## 2. Module verification

- [ ] 2.1 Garantir que `ModuleVerificationTest` (criado na Fase 1) continua funcionando sem `allowedDependencies` temporárias
- [ ] 2.2 Remover TODAS as `allowedDependencies` temporárias dos `package-info.java`
- [ ] 2.3 Configurar verificação em modo estrito (sem permissões extras)
- [ ] 2.4 Rodar `ModuleVerificationTest` e verificar que passa sem violações

## 3. Documentação automática

- [ ] 3.1 Adicionar `spring-modulith-starter-docs` ao `pom.xml`
- [ ] 3.2 Gerar documentação com `./mvnw package` e verificar saída em `target/generated-docs/`
- [ ] 3.3 Verificar diagrama C4-style de dependências entre módulos

## 4. CI integration

- [ ] 4.1 Adicionar verificação de módulos ao CI (como etapa do build Maven)
- [ ] 4.2 Criar script `scripts/modulith-verify.sh` para verificação rápida local
- [ ] 4.3 Verificar que CI passa com todas as verificações

## 5. Finalização

- [ ] 5.1 Rodar `./mvnw test` completo (todos os testes existentes + novos module tests + verification)
- [ ] 5.2 Atualizar `openspec/specs/server-architecture/spec.md` com o estado final da migração
- [ ] 5.3 Verificar que não há violações de módulo pendentes
