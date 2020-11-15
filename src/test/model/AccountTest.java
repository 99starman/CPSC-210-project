package model;

import exception.AccountBalanceException;
import exception.NotPositiveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Test the Account class */

public class AccountTest {
    private Account account;

    private Item item1 = new Item("chips", 2.45, "Snacks");
    private Item item2 = new Item("avocado", 1.49, "Fruit");
    private Item item3 = new Item("milk", 4.55, "Dairy");
    private ItemWithDiscount item4 = new ItemWithDiscount("apple", 1.25, "Fruit", 0.15, 20);
    private ItemWithDiscount item5 = new ItemWithDiscount("cashew", 6.55, "Snacks", 1.00, 50);

    private ShoppingCart cart;

    @BeforeEach
    public void SetUp() {
        account = new Account();
        cart = new ShoppingCart();
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);
        cart.addItem(item5);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, account.getBalance());
        assertEquals(0, account.getRewardPoints());
    }

    @Test
    public void testLoad() {
        try {
            account.load(15);
        } catch (NotPositiveException e) {
            fail("Should not have thrown this exception");
        }
        assertEquals(10+15, account.getBalance());
        assertEquals(0, account.getRewardPoints());
    }

    @Test
    public void testMakePurchase() {
        try {
            account.load(5);
        } catch (NotPositiveException e) {
            fail("Should not have thrown this exception");
        }
        assertFalse(account.makePurchase(cart));   // insufficient balance, insufficient points

        try {
            account.load(10);
        } catch (NotPositiveException e) {
            fail("Should not have thrown this exception");
        }
        assertEquals(0, account.getRewardPoints());
        assertTrue(account.makePurchase(cart));   // sufficient balance, insufficient points

        assertEquals(25-22.84, account.getBalance()); // purchase successful, no redeem
        assertEquals(22, account.getRewardPoints());

        assertEquals(0, cart.size());
        cart.addItem(item3);
        cart.addItem(item4);
        assertEquals(2, cart.size());

        assertFalse(account.makePurchase(cart)); // insufficient balance, sufficient points, no purchase
        assertEquals(25-22.84, account.getBalance());

        try {
            account.load(10);
        } catch (NotPositiveException e) {
            fail("Should not have thrown this exception");
        }
        assertTrue(account.makePurchase(cart));  // sufficient balance, sufficient points
        assertEquals(25-22.84+10-4.55-(1.25-0.15), account.getBalance(), 0.0000001);
        assertEquals(7, account.getRewardPoints());  // redeem successful

    }

    @Test
    public void testLoadNotPositive() {
        try {
            account.load(0);
            fail("Should have thrown an exception");
        } catch (NotPositiveException e) {
            assertEquals(10, account.getBalance());
            assertEquals(0, account.getRewardPoints());
        }
        try {
            account.load(-10);
            fail("Should have thrown an exception");
        } catch (NotPositiveException e) {
            assertEquals(10, account.getBalance());
            assertEquals(0, account.getRewardPoints());
        }
    }

}
