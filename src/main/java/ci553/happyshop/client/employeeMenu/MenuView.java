package ci553.happyshop.client.employeeMenu;

import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import java.sql.SQLException;

public class MenuView {
    public MenuController menController;
    public Main main;

    public Parent getRoot() {

        VBox root = new VBox(15);
        root.setStyle(UIStyle.rootStyleWarehouse);

        Button orderTrackerBtn = new Button("Order Tracker");
        orderTrackerBtn.setOnAction(this::buttonClicked);

        Button pickerBtn = new Button("Picker");
        pickerBtn.setOnAction(this::buttonClicked);

        Button warehouseBtn = new Button("Warehouse");
        warehouseBtn.setOnAction(this::buttonClicked);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(this::buttonClicked);

        root.getChildren().addAll(orderTrackerBtn, pickerBtn, warehouseBtn, logoutBtn);
        root.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        return root;
    }

    public void buttonClicked(ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            String action = btn.getText();

            menController.doAction(action);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
