package ci553.happyshop.client.customerCreateAcc;

import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class CreateAccView {
    public CreateAccModel createAccModel;
    public CreateAccController createAccController;
    public Main main;
    public VBox root;

    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;

    private TextField username;
    private TextField password;
    private TextField confirmPass;


    public Parent getRoot() {

        root = new VBox(10);

        username = new TextField();
        username.setPromptText("Username");

        password = new TextField();
        password.setPromptText("Password");

        confirmPass = new TextField();
        confirmPass.setPromptText("Re-Enter Password");


        Button btnCreateAccount = new Button("Create Account");
        btnCreateAccount.setOnAction(e ->
                createAccController.initiateValidation(
                        username.getText(),
                        password.getText(),
                        confirmPass.getText()
                )
        );

        root.getChildren().addAll(username, password, confirmPass,btnCreateAccount);


        return root;
    }

}
