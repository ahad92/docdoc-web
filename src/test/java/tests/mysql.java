package tests;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;



public class mysql {
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        String url = "jdbc:mysql://localhost:3306/parse?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Connection connection = DriverManager.getConnection(url, "root", "");
        Statement st = connection.createStatement();
        st.executeUpdate("INSERT INTO books(authors,pages) VALUES('smith' , 56546)");


        try {
            connection = DriverManager.getConnection(url, "root", "");


        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

        System.out.println("Inserting records into the table...");


    }
}





/* public void insert(String authors, double pages) {
        String sql = "INSERT INTO books(authors,pages) VALUES(?,?)";
        try (Connection connection = this.connection(authors,pages);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, authors);
            pstmt.setDouble(2, pages);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}

*/