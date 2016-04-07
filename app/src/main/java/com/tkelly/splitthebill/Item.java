package com.tkelly.splitthebill;

import java.util.HashMap;

/**
 * A class which stores information about a single line item on the bill
 * <p>
 * Members:
 *  qty - an integer (>0) containing the quantity of the item
 *  cost - a double containing the cost for each item
 *  name - a string containing the name of the item
 *  completed - a boolean indicating whether or not this item has been assigned payment already
 *  payments - a HashMap containing Integer keys and Double values (each key corresponds to
 *             the index of a Payer responsible for payment towards this item, and each value
 *             corresponds to the amount that Payer owes)
 */
public class Item {

    // Item members
    private int qty;
    private double cost;
    private String name;
    private boolean completed;
    private HashMap<Integer, Double> payments;

    /**
     * Item constructor
     *
     * @param cost_per_item The cost of each item
     * @param quantity The quantity of the item
     * @param item_name The name of the item
     */
    public Item(double cost_per_item, int quantity, String item_name) {
        qty = quantity;
        cost = cost_per_item;
        name = item_name;
        completed = false;
        payments = new HashMap<>();
    }

    // Get methods
    public int getQty() { return qty; }
    public double getCost() { return cost; }
    public String getName() { return name; }
    public boolean isCompleted() { return completed; }
    public HashMap<Integer, Double> getPayments() { return payments; }

    // Set methods
    public void setQty(int new_qty) { qty = new_qty; }
    public void setCost(double new_cost) { cost = new_cost; }
    public void setName(String new_name) { name = new_name; }
    public void setCompleted(boolean is_completed) { completed = is_completed; }

    /**
     * Method to add a payment for this item from the specified Payer
     *
     * @param payer_idx The index of the Payer responsible for this payment
     * @param amount The amount being paid
     */
    public void addPayment(int payer_idx, double amount) {
        if (payments.containsKey(payer_idx)) {
            payments.put(payer_idx, payments.get(payer_idx) + amount);
        } else {
            payments.put(payer_idx, amount);
        }
    }

    /**
     * Method to clear all payments for this item
     */
    public void clearPayments() { payments.clear(); }

}
