package ci553.happyshop.client.employeeMenu;

import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginController;

import java.io.IOException;
import java.sql.SQLException;

public class MenuController {
    Main main;
    LoginController loginController;
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
