package com.pluralsight;

import java.io.*;
import java.util.*;

import static com.pluralsight.UtilizedMethods.*;

public class LedgerApp {

    public static Scanner input = new Scanner(System.in);

    public static String fileName = pickLedger();

    public static ArrayList<Transaction> openLedger = new ArrayList<>();

    public static void main(String[] args) {

        loadLedger(fileName);

        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    Please select an option from the following menu:
                    (D) Add deposit
                    (P) Add payment
                    (L) View ledger
                    (I) More info
                    (X) Exit program""");

            char mainMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
            switch (mainMenuOption) {
                case 'D':
                    addTransaction("deposit");
                    break;
                case 'P':
                    addTransaction("payment");
                    break;
                case 'L':
                    viewLedger();
                    break;
                case 'I':
                    moreInfo();
                    break;
                case 'X':
                    System.out.println("EXITING PROGRAM...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

    public static void viewLedger() {

        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    Which entries would you like to see?
                    (A) Display all entries
                    (D) Display only deposits
                    (P) Display only payments
                    (R) Run reports
                    (H) Back to main menu""");

            char mainLedgerOption = Character.toUpperCase(input.nextLine().trim().charAt(0));

            switch (mainLedgerOption) {
                case 'A':
                    displayEntries(openLedger);
                    break;
                case 'D':
                    displayEntriesByType("deposit");
                    break;
                case 'P':
                    displayEntriesByType("payment");
                    break;
                case 'R':
                    runReports();
                    break;
                case 'H':
                    System.out.println("Returning to main menu....");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

    public static void runReports() {

        boolean isRunning = true;
        while (isRunning) {

            System.out.println("""
                    What report would you like to run?
                    (1) Month to date
                    (2) Previous month
                    (3) Year to date
                    (4) Previous year
                    (5) Search by vendor
                    (6) Custom search
                    (7) Back to ledger menu""");

            char reportMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));

            switch (reportMenuOption) {
                case '1':
                    runReportDefault("monthToDate");
                    break;
                case '2':
                    runReportDefault("previousMonth");
                    break;
                case '3':
                    runReportDefault("yearToDate");
                    break;
                case '4':
                    runReportDefault("previousYear");
                    break;
                case '5':
                    runReportByVendor();
                    break;
                case '6':
                    gatherInfoCustomSearch();
                    break;
                case '7':
                    System.out.println("Returning to ledger menu....");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Sorry, I don't recognize that option, please try again.");
            }
        }
    }

}