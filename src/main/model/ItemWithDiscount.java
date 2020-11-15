package model;

/* This class extends Item, and represents specifically an item eligible for discount
     (that specifies the amount of the discount and number of points needed for that discount)
 */

public class ItemWithDiscount extends Item {

    private double discount;
    private int redeemPoint;

    // EFFECTS: constructs an item with discount
    public ItemWithDiscount(String name, double price, String category, double discount, int redeemPoint) {
        super(name, price, category);
        this.discount = discount;
        this.redeemPoint = redeemPoint;
    }

    // getters
    public double getDiscount() {
        return discount;
    }

    public int getRedeemPoint() {
        return redeemPoint;
    }

    // EFFECTS: returns whether this is eligible for discount
    @Override
    public boolean eligible() {
        return true;
    }


}
