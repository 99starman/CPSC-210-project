package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Test the Item class */

class ItemTest {

    private Item item1;
    private Item item2;
    private Item item3;


    @BeforeEach
    public void SetUp() {
        item1 = new Item("chips", 2.45, "Snacks");
        item2 = new Item("apple", 1.25, "Fruit");
        item3 = new Item("chocolate", 2.99, "Snacks");

    }

    @Test
    public void testGetName() {
        assertEquals("apple", item2.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(2.99, item3.getPrice());
    }

    @Test
    public void testGetCategory() {
        assertEquals("Snacks", item1.getCategory());
    }


}
