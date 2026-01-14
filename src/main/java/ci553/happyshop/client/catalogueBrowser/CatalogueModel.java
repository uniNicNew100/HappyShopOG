package ci553.happyshop.client.catalogueBrowser;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogueModel {
    public DatabaseRW databaseRW;

    public CatalogueModel(DatabaseRW databaseRW) {
        this.databaseRW = databaseRW;
    }

    public List<Product> getAllProducts() {
        return databaseRW.getAllProducts(); // Assumes DatabaseRW has this method
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        try {
            Product p = databaseRW.searchByProductId(keyword);
            if (p != null) results.add(p);

            results.addAll(databaseRW.searchProduct(keyword));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Product> filterByCategory(String category) {
        return databaseRW.getProductsByCategory(category);
    }
}
