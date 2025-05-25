# Documentação da API - Sistema de Biblioteca

## Visão Geral

Esta documentação apresenta as principais classes e métodos do sistema de gerenciamento de biblioteca.

## Estrutura do Projeto

O projeto está organizado nos seguintes pacotes:

- **model**: Entidades de domínio (Aluno, Livro, Emprestimo)
- **dao**: Camada de acesso a dados com operações CRUD
- **util**: Utilitários para conexão com banco de dados
- **Main**: Interface de usuário via console

## Pacote Model

### Classe Aluno

Representa um estudante cadastrado no sistema com validações de matrícula e nome obrigatório.

**Principais métodos:**
- `setNomeAluno()`: Define nome (obrigatório)
- `setMatricula()`: Define matrícula de 7 caracteres
- Getters/setters padrão para todos os atributos

### Classe Livro

Representa um livro do acervo com controle de estoque e validações.

**Principais métodos:**
- `setTitulo()`: Define título (obrigatório)
- `setAutor()`: Define autor (padrão "Desconhecido")
- `setQuantidadeEstoque()`: Define estoque (não pode ser negativo)

### Classe Emprestimo

Representa um empréstimo de livro para um aluno com controle de datas.

**Principais métodos:**
- `isDevolvido()`: Verifica se foi devolvido
- `diasDeAtraso()`: Calcula dias de atraso

## Pacote DAO

### Classe AlunoDAO

Gerencia operações CRUD para alunos.

**Métodos principais:**
- `save(Aluno)`: Salva novo aluno, retorna ID gerado
- `findById(int)`: Busca aluno por ID
- `listAll()`: Lista todos os alunos
- `update(Aluno)`: Atualiza dados do aluno
- `delete(int)`: Remove aluno

### Classe LivroDAO

Gerencia operações CRUD para livros com controle de estoque.

**Métodos principais:**
- `save(Livro)`: Salva novo livro
- `findById(int)`: Busca livro por ID
- `listAll()`: Lista todos os livros
- `alterarEstoque(int, int)`: Altera quantidade em estoque

### Classe EmprestimoDAO

Gerencia empréstimos e devoluções com transações.

**Métodos principais:**
- `registrarEmprestimo(int, int, int)`: Registra empréstimo com validação de estoque
- `registrarDevolucao(int)`: Registra devolução e repõe estoque
- `listarPendentes()`: Lista empréstimos não devolvidos

## Pacote Util

### Classe Database

Gerencia conexões com MySQL.

**Método principal:**
- `getConnection()`: Retorna conexão ativa com o banco

## Classe Main

Interface de console com menu interativo para todas as operações do sistema.

**Operações disponíveis:**
- Cadastro e listagem de alunos
- Cadastro e listagem de livros
- Registro de empréstimos e devoluções
- Consulta de empréstimos pendentes

## Tratamento de Erros

O sistema trata exceções SQL e de validação, exibindo mensagens apropriadas ao usuário. Operações de empréstimo/devolução utilizam transações com rollback automático em caso de erro. 