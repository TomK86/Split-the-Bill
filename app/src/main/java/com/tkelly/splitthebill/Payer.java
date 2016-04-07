package com.tkelly.splitthebill;

import java.text.NumberFormat;
import java.text.ParseException;

/*
 * A class which stores information about a single party member
 *
 * Members:
 *  name - a String containing the name of this payer
 *  amt_owed - a double containing the amount this payer owes
 */
public class Payer {

    // Payer members
    private String name;
    private double amt_owed;

    /**
     * Payer constructor
     *
     * @param payer_name The name of this payer
     */
    public Payer(String payer_name) {
        name = payer_name;
        amt_owed = 0d;
    }

    // Get methods
    public String getName() { return name; }
    public double getAmtOwed() { return amt_owed; }

    // Set method
    public void setName(String new_name) { name = new_name; }

    /**
     * Method to reset the amount this payer owes to zero
     */
    public void clearAmtOwed() { amt_owed = 0d; }

    /**
     * Method to add the given amount to the amount this payer owes
     *
     * @param amount The amount to be added
     */
    public void updateAmtOwed(double amount) { amt_owed += amount; }

    /**
     * Method to get an HTML-formatted string containing this payer's name and the amount they
     * owe, if any
     *
     * @param tax The local sales tax (a multiplier between 1 and 2)
     * @return The HTML-formatted string describing this payer and the amount they owe, or an
     *         empty string if this payer doesn't owe anything
     */
    public String getResult(double tax) {
        if (amt_owed > 0d) {
            return "<b>" + name + " owes " +
                    NumberFormat.getCurrencyInstance().format(amt_owed * tax) +
                    "</b><br/>(" +
                    NumberFormat.getCurrencyInstance().format(amt_owed) +
                    " before tax)<br/>";
        } else { return ""; }
    }

    /**
     * Method to get an HTML-formatted string containing a tip guide based on the amount this
     * payer owes, if any
     *
     * @return The HTML-formatted tip guide string, or an empty string if this payer doesn't
     *         owe anything
     */
    public String getTipGuide() {
        if (amt_owed > 0d) {
            return "10% tip ... " +
                    NumberFormat.getCurrencyInstance().format(amt_owed * 0.1d) +
                    "<br/>15% tip ... " +
                    NumberFormat.getCurrencyInstance().format(amt_owed * 0.15d) +
                    "<br/>20% tip ... " +
                    NumberFormat.getCurrencyInstance().format(amt_owed * 0.2d) +
                    "<br/><br/>";
        } else { return ""; }
    }

}
