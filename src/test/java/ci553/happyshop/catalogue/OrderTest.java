package ci553.happyshop.catalogue;

import ci553.happyshop.orderManagement.OrderState;
import ci553.happyshop.utility.ProductListFormatter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Order.
 */
public class OrderTest {

    @Test
    void testCreateOrder(){
        ArrayList<Product> items = new ArrayList<>();
        items.add(new Product("0001", "Mouse", "0001.jpg", 19.99, 10));
        items.add(new Product("0002", "Keyboard", "0002.jpg", 49.99, 5));

        Order order = new Order(12, OrderState.Ordered, "2026-01-10 12:00:00", items);

        assertEquals(12, order.getOrderId());
        assertEquals(OrderState.Ordered, order.getState());
        assertNotNull(order.getProductList());
        assertEquals(2, order.getProductList().size());
    }


    @Test
    void testUpdateOrderState() {
        ArrayList<Product> items = new ArrayList<>();
        items.add(new Product("0001", "Mouse", "0001.jpg", 19.99, 10));

        Order order = new Order(14, OrderState.Ordered, "2026-01-10 12:00:00", items);

        order.setState(OrderState.Progressing);
        assertEquals(OrderState.Progressing, order.getState());

        order.setState(OrderState.Collected);
        assertEquals(OrderState.Collected, order.getState());
    }

    @Test
    void testGetOrderDetails() {
        ArrayList<Product> items = new ArrayList<>();
        Product p = new Product("0001", "Mouse", "0001.jpg", 19.99, 10);
        p.setOrderedQuantity(2);
        items.add(p);

        Order order = new Order(30, OrderState.Ordered, "2026-01-10 12:00:00", items);

        String details = order.orderDetails();



        assertTrue(details.contains("30"));
        assertTrue(details.contains("Ordered"));
        assertTrue(details.contains("2026-01-10 12:00:00"));
       assertTrue(details.contains("0001"));
       assertTrue(details.contains("Mouse"));
       assertTrue(details.contains("2"));

    }
}
