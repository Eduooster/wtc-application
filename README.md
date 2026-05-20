# WTC - CRM

Sistema de gerenciamento de relacionamento com clientes (CRM) desenvolvido para centralizar, organizar 
e automatizar processos relacionados à gestão de campanhas, usuários, participantes e comunicação dentro da plataforma WTC.

O projeto foi construído utilizando arquitetura em camadas com foco em escalabilidade, 
manutenção e boas práticas de desenvolvimento backend, oferecendo recursos como autenticação JWT, controle de acesso por perfis, 
auditoria, documentação com Swagger, mensageria assíncrona, notificações e integração com diferentes ambientes através de perfis de configuração.

#
## 🚀 Como executar localmente com Docker

### 1. Clonar o repositório
```bash
git clone <https://github.com/Eduooster/wtc-application.git>
```


## Subir o ambiente com Docker Compose

```bash
docker-compose up --build
```

## Acessar a aplicação

```http
http://localhost:8080
```
##  O que será iniciado:
- Aplicação Spring Boot
- Banco de dados PostgreSQL (container)
- Rede interna Docker entre os serviços
- Volume para persistência do banco

# API Endpoints - WTC CRM

## Campanhas

Endpoints responsáveis pela criação, gerenciamento, agendamento e disparo de campanhas de marketing.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/campaigns/{id}` | ADMIN, OPERATOR | Buscar campanha pelo identificador |
| PUT | `/campaigns/{id}` | ADMIN, OPERATOR | Atualizar informações de uma campanha |
| DELETE | `/campaigns/{id}` | ADMIN | Realizar exclusão lógica da campanha |
| GET | `/campaigns` | ADMIN, OPERATOR | Listar campanhas paginadas |
| POST | `/campaigns` | ADMIN, OPERATOR | Criar uma nova campanha |
| POST | `/campaigns/{campaignId}/send` | ADMIN, OPERATOR | Disparar campanha imediatamente |
| POST | `/campaigns/{campaignId}/schedule` | ADMIN, OPERATOR | Agendar disparo de campanha |

---

## Mensagens

Endpoints para gerenciamento de mensagens dentro das conversas.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| POST | `/messages` | ADMIN, OPERATOR, CLIENT | Enviar uma nova mensagem |
| PATCH | `/messages/{id}/read` | ADMIN, OPERATOR, CLIENT | Marcar mensagem como lida |
| DELETE | `/messages/{id}` | ADMIN, OPERATOR | Excluir uma mensagem |

---

## Conversas & Atendimentos

Endpoints relacionados à criação e gerenciamento de conversas e atendimentos.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| POST | `/conversations/{conversationId}/join` | ADMIN, OPERATOR | Entrar em uma conversa ativa |
| POST | `/conversations/{conversationId}/assign` | OPERATOR | Atribuir conversa ao operador autenticado |
| POST | `/conversations/user` | ADMIN, OPERATOR | Criar conversa iniciada por Operador/Admin |
| POST | `/conversations/client` | CLIENT | Criar conversa iniciada por Cliente |
| GET | `/conversations` | ADMIN, OPERATOR | Filtrar conversas por status |
| GET | `/conversations/{id}/messages` | ADMIN, OPERATOR, CLIENT | Obter histórico de mensagens |
| GET | `/conversations/my-conversations` | ADMIN, OPERATOR, CLIENT | Listar conversas do usuário autenticado |

---

## Segmentos

Endpoints para gerenciamento de públicos segmentados.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/segments/{id}` | ADMIN, OPERATOR | Buscar segmento por ID |
| PUT | `/segments/{id}` | ADMIN, OPERATOR | Atualizar segmento |
| DELETE | `/segments/{id}` | ADMIN | Remover segmento |
| GET | `/segments` | ADMIN, OPERATOR | Listar segmentos paginados |
| POST | `/segments` | ADMIN, OPERATOR | Criar novo segmento |

---

## Notificações

Endpoints responsáveis pelo gerenciamento de notificações.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| PATCH | `/notifications/{conversationId}/read` | ADMIN, OPERATOR, CLIENT | Marcar notificações da conversa como lidas |
| GET | `/notifications/notifications/unread` | ADMIN, OPERATOR, CLIENT | Listar notificações não lidas |

---

## Clientes

Endpoints relacionados ao cadastro e gerenciamento de clientes.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| PUT | `/clients/{userId}/tags` | ADMIN, OPERATOR | Atualizar tags vinculadas ao cliente |
| GET | `/clients/{id}` | ADMIN, OPERATOR | Buscar cliente por ID |
| PUT | `/clients/{id}` | ADMIN, OPERATOR | Atualizar dados do cliente |
| DELETE | `/clients/{id}` | ADMIN | Remover cliente |
| PUT | `/clients/{id}/device-token` | CLIENT | Atualizar token Firebase do dispositivo |
| GET | `/clients` | ADMIN, OPERATOR | Listar clientes |
| POST | `/clients` | ADMIN | Cadastrar novo cliente |
| POST | `/clients/firebase-token` | CLIENT | Salvar token Firebase do cliente autenticado |
| PATCH | `/clients/{clientId}/segments` | ADMIN, OPERATOR | Atualizar segmentos do cliente |

---

## Métricas de Campanha

Endpoints responsáveis pelo rastreamento de campanhas.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/campaign-metrics/{campaignCode}` | CLIENT | Processar clique e registrar métrica da campanha |

---

## Auditoria

Endpoints para controle e rastreamento de ações no sistema.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/audits/{id}` | ADMIN | Buscar registro de auditoria |
| PUT | `/audits/{id}` | ADMIN | Atualizar registro de auditoria |
| DELETE | `/audits/{id}` | ADMIN | Remover registro de auditoria |
| GET | `/audits` | ADMIN | Listar registros de auditoria |
| POST | `/audits` | ADMIN | Criar registro de auditoria |

---

## Usuários Internos

Endpoints responsáveis pelo gerenciamento de operadores e administradores.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/users/{id}` | ADMIN | Buscar usuário por ID |
| PUT | `/users/{id}` | ADMIN | Atualizar dados do usuário |
| DELETE | `/users/{id}` | ADMIN | Deletar usuário |
| GET | `/users` | ADMIN | Listar usuários |
| POST | `/users` | ADMIN | Cadastrar novo usuário |
| PATCH | `/users/{userId}/segments` | ADMIN | Atualizar segmentos do usuário |

---

## Autenticação & Registro

Endpoints responsáveis pelo controle de autenticação e criação de contas.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| POST | `/auth/register/user` | ADMIN | Registrar novo usuário interno |
| POST | `/auth/register/client` | Público | Registrar novo cliente |
| POST | `/auth/login` | Público | Realizar autenticação no sistema |

---

## Etiquetas (Tags)

Endpoints para gerenciamento de tags de classificação.

| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/tags/{id}` | ADMIN, OPERATOR | Buscar tag por ID |
| PUT | `/tags/{id}` | ADMIN, OPERATOR | Atualizar tag |
| DELETE | `/tags/{id}` | ADMIN | Remover tag |
| GET | `/tags` | ADMIN, OPERATOR | Listar tags |
| POST | `/tags` | ADMIN, OPERATOR | Criar nova tag |



# Arquitetura Utilizada

O projeto **WTC - CRM** foi desenvolvido utilizando uma arquitetura em camadas (*Layered Architecture*), organizada de forma modular para facilitar manutenção, escalabilidade, reutilização de código e separação de responsabilidades.

A estrutura da aplicação segue padrões amplamente utilizados no ecossistema Spring Boot, garantindo melhor organização dos componentes e facilidade na evolução do sistema.

---

# Estrutura de Pacotes

```text
src/main/java
└── br/com/wtccrm
    ├── campaigns
    ├── clients
    ├── conversations
    ├── messages
    ├── notifications
    ├── segments
    ├── tags
    ├── users
    ├── audits
    ├── auth
    ├── config
    ├── security
    └── common
```

---

# Organização Interna dos Módulos

Cada módulo da aplicação segue uma padronização própria de separação por responsabilidade:

```text
campaigns
├── controller
├── dto
├── entity
├── enums
├── exceptions
├── mapper
├── repository
└── service
```

---

# Responsabilidade de Cada Camada

| Camada | Responsabilidade |
|---|---|
| `controller` | Responsável pela exposição dos endpoints REST da aplicação |
| `dto` | Objetos utilizados para transferência de dados entre cliente e servidor |
| `entity` | Representação das entidades persistidas no banco de dados |
| `enums` | Enumerações utilizadas para padronização de estados e tipos |
| `exceptions` | Tratamento de exceções específicas do módulo |
| `mapper` | Conversão entre DTOs e entidades |
| `repository` | Comunicação direta com o banco de dados através do Spring Data JPA |
| `service` | Implementação das regras de negócio da aplicação |

---

# Padrões e Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| Spring Boot | Estrutura principal da aplicação |
| Spring Security | Controle de autenticação e autorização |
| JWT | Autenticação baseada em token |
| Spring Data JPA | Persistência de dados |
| PostgreSQL | Banco de dados principal |
| H2 Database | Banco utilizado em ambiente de testes |
| Oracle Database | Banco utilizado em ambiente específico de desenvolvimento |
| Docker & Docker Compose | Containerização da aplicação |
| Swagger / OpenAPI | Documentação automática da API |
| MapStruct | Mapeamento entre DTOs e entidades |
| Lombok | Redução de código boilerplate |

---

# Perfis de Ambiente

O projeto utiliza múltiplos perfis de configuração para facilitar a execução em diferentes ambientes.

| Perfil | Banco Utilizado | Objetivo |
|---|---|---|
| `dev` | Oracle Database | Ambiente de desenvolvimento |
| `test` | H2 Database | Execução de testes automatizados |
| `docker` | PostgreSQL | Ambiente containerizado |

---

# Características Arquiteturais

- Arquitetura modular baseada em domínio
- Separação clara de responsabilidades
- Controle de acesso baseado em Roles (`ADMIN`, `OPERATOR`, `CLIENT`)
- Auditoria de ações críticas do sistema
- Estrutura preparada para escalabilidade e manutenção contínua
- Documentação automática da API utilizando Swagger
- Suporte a múltiplos ambientes através de perfis Spring
