package dao;

import model.Aluno;
import util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações CRUD da entidade Aluno.
 */
public class AlunoDAO {

    /* ------------------ CREATE ------------------ */
    public int save(Aluno aluno) throws SQLException {
        final String sql = """
                INSERT INTO Alunos (nome_aluno, matricula, data_nascimento)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, aluno.getNomeAluno());
            ps.setString(2, aluno.getMatricula());
            ps.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    aluno.setIdAluno(idGerado);
                    return idGerado;
                }
            }
        }
        return -1;
    }

    /* ------------------ READ ------------------ */
    public Aluno findById(int id) throws SQLException {
        final String sql = "SELECT * FROM Alunos WHERE id_aluno = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAluno(rs) : null;
            }
        }
    }

    public List<Aluno> listAll() throws SQLException {
        final String sql = "SELECT * FROM Alunos";
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) alunos.add(mapAluno(rs));
        }
        return alunos;
    }

    /* ------------------ UPDATE ------------------ */
    public boolean update(Aluno aluno) throws SQLException {
        final String sql = """
                UPDATE Alunos
                   SET nome_aluno = ?, matricula = ?, data_nascimento = ?
                 WHERE id_aluno   = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aluno.getNomeAluno());
            ps.setString(2, aluno.getMatricula());
            ps.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            ps.setInt(4, aluno.getIdAluno());
            return ps.executeUpdate() > 0;
        }
    }

    /* ------------------ DELETE ------------------ */
    public boolean delete(int id) throws SQLException {
        final String sql = "DELETE FROM Alunos WHERE id_aluno = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /* ------------------ MAPPER ------------------ */
    private Aluno mapAluno(ResultSet rs) throws SQLException {
        return new Aluno(
                rs.getInt("id_aluno"),
                rs.getString("nome_aluno"),
                rs.getString("matricula"),
                rs.getDate("data_nascimento").toLocalDate()
        );
    }
}
