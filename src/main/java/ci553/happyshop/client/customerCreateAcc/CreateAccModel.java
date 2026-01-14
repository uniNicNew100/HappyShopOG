package ci553.happyshop.client.customerCreateAcc;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import java.sql.*;


public class CreateAccModel {
    public CreateAccView createAccView;



    public boolean detailsValidation(String username, String password, String passwordConfirm) {
        if (username == null || username.isBlank()) {
            System.out.println("Username cannot be empty");
            return false;
        }

        if (password == null || password.isBlank()) {
            System.out.println("Password cannot be empty");
        }

        if (!password.equals(passwordConfirm)) {
            System.out.println("Passwords do not match");
        }

        return saveCust (username,password,"customer");

    }
    public boolean saveCust(String username, String password, String role) {

        String sql = "INSERT INTO LoginTable (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseRWFactory.dbURL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            stmt.executeUpdate();
            System.out.println("User registered successfully");
            return true;


        } catch (SQLException e) {
            System.out.println("Username already exists or DB error");
            return false;
        }
    }
}
