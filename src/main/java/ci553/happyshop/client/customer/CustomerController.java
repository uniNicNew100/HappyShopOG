package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.Main;
import ci553.happyshop.client.login.LoginController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerController {
    public CustomerModel cusModel;
    public CustomerView cusView;
    public Main main;
    /**
    Constructor that refrences the main class so that customer controller can call a method to switch scenes
    or start another view
     */
    public CustomerController(Main main) {
        this.main = main;
    }

    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "Search":
                search();
                break;
            /**
             * Gets the selected product from customer view and if the product is not null do the addtotrolley
             * method in the model
             */
            case "Add to Trolley":
                Product selected = cusView.getSelectedProduct();
                if (selected != null) {
                    cusModel.addToTrolley(selected);
                }
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
            case "Filter":
                filter();
                break;
        }
    }

    /**
     * Loads all the products from the models product getter and displays them in the view
     */
    public void loadAllProducts() {
        List<Product> products = cusModel.getAllProducts();
        cusView.showProducts(products);
    }

    /**
     * Calls the method in model that searches for a product based on the text entered in the search bar, if the search bar isnt empty get
     * matching products otherwise call method that displays all products
     */
    private void search() {
        String keyword = cusView.searchField.getText().trim();

        if (!keyword.isEmpty()) {
            List<Product> results = cusModel.searchProducts(keyword);
            cusView.showProducts(results);
        } else {
            loadAllProducts();
        }
    }

    /**
     *  Calls the filters products by category method in model based on the category selected in the view
     */
    private void filter() {
        String category = cusView.filterComboBox.getValue();

        if ("All".equals(category)) {
            loadAllProducts();
        } else {
            List<Product> results = cusModel.filterByCategory(category);
            cusView.showProducts(results);
        }
    }

}
