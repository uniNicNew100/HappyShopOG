package ci553.happyshop.client.customer;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Behaviour-based test: verifies that CustomerModel interacts with DatabaseRW during checkout.
 */
public class CustDatabaseMockInteraction {

    private CustomerModel model;
    private DatabaseRW dbMock;

    @BeforeEach
    void setup() {
        model = new CustomerModel();
        dbMock = mock(DatabaseRW.class);

        model.databaseRW = dbMock;

        model.removeProductNotifier = new RemoveProductNotifier() {
            @Override public void showRemovalMsg(String msg) {}
            @Override public void closeNotifierWindow() {}
        };

        model.cusView = new CustomerView() {
            @Override public void update(String imageName, String trolley, String receipt) {}
        };
    }
    @Test
    void testCheckoutCallsPurchaseStocksOnce() throws Exception {

        Product p = new Product("0001", "Mouse", "0001.jpg", 19.99, 10);
        model.addToTrolley(p);

        when(dbMock.purchaseStocks(any(ArrayList.class))).thenReturn(new ArrayList<>());

        model.checkOut();

        verify(dbMock, times(1)).purchaseStocks(any(ArrayList.class));
    }
}

