package ci553.happyshop.client.customerCreateAcc;
import ci553.happyshop.client.Main;

public class CreateAccController {
    public CreateAccModel createAccModel;
    public Main main;

    /**
     Constructor that refrences the main class so that create account controller can call a method to switch scenes/start another view
     */
    public CreateAccController(Main main) {
        this.main = main;
    }

    /**
     * Tries to validate a users login details when the user tries to create an account
     * @param user username
     * @param pass password
     * @param conPass confirm password
     */
    public void initiateValidation(String user, String pass, String conPass) {

        //checks if the user has inputted their credintials
        if (user.isBlank() || pass.isBlank() || conPass.isBlank()) {
            System.out.println("All fields are required");
            return;
        }

        //send details to model to handle logic
        boolean success = createAccModel.detailsValidation(user, pass, conPass);


        if (success) {
            main.startLoginScene();
        } else {
            System.out.println("Account creation failed");
        }
    }



}
