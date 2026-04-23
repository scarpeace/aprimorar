# Padrões de Teste

**Data da Análise:** 17-04-2026

## Framework de Teste

**Executor (Runner):**
- Os testes de backend são executados no JUnit 5 via `spring-boot-starter-test` em `server/api-aprimorar/pom.xml`.
- Configuração: `server/api-aprimorar/pom.xml`

**Biblioteca de Asserção:**
- AssertJ é o estilo de asserção primário, visível em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.

**Comandos de Execução:**
```bash
cd server/api-aprimorar && ./mvnw test                 # Executar testes de backend
cd server/api-aprimorar && ./mvnw -Dtest=StudentServiceTest test   # Executar uma classe de teste de backend específica
cd server/api-aprimorar && ./mvnw verify               # Executar verificação de backend e relatório Jacoco
```

## Organização de Arquivos de Teste

**Localização:**
- Os testes de backend residem em uma árvore espelhada separada sob `server/api-aprimorar/src/test/java/`.
- O frontend não possui um executor de testes dedicado ou arquivos de teste comitados sob `client/src/`, conforme `client/AGENTS.md` e a ausência de arquivos `*.test.*` / `*.spec.*` em `client/src/`.

**Nomenclatura:**
- Os testes unitários/de serviço usam `*Test.java`, por exemplo, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.
- O teste de fumaça (smoke test) do contexto Spring usa `ApiAprimorarApplicationTests` em `server/api-aprimorar/src/test/java/com/aprimorar/api/ApiAprimorarApplicationTests.java`.

**Estrutura:**
```
server/api-aprimorar/src/test/java/com/aprimorar/api/
├── ApiAprimorarApplicationTests.java
├── exception/GlobalExceptionHandlerTest.java
└── domain/
    ├── employee/EmployeeServiceTest.java
    ├── employee/EmployeeTest.java
    ├── event/EventServiceTest.java
    ├── event/EventTest.java
    ├── parent/ParentServiceTest.java
    ├── parent/ParentTest.java
    ├── student/StudentServiceTest.java
    ├── student/StudentTest.java
    └── dashboard/DashboardSummaryResponseDTOTest.java
```

## Estrutura do Teste

**Organização da Suite:**
```typescript
// Padrão Java de `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {
        @Test
        @DisplayName("should create student when input is valid")
        void shouldCreateStudentWhenInputIsValid() { ... }
    }

    @Nested
    @DisplayName("Query methods")
    class QueryMethods {
        @Test
        @DisplayName("should return paged students")
        void shouldReturnPagedStudents() { ... }
    }
}
```

**Padrões:**
- Usar `@Nested` para separar os comportamentos de comando e consulta, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- Usar `@DisplayName` em classes e métodos de teste para uma intenção legível, como visto em todos os testes de backend.
- Manter constantes determinísticas no topo da classe para IDs e timestamps, por exemplo, em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.
- Adicionar métodos de fábrica auxiliares (helper factory methods) no final da classe de teste para construir DTOs e entidades, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.

## Mocking (Simulação)

**Framework:**
- Mockito com extensão JUnit 5 é a abordagem de simulação padrão do backend em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- A configuração do recurso de teste do Mockito está presente em `server/api-aprimorar/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`.

**Padrões:**
```typescript
// Padrão Java de `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`
@Mock
private ParentRepository parentRepo;

@InjectMocks
private ParentService parentService;

when(parentRepo.findById(id)).thenReturn(Optional.of(input));

ParentResponseDTO actual = parentService.findById(id);

assertThat(actual).isEqualTo(expected);
verify(parentRepo).findById(id);
```

**O que simular (Mock):**
- Simular repositórios, mappers e colaboradores de infraestrutura ao testar a lógica de serviço, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`.
- Simular o `Clock` para regras de negócio sensíveis ao tempo, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.
- Usar `MockMvcBuilders.standaloneSetup(...)` para testes de limite de tratador de exceção/controller em vez da inicialização completa da aplicação, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`.

**O que NÃO simular:**
- Não simule a entidade de domínio sob validação em testes de domínio puros; instancie entidades reais diretamente, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java`.
- Não adicione simulações de frontend hoje porque não há um harness de teste de frontend configurado sob `client/`.

## Fixtures e Fábricas

**Dados de Teste:**
```typescript
// Padrão Java de `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`
private static final UUID STUDENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
private static final Instant CREATED_AT = Instant.parse("2026-01-05T08:00:00Z");

private static StudentRequestDTO request() {
    return new StudentRequestDTO(...);
}
```

**Localização:**
- As fixtures são definidas inline dentro de cada classe de teste do backend, em vez de serem centralizadas em fábricas compartilhadas, por exemplo, em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.

## Cobertura

**Requisitos:**
- O relatório JaCoCo está configurado em `server/api-aprimorar/pom.xml`, mas nenhum limite de cobertura mínimo explícito foi detectado.
- `./mvnw verify` é o comando de verificação de backend mais amplo documentado no `server/api-aprimorar/AGENTS.md`.

**Visualizar Cobertura:**
```bash
cd server/api-aprimorar && ./mvnw verify   # Gera target/site/jacoco/index.html
```

## Tipos de Teste

**Testes Unitários:**
- A maioria dos testes de backend são testes unitários em torno de serviços e entidades de domínio usando Mockito ou construção direta de objetos, por exemplo, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.

**Testes de Integração:**
- Existe integração MVC leve para tratamento de exceções em `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java` usando `MockMvc` com um controller standalone.
- Existe um teste de fumaça de contexto Spring mínimo em `server/api-aprimorar/src/test/java/com/aprimorar/api/ApiAprimorarApplicationTests.java`; ele exclui a autoconfiguração de datasource, JPA, Flyway e repositório para manter a inicialização isolada do banco de dados.

**Testes E2E:**
- Não utilizados. Nenhuma configuração de Playwright, Cypress ou E2E de frontend foi detectada na raiz do repositório, em `client/` ou em `server/api-aprimorar/`.

## Padrões Comuns

**Testes Assíncronos:**
```typescript
Não aplicável nos testes atuais. Não foram detectados testes de frontend pesados em async/await ou fluxos de teste de backend assíncronos.
```

**Testes de Erro:**
```typescript
// Padrão Java de `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`
assertThatThrownBy(() -> input.updateDetails(...))
    .isInstanceOf(InvalidEventException.class)
    .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
```

- Preferir `assertThatThrownBy` com o tipo de exceção e verificações de mensagem em português, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- Para respostas de erro HTTP, assevere tanto o status quanto os campos do corpo JSON usando `jsonPath`, como em `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`.

---

*Análise de testes: 17-04-2026*
