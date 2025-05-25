# Guia de Configuração - Sistema de Biblioteca

## 🚀 Configuração Rápida

### Pré-requisitos
- Java 17 ou superior
- MySQL 8.0 ou superior
- MySQL Connector/J 8.0+

### Passos Rápidos
```bash
# 1. Clone/baixe o projeto
# 2. Configure o banco (veja seção detalhada)
# 3. Compile e execute
javac -cp ".:mysql-connector-java.jar" src/**/*.java
java -cp ".:mysql-connector-java.jar:src" Main
```

---

## 📋 Configuração Detalhada

### 1. Instalação do Java

#### Windows
```bash
# Baixe e instale o JDK 17+ do site da Oracle ou OpenJDK
# Verifique a instalação
java -version
javac -version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

#### macOS
```bash
# Usando Homebrew
brew install openjdk@17
java -version
```

### 2. Instalação do MySQL

#### Windows
1. Baixe o MySQL Installer do site oficial
2. Execute a instalação completa
3. Configure senha do root durante a instalação

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

#### macOS
```bash
# Usando Homebrew
brew install mysql
brew services start mysql
mysql_secure_installation
```

### 3. Download do MySQL Connector/J

```bash
# Baixe de: https://dev.mysql.com/downloads/connector/j/
# Ou usando wget (Linux/macOS):
wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.33.jar
```

---

## 🗄️ Configuração do Banco de Dados

### 1. Criar Banco e Usuário

```sql
-- Conecte como root
mysql -u root -p

-- Criar banco
CREATE DATABASE livraria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Criar usuário específico (opcional, mais seguro)
CREATE USER 'biblioteca'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON livraria.* TO 'biblioteca'@'localhost';
FLUSH PRIVILEGES;

-- Usar o banco
USE livraria;
```

### 2. Executar Script de Schema

```bash
# Opção 1: Via linha de comando
mysql -u root -p livraria < database_schema.sql

# Opção 2: Via MySQL Workbench
# - Abra o arquivo database_schema.sql
# - Execute o script completo
```

### 3. Verificar Instalação

```sql
-- Verificar tabelas criadas
SHOW TABLES;

-- Verificar estrutura
DESCRIBE Alunos;
DESCRIBE Livros;
DESCRIBE Emprestimos;

-- Verificar dados de exemplo
SELECT COUNT(*) FROM Alunos;
SELECT COUNT(*) FROM Livros;
```

---

## ⚙️ Configuração da Aplicação

### 1. Estrutura de Diretórios

```
ProjetoJavaUnifor/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Aluno.java
│   │   ├── Livro.java
│   │   └── Emprestimo.java
│   ├── dao/
│   │   ├── AlunoDAO.java
│   │   ├── LivroDAO.java
│   │   └── EmprestimoDAO.java
│   └── util/
│       └── Database.java
├── mysql-connector-java-8.0.33.jar
├── README.md
├── database_schema.sql
└── API_DOCUMENTATION.md
```

### 2. Configurar Credenciais do Banco

Edite o arquivo `src/util/Database.java`:

```java
public final class Database {
    // Altere estas configurações conforme seu ambiente
    private static final String URL = "jdbc:mysql://localhost:3306/livraria?serverTimezone=UTC";
    private static final String USER = "root";  // ou 'biblioteca'
    private static final String PASSWORD = "sua_senha_aqui";
    
    // ... resto do código
}
```

### 3. Configurações Avançadas

#### Configuração de Timezone
```java
// Se tiver problemas de timezone, use:
private static final String URL = "jdbc:mysql://localhost:3306/livraria?serverTimezone=America/Sao_Paulo";
```

#### Configuração SSL (Produção)
```java
// Para ambientes de produção:
private static final String URL = "jdbc:mysql://localhost:3306/livraria?useSSL=true&requireSSL=true";
```

---

## 🔧 Compilação e Execução

### Método 1: Linha de Comando (Recomendado)

```bash
# Navegar para o diretório do projeto
cd /caminho/para/ProjetoJavaUnifor

# Compilar todos os arquivos Java
javac -cp ".:mysql-connector-java-8.0.33.jar" src/**/*.java

# Executar a aplicação
java -cp ".:mysql-connector-java-8.0.33.jar:src" Main
```

### Método 2: Script de Build (Linux/macOS)

Crie um arquivo `build.sh`:
```bash
#!/bin/bash
echo "Compilando projeto..."
javac -cp ".:mysql-connector-java-8.0.33.jar" src/**/*.java

if [ $? -eq 0 ]; then
    echo "Compilação bem-sucedida!"
    echo "Executando aplicação..."
    java -cp ".:mysql-connector-java-8.0.33.jar:src" Main
else
    echo "Erro na compilação!"
fi
```

```bash
chmod +x build.sh
./build.sh
```

### Método 3: Script de Build (Windows)

Crie um arquivo `build.bat`:
```batch
@echo off
echo Compilando projeto...
javac -cp ".;mysql-connector-java-8.0.33.jar" src\**\*.java

if %errorlevel% == 0 (
    echo Compilacao bem-sucedida!
    echo Executando aplicacao...
    java -cp ".;mysql-connector-java-8.0.33.jar;src" Main
) else (
    echo Erro na compilacao!
)
pause
```

### Método 4: IDE (IntelliJ IDEA)

1. **Importar Projeto**:
   - File → Open → Selecionar pasta do projeto

2. **Configurar Classpath**:
   - File → Project Structure → Libraries
   - Adicionar `mysql-connector-java-8.0.33.jar`

3. **Configurar Run Configuration**:
   - Run → Edit Configurations
   - Main class: `Main`
   - Classpath: Incluir todas as dependências

### Método 5: IDE (Eclipse)

1. **Importar Projeto**:
   - File → Import → Existing Projects into Workspace

2. **Configurar Build Path**:
   - Right-click projeto → Properties → Java Build Path
   - Libraries → Add External JARs → Selecionar MySQL Connector

3. **Executar**:
   - Right-click `Main.java` → Run As → Java Application

---

## 🧪 Testes e Validação

### 1. Teste de Conexão

Crie um arquivo `TestConnection.java`:
```java
import util.Database;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = Database.getConnection();
            System.out.println("Conexão bem-sucedida!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
    }
}
```

### 2. Teste Básico de Funcionalidades

```bash
# Execute a aplicação e teste:
# 1. Cadastrar um aluno
# 2. Cadastrar um livro
# 3. Registrar um empréstimo
# 4. Listar empréstimos pendentes
# 5. Registrar devolução
```

---

## 🐛 Solução de Problemas

### Erro: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
```bash
# Solução: Verificar se o JAR está no classpath
ls -la mysql-connector-java*.jar
# Recompilar com o caminho correto do JAR
```

### Erro: "Access denied for user"
```sql
-- Verificar usuário e senha
SELECT user, host FROM mysql.user WHERE user = 'root';

-- Resetar senha se necessário
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nova_senha';
```

### Erro: "Unknown database 'livraria'"
```sql
-- Criar o banco novamente
CREATE DATABASE livraria;
USE livraria;
SOURCE database_schema.sql;
```

### Erro: "Table doesn't exist"
```sql
-- Verificar se as tabelas foram criadas
SHOW TABLES;

-- Se não existirem, executar o schema novamente
SOURCE database_schema.sql;
```

### Erro de Compilação: "package does not exist"
```bash
# Verificar estrutura de diretórios
find src -name "*.java" -type f

# Compilar a partir do diretório correto
cd ProjetoJavaUnifor
javac -cp ".:mysql-connector-java.jar" src/**/*.java
```

---

## 🔒 Configurações de Segurança

### 1. Configuração de Usuário Específico

```sql
-- Criar usuário com privilégios limitados
CREATE USER 'app_biblioteca'@'localhost' IDENTIFIED BY 'senha_forte_123';
GRANT SELECT, INSERT, UPDATE, DELETE ON livraria.* TO 'app_biblioteca'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuração de Firewall (Produção)

```bash
# Linux (UFW)
sudo ufw allow 3306/tcp
sudo ufw enable

# Ou restringir a IPs específicos
sudo ufw allow from 192.168.1.0/24 to any port 3306
```

### 3. Backup Automático

```bash
# Script de backup diário
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u root -p livraria > backup_livraria_$DATE.sql
```

---

## 📈 Monitoramento e Logs

### 1. Habilitar Logs do MySQL

```sql
-- Verificar configuração de logs
SHOW VARIABLES LIKE 'general_log%';

-- Habilitar log geral (desenvolvimento)
SET GLOBAL general_log = 'ON';
```

### 2. Logs da Aplicação

Adicione logging básico na classe `Database.java`:
```java
public static Connection getConnection() throws SQLException {
    System.out.println("[" + LocalDateTime.now() + "] Conectando ao banco...");
    // ... código existente
}
```

---

## 🚀 Deploy em Produção

### 1. Configurações de Produção

```java
// Database.java para produção
private static final String URL = "jdbc:mysql://servidor-producao:3306/livraria?useSSL=true";
private static final String USER = System.getenv("DB_USER");
private static final String PASSWORD = System.getenv("DB_PASSWORD");
```

### 2. Variáveis de Ambiente

```bash
# Linux/macOS
export DB_USER=usuario_producao
export DB_PASSWORD=senha_segura
export DB_URL=jdbc:mysql://servidor:3306/livraria

# Windows
set DB_USER=usuario_producao
set DB_PASSWORD=senha_segura
```

### 3. Empacotamento JAR

```bash
# Criar JAR executável
jar cfm biblioteca.jar manifest.txt -C src . mysql-connector-java.jar

# Executar JAR
java -jar biblioteca.jar
```

---

## 📞 Suporte

### Logs Úteis para Debug

```bash
# Logs do MySQL
tail -f /var/log/mysql/error.log

# Verificar processos Java
jps -v

# Verificar conexões MySQL
mysql -u root -p -e "SHOW PROCESSLIST;"
```

### Informações do Sistema

```bash
# Versão do Java
java -version

# Versão do MySQL
mysql --version

# Verificar porta MySQL
netstat -tlnp | grep 3306
``` 