# 🐾 MeuPet

Um sistema de monitoramento e gerenciamento de pets, feito para facilitar o cuidado animal tanto no setor doméstico quanto no comercial.

> **Status do Projeto:** em fase de prototipagem e desenvolvimento da API (com spring).

## 📖 Sobre o Projeto
O **MeuPet** é uma aplicação focada em centralizar e organizar as informações vitais dos nossos queridos pets.
O objetivo é permitir que tutores e profissionais (como clínicas, creches pet ou pet shops) gerenciem de forma eficiente os dados dos animais, acompanhando de perto o histórico de saúde, vacinas e tratamentos.

* **Linguagem principal:** Java
* **Paradigma:** Programação Orientada a Objetos (POO)
* **Disciplina:** Linguagem de Programação II (LP2)

## 🚀 Como executar

### Pré-requisitos
* JDK 21+
* Maven (ou use o wrapper `./mvnw`)

### Variáveis de ambiente obrigatórias
A aplicação exige duas variáveis de ambiente para funcionar:

| Variável | Descrição |
| --- | --- |
| `MEUPET_SECRET_KEY` | Segredo usado para assinar os tokens JWT (HS256). Recomenda-se uma chave de 32+ caracteres. |
| `MEUPET_SALT` | Salt usado nas operações de criptografia (BCrypt). |

### Perfis de execução

* **`dev`** (padrão): banco H2 em memória com `create-drop` e seed automático. Ideal para desenvolvimento.
* **`prod`**: PostgreSQL a partir das variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`, com `ddl-auto=validate`.

```bash
# Rodando em desenvolvimento
MEUPET_SECRET_KEY=minha-chave-secreta MEUPET_SALT=meu-salt ./mvnw spring-boot:run

# Rodando com o perfil de produção
SPRING_PROFILES_ACTIVE=prod DB_URL=jdbc:postgresql://localhost:5432/meupet \
  DB_USERNAME=postgres DB_PASSWORD=postgres \
  MEUPET_SECRET_KEY=minha-chave-secreta MEUPET_SALT=meu-salt \
  ./mvnw spring-boot:run
```

### Rodando os testes

```bash
MEUPET_SECRET_KEY=test-secret MEUPET_SALT=test-salt ./mvnw test
```

O build também executa o **Checkstyle** (regras em `config/checkstyle/checkstyle.xml`), vinculado à fase `validate`.

### Rodando com Docker
O `Dockerfile` é multi-stage — o build compila o jar dentro da imagem (não é preciso rodar `./mvnw package` antes):

```bash
# construir a imagem
docker build -t meupet .

# rodar (perfil dev, H2 em memória)
docker run -p 8080:8080 \
  -e MEUPET_SECRET_KEY=minha-chave \
  -e MEUPET_SALT=meu-salt \
  meupet

# rodar em produção (PostgreSQL)
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://localhost:5432/meupet \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=sua-senha \
  -e MEUPET_SECRET_KEY=minha-chave \
  -e MEUPET_SALT=meu-salt \
  meupet
```

> O container roda como usuário não-root (`10001`) e expõe a porta `8080`.

### Documentação da API (Swagger)
Com a aplicação rodando, acesse:

* UI do Swagger: `http://localhost:8080/swagger-ui.html`
* OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🔐 Autenticação

Todos os endpoints (exceto `/api/auth/**`) exigem um token JWT no header:

```
Authorization: Bearer <token>
```

### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Fulano","email":"fulano@email.com","senha":"senha12345"}'
```

Retorna `200 OK` com `token`, `id`, `nome` e `email`.

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"fulano@email.com","senha":"senha12345"}'
```

### Usuário de seed (perfil dev)
No banco H2 de desenvolvimento já existe um usuário criado pelo `data.sql`:

* **E-mail:** `dono@meupet.com`
* **Senha:** `senha123`

## 🪵 Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Autenticar e obter token JWT |
| GET/POST | `/api/usuarios` | Listar / criar usuários |
| GET/PUT/DELETE | `/api/usuarios/{id}` | Buscar / atualizar / excluir usuário |
| GET/POST | `/api/animais` | Listar / criar animais |
| GET/PUT/DELETE | `/api/animais/{id}` | Buscar / atualizar / excluir animal |
| GET | `/api/animais/{id}/tarefas` | Tarefas de um animal |
| GET/POST | `/api/cachorros` · `/api/gatos` | Listar / criar cachorros ou gatos |
| GET/PUT/DELETE | `/api/cachorros/{id}` · `/api/gatos/{id}` | Buscar / atualizar / excluir |
| GET/POST | `/api/tarefas` | Listar (com filtros) / criar tarefas |
| GET | `/api/tarefas/resumo` | Resumo de tarefas (opcional `?animalId=`) |
| GET/PUT/DELETE | `/api/tarefas/{id}` | Buscar / atualizar / excluir tarefa |
| PATCH | `/api/tarefas/{id}/concluir` · `/reabrir` | Concluir / reabrir tarefa |
| GET/POST | `/api/vacinas` | Listar / criar vacinas |
| GET/PUT/DELETE | `/api/vacinas/{id}` | Buscar / atualizar / excluir vacina |
| GET/POST | `/api/doencas` | Listar / criar doenças |
| GET/PUT/DELETE | `/api/doencas/{id}` | Buscar / atualizar / excluir doença |

> Cada usuário só acessa os próprios recursos: animais, tarefas, vacinas e doenças são isolados por usuário (campo `usuario_id`).

## 🛠️ Ferramentas de qualidade
* **Checkstyle** (`maven-checkstyle-plugin`) executado na fase `validate` — use `./mvnw checkstyle:check` para validar manualmente.
* **`.editorconfig`** padroniza indentação (tabs) e fim de linha entre editores.

## 👥 Desenvolvedores
Equipe:

* Edvaldo Henrique - [perfil GitHub](https://github.com/edvaldinhs)
* Lucas Lopes - [perfil GitHub](https://github.com/lucas1noid)
* Rafael de Moura - [perfil GitHub](https://github.com/rafael1109-moura)
* Thales Justino - [perfil GitHub](https://github.com/D410W)

Professor orientador:
* Alan de Oliveira Santana