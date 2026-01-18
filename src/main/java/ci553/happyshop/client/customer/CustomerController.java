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

    public CustomerController(Main main) {
        this.main = main;
    }

    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "Search":
                search();
                break;
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
    public void loadAllProducts() {
        List<Product> products = cusModel.getAllProducts();
        cusView.showProducts(products);
    }

    private void search() {
        String keyword = cusView.searchField.getText().trim();

        if (!keyword.isEmpty()) {
            List<Product> results = cusModel.searchProducts(keyword);
            cusView.showProducts(results);
        } else {
            loadAllProducts();
        }
    }

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
