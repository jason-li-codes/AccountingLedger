package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


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

            char menuOption = getValidMenuChar(new ArrayList<Character>(Arrays.asList('D', 'P', 'L', 'I', 'X')));

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
            }
        }
    }

    public static void loadLedger(String fileName) {

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

        System.out.printf("Welcome to your Accounting Ledger App, %s! Opening %s...\n", name, fileName);
        return fileName;
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

    public static char getValidMenuChar(ArrayList<Character> validMenuOptions) {

        // initializes inputChar and boolean badInput
        char inputChar = ' ';
        boolean badInput = false;

        // uses do/while loop
        do {
            // sets badInput to false first
            badInput = false;
            //tries to get an input
            try {
                inputChar = input.nextLine().trim().charAt(0);
                if (!validMenuOptions.contains(Character.toUpperCase(inputChar))) {
                    System.out.println("Sorry, I don't recognize that option, please try again.");
                    badInput = true;
                }
                // if it can't read as a valid menu option, catches exception and sets badInput to true to try again
            } catch (Exception e) {
                System.out.println("Sorry I don't know what you mean, please try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        // returns the correct inputChar as a String of one character
        return inputChar;
    }

}
