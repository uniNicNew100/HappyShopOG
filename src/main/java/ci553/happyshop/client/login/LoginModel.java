package ci553.happyshop.client.login;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import java.sql.*;

public class LoginModel {
    public LoginView loginView;

    /**
     * Authemticates a user by comparing the deatils submitted by the user with the details stored
     * in the daabase
     * @param username
     * @param password
     * @return the users role if authentication is valid or null if not
     */
    public String authenticate(String username, String password) {
        String dbURL = DatabaseRWFactory.dbURL;

        String role = null; //stores role if a matching user is found

        //sql statement
        String sql = "SELECT role FROM LoginTable WHERE username=? AND password=?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }
}
