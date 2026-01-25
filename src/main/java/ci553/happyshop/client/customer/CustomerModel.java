package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;


public class CustomerModel {
    public CustomerView cusView;
    public DatabaseRW databaseRW; //Interface type, not specific implementation
                                  //Benefits: Flexibility: Easily change the database implementation.

    private Product theProduct =null; // product found from search
    private final ArrayList<Product> trolley =  new ArrayList<>(); // a list of products in trolley

    // Four UI elements to be passed to CustomerView for display updates.
    private final String imageName = "imageHolder.jpg";                // Image to show in product preview (Search Page)
    public String displayLaSearchResult;                        // Label showing search result message (Search Page)
    private String displayTaTrolley = "";                                // Text area content showing current trolley items (Trolley Page)
    public String displayTaReceipt = "";                                // Text area content showing receipt after checkout (Receipt Page)
    public RemoveProductNotifier removeProductNotifier;

    /**
     *  Searches product database for items matching the text the user entered.
     *  Tries to search by the product id and then the product name (description)
     * Only products that have stock are returned
     *
     */

    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        try {
            Product product= databaseRW.searchByProductId(keyword);
            if (product != null && product.getStockQuantity()>0) results.add(product);
            else{
                theProduct=null;
                displayLaSearchResult = "No Product was found with term " + keyword;
                System.out.println(displayLaSearchResult);
            }
            results.addAll(databaseRW.searchProduct(keyword));


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     *  Adds selected product to the trolley. If the customer tries to add the same product to the trolley
     *  increase the amount added to the trolley by 1 and then update it in the view.
     */
    public void addToTrolley(Product selected) {
        if (selected == null) {
            displayLaSearchResult = "Please select a product first";
            updateView();
            return;
        }

        if (selected.getStockQuantity() <= 0) {
            removeProductNotifier.showRemovalMsg("Not enough stock for this product: " + selected.getProductDescription());
            updateView();
            return;
        }
        boolean exists = false;
        for (Product p : trolley) {
            if (p.getProductId().equals(selected.getProductId())) {
                p.setOrderedQuantity(p.getOrderedQuantity() + 1);
                exists = true;
                removeProductNotifier.closeNotifierWindow();
                break;
            }
        }
        if (!exists) {
            Product copy = new Product(
                    selected.getProductId(),
                    selected.getProductDescription(),
                    selected.getProductImageName(),
                    selected.getUnitPrice(),
                    selected.getStockQuantity()
            );
            copy.setOrderedQuantity(1);
            trolley.add(copy);
        }
        trolley.sort(Comparator.comparing(Product::getProductId)); //sort trolley by productID
        displayTaTrolley = ProductListFormatter.buildString(trolley);
        displayTaReceipt = ""; // Clear receipt
        updateView();
    }



    /**
     * Groups products by their productId to optimize database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */
    private ArrayList<Product> groupProductsById(ArrayList<Product> proList) {
        Map<String, Product> grouped = new HashMap<>();

        for (Product p : proList) {
            String id = p.getProductId();
            if (grouped.containsKey(id)) {
                Product existing = grouped.get(id);
                existing.setOrderedQuantity(existing.getOrderedQuantity() + p.getOrderedQuantity());
            } else {
                Product copy = new Product(
                        p.getProductId(),
                        p.getProductDescription(),
                        p.getProductImageName(),
                        p.getUnitPrice(),
                        p.getStockQuantity()
                );
                copy.setOrderedQuantity(p.getOrderedQuantity());
                grouped.put(id, copy);
            }
        }

        return new ArrayList<>(grouped.values());
    }

    void checkOut() throws IOException, SQLException {
        if(!trolley.isEmpty()){
            // Group the products in the trolley by productId to optimize stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organized (merged and sorted), grouping is unnecessary.
            ArrayList<Product> groupedTrolley= groupProductsById(trolley);
            ArrayList<Product> insufficientProducts= databaseRW.purchaseStocks(groupedTrolley);

            if(insufficientProducts.isEmpty()){ // If stock is sufficient for all products
                //get OrderHub and tell it to make a new Order
                OrderHub orderHub =OrderHub.getOrderHub();
                Order theOrder = orderHub.newOrder(trolley);
                trolley.clear();
                displayTaTrolley ="";
                displayTaReceipt = String.format(
                        "Order_ID: %s\nOrdered_Date_Time: %s\n%s",
                        theOrder.getOrderId(),
                        theOrder.getOrderedDateTime(),
                        ProductListFormatter.buildString(theOrder.getProductList())
                );
            }
            else{ // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for(Product p : insufficientProducts){
                    errorMsg.append("\u2022 "+ p.getProductId()).append(", ")
                            .append(p.getProductDescription()).append(" (Only ")
                            .append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }

                removeProductNotifier.showRemovalMsg(errorMsg.toString());


                //You can use the provided RemoveProductNotifier class and its showRemovalMsg method for this purpose.
                //remember close the message window where appropriate (using method closeNotifierWindow() of RemoveProductNotifier class)
                displayLaSearchResult = "Checkout failed due to insufficient stock for the following products:\n" + errorMsg;

            }
        }
            trolley.sort(Comparator.comparing(Product::getProductId)); //added to sort trolley by productId
            displayTaTrolley =  ProductListFormatter.buildString(trolley);

        updateView();
    }



    void cancel(){
        trolley.clear();
        displayTaTrolley="";
        removeProductNotifier.closeNotifierWindow();
        updateView();
    }
    void closeReceipt(){
        displayTaReceipt="";
    }

    /**
     * Struggling to get the image to display in the receipt so removed
     */
    public void updateView() {
        String imageName = "imageHolder.jpg"; // default image
        cusView.update(imageName, displayTaTrolley, displayTaReceipt);
    }

    /**
     * returns products info by category
     */
    public List<Product> filterByCategory(String category) {

        return databaseRW.getProductsByCategory(category);
    }
    //gets all products
    public List<Product> getAllProducts() {

        return databaseRW.getAllProducts();
    }

    // extra notes:
     //Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
     //File.toURI(): Converts a File object (a file on the filesystem) to a URI object

    public ArrayList<Product> getTrolley() {
        return trolley;
    }

}
