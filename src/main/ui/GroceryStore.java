package ui;

import exception.NotADoubleException;
import exception.NotPositiveException;
import model.Account;
import model.Item;
import model.ItemWithDiscount;
import model.ShoppingCart;
import persistence.Reader;
import persistence.Writer;
import ui.CartGUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/* This class simulates a ui for an online grocery store
   Attribution:
   The structure of this class is based on the TellerApp url: https://github.students.cs.ubc.ca/CPSC210/TellerApp
   In particular, the methods runApp(), processCommand(), displayMainMenu() are based on similar methods in TellerApp.
 */
public class GroceryStore {
    private Account account;
    public ArrayList<Item> items;
    private ShoppingCart cart = new ShoppingCart();
    private Scanner input;

    //getters
    public ArrayList<Item> getItems() {
        return items;
    }

    public ShoppingCart getCart() {
        return cart;
    }


    // EFFECTS: runs the application
    public GroceryStore() {
        runApp();
    }



    // EFFECTS: reloads data or initialize, and displays the main menu if not quit
    public void runApp() {
        boolean exiting = false;
        String command;
        input = new Scanner(System.in);

        try {
            reloadSaved();
        } catch (IOException e) {
            System.out.println("No previous data found. Initialize new data.");
            init();              // initialize a list of goods if not previously saved
        }

        while (!exiting) {
            displayMainMenu();
            command = (input.next()).toLowerCase();

            if (command.equals("q")) {
                promptToSave();
                System.out.println("\nExit?\t y -> yes\t others -> no");

                if (input.next().equals("y")) {
                    exiting = true;
                }
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nExiting... \nGoodbye!");
    }


    // MODIFIES: this
    // EFFECTS: reloads item list and account from file
    private void reloadSaved() throws IOException {
        account = new Account();
        Reader reader = new Reader();
        items = reader.readItems("./data/Items.json");
        account = reader.readAccount("./data/Account.json");
        System.out.println("Items and account loaded!");
    }

    // MODIFIES: this
    // EFFECTS: reloads Shopping cart from file
    private void resumeCart() throws IOException {
        cart = new ShoppingCart();
        Reader reader = new Reader();
        List<String> names;
        names = reader.readNames("./data/ShoppingCart.json");
        // read in the names of items in previous shopping cart
        for (String name : names) {
            for (Item it : items) {
                if (it.getName().equals(name)) {
                    cart.addItem(it);
                }
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes item list and account, if not saved previously
    public void init() {
        account = new Account();
        cart = new ShoppingCart();

        items = new ArrayList<>();

        Item item1 = new Item("chips", 2.45, "Snacks");
        Item item2 = new Item("avocado", 1.49, "Fruit");
        Item item3 = new Item("milk", 4.55, "Dairy");
        ItemWithDiscount item4 = new ItemWithDiscount("apple", 1.25, "Fruit", 0.15, 30);
        ItemWithDiscount item5 = new ItemWithDiscount("cashew", 6.55, "Snacks", 1.00, 50);

        Item[] all = {item1, item2, item3, item4, item5};

        items.addAll(Arrays.asList(all));


    }


    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "a":
                try {
                    addItem();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "l":
                showItems();
                break;
            case "c":
                resumeAndDisplayCart();
                break;
            case "p":
                showAccount();
                break;
            case "m":
                purchaseOrAdjust();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    private void resumeAndDisplayCart() {
        try {
            resumeCart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        CartGUI.createAndShowGUI(cart, items);
    }


    // EFFECTS: gives prompts to save account, item list and shopping cart
    private void promptToSave() {
        System.out.println("Save account?\n \t y -> yes,  others -> no");
        String bool = input.next();
        if (bool.equals("y")) {
            saveAccount();
            System.out.println("Account saved!");
        }
        System.out.println("\n Save all items and your shopping cart?\n \t y -> yes,  others -> no");
        bool = input.next();
        if (bool.equals("y")) {
            saveStore();
            System.out.println("All saved!");
        }
    }

    // EFFECTS: saves item list and shopping cart to file or prints stack trace if catches an io exception
    private void saveStore() {
        try {
            Writer writer = new Writer();
            writer.writeData(items);
            writer.writeData(cart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: saves account to file or prints stack trace if catches an io exception
    private void saveAccount() {
        try {
            Writer writer = new Writer();
            writer.writeData(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: displays main menu
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add items");
        System.out.println("\tl -> show list of goods");
        System.out.println("\tc -> resume and display shopping cart");
        System.out.println("\tm -> make a purchase");
        System.out.println("\tp -> personal account");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: adds a new item to the grocery store
    public void addItem() {
        System.out.println("\nPlease add the following attributes: name, price and category, separated by whitespace");
        String name = input.next();
        double price = addPrice(input.hasNextDouble());
        String category = input.next();

        if (price > 0) {
            System.out.println("\nEligible for discount?\n \t y -> yes,  others -> no");
            if (input.next().equals("y")) {
                System.out.println("input discount amount and redeem points, separated by a whitespace");
                double da = addDiscount(input.hasNextDouble(), price);
                int rp = addPoint(input.hasNextInt());
                if ((da <= 0) || (rp <= 0)) {
                    System.err.println("Invalid input!");
                } else {
                    ItemWithDiscount item = new ItemWithDiscount(name, price, category, da, rp);
                    addSingleItem(item);
                }
            } else {
                Item item = new Item(name, price, category);
                addSingleItem(item);
            }

        } else {
            System.err.println("Please input a positive number for price!");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new item to the grocery store
    private void addSingleItem(Item item) {
        items.add(item);
        System.out.println("Item added!");
    }

    // MODIFIES: this
    // EFFECTS: adds a discount to the item,
    //          or returns 0 or -1 if not a double or not smaller than original price
    private double addDiscount(boolean isDouble, double price) {
        if (isDouble) {
            double da = input.nextDouble();
            if (da >= price) {
                return 0;
            } else {
                return da;
            }
        } else {
            return -1;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a price to the item, or returns 0 if the input is not a double
    public double addPrice(Boolean isDouble) {
        if (isDouble) {
            return input.nextDouble();
        } else {
            return 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds redeem points required of the discounted item, or returns 0 if not an integer
    public int addPoint(Boolean isInt) {
        if (isInt) {
            return input.nextInt();
        } else {
            return 0;
        }
    }

    // EFFECTS: displays the list of items
    public void showItems() {
        int j = 1;
        for (Item it : items) {
            System.out.print(j + ".  ");
            showItem(it);
            if (!(it.getDiscount() == 0)) {
                showDiscount((ItemWithDiscount) it);
            }
            j++;
        }

    }

    // EFFECTS: displays a single item
    public void showItem(Item i) {
        String s = "name: " + i.getName() + "    price: " + i.getPrice() + "    category: " + i.getCategory();
        System.out.println(s);
    }

    // EFFECTS: displays the discount amount and points needed for an item with discount
    public void showDiscount(ItemWithDiscount i) {
        String s1 = "**Eligible for discount.    Discount amount:" + i.getDiscount();
        String s2 = "    points needed:" + i.getRedeemPoint();                     // couldn't get in one line
        System.out.println(s1 + s2);
    }


    // MODIFIES: this
    // EFFECTS: gives a prompt for next options (make a purchase / trigger cartGUI)
    public void purchaseOrAdjust() {
        try {
            resumeCart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("\nTotal price before discount: " + cart.totalPrice());
        System.out.println("Total price after discount: " + cart.finalPrice());
        Account.printAccount(account);
        System.out.println("Purchase or adjust shopping cart?");
        System.out.println("\tp -> purchase, a -> adjust cart, others -> menu");
        String bool = input.next();
        if (bool.equals("p")) {
            doPurchase();
        }
        if (bool.equals("a")) {
            try {
                CartGUI.createAndShowGUI(cart, items);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }


    // MODIFIES: this
    // EFFECTS: makes a purchase if possible, and prompts whether it's successful
    public void doPurchase() {

        boolean b = account.makePurchase(cart);
        if (b) {
            System.out.println("Purchase successful!");
        } else {
            System.out.println("Insufficient balance, reload account or adjust shopping cart!");
        }
    }


    // MODIFIES: this
    // EFFECTS: displays account, and provides the option to load money
    public void showAccount() {
        Account.printAccount(account);
        System.out.println("Load money?");
        System.out.println("\ty -> yes, others -> no");
        if (input.next().equals("y")) {
            System.out.println("Enter amount");
            try {
                loadAccount();
            } catch (NotADoubleException e) {
                System.err.println(e.getMessage());
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: loads money and displays the account
    // or returns error message if the input cannot be interpreted as a double
    public void loadAccount() throws NotADoubleException {
        if (input.hasNextDouble()) {
            try {
                account.load(input.nextDouble());
                model.Account.printAccount(account);
            } catch (NotPositiveException e) {
                System.err.println(e.getMessage());
            }
        } else {
            throw new NotADoubleException("Not a number. Please input a positive number");
        }
    }





}
