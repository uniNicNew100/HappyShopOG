package ci553.happyshop.client.customerCreateAcc;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import java.sql.*;


public class CreateAccModel {
    public CreateAccView createAccView;



    public boolean detailsValidation(String username, String password, String passwordConfirm) {
        //checks if username is not null or empty
        if (username == null || username.isBlank()) {
            System.out.println("Username cannot be empty");
            return false;
        }
        //checks if password is not null or empty
        if (password == null || password.isBlank()) {
            System.out.println("Password cannot be empty");
        }
        //ensures password and confirmation passwords are the same
        if (!password.equals(passwordConfirm)) {
            System.out.println("Passwords do not match");
        }
        //attempts to save to database
        return saveCust (username,password,"customer");

    }

    /**
     * Saves a new customer to LoginTable
     * @param username username
     * @param password password
     * @param role Will always be customer as no way to create employee account yet
     * @return
     */
    public boolean saveCust(String username, String password, String role) {

        //SQL statement for inserting a new user to table
        String sql = "INSERT INTO LoginTable (username, password, role) VALUES (?, ?, ?)";

        //ensures the connection and statement are closed
        try (Connection conn = DriverManager.getConnection(DatabaseRWFactory.dbURL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //Joins values entered by user to the statement
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            //excecute sql statement
            stmt.executeUpdate();
            System.out.println("User registered successfully");
            return true;

        //catch if theres exception
        } catch (SQLException e) {
            System.out.println("Username already exists or DB error");
            return false;
        }
    }
}
