
# Centro Comunitário API

Este projeto é uma API RESTful para gerenciar centros comunitários, permitindo adicionar centros, atualizar ocupação, intercâmbio de recursos, e fornecer relatórios sobre ocupação e histórico de negociações entre centros. Ele faz parte de um conjunto de microsserviços que visa organizar e prover informações sobre os centros comunitários no Brasil.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **MongoDB**
- **Docker/Docker-Compose**
- **Swagger/OpenAPI**
- **JUnit 5** para testes
- **Mockito** para testes unitários
- **Lombok** para reduzir código repetitivo

## Funcionalidades

- **Adicionar centros comunitários:** Cada centro comunitário tem nome, endereço, capacidade máxima, ocupação atual, e recursos disponíveis.
- **Atualizar ocupação:** Permite atualizar o número de pessoas ocupando o centro comunitário.
- **Intercâmbio de recursos:** Centros podem trocar recursos entre si, respeitando uma tabela de pontos. Se a ocupação de um centro for maior que 90%, a regra de pontos pode ser quebrada.
- **Relatórios:** Relatórios de centros com ocupação acima de 90%, média de recursos por centro, e histórico de negociações.

## Pré-requisitos

- **JDK 17**
- **Maven ou Gradle**
- **Docker** e **Docker-Compose** (para rodar o MongoDB)
- **Insomnia** ou **Postman** (para testar os endpoints da API)
  
## Como Rodar o Projeto Localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

### 2. Rodar o MongoDB com Docker-Compose

Certifique-se de que o Docker esteja instalado e rodando.

Suba o container do MongoDB usando o Docker-Compose:

```bash
docker-compose up -d
```

Este comando irá subir o MongoDB na porta 27017 com as credenciais especificadas no arquivo `docker-compose.yml`.

### 3. Configurar o projeto

#### Configurações no `application.properties`:

O Spring Boot já está configurado para se conectar ao MongoDB rodando localmente na porta 27017. O arquivo `application.properties` está configurado da seguinte forma:

```properties
spring:
    data:
        mongodb:
            host: localhost
            port: 27017
            database: centro_comunitario
```

### 4. Rodar a Aplicação

Após configurar o MongoDB, você pode rodar o projeto utilizando **Gradle**:

```bash
./gradlew bootRun
```

Ou utilizando **Maven** (caso esteja usando Maven):

```bash
./mvnw spring-boot:run
```

### 5. Documentação da API (Swagger)

Após a aplicação estar rodando, você pode acessar a documentação da API via **Swagger UI** no seguinte endereço:

```
http://localhost:8080/swagger-ui.html
```

A documentação será gerada automaticamente pelo Springdoc OpenAPI e você poderá testar os endpoints diretamente pela interface do Swagger.

### 6. Testes

O projeto conta com cobertura de testes unitários para os principais serviços, incluindo os fluxos de negociação e manipulação de centros comunitários.

Para rodar os testes unitários:

```bash
./gradlew test
```

Ou usando Maven:

```bash
./mvnw test
```

Os testes estão localizados na pasta `src/test/java` e utilizam **JUnit 5** e **Mockito** para realizar mocks de dependências e validar as funcionalidades principais.

## Endpoints Principais

### Centros Comunitários

- **POST /api/centros-comunitarios**: Adiciona um novo centro comunitário.
- **PUT /api/centros-comunitarios/{id}/ocupacao**: Atualiza a ocupação de um centro comunitário.
- **GET /api/centros-comunitarios/ocupacao-alta**: Lista centros comunitários com ocupação acima de 90%.
  
### Negociações

- **POST /api/negociacoes**: Realiza o intercâmbio de recursos entre dois centros comunitários.
- **GET /api/negociacoes/historico**: Consulta o histórico de negociações de um centro, com suporte a filtros de datas.
