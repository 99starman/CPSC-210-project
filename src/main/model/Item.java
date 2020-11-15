package model;

/* This class represents an item for sale in the grocery store. The item has fields name, price and category,
    and can be either eligible or ineligible for discount.
 */

public class Item {

    private String name;
    private double price;
    private String category;


    // EFFECTS: constructs an item with field name and price (in Canadian dollar)
    public Item(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public double getDiscount() {
        return 0.0;
    }

    // EFFECTS: returns whether this is eligible for discount
    public boolean eligible() {
        return false;
    }


}
