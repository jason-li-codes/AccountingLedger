package com.pluralsight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LedgerApp {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to your Accounting Ledger App!");
        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    Please select an option from the following menu:
                    (D) Add deposit
                    (P) Add payment
                    (L) View ledger
                    (I) More info
                    (X) Exit program""");

            String menuOption = getValidChar(new ArrayList<String>(Arrays.asList("D", "P", "L", "I", "X")));

            switch (menuOption) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    addPayment();
                    break;
                case "L":
                    viewLedger();
                    break;
                case "I":
                    moreInfo();
                    break;
                case "X":
                    System.out.println("EXITING PROGRAM...");
                    isRunning = false;
            }
        }
    }

    public static String getValidChar(ArrayList<String> validMenuOptions) {

        // initializes inputChar and boolean badInput
        String inputChar = "";
        boolean badInput = false;

        // uses do/while loop
        do {
            // sets badInput to false first
            badInput = false;
            //tries to get an input
            try {
                inputChar = String.valueOf(input.nextLine().trim().charAt(0));
                if (!validMenuOptions.contains(inputChar)) {
                    System.out.println("Sorry, I don't recognize that option, let's try again.");
                    badInput = true;
                }
                // if it can't read as a valid menu option, catches exception and sets badInput to true to try again
            } catch (Exception e) {
                System.out.println("Sorry I don't know what you mean, let's try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        // returns the correct inputChar as a String of one character
        return inputChar;
    }

}
