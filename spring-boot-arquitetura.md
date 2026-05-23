# Arquitetura Spring Boot — Cadastro de Aulas, Alunos e Professores

> Conversa sobre design de aplicações MVC com Spring Boot, evoluindo de Anemic Domain para Rich Domain e Modulith.

---

## 1. Arquitetura MVC básica — Cadastro de Aulas

### Visão geral das camadas

```
Client → Controller → Service → Repository → Database
              ↑            ↑           ↑
            DTOs        Mapper      Entity
```

Responsabilidades:

| Camada | Responsabilidade |
|---|---|
| `Entity` | Representa a tabela no banco. Só anotações JPA, sem lógica. |
| `DTO` | Contrato da API com o mundo externo. Validações ficam aqui. |
| `Mapper` | Único lugar que converte DTO ↔ Entity. Mantém as outras camadas limpas. |
| `Repository` | Acesso ao banco. Só queries, sem lógica de negócio. |
| `Service` | Toda a lógica de negócio vive aqui. Não conhece HTTP. |
| `Controller` | Fina camada HTTP. Recebe request, chama service, retorna response. |

---

### Entity — `Aula.java`

```java
@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private Integer vagas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAula status;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
```

```java
public enum StatusAula {
    AGENDADA, CANCELADA, CONCLUIDA
}
```

---

### DTOs

```java
// Request: o que o cliente envia para criar uma aula
public record AulaCriacaoRequest(
    @NotBlank(message = "Título é obrigatório")
    String titulo,

    @NotBlank(message = "Descrição é obrigatória")
    String descricao,

    @NotNull(message = "Data de início é obrigatória")
    @Future(message = "Data de início deve ser no futuro")
    LocalDateTime dataHoraInicio,

    @NotNull(message = "Data de fim é obrigatória")
    LocalDateTime dataHoraFim,

    @NotBlank(message = "Professor é obrigatório")
    String professor,

    @NotNull @Positive(message = "Vagas deve ser um número positivo")
    Integer vagas
) {}
```

```java
// Response: o que a API devolve ao cliente
public record AulaResponse(
    UUID id,
    String titulo,
    String descricao,
    LocalDateTime dataHoraInicio,
    LocalDateTime dataHoraFim,
    String professor,
    Integer vagas,
    StatusAula status,
    LocalDateTime criadoEm
) {}
```

```java
// Request para atualização parcial (todos os campos opcionais)
public record AulaAtualizacaoRequest(
    String titulo,
    String descricao,
    LocalDateTime dataHoraInicio,
    LocalDateTime dataHoraFim,
    String professor,
    Integer vagas,
    StatusAula status
) {}
```

---

### Mapper — `AulaMapper.java`

```java
@Component
public class AulaMapper {

    public Aula toEntity(AulaCriacaoRequest request) {
        return Aula.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .dataHoraInicio(request.dataHoraInicio())
                .dataHoraFim(request.dataHoraFim())
                .professor(request.professor())
                .vagas(request.vagas())
                .status(StatusAula.AGENDADA)
                .build();
    }

    public AulaResponse toResponse(Aula aula) {
        return new AulaResponse(
                aula.getId(),
                aula.getTitulo(),
                aula.getDescricao(),
                aula.getDataHoraInicio(),
                aula.getDataHoraFim(),
                aula.getProfessor(),
                aula.getVagas(),
                aula.getStatus(),
                aula.getCriadoEm()
        );
    }

    public void updateEntityFromRequest(AulaAtualizacaoRequest request, Aula aula) {
        if (request.titulo() != null)         aula.setTitulo(request.titulo());
        if (request.descricao() != null)      aula.setDescricao(request.descricao());
        if (request.dataHoraInicio() != null) aula.setDataHoraInicio(request.dataHoraInicio());
        if (request.dataHoraFim() != null)    aula.setDataHoraFim(request.dataHoraFim());
        if (request.professor() != null)      aula.setProfessor(request.professor());
        if (request.vagas() != null)          aula.setVagas(request.vagas());
        if (request.status() != null)         aula.setStatus(request.status());
    }
}
```

---

### Repository — `AulaRepository.java`

```java
@Repository
public interface AulaRepository extends JpaRepository<Aula, UUID> {

    List<Aula> findByStatus(StatusAula status);

    List<Aula> findByProfessorIgnoreCase(String professor);

    @Query("""
        SELECT a FROM Aula a
        WHERE a.dataHoraInicio BETWEEN :inicio AND :fim
        AND (:status IS NULL OR a.status = :status)
        ORDER BY a.dataHoraInicio ASC
    """)
    List<Aula> findByPeriodoEStatus(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("status") StatusAula status
    );

    boolean existsByProfessorAndDataHoraInicioAndDataHoraFim(
        String professor,
        LocalDateTime inicio,
        LocalDateTime fim
    );
}
```

---

### Service — `AulaService.java`

```java
@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;

    public AulaResponse criar(AulaCriacaoRequest request) {
        validarConflitoDeProfessor(request.professor(), request.dataHoraInicio(), request.dataHoraFim(), null);
        validarPeriodo(request.dataHoraInicio(), request.dataHoraFim());

        Aula aula = aulaMapper.toEntity(request);
        Aula salva = aulaRepository.save(aula);

        return aulaMapper.toResponse(salva);
    }

    public AulaResponse buscarPorId(UUID id) {
        return aulaMapper.toResponse(buscarEntidade(id));
    }

    public List<AulaResponse> listarTodas() {
        return aulaRepository.findAll()
                .stream()
                .map(aulaMapper::toResponse)
                .toList();
    }

    public List<AulaResponse> listarPorStatus(StatusAula status) {
        return aulaRepository.findByStatus(status)
                .stream()
                .map(aulaMapper::toResponse)
                .toList();
    }

    public AulaResponse atualizar(UUID id, AulaAtualizacaoRequest request) {
        Aula aula = buscarEntidade(id);

        if (aula.getStatus() == StatusAula.CANCELADA) {
            throw new IllegalStateException("Não é possível editar uma aula cancelada.");
        }

        aulaMapper.updateEntityFromRequest(request, aula);
        return aulaMapper.toResponse(aulaRepository.save(aula));
    }

    public void cancelar(UUID id) {
        Aula aula = buscarEntidade(id);

        if (aula.getStatus() == StatusAula.CONCLUIDA) {
            throw new IllegalStateException("Não é possível cancelar uma aula já concluída.");
        }

        aula.setStatus(StatusAula.CANCELADA);
        aulaRepository.save(aula);
    }

    private Aula buscarEntidade(UUID id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada com id: " + id));
    }

    private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
        }
    }

    private void validarConflitoDeProfessor(String professor, LocalDateTime inicio, LocalDateTime fim, UUID idIgnorar) {
        boolean conflito = aulaRepository.existsByProfessorAndDataHoraInicioAndDataHoraFim(professor, inicio, fim);
        if (conflito) {
            throw new IllegalStateException("O professor já tem uma aula agendada neste horário.");
        }
    }
}
```

---

### Controller — `AulaController.java`

```java
@RestController
@RequestMapping("/api/v1/aulas")
@RequiredArgsConstructor
@Validated
public class AulaController {

    private final AulaService aulaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AulaResponse criar(@RequestBody @Valid AulaCriacaoRequest request) {
        return aulaService.criar(request);
    }

    @GetMapping("/{id}")
    public AulaResponse buscarPorId(@PathVariable UUID id) {
        return aulaService.buscarPorId(id);
    }

    @GetMapping
    public List<AulaResponse> listarTodas(
        @RequestParam(required = false) StatusAula status
    ) {
        if (status != null) return aulaService.listarPorStatus(status);
        return aulaService.listarTodas();
    }

    @PutMapping("/{id}")
    public AulaResponse atualizar(
        @PathVariable UUID id,
        @RequestBody @Valid AulaAtualizacaoRequest request
    ) {
        return aulaService.atualizar(id, request);
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable UUID id) {
        aulaService.cancelar(id);
    }
}
```

---

### Tratamento global de erros — `GlobalExceptionHandler.java`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(EntityNotFoundException ex) {
        return Map.of("erro", ex.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleBusinessRule(RuntimeException ex) {
        return Map.of("erro", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return Map.of("erros", erros);
    }
}
```

---

## 2. Evoluindo para Rich Domain

### Conceito

- **Anemic Domain** — a entidade é só um saco de dados com getters/setters. Toda lógica vive no `Service`.
- **Rich Domain** — a entidade conhece suas próprias regras. Ela protege seu estado e só permite transições válidas através de métodos com intenção clara.

**Divisão de responsabilidades:**
- **Na entidade**: regras que dizem respeito ao objeto em si — invariantes, transições de estado, validações internas.
- **No Service**: regras que envolvem outros objetos ou infraestrutura — checar conflito de horário no banco, orquestrar múltiplas entidades, disparar eventos.

---

### Entity rica — `Aula.java`

```java
@Entity
@Table(name = "aulas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private Integer vagas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAula status;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    // Factory method: única porta de entrada para criar uma Aula válida
    public static Aula criar(String titulo, String descricao, LocalDateTime inicio,
                              LocalDateTime fim, String professor, Integer vagas) {
        Aula aula = new Aula();
        aula.titulo = Objects.requireNonNull(titulo, "Título é obrigatório");
        aula.descricao = Objects.requireNonNull(descricao, "Descrição é obrigatória");
        aula.professor = Objects.requireNonNull(professor, "Professor é obrigatório");
        aula.status = StatusAula.AGENDADA;

        aula.definirPeriodo(inicio, fim);
        aula.definirVagas(vagas);

        return aula;
    }

    // Comportamentos com intenção explícita
    public void cancelar() {
        if (this.status == StatusAula.CONCLUIDA) {
            throw new IllegalStateException("Não é possível cancelar uma aula já concluída.");
        }
        if (this.status == StatusAula.CANCELADA) {
            throw new IllegalStateException("A aula já está cancelada.");
        }
        this.status = StatusAula.CANCELADA;
    }

    public void concluir() {
        if (this.status != StatusAula.AGENDADA) {
            throw new IllegalStateException("Só é possível concluir uma aula agendada.");
        }
        this.status = StatusAula.CONCLUIDA;
    }

    public void reagendar(LocalDateTime novoInicio, LocalDateTime novoFim) {
        if (this.status != StatusAula.AGENDADA) {
            throw new IllegalStateException("Só é possível reagendar uma aula com status AGENDADA.");
        }
        definirPeriodo(novoInicio, novoFim);
    }

    public void atualizarDados(String titulo, String descricao, String professor, Integer vagas) {
        if (this.status == StatusAula.CANCELADA || this.status == StatusAula.CONCLUIDA) {
            throw new IllegalStateException("Não é possível editar uma aula " + this.status.name().toLowerCase() + ".");
        }
        if (titulo != null)    this.titulo = titulo;
        if (descricao != null) this.descricao = descricao;
        if (professor != null) this.professor = professor;
        if (vagas != null)     definirVagas(vagas);
    }

    // Queries sobre o próprio estado
    public boolean isAgendada() {
        return this.status == StatusAula.AGENDADA;
    }

    public boolean estaNoFuturo() {
        return this.dataHoraInicio.isAfter(LocalDateTime.now());
    }

    public Duration duracao() {
        return Duration.between(this.dataHoraInicio, this.dataHoraFim);
    }

    // Regras internas (private)
    private void definirPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Objects.requireNonNull(inicio, "Data de início é obrigatória");
        Objects.requireNonNull(fim, "Data de fim é obrigatória");
        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
        }
        this.dataHoraInicio = inicio;
        this.dataHoraFim = fim;
    }

    private void definirVagas(Integer vagas) {
        if (vagas == null || vagas <= 0) {
            throw new IllegalArgumentException("Vagas deve ser um número positivo.");
        }
        this.vagas = vagas;
    }
}
```

---

### Service com Rich Domain — `AulaService.java`

```java
@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;

    public AulaResponse criar(AulaCriacaoRequest request) {
        validarConflitoDeProfessor(request.professor(), request.dataHoraInicio(), request.dataHoraFim());

        Aula aula = Aula.criar(
                request.titulo(),
                request.descricao(),
                request.dataHoraInicio(),
                request.dataHoraFim(),
                request.professor(),
                request.vagas()
        );

        return aulaMapper.toResponse(aulaRepository.save(aula));
    }

    public AulaResponse buscarPorId(UUID id) {
        return aulaMapper.toResponse(buscarEntidade(id));
    }

    public List<AulaResponse> listarTodas() {
        return aulaRepository.findAll().stream()
                .map(aulaMapper::toResponse)
                .toList();
    }

    public AulaResponse atualizar(UUID id, AulaAtualizacaoRequest request) {
        Aula aula = buscarEntidade(id);

        aula.atualizarDados(
                request.titulo(),
                request.descricao(),
                request.professor(),
                request.vagas()
        );

        if (request.dataHoraInicio() != null || request.dataHoraFim() != null) {
            LocalDateTime novoInicio = request.dataHoraInicio() != null ? request.dataHoraInicio() : aula.getDataHoraInicio();
            LocalDateTime novoFim    = request.dataHoraFim()    != null ? request.dataHoraFim()    : aula.getDataHoraFim();
            aula.reagendar(novoInicio, novoFim);
        }

        return aulaMapper.toResponse(aulaRepository.save(aula));
    }

    public void cancelar(UUID id) {
        Aula aula = buscarEntidade(id);
        aula.cancelar();
        aulaRepository.save(aula);
    }

    public void concluir(UUID id) {
        Aula aula = buscarEntidade(id);
        aula.concluir();
        aulaRepository.save(aula);
    }

    private Aula buscarEntidade(UUID id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada: " + id));
    }

    private void validarConflitoDeProfessor(String professor, LocalDateTime inicio, LocalDateTime fim) {
        boolean conflito = aulaRepository.existsByProfessorAndDataHoraInicioAndDataHoraFim(professor, inicio, fim);
        if (conflito) {
            throw new IllegalStateException("O professor já tem uma aula agendada neste horário.");
        }
    }
}
```

---

### O que mudou: Anemic vs Rich Domain

| | Anemic | Rich Domain |
|---|---|---|
| `new Aula()` / setters | Qualquer um cria um objeto inválido | Só via `Aula.criar(...)` — nasce válida |
| `cancelar()` | `if` no Service | A própria entidade recusa se não puder |
| Validar período | Método privado no Service | Método privado na entidade, reutilizado |
| Setters | `public` para tudo | Removidos — estado muda por comportamento |
| Testar regras | Precisa subir Spring ou mockar | Teste unitário puro, sem Spring |

---

## 3. Estrutura de pacotes — adicionando Aluno

Organização por **módulo/domínio**, não por camada técnica.

```
src/main/java/br/com/escola/
│
├── config/
│   ├── SecurityConfig.java
│   ├── JacksonConfig.java
│   └── OpenApiConfig.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── BusinessException.java
│
├── aula/
│   ├── entity/
│   │   ├── Aula.java
│   │   └── StatusAula.java
│   ├── dto/
│   │   ├── AulaCriacaoRequest.java
│   │   ├── AulaAtualizacaoRequest.java
│   │   └── AulaResponse.java
│   ├── mapper/
│   │   └── AulaMapper.java
│   ├── repository/
│   │   └── AulaRepository.java
│   ├── service/
│   │   └── AulaService.java
│   └── controller/
│       └── AulaController.java
│
├── aluno/
│   ├── entity/
│   │   ├── Aluno.java
│   │   └── StatusAluno.java
│   ├── dto/
│   │   ├── AlunoCriacaoRequest.java
│   │   ├── AlunoAtualizacaoRequest.java
│   │   └── AlunoResponse.java
│   ├── mapper/
│   │   └── AlunoMapper.java
│   ├── repository/
│   │   └── AlunoRepository.java
│   ├── service/
│   │   └── AlunoService.java
│   └── controller/
│       └── AlunoController.java
│
└── EscolaApplication.java
```

**Por que organizar por domínio?** Com organização por camada, para entender o fluxo de "aluno" você navega por 5 pastas diferentes. Com organização por domínio, tudo que pertence a `aluno` vive em `aluno/`. Para deletar o módulo inteiro, basta apagar a pasta.

---

## 4. Agrupando Aluno e Professor em `pessoas/`

Aluno e Professor compartilham o mesmo contexto de negócio (são pessoas do sistema escolar), então faz sentido agrupá-los em um pacote pai.

```
src/main/java/br/com/escola/
│
├── config/
├── exception/
│
├── aula/
│   ├── entity/
│   ├── dto/
│   ├── mapper/
│   ├── repository/
│   ├── service/
│   └── controller/
│
├── pessoas/
│   ├── aluno/
│   │   ├── entity/
│   │   │   ├── Aluno.java
│   │   │   └── StatusAluno.java
│   │   ├── dto/
│   │   ├── mapper/
│   │   ├── repository/
│   │   ├── service/
│   │   └── controller/
│   │
│   └── professor/
│       ├── entity/
│       │   ├── Professor.java
│       │   └── EspecialidadeProfessor.java
│       ├── dto/
│       ├── mapper/
│       ├── repository/
│       ├── service/
│       └── controller/
│
└── EscolaApplication.java
```

> `pessoas/` é só um agrupador de pacotes — não tem código próprio (`Service`, `Controller`). Se surgir a necessidade de criar um `PessoaService.java` genérico, é sinal de que pode ser hora de pensar em herança de entidade com `@Inheritance`.

---

## 5. Evoluindo para Modulith

### Conceito

**Modulith** torna os limites entre módulos **explícitos e enforçados**. O Spring Modulith detecta dependências proibidas em tempo de teste e gera documentação da arquitetura automaticamente.

**Regra central**: um módulo só expõe o que está no seu pacote raiz. Tudo dentro de `internal/` é privado — outros módulos que tentarem importar de lá falham no teste de arquitetura.

---

### Estrutura de pacotes com Modulith

```
src/main/java/br/com/escola/
│
├── aula/                          ← módulo público
│   ├── AulaService.java           ← API pública do módulo
│   ├── AulaResponse.java          ← DTO público, outros módulos podem usar
│   ├── AulaAgendadaEvent.java     ← evento publicado para outros módulos
│   │
│   └── internal/                  ← PRIVADO — nenhum outro módulo importa daqui
│       ├── Aula.java
│       ├── StatusAula.java
│       ├── AulaMapper.java
│       ├── AulaRepository.java
│       ├── AulaCriacaoRequest.java
│       ├── AulaAtualizacaoRequest.java
│       └── AulaController.java
│
├── pessoas/
│   ├── aluno/
│   │   ├── AlunoService.java      ← API pública
│   │   ├── AlunoResponse.java
│   │   └── internal/
│   │       ├── Aluno.java
│   │       ├── StatusAluno.java
│   │       ├── AlunoMapper.java
│   │       ├── AlunoRepository.java
│   │       └── AlunoController.java
│   │
│   └── professor/
│       ├── ProfessorService.java
│       ├── ProfessorResponse.java
│       └── internal/
│           ├── Professor.java
│           ├── EspecialidadeProfessor.java
│           ├── ProfessorMapper.java
│           ├── ProfessorRepository.java
│           └── ProfessorController.java
│
├── config/
├── exception/
└── EscolaApplication.java
```

---

### O que muda concretamente

#### 1. Dependência entre módulos passa pelo serviço público

```java
// ERRADO no modulith — importa de dentro do internal de outro módulo
// import br.com.escola.pessoas.aluno.internal.AlunoRepository;

// CERTO — usa a API pública do módulo aluno
private final AlunoService alunoService;
```

#### 2. Comunicação assíncrona via eventos

```java
// aula/AulaAgendadaEvent.java — evento público do módulo
public record AulaAgendadaEvent(UUID aulaId, String professor, LocalDateTime dataHoraInicio) {}
```

```java
// aula/internal/AulaService.java — publica o evento
private final ApplicationEventPublisher events;

public AulaResponse criar(AulaCriacaoRequest request) {
    // ...
    events.publishEvent(new AulaAgendadaEvent(salva.getId(), salva.getProfessor(), salva.getDataHoraInicio()));
    return aulaMapper.toResponse(salva);
}
```

```java
// pessoas/aluno/internal/AlunoNotificacaoListener.java — escuta no módulo aluno
@ApplicationModuleListener
public void onAulaAgendada(AulaAgendadaEvent event) {
    // notifica alunos inscritos, por exemplo
}
```

#### 3. Teste de arquitetura vira parte do build

```java
@Test
void verificarEstruturaDosModulos() {
    ApplicationModules.of(EscolaApplication.class).verify();
}
```

#### 4. Dependência no `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-core</artifactId>
</dependency>
```

---

### Quando migrar para Modulith

O modulith brilha quando você tem mais de 2-3 módulos interagindo entre si e começa a perder controle de quem depende de quem. Para um projeto pequeno (aula + aluno + professor), a estrutura por domínio sem modulith já funciona bem.

O modulith é também um trampolim natural para microsserviços: cada módulo já tem seus limites definidos e pode ser extraído com menos dor quando chegar a hora.
