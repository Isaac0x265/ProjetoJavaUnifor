# Sistema de Gerenciamento de Biblioteca

Um sistema de gerenciamento de biblioteca desenvolvido em Java com interface de linha de comando, utilizando MySQL como banco de dados e seguindo o padrão DAO (Data Access Object).

## Funcionalidades

O sistema oferece as seguintes funcionalidades principais:

- **Gestão de Alunos**: Cadastro, listagem e validação de matrículas
- **Gestão de Livros**: Cadastro, listagem e controle de estoque
- **Sistema de Empréstimos**: Registro de empréstimos com controle de prazo
- **Sistema de Devoluções**: Registro de devoluções com reposição automática de estoque
- **Consultas**: Listagem de empréstimos pendentes

## Arquitetura

O projeto segue uma arquitetura em camadas:

```
src/
├── Main.java              # Interface de usuário (CLI)
├── model/                 # Entidades de domínio
│   ├── Aluno.java        # Modelo de estudante
│   ├── Livro.java        # Modelo de livro
│   └── Emprestimo.java   # Modelo de empréstimo
├── dao/                   # Camada de acesso a dados
│   ├── AlunoDAO.java     # CRUD de alunos
│   ├── LivroDAO.java     # CRUD de livros
│   └── EmprestimoDAO.java # Lógica de empréstimos/devoluções
└── util/
    └── Database.java      # Configuração de conexão com BD
```

## Modelo de Dados

O sistema utiliza três entidades principais:

#### Aluno
- `idAluno` (Integer): Chave primária auto-incrementada
- `nomeAluno` (String): Nome completo (obrigatório)
- `matricula` (String): Matrícula única de 7 caracteres
- `dataNascimento` (LocalDate): Data de nascimento

#### Livro
- `idLivro` (Integer): Chave primária auto-incrementada
- `titulo` (String): Título do livro (obrigatório)
- `autor` (String): Nome do autor (padrão: "Desconhecido")
- `anoPublicacao` (Integer): Ano de publicação
- `quantidadeEstoque` (Integer): Quantidade disponível

#### Emprestimo
- `idEmprestimo` (Integer): Chave primária auto-incrementada
- `idAluno` (Integer): Referência ao aluno
- `idLivro` (Integer): Referência ao livro
- `dataEmprestimo` (LocalDate): Data do empréstimo
- `dataDevolucao` (LocalDate): Data de devolução (null = pendente)

## Como Executar

### Pré-requisitos

1. **Java 17+** instalado
2. **MySQL Server** rodando
3. **MySQL Connector/J** no classpath

### Configuração do Banco

1. Crie um banco de dados MySQL chamado `livraria`
2. Configure as credenciais em `src/util/Database.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/livraria?serverTimezone=UTC";
   private static final String USER = "root";
   private static final String PASSWORD = "sua_senha";
   ```

3. Execute o script SQL para criar as tabelas:
   ```sql
   CREATE TABLE Alunos (
       id_aluno INT AUTO_INCREMENT PRIMARY KEY,
       nome_aluno VARCHAR(255) NOT NULL,
       matricula VARCHAR(7) UNIQUE NOT NULL,
       data_nascimento DATE NOT NULL
   );

   CREATE TABLE Livros (
       id_livro INT AUTO_INCREMENT PRIMARY KEY,
       titulo VARCHAR(255) NOT NULL,
       autor VARCHAR(255) DEFAULT 'Desconhecido',
       ano_publicacao INT,
       quantidade_estoque INT DEFAULT 0
   );

   CREATE TABLE Emprestimos (
       id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
       id_aluno INT NOT NULL,
       id_livro INT NOT NULL,
       data_emprestimo DATE NOT NULL,
       data_devolucao DATE,
       FOREIGN KEY (id_aluno) REFERENCES Alunos(id_aluno),
       FOREIGN KEY (id_livro) REFERENCES Livros(id_livro)
   );
   ```

### Compilação e Execução

```bash
# Compilar
javac -cp ".:mysql-connector-java.jar" src/**/*.java

# Executar
java -cp ".:mysql-connector-java.jar:src" Main
```

## Manual de Uso

### Menu Principal

```
===== Biblioteca =====
1  - Cadastrar aluno
2  - Listar alunos
3  - Cadastrar livro
4  - Listar livros
5  - Registrar empréstimo
6  - Registrar devolução
7  - Listar empréstimos pendentes
0  - Sair
```

### Operações Detalhadas

#### 1. Cadastrar Aluno
- **Nome**: Nome completo do aluno
- **Matrícula**: Exatamente 7 caracteres, deve ser única
- **Data de Nascimento**: Formato AAAA-MM-DD

#### 2. Cadastrar Livro
- **Título**: Título do livro (obrigatório)
- **Autor**: Nome do autor
- **Ano de Publicação**: Ano em formato numérico
- **Quantidade em Estoque**: Número de exemplares disponíveis

#### 3. Registrar Empréstimo
- **ID do Aluno**: ID numérico do aluno cadastrado
- **ID do Livro**: ID numérico do livro cadastrado
- **Prazo**: Número de dias para devolução

O sistema realiza as seguintes validações:
- Verifica se aluno e livro existem
- Verifica se há estoque disponível
- Decrementa automaticamente o estoque

#### 4. Registrar Devolução
- **ID do Empréstimo**: ID numérico do empréstimo ativo

O sistema realiza as seguintes validações:
- Verifica se o empréstimo existe e está pendente
- Incrementa automaticamente o estoque

## Características Técnicas

### Validações Implementadas

#### Modelo Aluno
- Nome não pode ser nulo
- Matrícula deve ter exatamente 7 caracteres
- Validação via setters com exceções

#### Modelo Livro
- Título não pode ser nulo
- Autor padrão "Desconhecido" se nulo
- Ano de publicação não pode ser negativo
- Estoque não pode ser negativo

#### Modelo Emprestimo
- IDs de aluno e livro são obrigatórios
- Data de empréstimo é obrigatória
- Métodos auxiliares para verificar status

### Padrões Utilizados

- **DAO Pattern**: Separação clara entre lógica de negócio e acesso a dados
- **Singleton implícito**: Classe Database com métodos estáticos
- **Transaction Management**: Operações de empréstimo/devolução são transacionais
- **Resource Management**: Try-with-resources para conexões e statements

### Tratamento de Erros

- Exceções SQL são capturadas e exibidas ao usuário
- Validações de entrada com mensagens específicas
- Rollback automático em caso de falha em transações

## Fluxo de Transações

### Empréstimo
1. Inicia transação
2. Decrementa estoque do livro
3. Insere registro de empréstimo
4. Commit ou rollback em caso de erro

### Devolução
1. Inicia transação
2. Atualiza data de devolução
3. Incrementa estoque do livro
4. Commit ou rollback em caso de erro

## Melhorias Futuras

O sistema pode ser expandido com as seguintes funcionalidades:

- Interface gráfica (JavaFX/Swing)
- Sistema de multas por atraso
- Relatórios de empréstimos
- Busca avançada de livros
- Sistema de reservas
- Autenticação de usuários
- Logs de auditoria

## Sobre o Projeto

Este é um projeto acadêmico desenvolvido para demonstrar conceitos fundamentais de desenvolvimento de software:

- Programação orientada a objetos
- Padrões de design (DAO)
- Integração com banco de dados
- Tratamento de exceções
- Validação de dados

O sistema foi projetado para ser funcional, educativo e facilmente extensível, servindo como base para o aprendizado de tecnologias Java e MySQL. 