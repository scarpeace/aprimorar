# Integrações Externas

**Data da Análise:** 17-04-2026

## APIs & Serviços Externos

**Integração HTTP interna:**
- API REST do backend Aprimorar - fonte primária de dados para o SPA
  - SDK/Cliente: Instância do Axios em `client/src/lib/shared/api.ts` mais hooks gerados do React Query configurados em `client/kubb.config.ts`
  - Autenticação: Não detectada na implementação atual; nenhuma fiação de cabeçalho de autenticação foi encontrada em `client/src/lib/shared/api.ts` ou em classes de segurança do backend

**Contrato/geração de código:**
- Documento OpenAPI servido pelo backend em `/v3/api-docs`
  - SDK/Cliente: Gerador Springdoc em `server/api-aprimorar/pom.xml` e `server/api-aprimorar/src/main/resources/application.yml`
  - Autenticação: Nenhuma detectada; o Kubb lê `http://localhost:8080/v3/api-docs` diretamente em `client/kubb.config.ts`

**Bibliotecas do lado do navegador:**
- Bibliotecas de toast, gráficos e seletor de data são apenas integrações de UI locais (`client/src/App.tsx`, `client/src/features/dashboard/components/PizzaChart.tsx`, `client/src/components/ui/date-time-input.tsx`); nenhuma chamada de API SaaS de terceiros foi detectada

**Não detectado:**
- Nenhuma integração ativa com Stripe, Supabase, AWS, Firebase, Sentry, Redis, Kafka, RabbitMQ, e-mail, SMS ou gateway de pagamento foi encontrada nos arquivos de código rastreados
- O `README.md` menciona JWT e Google Calendar, mas o código/configuração atual não mostra uma integração implementada; `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java` contém apenas um comentário TODO para campos do Google Calendar

## Armazenamento de Dados

**Bancos de Dados:**
- PostgreSQL
  - Conexão: configurada diretamente em `server/api-aprimorar/src/main/resources/application-dev.yml`
  - Cliente: Spring Data JPA + Hibernate via `server/api-aprimorar/pom.xml`

**Armazenamento de Arquivos:**
- Apenas sistema de arquivos local para artefatos de código/build; nenhuma integração de armazenamento de objetos externo detectada

**Caching:**
- Cache de requisição do lado do cliente via TanStack Query em `client/src/lib/shared/queryClient.ts`
- Nenhum Redis ou serviço de cache externo detectado para o backend

## Autenticação & Identidade

**Provedor de Autenticação:**
- Não detectado na base de código atual
  - Implementação: o backend expõe endpoints abertos configurados para CORS em `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java`; nenhuma classe de Spring Security ou de validação de token foi encontrada sob `server/api-aprimorar/src/main/java/`

## Monitoramento & Observabilidade

**Rastreamento de Erros:**
- Nenhum detectado

**Logs:**
- Logging do Spring Boot para logs de aplicação, com `com.aprimorar` definido como `DEBUG` em `server/api-aprimorar/src/main/resources/application-dev.yml`
- O frontend atualmente registra falhas de API/Zod no console do navegador em `client/src/lib/shared/api.ts`

## CI/CD & Implantação (Deployment)

**Hospedagem:**
- Não especificada pela configuração do repositório

**Pipeline de CI:**
- Nenhum detectado; nenhuma GitHub Actions ou outra configuração de CI foi explorada nos arquivos específicos de foco

## Configuração de Ambiente

**Variáveis de ambiente necessárias:**
- `VITE_API_URL` - substituição opcional da URL base da API do frontend em `client/src/lib/shared/api.ts`
- Nenhuma variável de ambiente de backend foi detectada em `server/api-aprimorar/src/main/resources/` ou `server/api-aprimorar/src/main/java/`

**Localização de segredos:**
- Nenhum arquivo `.env` foi detectado na raiz do repositório, em `client/` ou em `server/api-aprimorar/`
- As credenciais do banco de dados de desenvolvimento local são configuradas diretamente em `server/api-aprimorar/src/main/resources/application-dev.yml`
- `server/api-aprimorar/docker-compose.yml` está presente para a infraestrutura local, mas seu conteúdo não foi citado aqui

## Webhooks & Callbacks

**Entrada (Incoming):**
- Nenhum detectado

**Saída (Outgoing):**
- Nenhum detectado

---

*Auditoria de integração: 17-04-2026*
