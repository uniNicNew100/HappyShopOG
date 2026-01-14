package ci553.happyshop.client.employeeMenu;

import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginController;
import ci553.happyshop.client.orderTracker.OrderTrackerClient;
import ci553.happyshop.client.picker.PickerClient;
import ci553.happyshop.client.warehouse.WarehouseClient;
import javafx.stage.Stage;

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
                OrderTrackerClient otc = new OrderTrackerClient();
                otc.start(new Stage());
                break;
            case "Picker":
                PickerClient pc = new PickerClient();
                pc.start(new Stage());
                break;
            case "Warehouse":
                WarehouseClient wc = new WarehouseClient();
                wc.start(new Stage());
                break;

            case "Logout":
                loginController.clearCurrentUser();
                main.startLoginScene();
                break;
        }



    }

}
