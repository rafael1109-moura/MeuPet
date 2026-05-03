# 🐾 MeuPet

Um sistema de monitoramento e gerenciamento de pets, feito para facilitar o cuidado animal tanto no setor doméstico quanto no comercial.

> **Status do Projeto:** Em desenvolvimento inicial.

## 📖 Sobre o Projeto

O **MeuPet** é uma aplicação Java focada em centralizar e organizar informações importantes sobre pets.
O objetivo é permitir que tutores e profissionais, como clínicas, creches pet e pet shops, acompanhem dados dos animais, histórico de saúde, vacinas, tratamentos e informações de usuários.

Atualmente o projeto roda pelo terminal e demonstra:

- cadastro, listagem e salvamento de usuários;
- criação de cachorro e gato;
- consulta de vacinas recomendadas;
- exibição de dados dos animais;
- validações com exceções personalizadas;
- autenticação simples de usuário.

## 🧰 Tecnologias Utilizadas

- Java
- Programação Orientada a Objetos
- Manipulação de arquivos JSON sem biblioteca externa

## ✅ Requisitos

Antes de rodar o projeto, instale:

- **JDK 8 ou superior**
- Um terminal, como PowerShell, Prompt de Comando, Git Bash, Terminal do Linux ou Terminal do macOS

Para verificar se o Java está instalado, execute:

```bash
java -version
javac -version
```

Os dois comandos devem retornar uma versão instalada do Java. Se algum deles não for reconhecido, instale o JDK e confira se ele foi adicionado ao `PATH` do sistema.

## 📁 Estrutura do Projeto

```text
meuPet/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── meupet/
│                   ├── app/
│                   │   ├── Main.java
│                   │   └── Menu.java
│                   └── model/
│                       ├── Animal.java
│                       ├── Cachorro.java
│                       ├── Gato.java
│                       ├── Usuario.java
│                       ├── PetSaude.java
│                       ├── Vacina.java
│                       ├── Doenca.java
│                       ├── Autenticavel.java
│                       ├── AutenticacaoException.java
│                       └── DadoInvalidoException.java
├── usuarios.json
├── README.md
└── .gitignore
```

## ▶️ Como Rodar

Este projeto ainda não usa Maven ou Gradle. Por isso, a compilação é feita diretamente com `javac`.

### 1. Abrir o terminal na pasta do projeto

Entre na pasta onde o projeto está salvo:

```bash
cd caminho/para/meuPet
```

No Windows, usando o caminho atual do projeto, seria algo parecido com:

```powershell
cd C:\Users\rafam\OneDrive\Documentos\meuPet
```

### 2. Compilar o projeto

No PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
```

No Git Bash, Linux ou macOS:

```bash
javac -d out $(find src -name "*.java")
```

Esse comando compila todos os arquivos `.java` e salva os arquivos `.class` dentro da pasta `out`.

### 3. Executar o programa

Depois de compilar, execute:

```bash
java -cp out com.meupet.app.Main
```

## 🧪 Fluxo de Uso

Ao iniciar, o programa mostra um menu de usuários:

```text
Bem-vindo ao MeuPet!
1- Listar usuarios
2- Adicionar usuario
3- Salvar usuarios
4- Sair
```

Opções disponíveis:

- `1`: lista os usuários cadastrados durante a execução atual;
- `2`: adiciona um novo usuário;
- `3`: salva os usuários no arquivo `usuarios.json`;
- `4`: sai do menu e continua a demonstração principal do sistema.

Depois de sair do menu, o programa pede:

- nome do cachorro;
- nome do gato.

Em seguida, exibe informações sobre vacinas recomendadas, cuidados dos animais, sugestões de brincadeiras, testes de validação e autenticação.

## 💾 Arquivo `usuarios.json`

Quando a opção `3- Salvar usuarios` é escolhida no menu, o sistema cria ou atualiza o arquivo `usuarios.json` na raiz do projeto.

Esse arquivo guarda os usuários cadastrados no formato JSON.

Exemplo:

```json
[
  {"id": 1, "nome": "Rafael", "email": "rafael@email.com", "senha": "123456"}
]
```

Observação: no estado atual do projeto, os usuários são mantidos apenas em memória durante a execução. O programa salva no JSON, mas ainda não carrega automaticamente os usuários desse arquivo ao iniciar.

## 🧹 Limpar Arquivos Compilados

Se quiser remover os arquivos gerados pela compilação, apague a pasta `out`.

No PowerShell:

```powershell
Remove-Item -Recurse -Force out
```

No Git Bash, Linux ou macOS:

```bash
rm -rf out
```

Depois disso, basta compilar novamente seguindo os passos anteriores.

## 👥 Desenvolvedores

Equipe:

- [Edvaldo Henrique](https://github.com/edvaldinhs)
- [Lucas Lopes](https://github.com/lucas1noid)
- [Rafael de Moura](https://github.com/rafael1109-moura)

Professor orientador:

- Alan de Oliveira Santana
