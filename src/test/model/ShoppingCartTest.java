package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Test the ShoppingCart class */

public class ShoppingCartTest {

    private ShoppingCart cart;

    private Item item1 = new Item("chips", 2.45, "Snacks");
    private Item item2 = new Item("avocado", 1.49, "Fruit");
    private Item item3 = new Item("milk", 4.55, "Dairy");
    private ItemWithDiscount item4 = new ItemWithDiscount("apple", 1.25, "Fruit", 0.15, 30);
    private ItemWithDiscount item5 = new ItemWithDiscount("cashew", 6.55, "Snacks", 1.00, 50);

    @BeforeEach
    public void SetUp() {
        cart = new ShoppingCart();
    }

    @Test
    public void testAddItem() {
        assertEquals(0, cart.size());

        cart.addItem(item1);
        assertEquals(1, cart.size());
        assertEquals(item1, cart.getLast());

        cart.addItem(item2);
        assertEquals(2, cart.size());
        assertEquals(item2, cart.getLast());

        cart.addItem(item1);
        assertEquals(3, cart.size());
        assertEquals(item1, cart.getLast());

        cart.addItem(item5);
        assertEquals(4, cart.size());
        assertEquals(item5, cart.getLast());
    }

    @Test
    public void testDeleteItem() {
        assertEquals(0, cart.size());
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item4);

        cart.deleteItem(item3);
        assertEquals(2, cart.size());
        cart.deleteItem(item4);
        assertEquals(1, cart.size());
        cart.deleteItem(item4);
        assertEquals(0, cart.size());
    }

    @Test
    public void testReset() {
        assertEquals(0, cart.size());
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item4);

        cart.reset();
        assertEquals(0, cart.size());
    }

    @Test
    public void testTotalPrice() {
        assertEquals(0, cart.size());
        assertEquals(0, cart.totalPrice());
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);
        assertEquals(4.55+1.25+6.55, cart.totalPrice());

    }

    @Test
    public void testFinalPrice() {
        assertEquals(0, cart.size());
        assertEquals(0, cart.totalPrice());
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);
        assertEquals(4.55+1.25+6.55-0.15-1.00, cart.finalPrice());

    }

    @Test
    public void testGetLast() {
        cart.addItem(item3);
        assertEquals(item3, cart.getLast());
        cart.addItem(item4);
        assertEquals(item4, cart.getLast());
    }

    @Test
    public void testGetI() {
        cart.addItem(item3);
        cart.addItem(item4);
        assertEquals(item3, cart.getI(0));
        assertEquals(item4, cart.getI(1));
    }


}
