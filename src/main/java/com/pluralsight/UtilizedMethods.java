package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;

// Imports InputValidation and LedgerApp classes, which contains provide validation and code entry point
import static com.pluralsight.InputValidation.*;
import static com.pluralsight.LedgerApp.*;

/**
 * A collection of shared utility methods used throughout the Ledger application.
 * Includes file loading, transaction handling, reporting, and searching, and file creation.
 */
public class UtilizedMethods {

    /**
     * Prompts user to enter a passcode and returns the corresponding ledger file.
     *
     * @return the file name of the ledger associated with the passcode.
     */
    public static String pickLedger() {
        // initializes HashMap that will hode passcodes from file
        HashMap<String, String[]> passcodes = new HashMap<>();
        String name = "";
        String fileName = "";
        // Reads passcodes from the passcodes.csv file
        try (BufferedReader bufReader = new BufferedReader(new FileReader("passcodes.csv"))) {
            String input = bufReader.readLine(); // Skip header
            // Reads each line of file by defining readLine in the conditional
            while ((input = bufReader.readLine()) != null) {
                // Splits String along pipe '|' to get info individually
                String[] info = input.split("\\|");
                passcodes.put(info[0], new String[]{info[1], info[2]});
            }
        } catch (FileNotFoundException e) { // Exits application if FileNotFoundException is reached
            System.out.println("Sorry, there's a problem checking passcodes, please try again later.");
            System.exit(0);
        } catch (IOException e) { // Exits application if IOException is reached
            System.out.println("Sorry, there was a problem accessing accounts, please try again later.");
            System.exit(1);
        }
        // Uses do/while loop to with a boolean badInput to get a valid passcode from the user
        boolean badInput = false;
        do {
            System.out.println("Please enter your passcode: ");
            String userPasscode = getValidString(); // Gets user passcode with getValidString, ensuring non-empty input
            String[] userInfo = passcodes.get(userPasscode); // Uses passcode as key to retrieve appropriate value

            if (userInfo != null) {
                name = userInfo[0];
                fileName = userInfo[1]; // Parses value into array of Strings
            } else {
                System.out.println("Your passcode does not match any account we have, please try again.");
                badInput = true; // Sets badInput to true if key is not found in HashMap
            }
        } while (badInput);

        System.out.printf("Welcome to your Accounting Ledger, %s! ", name);
        return fileName; // Prints welcome message and returns fileName
    }

    /**
     * Loads transaction data from a ledger file and stores it in a collection.
     * <p>
     * This method reads a ledger file where each line (after the header) represents a transaction.
     * The transaction details are split by the '|' delimiter and used to create Transaction objects,
     * which are then added to the `openLedger` list. The ledger is then reversed to ease sorting later.
     * </p>
     *
     * @param fileName The name of the ledger file to be loaded.
     */
    public static void loadLedger(String fileName) {
        // Notifies user about the opening of the ledger file
        System.out.printf("Opening %s....\n", fileName);

        try (BufferedReader bufReader = new BufferedReader(new FileReader(fileName))) {
            // Reads the first line of the file to skip the header
            String input = bufReader.readLine();
            // Loops through the remaining lines (transactions) in the ledger file
            while ((input = bufReader.readLine()) != null) {
                // Splits the input line by pipe '|' to extract transaction details
                String[] info = input.split("\\|");
                // Parses the transaction date, time, and amount from the split data
                LocalDate transDate = LocalDate.parse(info[0]);
                LocalTime transTime = LocalTime.parse(info[1]);
                double amount = Double.parseDouble(info[4]);
                // Creates a new Transaction object with the extracted information and adds it to openLedger
                openLedger.add(new Transaction(transDate, transTime, info[2], info[3], amount));
            }
        } catch (FileNotFoundException e) { // Exits application if FileNotFoundException is reached
            System.out.println("Sorry, there's a problem finding your ledger, please try again later.");
            System.exit(2);
        } catch (IOException | DateTimeParseException e) { // Exits application if dates cannot be parsed
            System.out.println("Sorry, there was a problem reading your ledger, please try again later.");
            System.exit(3);
        }
        Collections.reverse(openLedger); // Reverses ledger after loading so it is ordered from newest to earliest
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
            System.out.println(newTransaction.convertToCsvFormat());
            System.out.println("""
                    Does this transaction contain the correct information you'd like to add?
                    (Y) Yes
                    (N) No, I need to re-enter the information
                    (X) No, I want to return to main menu""");

            char transactionMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
            switch (transactionMenuOption) {
                case 'Y':
                    LocalDateTime newTransactionDateTime = inputDate.atTime(inputTime);
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
                bufWriter.write(t.convertToCsvFormat());
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
                    bufWriter.write(transaction.convertToCsvFormat() + "\n");
                }
                System.out.println("Transaction added successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, there's a problem finding the ledger, please try again later.");
            } catch (IOException e) {
                System.out.println("Sorry, there's a problem writing the ledger, please try again later.");
            }
        }
    }

    public static void displayEntries(ArrayList<Transaction> ledger) {

        System.out.printf("%-12s %-10s %-40s %-20s %12s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("+------------+----------+---------------------------------------+------------------------+--------------+");
        for (Transaction t : ledger) {
            System.out.printf("|%-12s|%-10s|%-40s|%-20s|%12.2f|\n",
                    t.getTransactionDate(), t.getTransactionTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
        System.out.println("+------------+----------+---------------------------------------+------------------------+--------------+");
    }

    public static void displayEntriesByType(String type) {

        System.out.printf("Searching for all %ss on file....\n", type);

        if (Objects.equals(type, "deposit")) {
            runReportCustom(type, null, null, null, null, null, null, 0.0, null);
        } else {
            runReportCustom(type, null, null, null, null, null, null, null, 0.0);
        }
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
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                break;
            case "previousYear":
                startDate = startDate.minusYears(1).withDayOfYear(1);
                endDate = startDate.withMonth(12).withDayOfMonth(31);
                break;
        }
        runReportCustom(type, startDate, endDate, null, null, null, null, null, null);
    }

    public static void runReportByVendor() {

        System.out.println("What vendor matches your query?");
        String vendorInput = getValidString();

        runReportCustom("byVendor", null, null, null, null, null, vendorInput, null, null);
    }

    public static void gatherInfoCustomSearch() {

        System.out.println("Let's get information about this custom search.");

        LocalDate startDateInput;
        System.out.println("Enter the starting date you would like to search up to, or (N) for \"now\": ");
        String startDateInputStr = input.nextLine().trim();
        startDateInput = attemptParseDate(startDateInputStr);

        LocalDate endDateInput = null;
        System.out.println("Enter the ending date you would like to search up to, or (N) for \"now\": ");
        String endDateInputStr = input.nextLine().trim();
        endDateInput = attemptParseDate(endDateInputStr);

        if (startDateInput != null && endDateInput != null && startDateInput.isAfter(endDateInput)) {
            LocalDate temp = startDateInput;
            startDateInput = endDateInput;
            endDateInput = temp;
        }

        LocalTime startTimeInput = null;
        System.out.println("Enter the starting time you would like to search up to, or (N) for \"now\": ");
        String startTimeInputStr = input.nextLine().trim();
        startTimeInput = attemptParseTime(startTimeInputStr);

        LocalTime endTimeInput = null;
        System.out.println("Enter the ending time you would like to search up to, or (N) for \"now\": ");
        String endTimeInputStr = input.nextLine().trim();
        endTimeInput = attemptParseTime(endTimeInputStr);

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
            amountMinInput = attemptParseDouble(amountMinInputStr);
        }

        Double amountMaxInput = null;
        System.out.println("What is the maximum transaction you are looking for?");
        String amountMaxInputStr = input.nextLine().trim();
        if (!amountMaxInputStr.isEmpty()) {
            amountMaxInput = attemptParseDouble(amountMaxInputStr);
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

    public static void createFile(ArrayList<Transaction> filteredLedger, String type) {

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String newFileName = String.join("_", formattedDate, type, fileName);
        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(newFileName))) {
            bufWriter.write("date|time|description|vendor|amount\n");
            for (Transaction transaction : filteredLedger) {
                bufWriter.write(transaction.convertToCsvFormat() + "\n");
            }
            System.out.println("File created successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, there's a problem creating the file, please try again later.");
        } catch (IOException e) {
            System.out.println("Sorry, there's a problem writing the file, please try again later.");
        }
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
