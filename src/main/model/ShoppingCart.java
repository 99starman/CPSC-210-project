package model;

import java.util.ArrayList;

/* This class represents a shopping cart that is a list of items to purchase

 */
public class ShoppingCart {
    private ArrayList<Item> cart;

    public ShoppingCart() {
        cart = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS: adds an item to the list of goods in the shopping cart
    public void addItem(Item item) {
        cart.add(item);
    }

    // MODIFIES: this
    // EFFECTS: deletes an item from the list of goods in the shopping cart
    public void deleteItem(Item item) {
        cart.remove(item);
    }

    // MODIFIES: this
    // EFFECTS: clear and reset the shopping cart
    public void reset() {
        cart.clear();
    }

    // EFFECTS: returns the number of items in the shopping cart
    public int size() {
        return cart.size();
    }

    // EFFECTS: returns the total price of items in the shopping cart
    public double totalPrice() {
        double sum = 0;
        for (Item i: cart) {
            sum += i.getPrice();
        }
        return sum;
    }


    // EFFECTS: returns the total price of items after subtracting possible discount
    public double finalPrice() {
        double total = totalPrice();
        for (Item i: cart) {
            if (i.eligible()) {
                total -= i.getDiscount();
            }
        }
        return total;
    }

    // EFFECTS: returns the total points that should be redeemed
    public double pointRedeemed() {
        int point = 0;
        for (Item i: cart) {
            if (i.eligible()) {
                point += ((ItemWithDiscount) i).getRedeemPoint();
            }
        }
        return point;
    }

    // REQUIRES: the cart is not empty
    // EFFECTS: returns the last item added in the cart
    public Item getLast() {
        return cart.get(cart.size() - 1);
    }

    // REQUIRES: the cart is not empty, 0 <= i <= cart.size() - 1
    // EFFECTS: returns the ith items in the cart
    public Item getI(int i) {
        return cart.get(i);
    }
}
