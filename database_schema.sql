-- ============================================
-- Schema para Sistema de Gerenciamento de Biblioteca
-- ============================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS livraria 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE livraria;

-- ============================================
-- Tabela de Alunos
-- ============================================
CREATE TABLE Alunos (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    nome_aluno VARCHAR(255) NOT NULL,
    matricula VARCHAR(7) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    
    -- Índices para performance
    INDEX idx_matricula (matricula),
    INDEX idx_nome (nome_aluno)
) ENGINE=InnoDB;

-- ============================================
-- Tabela de Livros
-- ============================================
CREATE TABLE Livros (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) DEFAULT 'Desconhecido',
    ano_publicacao INT,
    quantidade_estoque INT DEFAULT 0 CHECK (quantidade_estoque >= 0),
    
    -- Índices para performance
    INDEX idx_titulo (titulo),
    INDEX idx_autor (autor),
    INDEX idx_ano (ano_publicacao)
) ENGINE=InnoDB;

-- ============================================
-- Tabela de Empréstimos
-- ============================================
CREATE TABLE Emprestimos (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL DEFAULT (CURDATE()),
    data_devolucao DATE NULL,
    
    -- Chaves estrangeiras
    FOREIGN KEY (id_aluno) REFERENCES Alunos(id_aluno) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_livro) REFERENCES Livros(id_livro) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    
    -- Índices para performance
    INDEX idx_aluno (id_aluno),
    INDEX idx_livro (id_livro),
    INDEX idx_data_emprestimo (data_emprestimo),
    INDEX idx_pendentes (data_devolucao),
    
    -- Constraint para garantir que data_devolucao >= data_emprestimo
    CONSTRAINT chk_datas CHECK (data_devolucao IS NULL OR data_devolucao >= data_emprestimo)
) ENGINE=InnoDB;

-- ============================================
-- Dados de exemplo (opcional)
-- ============================================

-- Inserir alguns alunos de exemplo
INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES
('João Silva Santos', '2021001', '2000-05-15'),
('Maria Oliveira Costa', '2021002', '1999-08-22'),
('Pedro Souza Lima', '2021003', '2001-03-10'),
('Ana Paula Ferreira', '2021004', '2000-11-30');

-- Inserir alguns livros de exemplo
INSERT INTO Livros (titulo, autor, ano_publicacao, quantidade_estoque) VALUES
('Clean Code', 'Robert C. Martin', 2008, 5),
('Design Patterns', 'Gang of Four', 1994, 3),
('Effective Java', 'Joshua Bloch', 2017, 4),
('Java: The Complete Reference', 'Herbert Schildt', 2020, 2),
('Spring in Action', 'Craig Walls', 2018, 3);

-- ============================================
-- Views úteis
-- ============================================

-- View para empréstimos ativos com informações completas
CREATE VIEW vw_emprestimos_ativos AS
SELECT 
    e.id_emprestimo,
    a.nome_aluno,
    a.matricula,
    l.titulo,
    l.autor,
    e.data_emprestimo,
    DATEDIFF(CURDATE(), e.data_emprestimo) AS dias_emprestado
FROM Emprestimos e
JOIN Alunos a ON e.id_aluno = a.id_aluno
JOIN Livros l ON e.id_livro = l.id_livro
WHERE e.data_devolucao IS NULL;

-- View para estatísticas de livros
CREATE VIEW vw_estatisticas_livros AS
SELECT 
    l.id_livro,
    l.titulo,
    l.autor,
    l.quantidade_estoque,
    COUNT(e.id_emprestimo) AS total_emprestimos,
    COUNT(CASE WHEN e.data_devolucao IS NULL THEN 1 END) AS emprestimos_ativos
FROM Livros l
LEFT JOIN Emprestimos e ON l.id_livro = e.id_livro
GROUP BY l.id_livro, l.titulo, l.autor, l.quantidade_estoque;

-- ============================================
-- Procedures úteis
-- ============================================

DELIMITER //

-- Procedure para verificar disponibilidade de livro
CREATE PROCEDURE sp_verificar_disponibilidade(
    IN p_id_livro INT,
    OUT p_disponivel BOOLEAN
)
BEGIN
    DECLARE v_estoque INT DEFAULT 0;
    
    SELECT quantidade_estoque INTO v_estoque
    FROM Livros 
    WHERE id_livro = p_id_livro;
    
    SET p_disponivel = (v_estoque > 0);
END //

-- Procedure para relatório de empréstimos por período
CREATE PROCEDURE sp_relatorio_emprestimos(
    IN p_data_inicio DATE,
    IN p_data_fim DATE
)
BEGIN
    SELECT 
        a.nome_aluno,
        a.matricula,
        l.titulo,
        l.autor,
        e.data_emprestimo,
        e.data_devolucao,
        CASE 
            WHEN e.data_devolucao IS NULL THEN 'Pendente'
            ELSE 'Devolvido'
        END AS status
    FROM Emprestimos e
    JOIN Alunos a ON e.id_aluno = a.id_aluno
    JOIN Livros l ON e.id_livro = l.id_livro
    WHERE e.data_emprestimo BETWEEN p_data_inicio AND p_data_fim
    ORDER BY e.data_emprestimo DESC;
END //

DELIMITER ;

-- ============================================
-- Triggers para auditoria (opcional)
-- ============================================

-- Tabela de log para auditoria
CREATE TABLE log_emprestimos (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_emprestimo INT,
    acao VARCHAR(20),
    data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) DEFAULT USER()
);

DELIMITER //

-- Trigger para log de novos empréstimos
CREATE TRIGGER tr_log_emprestimo_insert
AFTER INSERT ON Emprestimos
FOR EACH ROW
BEGIN
    INSERT INTO log_emprestimos (id_emprestimo, acao)
    VALUES (NEW.id_emprestimo, 'EMPRESTIMO');
END //

-- Trigger para log de devoluções
CREATE TRIGGER tr_log_emprestimo_update
AFTER UPDATE ON Emprestimos
FOR EACH ROW
BEGIN
    IF OLD.data_devolucao IS NULL AND NEW.data_devolucao IS NOT NULL THEN
        INSERT INTO log_emprestimos (id_emprestimo, acao)
        VALUES (NEW.id_emprestimo, 'DEVOLUCAO');
    END IF;
END //

DELIMITER ;

-- ============================================
-- Comentários finais
-- ============================================

-- Este schema inclui:
-- 1. Estrutura básica das tabelas com constraints apropriadas
-- 2. Índices para melhorar performance das consultas
-- 3. Dados de exemplo para testes
-- 4. Views para consultas comuns
-- 5. Procedures para operações específicas
-- 6. Sistema de auditoria com triggers
-- 7. Validações de integridade referencial 