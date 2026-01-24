package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class FakeDatabase implements DatabaseRW {
    private final List<Product> catalogue = new ArrayList<>();
    private boolean forceZeroStock = false;

    public FakeDatabase(List<Product> products) {
        catalogue.addAll(products);
    }

    public void setForceZeroStock(boolean value) {
        this.forceZeroStock = value;
    }

    @Override
    public ArrayList<Product> searchProduct(String keyword) {
        ArrayList<Product> out = new ArrayList<>();
        for (Product p : catalogue) {
            if (p.getProductId().equalsIgnoreCase(keyword)
                    || p.getProductDescription().toLowerCase().contains(keyword.toLowerCase())) {
                out.add(p);
            }
        }
        return out;
    }

    @Override
    public Product searchByProductId(String productId) {
        for (Product p : catalogue) {
            if (p.getProductId().equalsIgnoreCase(productId)) return p;
        }
        return null;
    }

    @Override
    public ArrayList<Product> purchaseStocks(ArrayList<Product> proList) throws SQLException {
        ArrayList<Product> zeroStock = new ArrayList<>();

        if (forceZeroStock) {
            zeroStock.addAll(proList);
            return zeroStock;
        }

        return zeroStock;
    }

    @Override public void updateProduct(String id, String des, double price, String imageName, int stock) {}
    @Override public void deleteProduct(String id) {}
    @Override public void insertNewProduct(String id, String des, double price, String image, int stock) {}
    @Override public void updateProductCategory(String productId, String categoryName) {}
    @Override public boolean isProIdAvailable(String productId) { return true; }
    @Override public List<Product> getAllProducts() { return new ArrayList<>(catalogue); }
    @Override public List<Product> getProductsByCategory(String category) { return new ArrayList<>(); }
    @Override public String getProductCategory(String productId) { return null; }
}

