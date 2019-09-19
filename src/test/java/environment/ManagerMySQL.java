package environment;


import java.sql.*;

public class ManagerMySQL {


    private static final String url = "jdbc:mysql://ibolit.amedzhidov.docdoc.pro:3306/ibolit_dev";
    private static final String user = "ibolit_dev";
    private static final String password = "Em3Oug9nrohJ0xei";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public static void executeQuery(String query) {
        try {
            connectToDB();
            rs = stmt.executeQuery(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            disconnectFromDB();
        }
    }

    private static void connectToDB() {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            disconnectFromDB();
        }
    }

    private static void disconnectFromDB() {
        try {
            con.close();
        } catch (SQLException se) { }
        try {
            stmt.close();
        } catch (SQLException se) { }
    }

    public static void executeUpdate(String query) {
        try {
            connectToDB();
            stmt.executeUpdate(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            disconnectFromDB();
        }
    }

    public static void connectClassDBDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load class.");
            e.printStackTrace();
        }
    }

    public static void createClinic(String clinicName) {
        executeUpdate("INSERT INTO clinics" +
                "(parent_id, name, description, avatar, site, city, street, email, " +
                "phone, rate, rate_type, include_price, active, created_at, updated_at, is_test, " +
                "main_color, font_color, default_doctor_id) " +
                "VALUES" +
                "(NULL, '" + clinicName + "', NULL, NULL, NULL, 'Donnellybury', '31771 Cummings Greens', " +
                "'langworth.kyleigh@stiedemann.com', '(956) 395-8386', '0.00', 'fixed', '1', '1', " +
                "'2019-04-05 09:55:40', '2019-04-05 09:55:40', '0', '', '', NULL)");
    }

    public static void deleteClinic(String clinicName) {
        executeUpdate("DELETE FROM clinics WHERE name = '" + clinicName + "'");
    }

    public static void createProduct(String productName) {
        executeUpdate("INSERT INTO products(name, is_active, created_at, updated_at) " +
                "VALUES('" + productName + "', '1', '2019-04-05 09:55:40', '2019-04-05 09:55:40')");
    }

    public static void deleteProduct(String productName) {
        executeUpdate("DELETE FROM products WHERE name = '" + productName + "'");
    }

    public static void createSpecialtyDoctor() {
        executeUpdate("INSERT INTO product_specialty_doctor(product_id, specialty, clinic_id, doctor_id) " +
                "VALUES('3', 'pediatrician', '20', '27')");
    }

    public static void createDoctor() {
        executeUpdate("INSERT INTO users(full_name, position, avatar, email, phone, date_of_birth, type, role_id," +
                " password, api_token, code, hash, fingerprint, send_sms, send_email, last_entered, remember_token," +
                " timezone, created_at, updated_at, type_chat, trial_time, auto_resume, registration_code," +
                " activated_at, is_test, external_id, deleted_at, experience, category, science_degree, specialty," +
                " is_available) VALUES('Ben Affleck', 'DOCTOR_URL', NULL, 'emmie.bosco@example.com', '+79169396111'," +
                " '1980-02-01', 'DOCTOR_URL', NULL, '$2y$10$LD3cZGOKU.AjuDqDsnFVWezELcaFWMRmjXq8uantoKfjRQaRG7De2'," +
                " 'MOn7bGPIxn6dpJRUoC2nPwYTqiL5aP2PtmIxUo4iE7oVD3GSCF3h8W7l5Fi3', '4phklS', 'qAS0mSA6'," +
                " '0', '0', '0', NULL, 'blUSTapXajDs1BNFfLOKPrSumVExvthjaE80cP9qqZpA6l54b7vwKXkqbg6W', NULL," +
                " '2019-04-01 17:25:56', '2019-04-01 17:25:56', 'free', '0', '0', '4233', '2019-04-01 17:25:56'," +
                " '0', NULL, NULL, NULL, NULL, NULL, 'pediatrician', '1')");
    }

    public static int getClinicId(String clinicName) {
        int id = -1;
        try {
            executeQuery("SELECT id FROM clinics WHERE name = '" + clinicName + "'");
            if (rs.next()) {
                id  = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectFromDB();
        }
        return id;
    }

    public static String getPediatricianLogin() {
        return getDoctorLogin("pediatrician");
    }

    public static String getTherapistLogin() {
        return getDoctorLogin("therapist");
    }

    public static String getDoctorLogin(String speciality) {
        String login = "";
        try {
            executeQuery("SELECT u.email FROM users u \n" +
                    "JOIN product_specialty_doctor s ON s.doctor_id = u.id\n" +
                    "WHERE u.specialty = '" + speciality + "' AND s.product_id = 1  AND s.clinic_id = 1");
            if (rs.next()) {
                login  = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return login;
    }

    public static void setAvailableDoctor0() {
        executeUpdate("UPDATE users u \n" +
                        "SET u.is_available = 0\n" +
                        "WHERE u.is_available = 1");
    }

    public static void closeAllActiveConsultations() {
        executeUpdate("UPDATE consultations c\n" +
                "SET c.`status` = 'cancelled', c.end_at = now(), c.is_closed = 1\n" +
                "WHERE c.status = 'active'");
    }


}
