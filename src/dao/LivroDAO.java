package dao;

import model.Livro;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações CRUD da entidade Livro.
 */
public class LivroDAO {

    /* ------------------ CREATE ------------------ */
    public int save(Livro livro) throws SQLException {
        final String sql = """
                INSERT INTO Livros (titulo, autor, ano_publicacao, quantidade_estoque)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setObject(3, livro.getAnoPublicacao(), Types.INTEGER);
            ps.setInt(4, livro.getQuantidadeEstoque());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    livro.setIdLivro(idGerado);
                    return idGerado;
                }
            }
        }
        return -1;
    }

    /* ------------------ READ ------------------ */
    public Livro findById(int id) throws SQLException {
        final String sql = "SELECT * FROM Livros WHERE id_livro = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapLivro(rs) : null;
            }
        }
    }

    public List<Livro> listAll() throws SQLException {
        final String sql = "SELECT * FROM Livros";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) livros.add(mapLivro(rs));
        }
        return livros;
    }

    /* ------------------ UPDATE ------------------ */
    public boolean update(Livro livro) throws SQLException {
        final String sql = """
                UPDATE Livros
                   SET titulo = ?, autor = ?, ano_publicacao = ?, quantidade_estoque = ?
                 WHERE id_livro = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setObject(3, livro.getAnoPublicacao(), Types.INTEGER);
            ps.setInt(4, livro.getQuantidadeEstoque());
            ps.setInt(5, livro.getIdLivro());
            return ps.executeUpdate() > 0;
        }
    }

    /* ------------------ DELETE ------------------ */
    public boolean delete(int id) throws SQLException {
        final String sql = "DELETE FROM Livros WHERE id_livro = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /* ------------------ Helpers ------------------ */
    private Livro mapLivro(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getInt("id_livro"),
                rs.getString("titulo"),
                rs.getString("autor"),
                (Integer) rs.getObject("ano_publicacao"),
                rs.getInt("quantidade_estoque")
        );
    }

    /** Ajusta o estoque em +delta (positivo ou negativo). */
    public boolean alterarEstoque(int idLivro, int delta) throws SQLException {
        final String sql = """
                UPDATE Livros
                   SET quantidade_estoque = quantidade_estoque + ?
                 WHERE id_livro = ? AND quantidade_estoque + ? >= 0
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, delta);
            ps.setInt(2, idLivro);
            ps.setInt(3, delta);
            return ps.executeUpdate() > 0;
        }
    }
}
