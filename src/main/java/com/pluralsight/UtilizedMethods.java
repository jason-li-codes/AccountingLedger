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
     */
    public static String pickLedger() {
        // initializes HashMap that holds passcodes from file
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
     * This method reads a ledger file where each line (after the header) represents a transaction.
     * The transaction details are split by the '|' delimiter and used to create Transaction objects,
     * which are then added to the `openLedger` list. The ledger is then reversed to ease sorting later.
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

    /**
     * Prompts the user to input details about a new transaction (either deposit or payment)
     * and adds it to ledger ArrayList if confirmed.
     * The method ensures that the user provides valid input for the transaction date, time, description,
     * vendor, and amount. The user is also given the option to review and confirm the transaction before
     * adding it to ledger. The transaction is inserted in chronological order within the ledger.
     */
    public static void addTransaction(String type) {
        // Informs the user that the program is ready to collect transaction information
        System.out.printf("Let's get information about this %s.\n", type);
        // Loops repeatedly to ask for transaction details until user confirms or exits
        boolean isRunning = true;
        do {
            // Prompts for and get the transaction date and time, either as a user input or "now"
            System.out.printf("Enter %s date (month/day/year), or (N) for \"now\": \n", type);
            LocalDate inputDate = getValidDate();
            System.out.printf("Enter %s time (hours:minutes), or (N) for \"now\": \n", type);
            LocalTime inputTime = getValidTime();
            // Prompts for and gets the transaction description and vendor with validated non-empty Strings
            System.out.printf("Enter %s description: \n", type);
            String inputDescription = getValidString();
            System.out.printf("Enter %s vendor: \n", type);
            String inputVendor = getValidString();
            // Prompts for and get the transaction amount, validated as a double
            System.out.printf("Enter %s amount: \n", type);
            double inputAmount = getValidDouble();
            // Adjusts the amount for deposits (positive values) or payments (negative values), informing user
            if (Objects.equals(type, "deposit") && inputAmount < 0) {
                inputAmount *= -1; // Makes deposit amount positive if negative
                System.out.printf("Amount set to positive, $%.2f.\n", inputAmount);
            } else if (Objects.equals(type, "payment") && inputAmount > 0) {
                System.out.printf("Amount set to negative, -$%.2f.\n", inputAmount);
                inputAmount *= -1; // Makes payment amount negative if positive
            }
            // Creates a new Transaction object with the collected data
            Transaction newTransaction = new Transaction(inputDate, inputTime, inputDescription, inputVendor, inputAmount);
            // Displays the transaction in CSV format for the user to review
            System.out.println(newTransaction.convertToCsvFormat());
            // Prompts the user to confirm if the information is correct
            System.out.println("""
                    Does this transaction contain the correct information you'd like to add?
                    (Y) Yes
                    (N) No, I need to re-enter the information
                    (X) No, I want to return to main menu""");

            char transactionMenuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
            switch (transactionMenuOption) {
                case 'Y':
                    // Finds correct position to insert the transaction chronologically by looping through ledger
                    LocalDateTime newTransactionDateTime = inputDate.atTime(inputTime);
                    int insertIndex = 0;
                    for (Transaction t : openLedger) {
                        if (!newTransactionDateTime.isBefore(t.getTransactionDate().atTime(t.getTransactionTime()))) {
                            break;
                        }
                        insertIndex++;
                    }
                    openLedger.add(insertIndex, newTransaction); // Adds the transaction at correct position
                    writeToCsvFile(insertIndex, newTransaction); // Writes transaction to CSV file at same position
                    System.out.printf("""
                            Would you like to add another %s?
                            (Y) Yes, add another %s
                            (N) No, return to main menu""", type, type);

                    char menuOption = Character.toUpperCase(input.nextLine().trim().charAt(0));
                    if (menuOption == 'N') { // Exits the loop and return to main menu
                        System.out.println("Returning to main menu...");
                        isRunning = false;
                    }
                    break;
                case 'N': // Continues loop if prompted
                    continue;
                case 'X': // Exits to main menu if prompted
                    isRunning = false;
                    break;
            }
        } while (isRunning); // Continues looping until user exits or confirms the transaction
    }

    /**
     * Writes a transaction to the CSV ledger file.
     * If the index is 0, the method appends the transaction to the end of the file.
     * If the index is not 0, the method rewrites the entire ledger file, ensuring the transactions are written
     * in reverse chronological order.
     */
    public static void writeToCsvFile(int index, Transaction t) {
        // Appends the new transaction to the end of the file if transaction is most recent
        if (index == 0) {
            try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(fileName, true))) {
                // Writes the transaction's CSV format string to the file, followed by a new line
                bufWriter.write(t.convertToCsvFormat());
                bufWriter.newLine();
                System.out.println("Transaction added successfully.");
            } catch (FileNotFoundException e) { // Handles case when the file is not found
                System.out.println("Sorry, there's a problem finding the ledger, please try again later.");
            } catch (IOException e) { // Handles any other IOException
                System.out.println("Sorry, there's a problem adding to the ledger, please try again later.");
            }
        } else {
            // If index is not 0, rewrite the whole file to include the new transaction in the middle
            try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(fileName))) {
                // Creates a reversed copy of ledger since the file and ArrayList are in different orders
                ArrayList<Transaction> reverseLedger = new ArrayList<>(openLedger);
                Collections.reverse(reverseLedger);
                bufWriter.write("date|time|description|vendor|amount\n"); // Writes the header for CSV file
                for (Transaction transaction : reverseLedger) { // Writes each transaction to file
                    bufWriter.write(transaction.convertToCsvFormat() + "\n");
                }
                System.out.println("Transaction added successfully.");
            } catch (FileNotFoundException e) { // Handles case when the file is not found
                System.out.println("Sorry, there's a problem finding the ledger, please try again later.");
            } catch (IOException e) { // Handles any other IOException
                System.out.println("Sorry, there's a problem writing the ledger, please try again later.");
            }
        }
    }

    /**
     * Displays the entries from a given ledger in a tabular format.
     * This method formats the data from the transactions in the provided ledger and
     * prints it to the console in a readable table format with columns for date, time, description,
     * vendor, and amount.
     */
    public static void displayEntries(ArrayList<Transaction> ledger) {
        // Prints the header for the table with column names and appropriate spacing
        System.out.printf("%-12s %-10s %-40s %-20s %12s%n", "Date", "Time", "Description", "Vendor", "Amount");
        // Prints a separator line to distinguish header from data
        System.out.println("+------------+----------+----------------------------------------+--------------------+--------------+");
        for (Transaction t : ledger) { // Iterates through each transaction in ledger and print details with spacing
            System.out.printf("|%-12s|%-10s|%-40s|%-20s|%12.2f|\n",
                    t.getTransactionDate(), t.getTransactionTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
        // Prints closing separator line after displaying transactions
        System.out.println("+------------+----------+----------------------------------------+--------------------+--------------+");
    }

    /**
     * Displays entries filtered by transaction type ("deposit" or "payment").
     * This method searches for all transactions of a specified type ("deposit" or "payment")
     * and passes the parameters to another method runReportCustom to handle filtering and report generation.
     */
    public static void displayEntriesByType(String type) {
        // Informs user system is searching for all entries of specified type
        System.out.printf("Searching for all %ss on file....\n", type);
        // Checks transaction type and runs a custom report with correct parameters
        if (Objects.equals(type, "deposit")) {
            runReportCustom(type, null, null, null, null, null, null, 0.0, null);
        } else {
            runReportCustom(type, null, null, null, null, null, null, null, 0.0);
        }
    }

    /**
     * Runs a default report based on the specified type, such as "monthToDate", "yearToDate",
     * "previousMonth", or "previousYear". The report is generated by calling runReportCustom
     * with the appropriate date range based on the type.
     * The method calculates the start and end dates for each report type and then invokes
     * runReportCustom with those dates to generate the report.
     */
    public static void runReportDefault(String type) {
        // Adjusts the start and end dates based on the report type
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch (type) {
            case "monthToDate": // Sets the start date to the first day of the current month
                startDate = startDate.withDayOfMonth(1);
                break;
            case "yearToDate": // Sets the start date to the first day of the current year
                startDate = startDate.withDayOfYear(1);
                break;
            case "previousMonth": // Sets the start and end dates to the first and last day of previous month
                startDate = startDate.minusMonths(1).withDayOfMonth(1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                break;
            case "previousYear": // Sets the start and end dates to the first and last day of previous year
                startDate = startDate.minusYears(1).withDayOfYear(1);
                endDate = startDate.withMonth(12).withDayOfMonth(31);
                break;
        }
        // Calls runReportCustom method with the calculated start and end dates
        runReportCustom(type, startDate, endDate, null, null, null, null, null, null);
    }

    /**
     * Runs a report filtered by vendor based on user input.
     * This method prompts the user to enter the name of the vendor they want to query,
     * then it calls runReportCustom with the vendor name and other necessary parameters set to null.
     */
    public static void runReportByVendor() {
        // Prompts the user for the vendor name to filter by
        System.out.println("What vendor matches your query?");
        String vendorInput = getValidString();
        // Calls runReportCustom with the vendor input and other parameters set to null
        runReportCustom("byVendor", null, null, null, null, null, vendorInput, null, null);
    }

    /**
     * Gathers information for a custom search query based on the user's inputs.
     * This method prompts the user to enter various search criteria, such as start and end dates,
     * start and end times, description, vendor, and transaction amounts. The method then calls
     * runReportCustom to generate a report based on the provided inputs.
     */
    public static void gatherInfoCustomSearch() {

        System.out.println("Let's get information about this custom search. Press enter to skip any question.");
        // Prompts user for the starting and ending dates of the search range
        LocalDate startDateInput;
        System.out.println("Enter the starting date you would like to search up to, or (N) for \"now\": ");
        String startDateInputStr = input.nextLine().trim();
        startDateInput = attemptParseDate(startDateInputStr);
        LocalDate endDateInput = null;
        System.out.println("Enter the ending date you would like to search up to, or (N) for \"now\": ");
        String endDateInputStr = input.nextLine().trim();
        endDateInput = attemptParseDate(endDateInputStr);

        // Swaps starting and ending dates if necessary
        if (startDateInput != null && endDateInput != null && startDateInput.isAfter(endDateInput)) {
            LocalDate temp = startDateInput;
            startDateInput = endDateInput;
            endDateInput = temp;
        }

        // Prompts user for the starting and ending times of the search range
        LocalTime startTimeInput = null;
        System.out.println("Enter the starting time you would like to search up to, or (N) for \"now\": ");
        String startTimeInputStr = input.nextLine().trim();
        startTimeInput = attemptParseTime(startTimeInputStr);
        LocalTime endTimeInput = null;
        System.out.println("Enter the ending time you would like to search up to, or (N) for \"now\": ");
        String endTimeInputStr = input.nextLine().trim();
        endTimeInput = attemptParseTime(endTimeInputStr);

        // Swaps starting and ending times if necessary
        if (startTimeInput != null && endTimeInput != null && startTimeInput.isAfter(endTimeInput)) {
            LocalTime temp = startTimeInput;
            startTimeInput = endTimeInput;
            endTimeInput = temp;
        }
        // Gets the description input from the user, converting it to lowercase for case-insensitive comparison
        System.out.println("What description matches your query?");
        String descriptionInput = input.nextLine().trim().toLowerCase();
        // Gets the vendor input from the user, converting it to lowercase for case-insensitive comparison
        System.out.println("What vendor matches your query?");
        String vendorInput = input.nextLine().trim().toLowerCase();

        // Gets the minimum and maximum transaction amount input from the user
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

        // Swaps minimum and maximum if necessary
        if (amountMinInput != null && amountMaxInput != null && amountMinInput > amountMaxInput) {
            Double temp = amountMinInput;
            amountMinInput = amountMaxInput;
            amountMaxInput = temp;
        }
        // Call the custom report method with the gathered criteria
        runReportCustom("custom", startDateInput, endDateInput, startTimeInput, endTimeInput,
                descriptionInput, vendorInput, amountMinInput, amountMaxInput);
    }

    /**
     * Runs a custom report based on the given filter criteria.
     * This method filters the ledger based on the provided parameters (date range, time range,
     * description, vendor, and amount) and displays the filtered results. If any matching transactions
     * are found, the user is given the option to save the report to a file.
     */
    public static void runReportCustom(String type, LocalDate startDate, LocalDate endDate, LocalTime startTime,
                                       LocalTime endTime, String description, String vendor,
                                       Double amountMin, Double amountMax) {
        // Filters ledger using the provided parameters
        System.out.println("Searching....");
        ArrayList<Transaction> filteredLedger = (ArrayList<Transaction>) openLedger.stream()
                .filter(t -> (startDate == null || !t.getTransactionDate().isBefore(startDate)) &&
                        (endDate == null || !t.getTransactionDate().isAfter(endDate))) // Filters by date range
                .filter(t -> (startTime == null || !t.getTransactionTime().isBefore(startTime)) &&
                        (endTime == null || !t.getTransactionTime().isAfter(endTime))) // Filters by time range
                .filter(t -> (description == null || description.isEmpty() ||
                        t.getDescription().toLowerCase().contains(description)))  // Filters by description
                .filter(t -> (vendor == null || vendor.isEmpty() ||
                        t.getVendor().toLowerCase().contains(vendor))) // Filters by vendor
                .filter(t -> (amountMin == null || t.getAmount() >= amountMin))
                .filter(t -> (amountMax == null || t.getAmount() <= amountMax)) // Filters by amount
                .collect(Collectors.toCollection(ArrayList::new)); // Collects filtered results into ArrayList

        // Displays the filtered entries
        displayEntries(filteredLedger);
        System.out.println("Searching complete.");
        // If there are matching results, asks if user wants to save the report to a file
        if (!filteredLedger.isEmpty()) {
            System.out.println("""
                    Would you like a file of this report?
                    (Y) Yes
                    (N) No, return to previous menu""");
            if (Character.toUpperCase(input.nextLine().trim().charAt(0)) == 'Y') {
                createFile(filteredLedger, type); // Calls createFile method to save the report
            }
        } else { // If no matching transactions were found, informs user and exits
            System.out.println("There are no transactions to display.");
        }
        System.out.println("Returning to previous menu...."); // Returns to previous menu
    }

    /**
     * Creates a CSV file based on the filtered ledger transactions and saves it to the file system.
     * This method generates a file name based on the current timestamp and the report type (e.g., "custom"),
     * and writes the filtered ledger transactions to that file in CSV format. Each transaction's details
     * (date, time, description, vendor, and amount) are written as a new line in the file.
     */
    public static void createFile(ArrayList<Transaction> filteredLedger, String type) {
        // Generates a formatted timestamp for the as newFileName
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String newFileName = String.join("_", formattedDate, type, fileName);

        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(newFileName))) {
            bufWriter.write("date|time|description|vendor|amount\n"); // Writes header for CSV file
            for (Transaction transaction : filteredLedger) { // Writes each transaction to file
                bufWriter.write(transaction.convertToCsvFormat() + "\n");
            }
            System.out.println("File created successfully.");
        } catch (FileNotFoundException e) { // Handles FileNotFoundException
            System.out.println("Sorry, there's a problem creating the file, please try again later.");
        } catch (IOException e) { // Handles other IOExceptions
            System.out.println("Sorry, there's a problem writing the file, please try again later.");
        }
    }

    /**
     * Displays detailed information about each main menu option to the user.
     * This method provides a brief description of what each menu option does. It's intended to help users
     * understand the purpose of each option available in the main menu of the program.
     */
    public static void moreInfo() {
        // Prints descriptions of each main menu option to the console
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
