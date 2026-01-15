package ci553.happyshop.client.login;
import ci553.happyshop.client.Main;
import ci553.happyshop.client.catalogueBrowser.CatalogueController;

public class LoginController {
    public LoginModel loginModel;
    public Main main;
    public CatalogueController catalogueController;

    private static Boolean currentUser;
    public static void setCurrentUser() { currentUser = true; }
    //public static Boolean getCurrentUser() { return currentUser; }
    public static void clearCurrentUser() { currentUser = false; }
    public LoginController(Main main) {
        this.main = main;
    }

    public void authenticate(String username, String password) {
        String role = loginModel.authenticate(username, password);

        if (role != null) {

            if (role.equalsIgnoreCase("customer")) {
                setCurrentUser();
                main.startCustomerClient();

            } else if (role.equalsIgnoreCase("employee")) {
                setCurrentUser();
                main.startEmployeeMenu();
            }
        } else {
            System.out.println("Login failed: invalid credentials.");
        }
    }
}
