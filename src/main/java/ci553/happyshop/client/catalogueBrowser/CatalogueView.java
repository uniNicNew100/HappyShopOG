package ci553.happyshop.client.catalogueBrowser;

import ci553.happyshop.catalogue.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


import java.util.List;

public class CatalogueView {
    public TextField searchField;
    public ComboBox<String> filterComboBox;
    private TextArea productArea;
    private BorderPane root;
    private CatalogueController controller;

    public CatalogueView() {
        root = new BorderPane();

        // Top: Search and filter
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(10));

        searchField = new TextField();
        searchField.setPromptText("Search products...");

        Button searchBtn = new Button("Search");

        filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Home","Kitchen", "Office", "Peripherals","PC","Laptops","Smartphones");
        filterComboBox.setValue("All");

        Button filterBtn = new Button("Filter");

        topBox.getChildren().addAll(searchField, searchBtn, filterComboBox, filterBtn);

        productArea = new TextArea();
        productArea.setEditable(false);

        root.setTop(topBox);
        root.setCenter(productArea);

        searchBtn.setOnAction(e -> controllerSearch());
        filterBtn.setOnAction(e -> controllerFilter());
    }

    public void setController(CatalogueController controller) {
        this.controller = controller;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void showProducts(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.toString()).append("\n------------------\n");
        }
        productArea.setText(sb.toString());
    }

    private void controllerSearch() {
        if (controller != null) controller.search();
    }

    private void controllerFilter() {
        if (controller != null) controller.filter();
    }
}
