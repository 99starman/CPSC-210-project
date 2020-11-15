package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Test the ItemWithDiscount class */

public class ItemWithDiscountTest {

    private ItemWithDiscount item1;
    private ItemWithDiscount item2;

    @BeforeEach
    public void SetUp() {
        item1 = new ItemWithDiscount("cashew", 6.55, "Snacks", 1.00, 50);
        item2 = new ItemWithDiscount("apple", 1.25, "Fruit", 0.15, 30);

    }

    @Test
    public void testGetDiscount() {
        assertEquals(0.15, item2.getDiscount());
    }

    @Test
    public void testGetRedeemPoint() {
        assertEquals(50, item1.getRedeemPoint());
    }

}
