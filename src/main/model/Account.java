package model;

import exception.NotPositiveException;

/* This class represents the User's account, with two fields: account balance and current reward points
   Attribution:
   Part of this code are based on both the original file and my own implementation of Lab4 <FoodServiceCard>,
   with similar structure but major changes to makePurchase method as I re-designed the reward mechanism
*/
public class Account {

    public static final int REWARD_POINTS_PER_DOLLAR_CHARGED = 1;

    private double balance;
    private int rewardPoint;


    // getters
    public double getBalance() {
        return balance;
    }

    public int getRewardPoints() {
        return rewardPoint;
    }

    // EFFECTS: constructs account with 10 dollar balance and 0 reward point
    public Account() {
        this.balance = 10;
        this.rewardPoint = 0;
    }


    // MODIFIES: this
    // EFFECTS: adds amount in dollar to the account's balance if amount is positive, otherwise throws an exception
    public void load(double amount) throws NotPositiveException {
        if (amount <= 0) {
            throw new NotPositiveException("Not positive. Please input a positive number.");
        } else {
            balance += amount;
        }
    }

    // MODIFIES: this
    // EFFECTS: if the items to purchase are eligible for discount and the account has sufficient points to redeem,
    //            - enjoy the discount and subtract required points.
    //            - if there is sufficient balance afterwards,
    //                - subtract final price from balance and add reward points and return true
    //            - else revert the discount and returns false
    //          else
    //            - if there is sufficient balance afterwards,
    //                - subtract total price (not discounted) from balance and add reward points and return true
    //            - else return false
    public boolean makePurchase(ShoppingCart cart) {

        if (rewardPoint >= cart.pointRedeemed()) {
            rewardPoint -= cart.pointRedeemed();

            if (balance >= cart.finalPrice()) {
                balance -= cart.finalPrice();
                rewardPoint += REWARD_POINTS_PER_DOLLAR_CHARGED * (int) cart.totalPrice();
                cart.reset();
                return true;
            } else {
                rewardPoint += cart.pointRedeemed();
                return false;
            }
        } else {
            if (balance >= cart.totalPrice()) {
                balance -= cart.totalPrice();
                rewardPoint += REWARD_POINTS_PER_DOLLAR_CHARGED * (int) cart.totalPrice();
                cart.reset();
                return true;
            } else {
                return false;
            }

        }
    }

    // EFFECTS: prints the account balance and reward points
    public static void printAccount(Account account) {
        System.out.println("Current balance: " + account.getBalance() + " CAD");
        System.out.println("Current reward points: " + account.getRewardPoints());
    }


}
