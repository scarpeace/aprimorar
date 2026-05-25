# Plano de Observabilidade (Spring Modulith)

Este documento descreve um plano incremental para observabilidade no backend (`server/`), mantendo `pessoas` como modulo unico.

## Objetivo

- Dar visibilidade de fluxo entre modulos (especialmente `atendimentos` <-> `pessoas::api`).
- Melhorar diagnostico de erro/latencia sem aumentar muito a complexidade.
- Evoluir em duas etapas: pacote minimo e pacote completo.

## Contexto Atual

- Modulith core ja esta presente (`spring-modulith-starter-core`).
- Ainda nao ha `Actuator` nem `spring-modulith-observability` configurados.
- Modulo `pessoas` esta definido como `@ApplicationModule` e `NamedInterface("api")` para contratos publicos.

---

## Pacote Minimo (PR pequeno)

### 1) Dependencias

Adicionar no `server/pom.xml`:

- `org.springframework.boot:spring-boot-starter-actuator`
- `org.springframework.modulith:spring-modulith-observability`
- `io.micrometer:micrometer-registry-prometheus` (opcional, mas recomendado)

### 2) Configuracao base

Adicionar em `server/src/main/resources/application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      probes:
        enabled: true
  metrics:
    tags:
      application: ${spring.application.name}
```

### 3) Convencoes de log (baixo custo)

- Padronizar logs de caso de uso com campos consistentes:
  - `module` (ex.: `pessoas`)
  - `use_case` (ex.: `create_colaborador`)
  - `entity_id` quando aplicavel

Sugestao: usar MDC no inicio dos fluxos HTTP mais criticos (sem over-engineering).

### 4) Verificacao

Com app rodando em `server/`:

- `GET /actuator/health`
- `GET /actuator/metrics`
- `GET /actuator/prometheus` (se registry habilitado)

### 5) Entrega esperada

- Endpoints de observabilidade ativos.
- Metricas tecnicas basicas disponiveis (JVM, HTTP, datasource, etc.).
- Visao inicial de comportamento modular via observability do Modulith.

---

## Pacote Completo (fase futura)

### 1) Tracing distribuido

Adicionar stack de tracing conforme padrao da equipe (OpenTelemetry/Zipkin/Tempo).

Configuracoes tipicas:

```yaml
management:
  tracing:
    enabled: true
    sampling:
      probability: 0.1
```

> Ajustar `sampling` por ambiente (dev maior, prod menor/controlado).

### 2) Metricas de negocio (custom)

Criar metricas por caso de uso critico no modulo `pessoas`:

- `pessoas.colaborador.created.count`
- `pessoas.colaborador.archived.count`
- `pessoas.colaborador.delete.denied.count`
- `pessoas.colaborador.request.duration` (timer)

Regras:

- Nome estavel e orientado a dominio.
- Baixa cardinalidade de tags (evitar ID de entidade como tag).
- Tags uteis: `module`, `use_case`, `result`.

### 3) Eventos e fluxos async

Se houver externalizacao/outbox no futuro:

- medir published/consumed/retry/failure
- criar alertas para backlog/retry anormal

### 4) Dashboards

Dashboard minimo por modulo:

- Throughput HTTP (RPS)
- Latencia p95/p99
- Erro 4xx/5xx
- Metricas de negocio do `pessoas`
- (futuro) eventos publicados/consumidos

### 5) Alertas iniciais

- Taxa de erro 5xx acima de limiar por 5 min
- Latencia p95 acima de limiar por endpoint critico
- Queda brusca de throughput (anomalia)

---

## Decisoes Arquiteturais

- Modulo unico `pessoas` e suficiente neste momento.
- Nao separar `aluno/colaborador/responsavel` em modulos ate existir dor real de governanca.
- Observabilidade deve refletir fronteira atual: foco em `pessoas` como bloco arquitetural.

---

## Checklist de Implementacao

### Pacote Minimo

- [ ] Adicionar dependencias no `server/pom.xml`
- [ ] Configurar `management.*` em `application.yml`
- [ ] Subir app e validar endpoints actuator
- [ ] Registrar evidencias (print/curl) no PR

### Pacote Completo

- [ ] Definir stack de tracing oficial
- [ ] Instrumentar metricas de negocio de `pessoas`
- [ ] Criar dashboard inicial
- [ ] Configurar alertas basicos
