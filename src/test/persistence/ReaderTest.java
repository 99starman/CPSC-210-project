package persistence;

import model.Account;
import model.Item;
import model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {
    private Reader testReader = new Reader();

    private Account testAccount;
    private ArrayList<Item> testItems = new ArrayList<>();
    private ShoppingCart testCart = new ShoppingCart();

    @Test
    void testReadAccount() {
        try {
            Account read = testReader.readAccount("./data/AccountTestFile.json");
            assertEquals(22.0, read.getBalance());
            assertEquals(10, read.getRewardPoints());
        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

    @Test
    void testReadItems() {
        try {
            ArrayList<Item> read = testReader.readItems("./data/ItemsTestFile.json");
            assertEquals(3,read.size());
            assertEquals("candy", read.get(0).getName());
            assertEquals("avocado", read.get(1).getName());
            assertEquals("cashew", read.get(2).getName());
            assertEquals(1.0, read.get(2).getDiscount());
        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

    @Test
    void testReadNames() {
        try {
            List<String> read = testReader.readNames("./data/ShoppingCartTestFile.json");
            assertEquals(2,read.size());
            assertEquals("chip", read.get(0));
            assertEquals("blueberry", read.get(1));

        } catch (IOException e) {
            fail("Should not have thrown this");
        }
    }

}
