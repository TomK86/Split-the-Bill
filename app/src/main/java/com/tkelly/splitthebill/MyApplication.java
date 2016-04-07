package com.tkelly.splitthebill;

import android.app.Application;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds all of the global data that can be accessed anywhere in the application.
 * <p>
 * Members:
 *   items - an ArrayList of stored Item objects, representing the user's bill
 *   payers - an ArrayList of stored Payer objects, representing the user's party
 *   tax - the user-specified local sales tax percentage, converted to a multiplier
 */
public class MyApplication extends Application {
    
    private ArrayList<Item> items;
    private ArrayList<Payer> payers;
    private double tax;

    public MyApplication() {
        super();
        items = new ArrayList<>();
        payers = new ArrayList<>();
        tax = 0d;
    }

    /**
     * Get the stored Item object at the given index
     *
     * @param idx The index of the stored Item object
     * @return The stored Item object at the given index, or null if not found
     */
    public Item getItem(int idx) {
        try {
            return items.get(idx);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the stored Payer object at the given index
     *
     * @param idx The index of the stored Payer object
     * @return The stored Payer object at the given index, or null if not found
     */
    public Payer getPayer(int idx) {
        try {
            return payers.get(idx);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    // Get methods
    public ArrayList<Item> getItems() { return items; }
    public ArrayList<Payer> getPayers() { return payers; }
    public double getTax() { return tax; }
    public int getNumItems() { return items.size(); }
    public int getNumPayers() { return payers.size(); }

    // Set method
    public void setTax(double new_tax) { tax = new_tax; }

    // Clear methods
    public void clearAmtsOwed() { for (Payer payer : payers) payer.clearAmtOwed(); }
    public void clearAllPayments() { for (Item item : items) item.clearPayments(); }
    public void clearCompleted() { for (Item item : items) item.setCompleted(false); }

    /**
     * Check if the ArrayList of stored Payer objects is empty
     *
     * @return `true` if the ArrayList is empty, else `false`
     */
    public boolean payersIsEmpty() { return payers.isEmpty(); }

    /**
     * Check if the ArrayList of stored Item objects is empty
     *
     * @return `true` if the ArrayList is empty, else `false`
     */
    public boolean itemsIsEmpty() { return items.isEmpty(); }

    /**
     * Check if all stored Item objects are marked completed
     *
     * @return `true` if all stored Item objects are marked completed, else `false`
     */
    public boolean allItemsAreCompleted() {
        boolean all_items_completed = true;
        for (Item item : items) {
            all_items_completed &= item.isCompleted();
        }
        return all_items_completed;
    }

    /**
     * Update each Payer object's amount owed according to the payments stored in each Item object
     */
    public void updateAmtsOwed() {
        clearAmtsOwed();
        for (Item item : items) {
            HashMap<Integer, Double> payments = item.getPayments();
            for (int i : payments.keySet()) {
                payers.get(i).updateAmtOwed(payments.get(i));
            }
        }
    }

    /**
     * Get the total cost before tax of all stored Item objects
     *
     * @return The total cost before tax
     */
    public double subTotal() {
        double total = 0d;
        for (Item item : items) {
            total += item.getCost() * item.getQty();
        }
        return total;
    }
}
