package ci553.happyshop.client;

import ci553.happyshop.client.customer.*;

import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.login.LoginController;
import ci553.happyshop.client.login.LoginModel;
import ci553.happyshop.client.login.LoginView;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.picker.PickerView;
import ci553.happyshop.client.employeeMenu.*;
import ci553.happyshop.client.customerCreateAcc.*;

import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.utility.UIStyle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The Main JavaFX application class. The Main class is executable directly.
 * It serves as a foundation for UI logic and starts all the clients (UI) in one go.
 *
 * This class launches all standalone clients (Customer, Picker, OrderTracker, Warehouse, EmergencyExit)
 * and links them together into a fully working system.
 *
 * It performs essential setup tasks, such as initializing the order map in the OrderHub
 * and registering observers.
 *
 * Note: Each client type can be instantiated multiple times (e.g., calling startCustomerClient() as many times as needed)
 * to simulate a multi-user environment, where multiple clients of the same type interact with the system concurrently.
 *
 * @version 1.0
 * @author  Shine Shan University of Brighton
 */

public class Main extends Application {

    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application and calls the @Override start()
    }

    //starts the system
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        startLoginScene();
        //startCustomerClient();
       // startPickerClient();
       // startOrderTracker();

        //startCustomerClient();
       // startPickerClient();
        //startOrderTracker();

        // Initializes the order map for the OrderHub. This must be called after starting the observer clients
        // (such as OrderTracker and Picker clients) to ensure they are properly registered for receiving updates.
        initializeOrderMap();

        startWarehouseClient();
        startWarehouseClient();

        //startEmergencyExit();
    }

    /** The customer GUI -search prodduct, add to trolley, cancel/submit trolley, view receipt
     *
     * Creates the Model, View, and Controller objects, links them together so they can communicate with each other.
     * Also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the CustomerModel.
     * Starts the customer interface.
     *
     * Also creates the RemoveProductNotifier, which tracks the position of the Customer View
     * and is triggered by the Customer Model when needed.
     */
    public void startCustomerClient(){
        CustomerView cusView = new CustomerView();
        CustomerController cusController = new CustomerController(this);
        CustomerModel cusModel = new CustomerModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        cusView.cusController = cusController;
        cusView.main = this;
        cusController.cusModel = cusModel;
        cusController.main = this;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;

        Scene scene = new Scene(cusView.getRoot(), UIStyle.customerWinWidth, UIStyle.customerWinHeight);
        primaryStage.setScene(scene);

        //RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        //removeProductNotifier.cusView = cusView;
        //cusModel.removeProductNotifier = removeProductNotifier;
    }

    /** The picker GUI, - for staff to pack customer's order,
     *
     * Creates the Model, View, and Controller objects for the Picker client.
     * Links them together so they can communicate with each other.
     * Starts the Picker interface.
     *
     * Also registers the PickerModel with the OrderHub to receive order notifications.
     */
    private void startPickerClient(){
        PickerModel pickerModel = new PickerModel();
        PickerView pickerView = new PickerView();
        PickerController pickerController = new PickerController();
        pickerView.pickerController = pickerController;
        pickerController.pickerModel = pickerModel;
        pickerModel.pickerView = pickerView;
        pickerModel.registerWithOrderHub();
        pickerView.start(new Stage());
    }

    //The OrderTracker GUI - for customer to track their order's state(Ordered, Progressing, Collected)
    //This client is simple and does not follow the MVC pattern, as it only registers with the OrderHub
    //to receive order status notifications. All logic is handled internally within the OrderTracker.
    private void startOrderTracker(){
        OrderTracker orderTracker = new OrderTracker();
        orderTracker.registerWithOrderHub();
    }

    //initialize the orderMap<orderId, orderState> for OrderHub during system startup
    private void initializeOrderMap(){
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
    }

    /** The Warehouse GUI- for warehouse staff to manage stock
     * Initializes the Warehouse client's Model, View, and Controller,and links them together for communication.
     * It also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the Model.
     * Once the components are linked, the warehouse interface (view) is started.
     *
     * Also creates the dependent HistoryWindow and AlertSimulator,
     * which track the position of the Warehouse window and are triggered by the Model when needed.
     * These components are linked after launching the Warehouse interface.
     */
    private void startWarehouseClient(){
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        // Link controller, model, and view and start view
        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;
        view.start(new Stage());

        //create dependent views that need window info
        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();

        // Link after start
        model.historyWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;
    }

    //starts the EmergencyExit GUI, - used to close the entire application immediatelly
    private void startEmergencyExit(){
        EmergencyExit.getEmergencyExit();
    }

    public void startLoginScene(){

        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(this);

        loginView.loginController = loginController;
        loginView.main = this;
        loginController.loginModel = loginModel;
        loginController.main = this;
        loginModel.loginView = loginView;
        Scene scene = new Scene(loginView.getRoot(), UIStyle.customerWinWidth, UIStyle.customerWinHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startEmployeeMenu() {
        MenuView menView = new MenuView();
        MenuController controller = new MenuController(this);

        menView.menController = controller;
        menView.main = this;


        Scene scene = new Scene(menView.getRoot(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startCustomerCreate(){

        CreateAccView createAccView = new CreateAccView();
        CreateAccController createAccController = new CreateAccController(this);
        CreateAccModel createAccModel = new CreateAccModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        createAccView.createAccController = createAccController;
        createAccView.main = this;
        createAccController.createAccModel= createAccModel;
        createAccModel.createAccView = createAccView;


        Scene scene = new Scene(createAccView.getRoot(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}



