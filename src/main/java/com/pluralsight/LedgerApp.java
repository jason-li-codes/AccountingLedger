package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.pluralsight.InputValidation.*;


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

    public static void runReportByVendor() {

        System.out.println("What vendor matches your query?");
        String vendorInput = getValidString();

        runReportCustom("byVendor", null, null, null, null, null, vendorInput, null, null);
    }

    public static void gatherInfoCustomSearch() {

        System.out.println("Let's get information about this custom search.");

        LocalDate startDateInput = null;
        System.out.println("Enter the starting date you would like to search up to, or (N) for \"now\": ");
        String startDateInputStr = input.nextLine().trim();
        if (!startDateInputStr.isEmpty()) {
            startDateInput = validateDate(startDateInputStr);
        }

        LocalDate endDateInput = null;
        System.out.println("Enter the ending date you would like to search up to, or (N) for \"now\": ");
        String endDateInputStr = input.nextLine().trim();
        if (!endDateInputStr.isEmpty()) {
            endDateInput = validateDate(endDateInputStr);
        }

        if (startDateInput != null && endDateInput != null && startDateInput.isAfter(endDateInput)) {
            LocalDate temp = startDateInput;
            startDateInput = endDateInput;
            endDateInput = temp;
        }

        LocalTime startTimeInput = null;
        System.out.println("Enter the starting time you would like to search up to, or (N) for \"now\": ");
        String startTimeInputStr = input.nextLine().trim();
        if (!startTimeInputStr.isEmpty()) {
            startTimeInput = validateTime(startTimeInputStr);
        }

        LocalTime endTimeInput = null;
        System.out.println("Enter the ending time you would like to search up to, or (N) for \"now\": ");
        String endTimeInputStr = input.nextLine().trim();
        if (!endTimeInputStr.isEmpty()) {
            endTimeInput = validateTime(endTimeInputStr);
        }

        if (startTimeInput != null && endTimeInput != null && startTimeInput.isAfter(endTimeInput)) {
            LocalTime temp = startTimeInput;
            startTimeInput = endTimeInput;
            endTimeInput = temp;
        }

        System.out.println("What description matches your query?");
        String descriptionInput = input.nextLine().trim().toLowerCase();

        System.out.println("What vendor matches your query?");
        String vendorInput = input.nextLine().trim().toLowerCase();

        Double amountMinInput = null;
        System.out.println("What is the minimum transaction you are looking for?");
        String amountMinInputStr = input.nextLine().trim();
        if (!amountMinInputStr.isEmpty()) {
            amountMinInput = validateDouble(amountMinInputStr);
        }

        Double amountMaxInput = null;
        System.out.println("What is the maximum transaction you are looking for?");
        String amountMaxInputStr = input.nextLine().trim();
        if (!amountMaxInputStr.isEmpty()) {
            amountMaxInput = validateDouble(amountMaxInputStr);
        }

        if (amountMinInput != null && amountMaxInput != null && amountMinInput > amountMaxInput) {
            Double temp = amountMinInput;
            amountMinInput = amountMaxInput;
            amountMaxInput = temp;
        }

        runReportCustom("custom", startDateInput, endDateInput, startTimeInput, endTimeInput,
                descriptionInput, vendorInput, amountMinInput, amountMaxInput);

    }

    public static void runReportCustom(String type, LocalDate startDate, LocalDate endDate, LocalTime startTime,
                                       LocalTime endTime, String description, String vendor,
                                       Double amountMin, Double amountMax) {

        System.out.println("Searching....");
        ArrayList<Transaction> filteredLedger = (ArrayList<Transaction>) openLedger.stream()
                .filter(t -> (startDate == null || !t.getTransactionDate().isBefore(startDate)) &&
                        (endDate == null || !t.getTransactionDate().isAfter(endDate)))
                .filter(t -> (startTime == null || !t.getTransactionTime().isBefore(startTime)) &&
                        (endTime == null || !t.getTransactionTime().isAfter(endTime)))
                .filter(t -> (description == null || description.isEmpty() ||
                        t.getDescription().toLowerCase().contains(description)))
                .filter(t -> (vendor == null || vendor.isEmpty() ||
                        t.getVendor().toLowerCase().contains(vendor)))
                .filter(t -> (amountMin == null || t.getAmount() >= amountMin))
                .filter(t -> (amountMax == null || t.getAmount() <= amountMax))
                .collect(Collectors.toCollection(ArrayList::new));

        displayEntries(filteredLedger);

        System.out.println("Searching complete.");

        if (!filteredLedger.isEmpty()) {
            System.out.println("""
                    Would you like a file of this report?
                    (Y) Yes
                    (N) No, return to previous menu""");
            if (Character.toUpperCase(input.nextLine().trim().charAt(0)) == 'Y') {
                createFile(filteredLedger, type);
            }
        } else {
            System.out.println("There are no transactions to display.");
        }
        System.out.println("Returning to previous menu....");
    }


    public static void runReportDefault(String type) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch (type) {
            case "monthToDate":
                startDate = startDate.withDayOfMonth(1);
                break;
            case "yearToDate":
                startDate = startDate.withDayOfYear(1);
                break;
            case "previousMonth":
                startDate = startDate.minusMonths(1).withDayOfMonth(1);
                endDate = endDate.minusMonths(1).withDayOfMonth(endDate.minusMonths(1).lengthOfMonth());
                break;
            case "previousYear":
                startDate = startDate.minusYears(1).withDayOfYear(1);
                endDate = endDate.minusYears(1).withMonth(12).withDayOfMonth(31);
                break;
        }
        runReportCustom(type, startDate, endDate, null, null, null, null, null, null);
    }

    public static void createFile(ArrayList<Transaction> list, String type) {

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String newFileName = String.join("_", formattedDate, type, fileName);
        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(newFileName))) {
            bufWriter.write("date|time|description|vendor|amount\n");
            for (Transaction transaction : list) {
                bufWriter.write(transaction.writeCsvFormat() + "\n");
            }
            System.out.println("File created successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, there's a problem creating the file, please try again later.");
        } catch (IOException e) {
            System.out.println("Sorry, there's a problem writing the file, please try again later.");
        }
    }

    public static void displayEntriesByType(String type) {

        System.out.printf("Searching for all %ss on file....\n", type);

        ArrayList<Transaction> filteredLedgerByType = null;
        if (Objects.equals(type, "deposit")) {
            runReportCustom(type, null, null, null, null, null, null, 0.0, null);
        } else {
            runReportCustom(type, null, null, null, null, null, null, null, 0.0);
        }
    }

    public static void displayEntries(ArrayList<Transaction> list) {

        System.out.printf("%-12s %-10s %-40s %-20s %12s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("+------------+----------+---------------------------------------+------------------------+--------------+");
        for (Transaction t : list) {
            System.out.printf("|%-12s|%-10s|%-40s|%-20s|%12.2f|\n",
                    t.getTransactionDate(), t.getTransactionTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
        System.out.println("+------------+----------+---------------------------------------+------------------------+--------------+");
    }

    public static void addTransaction(String type) {

        System.out.printf("Let's get information about this %s.\n", type);

        boolean isRunning = true;
        do {
            System.out.printf("Enter %s date (month/day/year), or (N) for \"now\": \n", type);
            LocalDate inputDate = getValidDate();
            System.out.printf("Enter %s time (hours:minutes), or (N) for \"now\": \n", type);
            LocalTime inputTime = getValidTime();
            System.out.printf("Enter %s description: \n", type);
            String inputDescription = getValidString();
            System.out.printf("Enter %s vendor: \n", type);
            String inputVendor = getValidString();
            System.out.printf("Enter %s amount: \n", type);
            double inputAmount = getValidDouble();
            if (Objects.equals(type, "deposit") && inputAmount < 0) {
                inputAmount *= -1;
                System.out.printf("Amount set to positive, $%.2f.\n", inputAmount);
            } else if (Objects.equals(type, "payment") && inputAmount > 0) {
                System.out.printf("Amount set to negative, -$%.2f.\n", inputAmount);
                inputAmount *= -1;
            }

            Transaction newTransaction = new Transaction(inputDate, inputTime, inputDescription, inputVendor, inputAmount);
            System.out.println(newTransaction.writeCsvFormat());
            System.out.println("""
                    Does this transaction contain the correct information you'd like to add?
                    (Y) Yes
                    (N) No, I need to re-enter the information
                    (X) No, I want to return to main menu""");

            char transactionMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
            switch (transactionMenuOption) {
                case 'Y':
                    LocalDateTime newTransactionDateTime = newTransaction.getTransactionDate().atTime(newTransaction.getTransactionTime());
                    int insertIndex = 0;
                    for (Transaction t : openLedger) {
                        if (!newTransactionDateTime.isBefore(t.getTransactionDate().atTime(t.getTransactionTime()))) {
                            break;
                        }
                        insertIndex++;
                    }
                    openLedger.add(insertIndex, newTransaction);
                    writeToCsvFile(insertIndex, newTransaction);
                    System.out.printf("""
                            Would you like to add another %s?
                            (Y) Yes, add another %s
                            (N) No, return to main menu""", type, type);

                    char menuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
                    if (menuOption == 'N') {
                        System.out.println("Returning to main menu...");
                        isRunning = false;
                    }
                    break;
                case 'N':
                    continue;
                case 'X':
                    isRunning = false;
                    break;
            }
        } while (isRunning);
    }

    public static void writeToCsvFile(int index, Transaction t) {

        if (index == 0) {
            try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(fileName, true))) {
                bufWriter.write(t.writeCsvFormat());
                bufWriter.newLine();
                System.out.println("Transaction added successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, there's a problem finding the ledger, please try again later.");
            } catch (IOException e) {
                System.out.println("Sorry, there's a problem adding to the ledger, please try again later.");
            }
        } else {
            // rewrite whole file
            try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(fileName))) {
                ArrayList<Transaction> reverseLedger = new ArrayList<>(openLedger);
                Collections.reverse(reverseLedger);
                bufWriter.write("date|time|description|vendor|amount\n");
                for (Transaction transaction : reverseLedger) {
                    bufWriter.write(transaction.writeCsvFormat() + "\n");
                }
                System.out.println("Transaction added successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, there's a problem finding the ledger, please try again later.");
            } catch (IOException e) {
                System.out.println("Sorry, there's a problem writing the ledger, please try again later.");
            }
        }
    }

    public static void loadLedger(String fileName) {

        System.out.printf("Opening %s....\n", fileName);

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
        } catch (IOException | DateTimeParseException e) {
            System.out.println("Sorry, there was a problem reading your ledger, please try again later.");
            System.exit(3);
        }
        Collections.reverse(openLedger);
    }

    public static String pickLedger() {

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
            System.out.println("Please enter your passcode: ");
            String userPasscode = getValidString();
            String[] userInfo = passcodes.get(userPasscode);

            if (userInfo != null) {
                name = userInfo[0];
                fileName = userInfo[1];
                break;
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
