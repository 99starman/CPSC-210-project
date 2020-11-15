package persistence;

import model.Account;
import model.Item;
import model.ItemWithDiscount;
import model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriterTest {

    private Writer testWriter;
    private Account testAccount;
    private ArrayList<Item> testItems = new ArrayList<>();
    private ShoppingCart testCart = new ShoppingCart();
    private Reader reader;

    private Item item1 = new Item("chocolate", 3.45, "Snacks");
    private Item item2 = new Item("avocado", 1.49, "Fruit");
    private Item item3 = new Item("milk", 4.55, "Dairy");
    private ItemWithDiscount item4 = new ItemWithDiscount("blueberry", 4.25, "Fruit", 0.55, 40);
    private ItemWithDiscount item5 = new ItemWithDiscount("cashew", 6.55, "Snacks", 1.00, 50);
    Item[] all = {item1, item2, item3, item4, item5};

    @BeforeEach
    void RunBefore() {
        testWriter = new Writer();
        reader = new Reader();
        testAccount = new Account();
        testItems.addAll(Arrays.asList(all));
        testCart.addItem(item1);
        testCart.addItem(item4);
    }

    @Test
    void testWriteAccount() {
        try {
            testWriter.writeData(testAccount);
            // read the account back in, as "read"
            Account read = reader.readAccount("./data/Account.json");
            assertEquals(testAccount.getBalance(), read.getBalance());
            assertEquals(testAccount.getRewardPoints(), read.getRewardPoints());
        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

    @Test
    void testWriteItems() {
        try {
            testWriter.writeData(testItems);
            // read the account back in, as "read"
            ArrayList<Item> read = reader.readItems("./data/Items.json");
            for (int i = 0; i < read.size(); i++) {
                assertEquals(testItems.get(i).getName(), read.get(i).getName());
                assertEquals(testItems.get(i).getPrice(), read.get(i).getPrice());
                assertEquals(testItems.get(i).getCategory(), read.get(i).getCategory());
            }
        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

    @Test
    void testWriteCart() {
        try {
            testWriter.writeData(testCart);
            // read the account back in, as "read"
            List<String> read = reader.readNames("./data/ShoppingCart.json");
            assertEquals(testCart.size(), read.size());
            for (int i = 0; i < read.size(); i++) {
                assertEquals(testCart.getI(i).getName(), read.get(i));
            }
        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

    @Test
    void testIOException() {
        try {
            testWriter.writeData("testCart");
            fail("Should have thrown an exception");
        } catch (IOException e) {
            // expected
        }
    }
}
