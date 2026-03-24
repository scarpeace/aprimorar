# Plano Simples: Erros de Student com Springdoc + Kubb

Este documento mostra um plano direto para:

1. Documentar respostas de erro no backend (Springdoc)
2. Gerar tipos/schemas de erro no frontend (Kubb)
3. Exibir mensagens de erro da API no client

Foco: endpoints de `student`.

---

## Resultado esperado

Depois de implementar:

- O Swagger mostra erros `400`, `404` e `409` com schema `ApiError`
- O Kubb gera tipo e schema de erro a partir do OpenAPI
- O frontend consegue mostrar mensagens da exception (em portugues)

---

## Etapa 1 - Documentar erros no GlobalExceptionHandler

Arquivo: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`

Objetivo: colocar `@ApiResponse` nos handlers para o Springdoc incluir os erros automaticamente nas operacoes.

Exemplo pequeno (404):

```java
@ExceptionHandler(StudentNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
@ApiResponse(
    responseCode = "404",
    description = "Aluno nao encontrado",
    content = @Content(schema = @Schema(implementation = ApiError.class))
)
public ResponseEntity<ApiError> handleStudentNotFound(
        RuntimeException ex,
        HttpServletRequest request
) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
}
```

Exemplo pequeno (409):

```java
@ExceptionHandler(StudentAlreadyExistException.class)
@ResponseStatus(HttpStatus.CONFLICT)
@ApiResponse(
    responseCode = "409",
    description = "Conflito de dados (CPF/email ja existem)",
    content = @Content(schema = @Schema(implementation = ApiError.class))
)
public ResponseEntity<ApiError> handleStudentConflict(
        RuntimeException ex,
        HttpServletRequest request
) {
    return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
}
```

Exemplo pequeno (400 de regra de negocio):

```java
@ExceptionHandler(InvalidStudentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ApiResponse(
    responseCode = "400",
    description = "Dados invalidos",
    content = @Content(schema = @Schema(implementation = ApiError.class))
)
public ResponseEntity<ApiError> handleInvalidStudent(
        RuntimeException ex,
        HttpServletRequest request
) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
}
```

---

## Etapa 2 - Garantir que o schema ApiError esteja bom

Arquivo: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/ApiError.java`

Objetivo: o OpenAPI precisa enxergar os campos do erro de forma clara.

Exemplo pequeno:

```java
@Getter
@Schema(description = "Resposta padrao de erro da API")
public class ApiError {
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;
    private final Instant timestamp;
}
```

Obs: se nao quiser `@Schema` agora, pode manter sem ele. O importante e o tipo aparecer no OpenAPI.

---

## Etapa 3 - Ajuste opcional de propriedade Springdoc

Arquivo: `server/api-aprimorar/src/main/resources/application.properties`

Objetivo: evitar comportamento de respostas genericas indesejadas.

Exemplo pequeno:

```properties
springdoc.override-with-generic-response=false
```

---

## Etapa 4 - Gerar OpenAPI atualizado e sincronizar Kubb

Objetivo: fazer o frontend receber os novos erros documentados.

Comandos:

```bash
# terminal 1 (backend)
cd server/api-aprimorar
./mvnw spring-boot:run

# terminal 2 (frontend)
cd client
npm run generate
```

Exemplo pequeno do que deve aparecer no frontend (arquivos gerados):

```text
client/src/gen/types/ApiError.ts
client/src/gen/schemas/apiErrorSchema.ts
```

Se esses arquivos nao aparecerem, revise os `@ApiResponse` da Etapa 1.

---

## Etapa 5 - Usar erro gerado no frontend

Arquivo sugerido: `client/src/lib/shared/api-errors.ts`

Objetivo: mostrar mensagem vinda do backend (ex.: "Aluno nao encontrado").

Exemplo pequeno:

```ts
import type { ApiError } from "@/kubb";

export function getFriendlyErrorMessage(error: unknown): string {
  const axiosError = error as {
    response?: { data?: ApiError; status?: number };
  };
  const apiError = axiosError.response?.data;

  if (apiError?.message) return apiError.message;
  if (axiosError.response?.status === 400) return "Dados invalidos";
  if (axiosError.response?.status === 404) return "Aluno nao encontrado";
  if (axiosError.response?.status === 409) return "Conflito de dados";

  return "Erro inesperado";
}
```

Exemplo pequeno de uso em mutation:

```ts
onError: (error) => {
  toast.error(getFriendlyErrorMessage(error));
};
```

---

## Etapa 6 - Checklist rapido de validacao

1. Abrir `http://localhost:8080/swagger-ui.html`
2. Verificar endpoint de student (ex.: `GET /v1/students/{studentId}`)
3. Confirmar respostas `400`, `404`, `409` com schema `ApiError`
4. Rodar `npm run generate` no client
5. Confirmar arquivos de erro gerados em `client/src/gen/`
6. Testar uma falha real no frontend e validar mensagem exibida

---

## Escopo deste plano

Este plano foca em `student` para simplificar.
Depois, o mesmo padrao pode ser repetido para `parent`, `event`, `employee` e `dashboard`.
