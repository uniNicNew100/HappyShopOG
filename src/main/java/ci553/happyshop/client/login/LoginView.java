package ci553.happyshop.client.login;
import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    public LoginController loginController;
    public Main main;
    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;

    private TextField username;
    private TextField password;

    public Parent getRoot() {

        VBox root = new VBox(10);

        username = new TextField();
        username.setPromptText("Username");

        password = new TextField();
        password.setPromptText("Password");

        Button btnLogin = new Button("Login");

        btnLogin.setOnAction(e ->
                loginController.authenticate(
                        username.getText(),
                        password.getText())
        );

        Button btnCreateAccount = new Button("Create Account");
        btnCreateAccount.setOnAction(e ->
                //main.showCustomerCreate());

        root.getChildren().addAll(username, password, btnLogin,btnCreateAccount);



        root.setStyle("-fx-padding: 20px; -fx-alignment: center; -fx-spacing: 10px;");
        return root;
    }
}
