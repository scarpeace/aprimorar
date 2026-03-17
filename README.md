# 🏪 Marketplace Multi-ONG - Desafio Full Stack

## 1. Visão Geral do Projeto

Este projeto foi desenvolvido como um desafio pessoal full-stack, focado na criação de um sistema de gerenciamento de alunos e atendimentos de um cursinho de preparatório para ENEM e aulas particulares. O objetivo principal era demonstrar proficiência em desenvolvimento seguro, performático em cima das melhores práticas de arquitetura e código limpo.

<br/>

## 2. 📋 Requisitos e Funcionalidades Principais


O projeto foi construído para atender aos seguintes requisitos:


* **CRUD de Produtos:** Funcionalidades completas de Criação, Leitura, Atualização e Exclusão de alunos, responsáveis e aulas.


* **Categorização:** Implementação de um sistema robusto de categorias de atendimentos.


[//]: # (* **Segurança e Autorização:** Implementação de controle de acesso para impedir que usuários modifiquem dados de terceiros.)


[//]: # (* **Autenticação JWT:** Sistema de login funcional e seguro utilizando JSON Web Tokens &#40;JWT&#41;.)


* **Paginação Performática:** Paginação da lista de atendimentos e alunos implementada diretamente no banco de dados.


* **Filtros Manuais:** Opções manuais para filtrar a lista de alunos.


[//]: # (* **Busca Inteligente por IA:** Utilização da Google Gemini API para interpretação de linguagem natural &#40;ex: "Eu quero chocolate"&#41; e retorno de produtos relacionados.)


[//]: # (* **Mecanismo de Fallback:** Implementação de um mecanismo de contingência caso a busca por IA demore ou falhe.)


[//]: # (* **Sistema de Carrinho de Compras:** Funcionalidade completa de adição e remoção de itens.)


* **Agendamento de Atendimentos via API do Google Calendar:** Fluxo de registro e agendamento de aulas via API do Google Calendar.


[//]: # (* **Registro de Logs:** Captura de logs de requisição e logs específicos da busca inteligente.)


<br/>


## 3. 🧰 Tecnologias Utilizadas


| Categoria | Tecnologia |

[//]: # (| :--- | :--- | :--- |)

| **Backend** | Java | Spring Boot | Hibernate | Mockito | JUnit | Swagger | OpenFeign

[//]: # (| **Frontend** | React | Desenvolvimento da interface do usuário |)

| **Banco de Dados** | PostgreSQL | Armazenamento persistente e eficiente de dados |

| **Containerização** | Docker | Docker e Docker Compose para ambiente de desenvolvimento isolado |

[//]: # (| **Inteligência Artificial** | Google Gemini API | Processamento de linguagem natural para a busca inteligente |)


<br/>

[//]: # ()
[//]: # ()
[//]: # (## 4. 💡 Detalhes da Implementação)

[//]: # ()
[//]: # (### 4.1. Estrutura de Autenticação e Autorização &#40;JWT&#41;)

[//]: # ()
[//]: # (A segurança foi implementada através de um sistema de cargos &#40;Roles&#41; para proteger as rotas de criação, edição e exclusão de dados. Foi utilizado **JWT** e cookies **HttpOnly** para mitigar a exposição do token.)

[//]: # ()
[//]: # (**Cargos e Responsabilidades:**)

[//]: # ()
[//]: # (| Cargo | Permissões |)

[//]: # (| :--- | :--- |)

[//]: # (| **ADMIN** | Acesso total, incluindo criação/exclusão de categorias e visualização de relatórios de observabilidade. |)

[//]: # (| **ONG** | CRUD de produtos próprios e vinculação a categorias. Acesso a todas as rotas de `USER`. |)

[//]: # (| **USER** | Realizar pedidos, visualizar pedidos e interagir com o catálogo de produtos. |)

[//]: # ()
[//]: # (### 4.2. Paginação e Filtros)

[//]: # ()
[//]: # (Para garantir a performance, a paginação e os filtros foram implementados via banco de dados &#40;`OFFSET`/`LIMIT`&#41; em vez de utilizar paginação em memória. Isso garante que apenas o *slice* de dados necessário seja carregado, mantendo a aplicação leve e rápida, mesmo com grandes volumes de dados.)

[//]: # ()
[//]: # (### 4.3. Implementação de Pedidos)

[//]: # ()
[//]: # (Um ponto de atenção no Marketplace foi o tratamento de pedidos que envolviam produtos de múltiplas ONGs.)

[//]: # ()
[//]: # (1. O **`USER`** finaliza o pedido único no carrinho &#40;processamento de pagamento&#41;.)

[//]: # ()
[//]: # (2. O sistema, ao gerar o pedido, **separa-o automaticamente** em pedidos individuais, um para cada **`ONG`** envolvida.)

[//]: # ()
[//]: # (<br />)

[//]: # ()
[//]: # (<img width="1349" height="602" alt="Captura de tela de 2025-10-22 04-24-45 &#40;1&#41;" src="https://github.com/user-attachments/assets/980ff711-c6cf-48c9-b82d-0f26311b9e47" />)

[//]: # (Essa abordagem garante que cada ONG receba apenas o registro de seu pedido, mantendo a segregação de dados e facilitando a logística de envio por parte de cada organização.)

[//]: # ()
[//]: # (### 4.4. Busca Inteligente &#40;Google Gemini API&#41;)

[//]: # ()
[//]: # (Esta foi a parte mais dinâmica do projeto. A Gemini API foi utilizada para transformar uma *query* em linguagem natural em parâmetros de busca estruturados.)

[//]: # ()
[//]: # (**Funcionamento:**)

[//]: # ()
[//]: # (1. O usuário digita uma frase &#40;ex: "Eu quero um carro bonito"&#41;.)

[//]: # ()
[//]: # (2. A API da Gemini é consumida com um `systemInstruction` para forçar o retorno de um JSON com os campos `Keywords`, `Category` e `Price`.)

[//]: # ()
[//]: # (3. O JSON retornado &#40;exemplo abaixo&#41; é usado para aplicar os filtros no banco de dados.)

[//]: # ()
[//]: # (**Usuario pesquisa: "Eu quero um carro bonito"**)

[//]: # (---)

[//]: # ()
[//]: # (**Oque eu achava que iria receber:**)

[//]: # ({ "Keywords": ["peugeot", "carro esportivo", "carro de luxo"], "Category": ["veiculo", "carro", "carro esportivo"], "Price": "null" })

[//]: # ()
[//]: # (**Oque realmente retornou:**)

[//]: # (---)

[//]: # (<img width="956" height="190" alt="Captura de tela de 2025-10-21 15-49-38 &#40;1&#41;" src="https://github.com/user-attachments/assets/4dbb2c27-e7d5-40c2-a429-45ae99ed9423" />)

[//]: # ()
[//]: # ()
[//]: # (> **Observação:** A IA, por natureza, tende a ser genérica &#40;como em `veiculo`, `carro`&#41;. Para um catálogo mais especializado, seria necessário refinar a instrução do sistema &#40;System Prompt&#41; enviada à API.)

[//]: # ()
[//]: # (<br/>)

[//]: # ()
[//]: # (## 5. 🚀 Como Instalar e Rodar Localmente)

[//]: # ()
[//]: # (Para iniciar o projeto, você precisará ter o **Docker** e o **Docker Compose** instalados, além de uma **Chave de API do Google Gemini**.)

[//]: # ()
[//]: # (### 5.1. Pré-requisitos)

[//]: # ()
[//]: # (* [**Docker**]&#40;https://www.docker.com/get-started&#41; e [**Docker Compose**]&#40;https://docs.docker.com/compose/install/&#41;)

[//]: # ()
[//]: # (* [**Git**]&#40;https://git-scm.com/&#41; para clonar o repositório.)

[//]: # ()
[//]: # (* Uma **Chave de API do Google Gemini** &#40;obtida no [Google AI Studio]&#40;https://aistudio.google.com/app/apikey&#41;&#41;.)

[//]: # ()
[//]: # (### 5.2. Configuração e Inicialização)

[//]: # ()
[//]: # (1. **Clone o Repositório:**)

[//]: # ()
[//]: # (2. **Crie o arquivo `.env`:**)

[//]: # ()
[//]: # (Dentro da pasta raiz, crie um arquivo chamado `.env` para armazenar as variáveis de ambiente com segurança:)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (JWT_SECRET_KEY=sua_chave_secreta_super_longa_e_segura_aqui)

[//]: # ()
[//]: # (DB_NAME=nome_do_banco_de_dados)

[//]: # ()
[//]: # (DB_PASSWORD=supersecretpassword)

[//]: # ()
[//]: # (DB_NAME=nome_do_banco_de_dados)

[//]: # ()
[//]: # (SECRET_API_GEMINI=sua_chave_api_gemini)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (3. **Inicie os Containers:**)

[//]: # (   Execute o Docker Compose para subir o Backend, o Frontend e o Banco de Dados.)

[//]: # ()
[//]: # (Após a inicialização, o backend estará acessível em `http://localhost:8080` &#40;ou a porta configurada&#41; e o frontend em `http://localhost:3000` &#40;ou a porta configurada&#41;.)

[//]: # ()
[//]: # (Por padrão vai ser criado um usuario ADMIN com as seguintes credenciais: admin@gmail.com | 123456)

[//]: # ()
[//]: # (Você pode mudar a geração dessas credencias no back-end antes de executar o docker em src/main/java/com/backend/marktplace/component/DataSeeder.java)

[//]: # ()
[//]: # (<br/>)

[//]: # ()
[//]: # (## 6. 🗺️ Rotas Principais)

[//]: # ()
[//]: # (### Rotas Públicas &#40;Não-Autenticadas&#41;)

[//]: # ()
[//]: # (* `/midia`: Endpoint para upload e acesso a imagens/mídias.)

[//]: # ()
[//]: # (* `/auth`: Endpoints de login e criação de novos usuários &#40;`USER` e `ONG`&#41;.)

[//]: # ()
[//]: # (### Rotas Autenticadas)

[//]: # ()
[//]: # (* `/order`: Gerenciamento de pedidos pelo `USER` &#40;visualização e criação&#41;.)

[//]: # ()
[//]: # (* `/dashboard`: Informações gerais e painel do `USER`.)

[//]: # ()
[//]: # (* `/dashboard-ong`: Área de gerenciamento da `ONG` &#40;CRUD de produtos, visualização de pedidos próprios&#41;.)

[//]: # ()
[//]: # (* `/dashboard-admin`: Painel de controle focado no `ADMIN` &#40;categorias, relatórios&#41;.)