package ci553.happyshop.client.employeeMenu;

import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginController;

import java.io.IOException;
import java.sql.SQLException;

public class MenuController {
    Main main;
    LoginController loginController;

    /**
     Constructor that refrences the main class so that menu controller can call a method to switch scenes/start another view
     */
    public MenuController(Main main) {
        this.main = main;
    }

    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "Order Tracker":
                main.startOrderTracker();
                break;
            case "Picker":
                main.startPickerClient();
                break;
            case "Warehouse":
                main.startWarehouseClient();
                break;

            case "Logout":
                loginController.clearCurrentUser();
                main.startLoginScene();
                break;
        }



    }

}
