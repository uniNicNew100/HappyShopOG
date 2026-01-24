package ci553.happyshop.client.login;
import ci553.happyshop.client.Main;


public class LoginController {
    public LoginModel loginModel;
    public Main main;

    //Need to implement. Tracks state of a user if they are logged in or not
    private static Boolean currentUser;
    public static void setCurrentUser() { currentUser = true; }
    //public static Boolean getCurrentUser() { return currentUser; }
    public static void clearCurrentUser() { currentUser = false; }
    /**
     Constructor that refrences the main class so that login controller can call a method to switch scenes/start another view
     */
    public LoginController(Main main) {
        this.main = main;
    }

    /**
     * Authenticates the user with the supplied credentials
     */
    public void authenticate(String username, String password) {
        // Asks the login model to authenticate and return the user's role
        String role = loginModel.authenticate(username, password);

        if (role != null) {
            // If the user is a customer start the customer client
            if (role.equalsIgnoreCase("customer")) {
                setCurrentUser();
                main.startCustomerClient();

                // If the user is an employee, start the employee menu
            } else if (role.equalsIgnoreCase("employee")) {
                setCurrentUser();
                main.startEmployeeMenu();
            }
        } else {
            System.out.println("Login failed: invalid credentials.");
        }
    }
}
