package ci553.happyshop.client.customer;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginView;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

/**
 * The CustomerView is separated into two sections by a line :
 *
 * 1. Search Page – Always visible, allowing customers to browse and search for products.
 * 2. the second page – display either the Trolley Page or the Receipt Page
 *    depending on the current context. Only one of these is shown at a time.
 */

public class CustomerView  {
    public CustomerController cusController;
    public Main main;

    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;
    private final int COLUMN_WIDTH = WIDTH / 2 - 10;

    private HBox hbRoot; // Top-level layout manager
    private VBox vbTrolleyPage;  //vbTrolleyPage and vbReceiptPage will swap with each other when need
    private VBox vbReceiptPage;
    private VBox vbSearchPage;
    private VBox vbProducts;

    TextField tfId; //for user input on the search page. Made accessible so it can be accessed or modified by CustomerModel
    TextField tfName; //for user input on the search page. Made accessible so it can be accessed by CustomerModel
    TextField searchField;
    ComboBox<String> filterComboBox;
    //four controllers needs updating when program going on
    private ImageView ivProduct; //image area in searchPage
    private Label lbProductInfo;//product text info in searchPage
    private TextArea taTrolley; //in trolley Page
    private TextArea taReceipt;//in receipt page
    private Product selectedProduct;
    String userSelectedImageUriEdit;
    boolean isUserSelectedImageEdit = false;
    private ImageView ivProNewPro;
    String imageUriNewPro;

    public Product getSelectedProduct() {
        return selectedProduct;
    }




    public Parent getRoot() {

        vbSearchPage = createSearchPage();
        vbProducts = new VBox(10);          // ✅ product list container
        vbProducts.setPadding(new Insets(10));

        ScrollPane productScroll = new ScrollPane(vbProducts);
        productScroll.setFitToWidth(true);

        vbTrolleyPage = CreateTrolleyPage();
        vbReceiptPage = createReceiptPage();

        Line line = new Line(0, 0, 0, HEIGHT);
        line.setStrokeWidth(4);
        line.setStroke(Color.PINK);

        VBox lineContainer = new VBox(line);
        lineContainer.setAlignment(Pos.CENTER);

        VBox leftSide = new VBox(10, vbSearchPage, productScroll);

        hbRoot = new HBox(10, leftSide, lineContainer, vbTrolleyPage);
        hbRoot.setAlignment(Pos.CENTER);
        hbRoot.setStyle(UIStyle.rootStyle);

        return hbRoot;

    }

    private VBox createSearchPage() {

        Label title = new Label("Product Catalogue");

        searchField = new TextField();
        searchField.setPromptText("Search products...");

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(this::buttonClicked);


        filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll(
                "All", "Home", "TVs", "Kitchen", "Office",
                "Peripherals", "PC", "Laptops", "Smartphones"
        );
        filterComboBox.setValue("All");

        Button btnFilter = new Button("Filter");
        btnFilter.setOnAction(this::buttonClicked);



        HBox hbControls = new HBox(10,
                searchField, btnSearch, filterComboBox, btnFilter
        );
        hbControls.setAlignment(Pos.CENTER);

        vbSearchPage = new VBox(10,
                title,
                hbControls
        );
        vbSearchPage.setPadding(new Insets(10));

        return vbSearchPage;
    }

    private VBox CreateTrolleyPage() {
        Label laPageTitle = new Label("🛒🛒  Trolley 🛒🛒");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefSize(WIDTH/2, HEIGHT-50);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(this::buttonClicked);
        btnCancel.setStyle(UIStyle.buttonStyle);

        Button btnCheckout = new Button("Check Out");
        btnCheckout.setOnAction(this::buttonClicked);
        btnCheckout.setStyle(UIStyle.buttonStyle);

        Button btnLogout = new Button("LogOut");
        btnLogout.setOnAction(this::buttonClicked);
        btnLogout.setStyle(UIStyle.buttonStyle);

        HBox hbBtns = new HBox(10, btnCancel,btnCheckout,btnLogout);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        vbTrolleyPage = new VBox(15, laPageTitle, taTrolley, hbBtns);
        vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
        vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
        vbTrolleyPage.setStyle("-fx-padding: 15px;");
        return vbTrolleyPage;
    }

    private VBox createReceiptPage() {
        Label laPageTitle = new Label("Receipt");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefSize(WIDTH/2, HEIGHT-50);

        Button btnCloseReceipt = new Button("OK & Close"); //btn for closing receipt and showing trolley page
        btnCloseReceipt.setStyle(UIStyle.buttonStyle);

        btnCloseReceipt.setOnAction(this::buttonClicked);

        vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
        vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
        vbReceiptPage.setAlignment(Pos.TOP_CENTER);
        vbReceiptPage.setStyle(UIStyle.rootStyleYellow);
        return vbReceiptPage;
    }
    private HBox createProductBox(Product product) {
        String imageName = product.getProductImageName(); // Get image name (e.g. "0001.jpg")
        String relativeImageUrl = StorageLocation.imageFolder + imageName;
        // Get the full absolute path to the image
        Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
        String imageFullUri = imageFullPath.toUri().toString();// Build the full image Uri
        ImageView ivPro = new ImageView();

        try {
            ivPro = ( new ImageView(new Image(imageFullUri, 50,45, true,true)));
        } catch (Exception e) {
            ivProduct.setImage(new Image("imageHolder.jpg", 60, 60, true, true));
        }

        Label lbInfo = new Label(
                product.getProductDescription() + "\n" +
                        "£" + product.getUnitPrice() + "\n" +
                        "Stock: " + product.getStockQuantity()
        );
        lbInfo.setMinWidth(200);
        lbInfo.setWrapText(true);

        Button btnAdd = new Button("Add to Trolley");
        btnAdd.setOnAction(e -> {
            selectedProduct = product;
            try {
                cusController.doAction("Add to Trolley");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox productBox = new HBox(15,ivPro, lbInfo, btnAdd);
        productBox.setPadding(new Insets(10));
        productBox.setAlignment(Pos.CENTER_LEFT);
        productBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");

        return productBox;
    }


    private void buttonClicked(ActionEvent event) {
        try{
            Button btn = (Button)event.getSource();
            String action = btn.getText();
            if(action.equals("Add to Trolley")){
                showTrolleyOrReceiptPage(vbTrolleyPage); //ensure trolleyPage shows if the last customer did not close their receiptPage
            }
            if(action.equals("OK & Close")){
                showTrolleyOrReceiptPage(vbTrolleyPage);
            }


            cusController.doAction(action);
        }
        catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(String imageName, String trolley, String receipt) {

        ivProduct.setImage(new Image(imageName));
        //lbProductInfo.setText(searchResult);
        taTrolley.setText(trolley);
        if (!receipt.equals("")) {
            showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
    }

    // Replaces the last child of hbRoot with the specified page.
    // the last child is either vbTrolleyPage or vbReceiptPage.
    private void showTrolleyOrReceiptPage(Node pageToShow) {
        int lastIndex = hbRoot.getChildren().size() - 1;
        if (lastIndex >= 0) {
            hbRoot.getChildren().set(lastIndex, pageToShow);
        }
    }

    public void showProducts(List<Product> products) {
        vbProducts.getChildren().clear();

        for (Product product : products) {
            vbProducts.getChildren().add(createProductBox(product));
        }
    }
    public WindowBounds getWindowBounds() {
        Stage stage = (Stage) hbRoot.getScene().getWindow();
        return new WindowBounds(
                stage.getX(),
                stage.getY(),
                stage.getWidth(),
                stage.getHeight()
        );
    }

}
