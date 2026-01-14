package ci553.happyshop.client.customerCreateAcc;
import ci553.happyshop.client.Main;

public class CreateAccController {
    public CreateAccModel createAccModel;
    public Main main;

    public CreateAccController(Main main) {
        this.main = main;
    }

    public void initiateValidation(String user, String pass, String conPass) {


        if (user.isBlank() || pass.isBlank() || conPass.isBlank()) {
            System.out.println("All fields are required");
            return;
        }


        boolean success = createAccModel.detailsValidation(user, pass, conPass);


        if (success) {
            main.startLoginScene();
        } else {
            System.out.println("Account creation failed");
        }
    }



}
