package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private static final String URL      = "jdbc:mysql://localhost:3306/livraria?serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "200704071223";

    private Database() { }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");     // carrega driver
        } catch (ClassNotFoundException e) {
            /* Converte a falha em SQLException para manter a mesma assinatura
               (poderia lançar RuntimeException se preferir). */
            throw new SQLException("MySQL JDBC Driver não encontrado no classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
