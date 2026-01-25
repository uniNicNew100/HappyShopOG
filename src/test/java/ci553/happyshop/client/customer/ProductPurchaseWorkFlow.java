package ci553.happyshop.client.customer;
import ci553.happyshop.catalogue.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class ProductPurchaseWorkFlow {
    private CustomerModel model;
    private FakeDatabase fakeDb;

    @BeforeEach
    void setupDatabaseAndProducts() {
        model = new CustomerModel();

        model.removeProductNotifier = new RemoveProductNotifier() {
            @Override public void showRemovalMsg(String msg) {}
            @Override public void closeNotifierWindow() {}
        };

            List<Product> testProducts = List.of(
                    new Product("0001", "Mouse", "0001.jpg", 19.99, 10),
                    new Product("0002", "Keyboard", "0002.jpg", 49.99, 10)
            );

                fakeDb = new FakeDatabase(testProducts);
                model.databaseRW = fakeDb;
                model.cusView = new CustomerView() {
                @Override
                public void update(String imageName, String trolley, String receipt) {}
        };
    }

    @Test
    void addToTrolleyAndCheckoutWithEnoughStock() throws Exception {
        Product selected = fakeDb.searchByProductId("0001");
        assertNotNull(selected);

        model.addToTrolley(selected);
        assertEquals(1, model.getTrolley().size());
        assertEquals("0001", model.getTrolley().get(0).getProductId());
        assertEquals(1, model.getTrolley().get(0).getOrderedQuantity());

        model.checkOut();

        assertEquals(0, model.getTrolley().size(), "Trolley clear after checkout");
        assertNotNull(model.displayTaReceipt, "Receipt created");
    }

    @Test
    void checkoutWithNoStock() throws Exception {
        Product selected = fakeDb.searchByProductId("0002");
        model.addToTrolley(selected);

        fakeDb.setForceZeroStock(true); // set product to 0
        model.checkOut();

        assertEquals(1, model.getTrolley().size(), "Trolley remains");
        assertTrue(model.displayLaSearchResult != null && model.displayLaSearchResult.contains("Checkout failed"));

    }
}

