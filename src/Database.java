import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    static String url = "jdbc:mysql://localhost:3306/livraria";
    static String username = "root";
    static String password= "200704071223";
    static String SQL = "SELECT * FROM livros";


     public static void connectToDatabase () throws Exception {

            Connection myConnection = DriverManager.getConnection
                    (url,username,password);
         System.out.println("Connection established successfully");

            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery(SQL);

            myResultSet.next();

            String name = myResultSet.getString(1);
            myResultSet.next();

            String name2 = myResultSet.getString(1);
            System.out.printf("Heres the result we got: %s %s",name,name2);

            myConnection.close();

    }

}
