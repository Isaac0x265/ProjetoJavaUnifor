# Guia de Configuração - Sistema de Biblioteca

## Configuração Rápida

### Pré-requisitos
- Java 17 ou superior
- MySQL 8.0 ou superior
- MySQL Connector/J

### Passos Básicos
1. Configure o banco de dados MySQL
2. Execute o script `database_schema.sql`
3. Configure credenciais em `src/util/Database.java`
4. Compile e execute o projeto

## Instalação do Ambiente

### Java
**Windows/Linux/macOS**: Baixe e instale o JDK 17+ do site da Oracle ou OpenJDK.

Verifique a instalação:
```bash
java -version
javac -version
```

### MySQL
**Windows**: Use o MySQL Installer oficial
**Linux**: `sudo apt install mysql-server`
**macOS**: `brew install mysql`

### MySQL Connector/J
Baixe o arquivo JAR em: https://dev.mysql.com/downloads/connector/j/

## Configuração do Banco de Dados

### 1. Criar Banco
```sql
mysql -u root -p
CREATE DATABASE livraria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE livraria;
```

### 2. Executar Schema
```bash
mysql -u root -p livraria < database_schema.sql
```

### 3. Verificar Instalação
```sql
SHOW TABLES;
SELECT COUNT(*) FROM Alunos;
SELECT COUNT(*) FROM Livros;
```

## Configuração da Aplicação

### Estrutura de Diretórios
```
ProjetoJavaUnifor/
├── src/
│   ├── Main.java
│   ├── model/
│   ├── dao/
│   └── util/
├── mysql-connector-java.jar
└── database_schema.sql
```

### Configurar Credenciais
Edite `src/util/Database.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/livraria?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "sua_senha";
```

## Compilação e Execução

### Linha de Comando
```bash
# Compilar
javac -cp ".:mysql-connector-java.jar" src/**/*.java

# Executar
java -cp ".:mysql-connector-java.jar:src" Main
```

### Script de Build (Linux/macOS)
```bash
#!/bin/bash
javac -cp ".:mysql-connector-java.jar" src/**/*.java
java -cp ".:mysql-connector-java.jar:src" Main
```

### Script de Build (Windows)
```batch
javac -cp ".;mysql-connector-java.jar" src\**\*.java
java -cp ".;mysql-connector-java.jar;src" Main
```

### IDEs
**IntelliJ IDEA**: Adicione o JAR do MySQL Connector nas bibliotecas do projeto
**Eclipse**: Configure o Build Path para incluir o JAR

## Teste da Aplicação

Execute o sistema e teste as funcionalidades básicas:
1. Cadastrar um aluno
2. Cadastrar um livro
3. Registrar um empréstimo
4. Listar empréstimos pendentes
5. Registrar devolução

## Solução de Problemas Comuns

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
Verifique se o JAR do MySQL Connector está no classpath.

### "Access denied for user"
Verifique usuário e senha no MySQL:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nova_senha';
```

### "Unknown database 'livraria'"
Crie o banco novamente:
```sql
CREATE DATABASE livraria;
SOURCE database_schema.sql;
```

### Erro de Compilação
Verifique se está compilando do diretório correto e se a estrutura de pastas está correta.

## Configurações de Produção

Para ambientes de produção, considere:
- Criar usuário específico para a aplicação
- Usar variáveis de ambiente para credenciais
- Configurar SSL para conexões
- Implementar backup automático 