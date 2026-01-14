package ci553.happyshop.client.customer;

import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginController;
import ci553.happyshop.client.login.LoginView;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {
    public CustomerModel cusModel;
    public Main main;

    public CustomerController(Main main) {
        this.main = main;
    }

    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "Search":
                cusModel.search();
                break;
            case "Add to Trolley":
                cusModel.addToTrolley();
                break;
            case "Cancel":
                cusModel.cancel();
                break;
            case "Check Out":
                cusModel.checkOut();
                break;
            case "OK & Close":
                cusModel.closeReceipt();
                break;
            case "LogOut":
                LoginController.clearCurrentUser();
                main.startLoginScene();
                break;
        }
    }

}
