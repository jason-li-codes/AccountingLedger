package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import static com.pluralsight.InputValidation.*;


public class LedgerApp {

    public static Scanner input = new Scanner(System.in);

    public static ArrayList<Transaction> openLedger = new ArrayList<>();

    public static void main(String[] args) {

        String fileName = pickLedger();
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

            char mainMenuOption = getValidMenuChar(Set.of('D', 'P', 'L', 'I', 'X'));

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
                    (H) Back to home page""");

            char mainLedgerOption = getValidMenuChar(Set.of('A', 'D', 'P', 'R', 'H'));

            switch (mainLedgerOption) {
                case 'A':
                    displayAllEntries();
                    break;
                case 'D':
                    displayTransactionsByType("deposit");
                    break;
                case 'P':
                    displayTransactionsByType("payment");
                    break;
                case 'R':
                    runReports();
                    break;
                case 'H':
                    System.out.println("Returning to main menu....");
                    isRunning = false;
                    break;
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
                    (7) Back to home page""");

            char reportMenuOption = getValidMenuChar(Set.of('1', '2', '3', '4', '5', '6', '7'));

            switch (reportMenuOption) {
                case '1':
                    runReport("toMonth");
                    break;
                case '2':
                    runReport("prevMonth");
                    break;
                case '3':
                    runReport("toYear");
                    break;
                case '4':
                    runReport("prevYear");
                    break;
                case '5':
                    searchByVendor();
                    break;
                case '6':
                    runCustomSearch();
                    break;
                case '7':
                    System.out.println("Returning to main menu....");
                    isRunning = false;
                    break;
            }
        }

    }

    public static void runReport(String type) {

        LocalDate compareDate = LocalDate.now();
        printHeader();
        switch (type) {
            case "toMonth":
                compareDate = compareDate.withDayOfMonth(1);
                break;
            case "toYear":
                compareDate = compareDate.withDayOfYear(1);
                break;
            case "prevMonth":
                if (compareDate.getMonthValue() == 1) {
                    compareDate = compareDate.withMonth(12).withYear(compareDate.getYear() - 1);
                } else {
                    compareDate = compareDate.withMonth(compareDate.getMonthValue() - 1);
                }
                break;
            case "prevYear":
                compareDate = compareDate.withYear(compareDate.getYear() - 1);
                break;
        }

        for (Transaction t : openLedger) {
            if (!compareDate.isAfter(t.getTransactionDate())) {
                displayEntry(t);
            }
        }
        System.out.println("Searching complete.");
    }

    public static void displayTransactionsByType(String type) {

        System.out.printf("Searching for all %ss on file....\n", type);
        printHeader();
        if (Objects.equals(type, "deposit")) {
            for (Transaction t : openLedger) {
                if (t.getAmount() > 0) {
                    displayEntry(t);
                }
            }
        } else {
            for (Transaction t : openLedger) {
                if (t.getAmount() < 0) {
                    displayEntry(t);
                }
            }
        }
        System.out.println("Searching complete.");
    }

    public static void displayAllEntries() {

        printHeader();
        for (Transaction t : openLedger) {
            displayEntry(t);
        }
    }

    public static void printHeader() {

        System.out.printf("%-12s %-10s %-20s %-15s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("+------------+----------+----------------------+-----------------+------------+");

    }

    public static void displayEntry(Transaction t) {

        System.out.printf("%-12s %-10s %-20s %-15s %10.2f%n",
                t.getTransactionDate(), t.getTransactionTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    public static void addTransaction(String type) {

        System.out.printf("Let's get information about this %s\n.", type);

        boolean isRunning = true;
        do {
            System.out.printf("Enter %s date (MM/dd/yyyy), or N for \"now\": \n", type);
            LocalDate inputDate = getValidDate();
            System.out.printf("Enter %s time: \n", type);
            LocalTime inputTime = getValidTime();
            System.out.printf("Enter %s description: \n", type);
            String inputDescription = getValidString();
            System.out.printf("Enter %s vendor: \n", type);
            String inputVendor = getValidString();
            System.out.printf("Enter %s amount: \n", type);
            double inputAmount = getValidDouble();
            if (Objects.equals(type, "deposit") && inputAmount != Math.abs(inputAmount)) {
                System.out.printf("Amount set to positive, $%f.\n", inputAmount);
                inputAmount = Math.abs(getValidDouble());
            } else if (Objects.equals(type, "payment") && inputAmount != -1 * Math.abs(inputAmount)) {
                System.out.printf("Amount set to negative, -$%f.\n", inputAmount);
                inputAmount = -1 * Math.abs(inputAmount);
            }

            Transaction newTransaction = new Transaction(inputDate, inputTime, inputDescription, inputVendor, inputAmount);
            System.out.println("""
                    Does this transaction contain the correct information you'd like to add?
                    (Y) Yes
                    (N) No, I need to re-enter the information""");

            switch (getValidMenuChar(Set.of('Y', 'N'))) {
                case 'Y':
                    openLedger.add(newTransaction);
                    break;
                case 'N':
                    continue;
            }
            // TODO WRITE TO CSV FILE

            System.out.println("Transaction added successfully.");
            System.out.printf("""
                    Would you like to add another %s?
                    (Y) Yes, add another %s
                    (N) No, return to main menu
                    """, type, type);

            char menuOption = getValidMenuChar(Set.of('Y', 'N'));
            if (menuOption == 'N') {
                System.out.println("Returning to main menu...");
                isRunning = false;
            }
        } while (isRunning);
    }

    public static void loadLedger(String fileName) {

        System.out.printf("Opening %s....", fileName);

        try (BufferedReader bufReader = new BufferedReader(new FileReader(fileName))) {
            // eats the first line because it is a label of file columns
            String input = bufReader.readLine();

            // while loop to read each Product object by initializing input in the conditional
            while ((input = bufReader.readLine()) != null) {

                // splits String along | to get info individually
                String[] info = input.split("\\|");

                LocalDate transDate = LocalDate.parse(info[0]);
                LocalTime transTime = LocalTime.parse(info[1]);
                double amount = Double.parseDouble(info[4]);

                openLedger.add(new Transaction(transDate, transTime, info[2], info[3], amount));
            }
            // if there is an exception, displays error message and sets badInput to false
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, there's a problem finding your ledger, please try again later.");
            System.exit(2);
        } catch (IOException e) {
            System.out.println("Sorry, there was a problem reading your ledger, please try again later.");
            System.exit(3);
        }
        Collections.reverse(openLedger);
    }

    public static String pickLedger() {

        System.out.println("Please enter your passcode: ");
        String userPasscode = getValidString();
        HashMap<String, String[]> passcodes = new HashMap<>();
        String name = "";
        String fileName = "";

        try (BufferedReader bufReader = new BufferedReader(new FileReader("passcodes.csv"))) {
            // eats the first line because it is a label of file columns
            String input = bufReader.readLine();

            // while loop to read each Product object by initializing input in the conditional
            while ((input = bufReader.readLine()) != null) {

                // splits String along | to get info individually
                String[] info = input.split("\\|");

                passcodes.put(info[0], new String[]{info[1], info[2]});
            }
            // if there is an exception, displays error message and sets badInput to false
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, there's a problem checking passcodes, please try again later.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Sorry, there was a problem accessing accounts, please try again later.");
            System.exit(1);
        }

        boolean badInput = false;
        do {
            String[] userInfo = passcodes.get(userPasscode);

            if (userInfo != null) {
                name = userInfo[0];
                fileName = userInfo[1];
            } else {
                System.out.println("Your passcode does not match any account we have, please try again.");
                badInput = true;
            }

        } while (badInput);

        System.out.printf("Welcome to your Accounting Ledger, %s! ", name);
        return fileName;
    }

    public static void moreInfo() {

        System.out.println("""
                Here's more info about what each of these menu options do:
                (D) Add deposit
                This will prompt you to add information about a deposit you received. You must provide the date,
                time, description, vendor and amount of the transaction, which should be positive.
                (P) Add payment
                This will prompt you to add information about a payment you paid. You must provide the date, time,
                description, vendor and amount of the transaction, which should be negative.
                (L) View ledger
                This will allow you to view your ledger in its entirety, or run a search based on parameters in order
                to provide a more specific dataset.
                (I) More info
                This is the menu you are currently in, and details basic information about the main menu options.
                (X) Exit program
                This will close the program, saving any changes you've made and allowing another user to login.
                Returning to main menu....""");

    }

}
