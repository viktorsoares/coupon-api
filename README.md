# Coupon API

API REST para gerenciamento de cupons, desenvolvida em Java com Spring Boot. O projeto foi estruturado para ser simples de executar localmente, totalmente containerizado com Docker e com persistência de dados garantida através de volume Docker.

A aplicação expõe endpoints REST, utiliza JPA para persistência e disponibiliza documentação via Swagger.

---

## Visão Geral

A Coupon API tem como objetivo fornecer uma base para gerenciamento de cupons de desconto, permitindo operações de criação, consulta, listagem e exclusão dos dados. O projeto foi desenvolvido seguindo boas práticas de organização de código, separação de responsabilidades e configuração moderna do ecossistema Spring.

O foco principal foi:
- Facilidade de execução em ambiente local
- Persistência de dados mesmo com uso de containers
- Configuração simples para estudos, testes e evolução futura

---

## Tecnologias Utilizadas

### Linguagem e Plataforma
- Java 21
- Maven 3.9.11

### Frameworks e Bibliotecas
- Spring Boot 3.4.2
- Spring Web
- Spring Data JPA
- Hibernate ORM 6.6.5
- HikariCP (pool de conexões)

### Banco de Dados
- H2 Database 2.3.232
    - Configurado em modo arquivo (file)
    - Persistência garantida via volume Docker

### Infraestrutura e DevOps
- Docker
- Docker Compose
- Imagens base Eclipse Temurin (Java 21)

### Documentação
- Swagger (http://localhost:8080/swagger-ui.html)

---

## Banco de Dados

O banco de dados utilizado é o H2, configurado para rodar em modo arquivo, garantindo persistência mesmo após reinicialização dos containers.

- URL de conexão utilizada pela aplicação:

```text
jdbc:h2:file:/data/coupondb
```

Os dados são armazenados no diretório `/data` dentro do container, que é mapeado para um volume Docker.

Dessa forma:
1. Reiniciar o container não apaga os dados.
2. Os dados só são removidos caso o volume Docker seja excluído manualmente.

---

## Estrutura Docker

### Dockerfile
O Dockerfile é responsável por empacotar a aplicação Spring Boot em um container. Ele:
- Utiliza uma imagem base com Java 21
- Copia o arquivo JAR gerado pelo Spring Boot
- Define o comando de inicialização da aplicação

### Docker Compose
O Docker Compose é utilizado para:
- Construir a imagem da aplicação
- Subir o container da API
- Expor a porta 8080
- Criar e gerenciar o volume Docker para persistência do banco H2

---

## Como Instalar e Executar o Projeto

### Passo a Passo

1. Clone o repositório do projeto:
   ```bash
   git clone <url-do-repositorio>
   cd coupon-api
   ```

2. Execute o build e suba a aplicação com Docker Compose:
   ```bash
   docker compose up --build
   ```

3. Aguarde a inicialização completa da aplicação. Ao final, a API estará disponível em:
    - http://localhost:8080

### Rodar sem Docker
Para executar a aplicação sem Docker, é necessário ter o Java 21 e o Maven 3.9.11 instalados na máquina.

  ```bash
    mvn spring-boot:run
  ```

---

## Endpoints Disponíveis

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/coupon` | Cria um novo cupom |
| GET | `/coupon/{id}` | Consulta um cupom por ID |
| GET | `/coupon?page=x&size=y` | Lista cupons ativos paginados |
| DELETE | `/coupon/{id}` | Exclui (soft delete) um cupom |

---

## Persistência de Dados

Para parar e subir novamente a aplicação sem perder os dados:

  ```bash
    docker compose down
    docker compose up
  ```

Para remover completamente os dados do banco:

  ```bash
    docker compose down -v
  ```

---

## Observações Finais

Este projeto serve como base para estudos e para evolução futura. Ele pode ser facilmente adaptado para uso com bancos de dados externos como PostgreSQL ou MySQL, além da inclusão de ferramentas de migração de banco de dados como Flyway ou Liquibase.

A estrutura atual facilita a manutenção, testes e expansão da aplicação.

---

## Autor
João Vitor Soares