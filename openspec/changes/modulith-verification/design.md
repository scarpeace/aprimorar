## Context

Após 4 fases de migração, a arquitetura modular está implementada. Esta fase cria a camada de segurança e documentação para garantir que a arquitetura se mantenha no tempo.

## Goals / Non-Goals

**Goals:**
- Criar `@ModuleTest` para cada módulo validando que o contexto Spring do módulo carrega
- Configurar `@ApplicationModuleTest` com verificação de regras de módulo (incluindo no CI)
- Remover `allowedDependencies` temporárias adicionadas na Fase 1
- Gerar documentação de módulos com `spring-modulith-docs`
- Adicionar verificação ao CI (Maven verify phase)

**Non-Goals:**
- Escrever testes de integração completos para cada módulo (apenas teste de contexto + verificação)
- Documentar API REST (já coberto por Swagger/springdoc)
- Implementar event-driven communication entre módulos

## Decisions

### 1. Estrutura de testes de módulo

Cada módulo terá um teste de contexto:

```
src/test/java/com/aprimorar/api/<modulo>/
└── <Modulo>ModuleTest.java
```

Para módulos que dependem de outros, o contexto Spring carrega apenas os módulos necessários:

```java
@ModuleTest(MyModule.class)
class MyModuleModuleTest {
    @Test
    void contextLoads() {
    }
}
```

Para módulos com dependências externas:
```java
@ModuleTest(EventModule.class)
class EventModuleModuleTest {
    @Autowired
    private EventService eventService;

    @Test
    void shouldProvideEventService() {
        assertThat(eventService).isNotNull();
    }
}
```

### 2. Verificação de regras de módulo

Um teste central que verifica TODAS as regras:

```java
@ApplicationModuleTest
class ModuleVerificationTest {
    @Test
    void verifyModuleStructure() {
        // Verifica que módulos só acessam api/ de outros módulos
        // Verifica shared packages
        // Verifica que não há dependências cíclicas
    }
}
```

### 3. allowedDependencies finais (pós-fase 4)

Após todas as refatorações, as `allowedDependencies` devem ser:

| Módulo | Dependências finais |
|--------|---------------------|
| auth | (nenhuma — User só tem employeeId UUID) |
| student | address (Address é @Embedded) |
| parent | (nenhuma) |
| employee | (nenhuma) |
| event | student, employee, finance (service-to-service legítimos) |
| finance | event (TransactionService chamado por EventService) |
| dashboard | event (agregação de dados) |
| address | (nenhuma) |

Novos módulos introduzidos:
- `shared`, `enums`, `exception`, `config` → shared packages (não são módulos)

### 4. Documentação automática

Adicionar `spring-modulith-starter-docs` para gerar documentação:

```
target/generated-docs/
├ index.html
├ modules/
│   ├ auth.html
│   ├ student.html
│   ├ ...
└ application-modules.html
```

Também gerar diagrama C4-style das dependências entre módulos.

### 5. CI integration

Adicionar ao `pom.xml` para rodar verificação durante `verify`:

```xml
<plugin>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-maven-plugin</artifactId>
</plugin>
```

Ou via profile Maven:
```bash
./mvnw verify -Pmodulith-verify
```

### 6. Script de verificação

Um script na raiz para verificação rápida:
```bash
./scripts/modulith-verify.sh
# Roda: ./mvnw test -Dtest="*ModuleVerificationTest"
```

## Risks / Trade-offs

- [Falsos positivos na verificação] → Ajustar regras de verificação para ignorar pacotes de teste e infraestrutura Spring.
- [Documentação desatualizada] → A documentação é gerada no build. Garantir que o CI gere e publique como artefato.
- [allowedDependencies residuais] → Se alguma dependência temporária não for resolvida nas fases 2-4, a verificação falha. Resolver antes de finalizar.
