package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a financial transaction with date, time, description, vendor, and amount details.
 */
public class Transaction {

    // Fields representing the properties of a transaction.
    private LocalDate transactionDate;     // Date when the transaction occurred
    private LocalTime transactionTime;     // Time when the transaction occurred
    private String description;            // Description of the transaction
    private String vendor;                 // Vendor involved in the transaction
    private double amount;                 // Amount of money involved (positive for deposit, negative for payment)

    /**
     * Constructor to initialize all fields of the transaction.
     *
     * @param transactionDate The date of the transaction.
     * @param transactionTime The time of the transaction.
     * @param description     A brief description of the transaction.
     * @param vendor          The vendor involved in the transaction.
     * @param amount          The monetary amount (positive or negative).
     */
    public Transaction(LocalDate transactionDate, LocalTime transactionTime, String description, String vendor, double amount) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    /**
     * Returns the date of the transaction.
     *
     * @return LocalDate representing the transaction date.
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the transaction date.
     *
     * @param transactionDate A LocalDate object representing the new transaction date.
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Returns the time of the transaction.
     *
     * @return LocalTime representing the transaction time.
     */
    public LocalTime getTransactionTime() {
        return transactionTime;
    }

    /**
     * Sets the transaction time.
     *
     * @param transactionTime A LocalTime object representing the new transaction time.
     */
    public void setTransactionTime(LocalTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * Returns the transaction's description.
     *
     * @return A String describing the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the transaction description.
     *
     * @param description A String describing the transaction.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the vendor associated with the transaction.
     *
     * @return A String with the vendor name.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the vendor name for the transaction.
     *
     * @param vendor A String with the vendor name.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * Returns the monetary amount of the transaction.
     *
     * @return A double representing the transaction amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount.
     *
     * @param amount A double value representing the transaction amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Converts the transaction data to a CSV-formatted string using pipe as a delimiter for documentary purposes.
     *
     * @return A single-line String in CSV format representing the transaction.
     */
    public String convertToCsvFormat() {
        return String.join("|", this.transactionDate.toString(), this.transactionTime.toString(),
                this.description, this.vendor, String.valueOf(String.format("%.2f", this.amount)));
    }
}
