package com.pluralsight;

import java.util.*;

// Imports UtilizedMethods class, which contains bulk of code
import static com.pluralsight.UtilizedMethods.*;

/**
 * Main class for the Ledger Application.
 * Allows the user to add transactions, view the ledger, run predefined or custom reports, and create ledger files.
 */
public class LedgerApp {

    /**
     * Shared Scanner object for reading user input.
     */
    public static Scanner input = new Scanner(System.in);
    /**
     * The name of the currently open ledger file.
     */
    public static String fileName;
    /**
     * The main ledger ArrayList storing all transactions loaded from or written to the file.
     */
    public static ArrayList<Transaction> openLedger = new ArrayList<>();

    /**
     * Entry point of the application. Loads the ledger file,
     * and displays the main menu in a loop until the user exits.
     */
    public static void main(String[] args) {
        // Prompts user to select a ledger file
        fileName = pickLedger();
        // Loads transactions from the selected file
        loadLedger(fileName);
        // Main menu loop
        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    ================== MAIN MENU ==================
                    Please select an option from the following menu:
                    (D) Add deposit
                    (P) Add payment
                    (L) View ledger
                    (I) More info
                    (X) Exit program""");
            // Reads user input and converts to uppercase
            char mainMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
            switch (mainMenuOption) {
                case 'D': // Adds a deposit transaction
                    addTransaction("deposit");
                    break;
                case 'P': // Adds a payment transaction
                    addTransaction("payment");
                    break;
                case 'L': // Navigates to the ledger submenu
                    viewLedgerMenu();
                    break;
                case 'I': // Shows additional program information
                    moreInfo();
                    break;
                case 'X': // Exits the application
                    System.out.println("EXITING PROGRAM...");
                    isRunning = false;
                    break;
                default: // Handles invalid input
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

    /**
     * Displays the Ledger View Menu, allowing the user to filter and view entries
     * based on type or run reports.
     */
    public static void viewLedgerMenu() {
        // Ledger submenu loop
        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    ================== LEDGER MENU ==================
                    Which entries would you like to see?
                    (A) Display all entries
                    (D) Display only deposits
                    (P) Display only payments
                    (R) Run reports
                    (H) Back to main menu""");

            char ledgerMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));

            switch (ledgerMenuOption) {
                case 'A': // Displays all transactions
                    displayEntries(openLedger);
                    break;
                case 'D': // Displays only deposit transactions
                    displayEntriesByType("deposit");
                    break;
                case 'P': // Displays only payment transactions
                    displayEntriesByType("payment");
                    break;
                case 'R': // Navigates to report generation submenu
                    viewReportsMenu();
                    break;
                case 'H': // Returns to main menu
                    System.out.println("Returning to main menu....");
                    isRunning = false;
                    break;
                default: // Handles invalid input
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

    /**
     * Displays the Reports Menu, allowing the user to run default reports,
     * search by vendor, or run a custom report.
     */
    public static void viewReportsMenu() {

        // Reports submenu loop
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
                    ================== REPORTS MENU ==================
                    What report would you like to run?
                    (1) Month to date
                    (2) Previous month
                    (3) Year to date
                    (4) Previous year
                    (5) Search by vendor
                    (6) Custom search
                    (7) Back to ledger menu""");

            char reportsMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));

            switch (reportsMenuOption) {
                case '1': // Runs report for current month up to today
                    runReportDefault("monthToDate");
                    break;
                case '2': // Runs report for previous calendar month
                    runReportDefault("previousMonth");
                    break;
                case '3': // Runs report for current year up to today
                    runReportDefault("yearToDate");
                    break;
                case '4': // Runs report for previous calendar year
                    runReportDefault("previousYear");
                    break;
                case '5': // Runs report that filters by vendor name
                    runReportByVendor();
                    break;
                case '6': // Allows user to define custom filters
                    gatherInfoCustomSearch();
                    break;
                case '7': // Returns to ledger submenu
                    System.out.println("Returning to ledger menu....");
                    isRunning = false;
                    break;
                default: // Handles invalid input
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

}