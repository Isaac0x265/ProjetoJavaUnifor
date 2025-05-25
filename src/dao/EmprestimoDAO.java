package dao;

import model.Emprestimo;
import util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelo fluxo de empréstimos e devoluções.
 */
public class EmprestimoDAO {

    private final LivroDAO livroDAO = new LivroDAO();

    /* ------------------ Registrar Empréstimo ------------------ */
    public boolean registrarEmprestimo(int idAluno, int idLivro, int prazoDias) throws SQLException {
        final String sqlInsert = """
                INSERT INTO Emprestimos
                      (id_aluno, id_livro, data_emprestimo, data_devolucao)
                VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY))
                """;

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);           // início da transação

            // 1) Decrementa estoque
            if (!livroDAO.alterarEstoque(idLivro, -1)) {
                conn.rollback();
                return false;
            }

            // 2) Insere registro de empréstimo
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, idAluno);
                ps.setInt(2, idLivro);
                ps.setInt(3, prazoDias);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    /* ------------------ Registrar Devolução ------------------ */
    public boolean registrarDevolucao(int idEmprestimo) throws SQLException {
        final String sqlSelect  = "SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ? AND data_devolucao IS NOT NULL";
        final String sqlUpdate  = "UPDATE Emprestimos SET data_devolucao = CURDATE() WHERE id_emprestimo = ? AND data_devolucao IS NULL";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // 1) Marca devolução
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, idEmprestimo);
                if (ps.executeUpdate() == 0) {   // não encontrou ou já devolvido
                    conn.rollback();
                    return false;
                }
            }

            // 2) Recupera id_livro para repor estoque
            int idLivro;
            try (PreparedStatement ps2 = conn.prepareStatement("SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ?")) {
                ps2.setInt(1, idEmprestimo);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    idLivro = rs.getInt("id_livro");
                }
            }

            // 3) Incrementa estoque
            if (!livroDAO.alterarEstoque(idLivro, +1)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        }
    }

    /* ------------------ Consultas ------------------ */
    public Emprestimo findById(int id) throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE id_emprestimo = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapEmprestimo(rs) : null;
            }
        }
    }

    /** Lista todos os empréstimos ainda não devolvidos. */
    public List<Emprestimo> listarPendentes() throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE data_devolucao IS NULL";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapEmprestimo(rs));
        }
        return lista;
    }

    /* ------------------ Mapper ------------------ */
    private Emprestimo mapEmprestimo(ResultSet rs) throws SQLException {
        Date devolucao = rs.getDate("data_devolucao");
        return new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_aluno"),
                rs.getInt("id_livro"),
                rs.getDate("data_emprestimo").toLocalDate(),
                devolucao == null ? null : devolucao.toLocalDate()
        );
    }
}
