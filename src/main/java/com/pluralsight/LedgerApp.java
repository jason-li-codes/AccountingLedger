package com.pluralsight;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


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

            char menuOption = getValidMenuChar(Set.of('D', 'P', 'L', 'I', 'X'));

            switch (menuOption) {
                case 'D':
                    addDeposit();
                    break;
                case 'P':
                    addPayment();
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

            char menuOption = getValidMenuChar(Set.of('A', 'D', 'P', 'R', 'H'));

            switch (menuOption) {
                case 'A':
                    displayAllEntries();
                    break;
                case 'D':
                    displayTransactionsByType('+');
                    break;
                case 'P':
                    displayTransactionsByType('-');
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

            char menuOption = getValidMenuChar(Set.of('1', '2', '3', '4', '5', '6', '7'));

            switch (menuOption) {
                case '1':
                    runMonthToDate();
                    break;
                case '2':
                    runPrevMonth();
                    break;
                case '3':
                    runYearToDate();
                    break;
                case '4':
                    runPrevYear();
                    break;
                case '5':
                    runSearchByVendor();
                    break;
                case 6:
                    runCustomSearch();
                    break;
                case '7':
                    System.out.println("Returning to main menu....");
                    isRunning = false;
                    break;
            }
        }

    }

    public static void displayTransactionsByType(char type) {

        System.out.printf("Searching for all %ss on file....\n", type);
        printHeader();
        if (Objects.equals(type, '+')) {
            for (Transaction t : openLedger) {
                if (t.getAmount() > 0) {
                    displayEntry(t);
                }
            }
        } else {
            System.out.println("Searching for all payments on file....");
            printHeader();
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

    public static void addDeposit() {

        boolean isRunning = true;
        do {
            System.out.println("Let's get info on this deposit.");
            System.out.print("Enter deposit date (MM/dd/yyyy), or N for \"now\": ");
            LocalDate inputDate = getValidDate();
            System.out.print("Enter deposit time: ");
            LocalTime inputTime = getValidTime();
            System.out.print("Enter deposit description: ");
            String inputDscrptn = getValidString();
            System.out.print("Enter deposit vendor: ");
            String inputVendor = getValidString();
            System.out.print("Enter deposit amount: ");
            double inputAmount = Math.abs(getValidDouble());

            openLedger.add(new Transaction(inputDate, inputTime, inputDscrptn, inputVendor, inputAmount));

            System.out.println("Transaction added successfully.");
            System.out.println("""
                    Would you like to add another?
                    (Y) Yes, add another deposit
                    (N) No, return to main menu""");

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

    public static LocalDate getValidDate() {

        // initializes LocalDate and boolean badInput
        LocalDate inputDate = null;

        boolean badInput = false;
        // uses do/while loop
        do {
            String userInputDate = getValidString();
            // sets badInput to false first
            badInput = false;
            // if input contains letter N at the start, assume LocalDate is now
            if (Character.toUpperCase(userInputDate.charAt(0)) == 'N') {
                inputDate = LocalDate.now();
                System.out.println("Date set as current date, " + inputDate);
                return inputDate;
            } else {
                Set<DateTimeFormatter> dateFormatters = Set.of(
                        // M/d/yy formats
                        DateTimeFormatter.ofPattern("M/d/yy"),
                        DateTimeFormatter.ofPattern("M-d-yy"),
                        DateTimeFormatter.ofPattern("M.d.yy"),
                        // M/d/yyyy formats
                        DateTimeFormatter.ofPattern("M/d/yyyy"),
                        DateTimeFormatter.ofPattern("M-d-yyyy"),
                        DateTimeFormatter.ofPattern("M.d.yyyy"),
                        // MM/dd/yy formats
                        DateTimeFormatter.ofPattern("MM/dd/yy"),
                        DateTimeFormatter.ofPattern("MM-dd-yy"),
                        DateTimeFormatter.ofPattern("MM.dd.yy"),
                        // MM/dd/yyyy formats
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                        DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                        DateTimeFormatter.ofPattern("MM.dd.yyyy")
                );
                //tries to match the inputDate with the list dateFormatters
                for (DateTimeFormatter formatter : dateFormatters) {
                    try {
                        return LocalDate.parse(userInputDate, formatter);
                    } catch (DateTimeParseException e) {
                        // tries the next format
                    }
                }
                System.out.println("You have not entered a valid date, please try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        return inputDate;
    }

    public static LocalTime getValidTime() {

        // initializes LocalDate and boolean badInput
        LocalTime inputTime = null;

        boolean badInput = false;
        // uses do/while loop
        do {
            String userInputTime = getValidString();
            // sets badInput to false first
            badInput = false;
            // if input contains letter N at the start, assume LocalDate is now
            if (Character.toUpperCase(userInputTime.charAt(0)) == 'N') {
                inputTime = LocalTime.now();
                System.out.println("Date set as current date, " + inputTime);
                return inputTime;
            } else {
                Set<DateTimeFormatter> timeFormatters = Set.of(
                        // 24-hour formatters
                        DateTimeFormatter.ofPattern("H:mm"),
                        DateTimeFormatter.ofPattern("HH:mm"),
                        DateTimeFormatter.ofPattern("H-mm"),
                        DateTimeFormatter.ofPattern("HH-mm"),
                        DateTimeFormatter.ofPattern("H.mm"),
                        DateTimeFormatter.ofPattern("HH.mm"),
                        // 12-hour formatters with AM/PM
                        DateTimeFormatter.ofPattern("h:mm a"),
                        DateTimeFormatter.ofPattern("hh:mm a"),
                        DateTimeFormatter.ofPattern("h-mm a"),
                        DateTimeFormatter.ofPattern("hh-mm a"),
                        DateTimeFormatter.ofPattern("h.mm a"),
                        DateTimeFormatter.ofPattern("hh.mm a")
                );
                //tries to match the inputDate with the list dateFormatters
                for (DateTimeFormatter formatter : timeFormatters) {
                    try {
                        return LocalTime.parse(userInputTime, formatter);
                    } catch (DateTimeParseException e) {
                        // tries the next format
                    }
                }
                System.out.println("You have not entered a valid time, please try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        return inputTime;
    }

    public static double getValidDouble() {

        // initializes inputDouble and boolean badInput
        double inputDouble = 0;
        boolean badInput = false;

        // uses do/while loop
        do {

            // sets badInput to false first
            badInput = false;

            //tries to get a double
            try {
                inputDouble = input.nextDouble();
                // if input is a double but has too many decimal points, rounds it and informs user
                if (BigDecimal.valueOf(inputDouble).scale() > 2) {
                    inputDouble = BigDecimal.valueOf(inputDouble).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    System.out.printf("Your input has been rounded to $%.2f.", inputDouble);
                }
                // if it can't read as double, throws exception with error message and sets badInput to try again
            } catch (Exception e) {
                System.out.println("Sorry I don't know what you mean, please try again.");
                badInput = true;
            }
            // eats buffer
            input.nextLine();
            // conditional checks badInput boolean
        } while (badInput);

        // returns the correct inputDouble as a double
        return inputDouble;
    }

    public static String getValidString() {

        String string;
        boolean badInput = false;

        do {
            badInput = false;
            string = input.nextLine().trim();

            if (string.isEmpty() || string.equals("\n")) {
                System.out.println("You have not entered anything, please try again.");
                badInput = true;
            }
        } while (badInput);

        return string;
    }

    public static char getValidMenuChar(Set<Character> validMenuOptions) {

        // initializes inputChar and boolean badInput
        char inputChar = ' ';
        boolean badInput = false;

        // uses do/while loop
        do {
            // sets badInput to false first
            badInput = false;
            //tries to get an input
            try {
                // takes uppercase first character of user input
                inputChar = Character.toUpperCase(input.nextLine().trim().charAt(0));
                // if input does not match Set of characters in method parameters, asks for another attempt
                if (!validMenuOptions.contains(inputChar)) {
                    System.out.println("Sorry, I don't recognize that option, please try again.");
                    badInput = true;
                }
                // if it can't read as a valid char, catches exception and sets badInput to true to try again
            } catch (Exception e) {
                System.out.println("Sorry I don't know what you mean, please try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        // returns the correct inputChar as an uppercase char
        return inputChar;
    }

}
