# Documentação da API - Sistema de Biblioteca

## Visão Geral

Esta documentação detalha todas as classes, métodos e funcionalidades do sistema de gerenciamento de biblioteca.

## 📦 Pacotes

### `model` - Entidades de Domínio
### `dao` - Camada de Acesso a Dados  
### `util` - Utilitários
### `Main` - Interface de Usuário

---

## 🏗️ Pacote `model`

### Classe `Aluno`

Representa um estudante cadastrado no sistema.

#### Atributos
```java
private Integer idAluno;          // PK auto-incrementada
private String nomeAluno;         // Nome completo (obrigatório)
private String matricula;         // 7 caracteres únicos
private LocalDate dataNascimento; // Data de nascimento
```

#### Construtores
```java
public Aluno()
public Aluno(Integer idAluno, String nomeAluno, String matricula, LocalDate dataNascimento)
```

#### Métodos Principais

##### `setNomeAluno(String nomeAluno)`
- **Descrição**: Define o nome do aluno
- **Validação**: Nome não pode ser nulo
- **Exceção**: `NullPointerException` se nome for nulo

##### `setMatricula(String matricula)`
- **Descrição**: Define a matrícula do aluno
- **Validação**: Deve ter exatamente 7 caracteres
- **Exceção**: `IllegalArgumentException` se não tiver 7 caracteres

##### `toString()`
- **Retorno**: `"Aluno[id=%d, nome='%s', matrícula='%s']"`

---

### Classe `Livro`

Representa um livro do acervo da biblioteca.

#### Atributos
```java
private Integer idLivro;            // PK auto-incrementada
private String titulo;              // Título (obrigatório)
private String autor;               // Autor (padrão: "Desconhecido")
private Integer anoPublicacao;      // Ano de publicação
private Integer quantidadeEstoque;  // Quantidade disponível
```

#### Construtores
```java
public Livro()
public Livro(Integer idLivro, String titulo, String autor, Integer anoPublicacao, Integer quantidadeEstoque)
```

#### Métodos Principais

##### `setTitulo(String titulo)`
- **Descrição**: Define o título do livro
- **Validação**: Título não pode ser nulo
- **Exceção**: `NullPointerException` se título for nulo

##### `setAutor(String autor)`
- **Descrição**: Define o autor do livro
- **Comportamento**: Se nulo, define como "Desconhecido"

##### `setAnoPublicacao(Integer anoPublicacao)`
- **Descrição**: Define o ano de publicação
- **Validação**: Não pode ser negativo
- **Exceção**: `IllegalArgumentException` se ano for negativo

##### `setQuantidadeEstoque(Integer quantidadeEstoque)`
- **Descrição**: Define a quantidade em estoque
- **Validação**: Não pode ser negativo
- **Exceção**: `IllegalArgumentException` se quantidade for negativa

---

### Classe `Emprestimo`

Representa um empréstimo de livro para um aluno.

#### Atributos
```java
private Integer idEmprestimo;      // PK auto-incrementada
private Integer idAluno;           // FK para Aluno
private Integer idLivro;           // FK para Livro
private LocalDate dataEmprestimo;  // Data do empréstimo
private LocalDate dataDevolucao;   // Data de devolução (null = pendente)
```

#### Construtores
```java
public Emprestimo()
public Emprestimo(Integer idEmprestimo, Integer idAluno, Integer idLivro, LocalDate dataEmprestimo, LocalDate dataDevolucao)
```

#### Métodos Principais

##### `isDevolvido()`
- **Descrição**: Verifica se o livro foi devolvido
- **Retorno**: `boolean` - true se `dataDevolucao` não for nula

##### `diasDeAtraso(LocalDate dataReferencia)`
- **Descrição**: Calcula dias de atraso na devolução
- **Parâmetros**: `dataReferencia` - data para comparação
- **Retorno**: `long` - número de dias (0 se não devolvido ou sem atraso)

---

## 🗄️ Pacote `dao`

### Classe `AlunoDAO`

Gerencia operações CRUD para a entidade Aluno.

#### Métodos

##### `save(Aluno aluno)`
- **Descrição**: Salva um novo aluno no banco
- **Parâmetros**: `aluno` - objeto Aluno a ser salvo
- **Retorno**: `int` - ID gerado pelo banco (-1 se falhou)
- **Exceção**: `SQLException` em caso de erro de banco
- **SQL**: `INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES (?, ?, ?)`

##### `findById(int id)`
- **Descrição**: Busca aluno por ID
- **Parâmetros**: `id` - ID do aluno
- **Retorno**: `Aluno` - objeto encontrado ou null
- **Exceção**: `SQLException` em caso de erro de banco

##### `listAll()`
- **Descrição**: Lista todos os alunos cadastrados
- **Retorno**: `List<Aluno>` - lista de todos os alunos
- **Exceção**: `SQLException` em caso de erro de banco

##### `update(Aluno aluno)`
- **Descrição**: Atualiza dados de um aluno existente
- **Parâmetros**: `aluno` - objeto com dados atualizados
- **Retorno**: `boolean` - true se atualizou com sucesso
- **Exceção**: `SQLException` em caso de erro de banco

##### `delete(int id)`
- **Descrição**: Remove um aluno do banco
- **Parâmetros**: `id` - ID do aluno a ser removido
- **Retorno**: `boolean` - true se removeu com sucesso
- **Exceção**: `SQLException` em caso de erro de banco

---

### Classe `LivroDAO`

Gerencia operações CRUD para a entidade Livro.

#### Métodos

##### `save(Livro livro)`
- **Descrição**: Salva um novo livro no banco
- **Parâmetros**: `livro` - objeto Livro a ser salvo
- **Retorno**: `int` - ID gerado pelo banco (-1 se falhou)
- **Exceção**: `SQLException` em caso de erro de banco

##### `findById(int id)`
- **Descrição**: Busca livro por ID
- **Parâmetros**: `id` - ID do livro
- **Retorno**: `Livro` - objeto encontrado ou null
- **Exceção**: `SQLException` em caso de erro de banco

##### `listAll()`
- **Descrição**: Lista todos os livros cadastrados
- **Retorno**: `List<Livro>` - lista de todos os livros
- **Exceção**: `SQLException` em caso de erro de banco

##### `alterarEstoque(int idLivro, int delta)`
- **Descrição**: Altera a quantidade em estoque de um livro
- **Parâmetros**: 
  - `idLivro` - ID do livro
  - `delta` - quantidade a ser adicionada/subtraída
- **Retorno**: `boolean` - true se alterou com sucesso
- **Exceção**: `SQLException` em caso de erro de banco
- **Validação**: Não permite estoque negativo

---

### Classe `EmprestimoDAO`

Gerencia operações de empréstimo e devolução.

#### Métodos

##### `registrarEmprestimo(int idAluno, int idLivro, int prazoDias)`
- **Descrição**: Registra um novo empréstimo
- **Parâmetros**:
  - `idAluno` - ID do aluno
  - `idLivro` - ID do livro
  - `prazoDias` - prazo em dias para devolução
- **Retorno**: `boolean` - true se registrou com sucesso
- **Exceção**: `SQLException` em caso de erro de banco
- **Transação**: 
  1. Decrementa estoque do livro
  2. Insere registro de empréstimo
  3. Commit ou rollback em caso de erro

##### `registrarDevolucao(int idEmprestimo)`
- **Descrição**: Registra a devolução de um empréstimo
- **Parâmetros**: `idEmprestimo` - ID do empréstimo
- **Retorno**: `boolean` - true se registrou com sucesso
- **Exceção**: `SQLException` em caso de erro de banco
- **Transação**:
  1. Atualiza data de devolução
  2. Incrementa estoque do livro
  3. Commit ou rollback em caso de erro

##### `findById(int id)`
- **Descrição**: Busca empréstimo por ID
- **Parâmetros**: `id` - ID do empréstimo
- **Retorno**: `Emprestimo` - objeto encontrado ou null
- **Exceção**: `SQLException` em caso de erro de banco

##### `listarPendentes()`
- **Descrição**: Lista todos os empréstimos ainda não devolvidos
- **Retorno**: `List<Emprestimo>` - lista de empréstimos pendentes
- **Exceção**: `SQLException` em caso de erro de banco
- **SQL**: `SELECT * FROM Emprestimos WHERE data_devolucao IS NULL`

---

## 🔧 Pacote `util`

### Classe `Database`

Gerencia conexões com o banco de dados MySQL.

#### Constantes
```java
private static final String URL = "jdbc:mysql://localhost:3306/livraria?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "200704071223";
```

#### Métodos

##### `getConnection()`
- **Descrição**: Obtém uma conexão com o banco de dados
- **Retorno**: `Connection` - conexão ativa com o banco
- **Exceção**: `SQLException` se não conseguir conectar ou driver não encontrado
- **Comportamento**: 
  - Carrega o driver MySQL JDBC
  - Estabelece conexão com as credenciais configuradas

---

## 🖥️ Classe `Main`

Interface de linha de comando para interação com o usuário.

### Atributos Estáticos
```java
private static final AlunoDAO alunoDAO = new AlunoDAO();
private static final LivroDAO livroDAO = new LivroDAO();
private static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
private static final Scanner in = new Scanner(System.in);
```

### Métodos Principais

#### `main(String[] args)`
- **Descrição**: Ponto de entrada da aplicação
- **Comportamento**: Exibe menu e processa escolhas do usuário

#### Operações do Menu

##### `cadastrarAluno()`
- **Entrada**: Nome, matrícula (7 chars), data nascimento (AAAA-MM-DD)
- **Validação**: Formato de data, matrícula única
- **Saída**: Confirmação com ID gerado

##### `listarAlunos()`
- **Comportamento**: Exibe todos os alunos cadastrados
- **Formato**: Utiliza `toString()` da classe Aluno

##### `cadastrarLivro()`
- **Entrada**: Título, autor, ano publicação, quantidade estoque
- **Validação**: Ano não negativo, estoque não negativo
- **Saída**: Confirmação com ID gerado

##### `listarLivros()`
- **Comportamento**: Exibe todos os livros cadastrados
- **Formato**: Utiliza `toString()` da classe Livro

##### `registrarEmprestimo()`
- **Entrada**: ID aluno, ID livro, prazo em dias
- **Validação**: 
  - Verifica se aluno existe
  - Verifica se livro existe e tem estoque
- **Saída**: Confirmação de sucesso ou falha

##### `registrarDevolucao()`
- **Entrada**: ID do empréstimo
- **Validação**: Verifica se empréstimo existe e está pendente
- **Saída**: Confirmação de sucesso ou falha

##### `listarPendentes()`
- **Comportamento**: Exibe todos os empréstimos não devolvidos
- **Formato**: Utiliza `toString()` da classe Emprestimo

---

## 🔄 Fluxos de Transação

### Empréstimo
```
1. Validar entrada do usuário
2. Iniciar transação
3. Verificar disponibilidade do livro
4. Decrementar estoque
5. Inserir registro de empréstimo
6. Commit da transação
7. Retornar resultado
```

### Devolução
```
1. Validar entrada do usuário
2. Iniciar transação
3. Verificar se empréstimo existe e está pendente
4. Atualizar data de devolução
5. Recuperar ID do livro
6. Incrementar estoque
7. Commit da transação
8. Retornar resultado
```

---

## ⚠️ Tratamento de Exceções

### Exceções de Validação
- `NullPointerException`: Campos obrigatórios nulos
- `IllegalArgumentException`: Valores inválidos (matrícula, estoque, ano)

### Exceções de Banco
- `SQLException`: Erros de conexão, SQL inválido, violação de constraints
- `ClassNotFoundException`: Driver JDBC não encontrado

### Estratégia de Tratamento
- Captura na camada de apresentação (`Main`)
- Exibição de mensagens amigáveis ao usuário
- Rollback automático em transações
- Log de erros para depuração

---

## 📊 Métricas de Performance

### Índices Recomendados
- `Alunos.matricula` (UNIQUE)
- `Livros.titulo`
- `Emprestimos.data_devolucao` (para consultas de pendentes)

### Otimizações Implementadas
- Try-with-resources para fechamento automático de conexões
- PreparedStatements para prevenção de SQL injection
- Transações para operações compostas
- Conexões sob demanda (não pool) 