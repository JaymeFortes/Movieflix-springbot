# Movieflix API

API REST para catálogo de filmes e avaliações, com autenticação OAuth2/JWT e controle de acesso por perfil.

## Visão Geral

Este projeto implementa casos de uso de uma plataforma de filmes (Movieflix), incluindo:

- Listagem de gêneros.
- Consulta paginada de filmes, com filtro por gênero.
- Detalhamento de filme.
- Listagem de reviews por filme.
- Cadastro de review por usuário autenticado com papel de membro.
- Consulta do perfil do usuário logado.

## Tecnologias

- Java 21
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Authorization Server (OAuth2)
- OAuth2 Resource Server (JWT)
- Bean Validation
- Banco H2 (ambiente de teste/dev)
- Maven (com Maven Wrapper)

## Estrutura do Projeto

Estrutura principal em `src/main/java/com/devsuperior/movieflix`:

- `controllers`: endpoints REST.
- `services`: regras de negocio.
- `repositories`: acesso a dados com JPA.
- `entities`: entidades do dominio.
- `dto`: objetos de transferencia.
- `config`: seguranca, CORS e configuracoes gerais.

Recursos em `src/main/resources`:

- `application.properties`
- `application-test.properties`
- `import.sql` (carga inicial de dados)

## Pre-requisitos

- JDK 21
- Git (opcional)
- Sem necessidade de instalar Maven globalmente (usa `mvnw`/`mvnw.cmd`)

## Como Executar

No Windows (PowerShell ou CMD), na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

A aplicacao sobe por padrao em:

- `http://localhost:8080`

## Configuracao por Variaveis de Ambiente

As propriedades usam valores padrao, mas podem ser sobrescritas:

- `APP_PROFILE` (default: `test`)
- `CLIENT_ID` (default: `myclientid`)
- `CLIENT_SECRET` (default: `myclientsecret`)
- `JWT_DURATION` (default: `86400` segundos)
- `CORS_ORIGINS` (default: `http://localhost:3000,http://localhost:5173`)

Exemplo (PowerShell):

```powershell
$env:APP_PROFILE="test"
$env:CLIENT_ID="myclientid"
$env:CLIENT_SECRET="myclientsecret"
.\mvnw.cmd spring-boot:run
```

## Autenticacao (OAuth2 Password Grant)

Endpoint de token:

- `POST /oauth2/token`

Formato esperado:

- `grant_type=password`
- `client_id=<CLIENT_ID>`
- `username=<email do usuario>`
- `password=<senha>`
- Basic Auth com `CLIENT_ID` e `CLIENT_SECRET`

Exemplo com `curl`:

```bash
curl --request POST "http://localhost:8080/oauth2/token" \
  --user myclientid:myclientsecret \
  --header "Content-Type: application/x-www-form-urlencoded" \
  --data "grant_type=password&client_id=myclientid&username=bob@gmail.com&password=123456"
```

Use o `access_token` retornado no header:

```text
Authorization: Bearer SEU_TOKEN
```

## Usuarios Seed

Dados iniciais carregados por `import.sql`:

- `bob@gmail.com` com papel `ROLE_VISITOR`
- `ana@gmail.com` com papel `ROLE_MEMBER`
- Senha utilizada nos testes: `123456`

## Endpoints Principais

### Generos

- `GET /genres`
  - Roles: `ROLE_VISITOR` ou `ROLE_MEMBER`

### Filmes

- `GET /movies?genreId=0&page=0&size=10`
  - Lista paginada de filmes (com filtro opcional por genero)
  - Roles: `ROLE_VISITOR` ou `ROLE_MEMBER`

- `GET /movies/{id}`
  - Detalhes de um filme
  - Roles: `ROLE_VISITOR` ou `ROLE_MEMBER`

- `GET /movies/{id}/reviews`
  - Reviews de um filme
  - Roles: `ROLE_VISITOR` ou `ROLE_MEMBER`

### Reviews

- `POST /reviews`
  - Cria review para o filme informado no body
  - Role: `ROLE_MEMBER`

Body exemplo:

```json
{
  "movieId": 1,
  "text": "Excelente filme!"
}
```

### Usuario

- `GET /users/profile`
  - Retorna dados do usuario autenticado
  - Roles: `VISITOR` ou `MEMBER` (mapeados a partir do token)

## H2 Console

Com perfil `test`, o console H2 fica disponivel em:

- `http://localhost:8080/h2-console`

Configuracao padrao:

- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: vazio

## Testes

Executar testes:

```powershell
.\mvnw.cmd test
```

## Postman

Arquivos incluidos na raiz:

- `Desafio Movieflix casos de uso.postman_collection.json`
- `Movieflix env.postman_environment.json`

Importe ambos no Postman para facilitar a validacao dos endpoints.

## Observacoes

- O projeto usa token JWT com claims customizadas, incluindo `authorities` e `username`.
- O Authorization Server e Resource Server estao no mesmo servico.