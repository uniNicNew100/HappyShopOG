package ci553.happyshop.client.catalogueBrowser;
import ci553.happyshop.catalogue.Product;

import java.util.List;

public class CatalogueController {
    public CatalogueModel model;
    public CatalogueView view;

    public void loadAllProducts() {
        List<Product> products = model.getAllProducts();
        view.showProducts(products);
    }

    public void search() {
        String keyword = view.searchField.getText().trim();
        if (!keyword.equals("")) {
            List<Product> results = model.searchProducts(keyword);
            view.showProducts(results);
        }
        else{

            System.out.println("please type product ID or name to search");
        }

    }

    public void filter() {
        String category = view.filterComboBox.getValue();
        if (category.equals("All")) {
            loadAllProducts();
        } else {
            List<Product> results = model.filterByCategory(category);
            view.showProducts(results);
        }
    }
}