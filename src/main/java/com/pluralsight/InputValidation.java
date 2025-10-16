package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static com.pluralsight.LedgerApp.input;

/**
 * A class for validating and parsing the following user input: dates, times, doubles, and Strings.
 */
public class InputValidation {
    /**
     * Prompts the user for a valid date until a proper one is entered.
     * Supports multiple date formats with month, then day, then year, with all supported delimiters.
     *
     * @return A valid LocalDate object entered by the user.
     */
    public static LocalDate getValidDate() {

        // Initializes LocalDate inputDate
        LocalDate inputDate = null;

        // Uses do/while loop with a boolean badInput to get a valid date from the user, calling on attemptParseDate
        // method, which returns null and tries with new user input
        boolean badInput = false;
        do {
            // Sets badInput to false first, to ensure loop doesn't run continuously
            badInput = false;
            String userInputDate = getValidString(); // Calls getValidString method to ensure input is not empty
            inputDate = attemptParseDate(userInputDate); // Calls attemptParseDate to check if date is valid
            if (inputDate == null) {
                System.out.println("You have not entered a valid date, please try again.");
                badInput = true; // Sets badInput to true if date input cannot be parsed correctly
            }
        } while (badInput);

        return inputDate; // return statement to pacify IntelliJ
    }

    /**
     * Attempts to parse the user-provided date string using a set of supported formats.
     * If the input starts with 'N' or 'n' for "now", it returns the current date.
     *
     * @param date The input date string.
     * @return A LocalDate if parsing succeeds; null otherwise.
     */
    public static LocalDate attemptParseDate(String date) {

        // Checks if input begins with an 'N' or 'n', and returns currentDate if so
        if (Character.toUpperCase(date.charAt(0)) == 'N') {
            LocalDate currentDate = LocalDate.now();
            System.out.println("Date set as current date, " + currentDate);
            return currentDate;
        } else {
            // Set of DateTimeFormatters for all supported delimiters
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
            // Tries to match the inputDate with the every DateTimeFormatter in list dateFormatters
            for (DateTimeFormatter formatter : dateFormatters) {
                try {
                    return LocalDate.parse(date, formatter); // Attempts to return a parsed LocalDate
                } catch (DateTimeParseException e) {
                    // Tries the next format, ignoring the Exception
                }
            }
        }
        return null; // Returns null value if all parse attempts fail
    }

    /**
     * Prompts the user for a valid time until a proper one is entered.
     * Supports both 12-hour and 24-hour formats, with all supported delimiters.
     *
     * @return A valid LocalTime object entered by the user.
     */
    public static LocalTime getValidTime() {

        // initializes LocalTime inputTime
        LocalTime inputTime = null;

        // Uses do/while loop with a boolean badInput to get a valid time from the user, calling on attemptParseTime
        // method, which returns null and tries with new user input
        boolean badInput = false;
        do {
            // Sets badInput to false first, to ensure loop doesn't run continuously
            badInput = false;
            String userInputTime = getValidString(); // Calls getValidString method to ensure input is not empty
            inputTime = attemptParseTime(userInputTime); // Calls attemptParseDate to check if date is valid
            if (inputTime == null) {
                System.out.println("You have not entered a valid time, please try again.");
                badInput = true; // Sets badInput to true if date input cannot be parsed correctly
            }
        } while (badInput);

        return inputTime; // return statement to pacify IntelliJ
    }

    /**
     * Attempts to parse the user-provided time string using a set of time formats.
     * If input starts with 'N' or 'n' for "now", returns the current time.
     *
     * @param time The input time string.
     * @return A LocalTime if parsing succeeds; null otherwise.
     */
    public static LocalTime attemptParseTime(String time) {

        // Checks if input begins with an 'N' or 'n', and returns currentTime if so
        if (Character.toUpperCase(time.charAt(0)) == 'N') {
            LocalTime currentTime = LocalTime.now();
            System.out.println("Time set as current time, " + currentTime);
            return currentTime;
        } else {
            // Set of DateTimeFormatters for all supported delimiters
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
            // Tries to match the inputTime with the every DateTimeFormatter in list timeFormatters
            for (DateTimeFormatter formatter : timeFormatters) {
                try {
                    return LocalTime.parse(time, formatter); // Attempts to return a parsed LocalDate
                } catch (DateTimeParseException e) {
                    // Tries the next format, ignoring the Exception
                }
            }
        }
        return null; // Returns null value if all parse attempts fail
    }

    /**
     * Prompts the user until a valid decimal number (double) is entered.
     * Rounds the result to two decimal places.
     *
     * @return A valid double value entered by the user.
     */
    public static double getValidDouble() {

        // Initializes inputDouble as null
        String userInputDouble = null;
        Double inputDouble = null;

        // Uses do/while loop with a boolean badInput to get a valid double from the user, calling on attemptParseDouble
        // method, which returns null and tries with new user input
        boolean badInput = false;
        do {
            // Sets badInput to false first, to ensure loop doesn't run continuously
            badInput = false;
            userInputDouble = input.nextLine(); // Accepts next user input as a String
            inputDouble = attemptParseDouble(userInputDouble); // calls attemptParseDouble to check for valid double
            if (inputDouble != null) {
                inputDouble = Math.round(inputDouble * 100) / 100.0;  // Rounds it to 2 decimal points if parse succeeds
                return inputDouble;
            } else {
                System.out.println("Sorry I don't know what you mean, please try again.");
                badInput = true; // Sets badInput to true if input cannot be parsed correctly
            }
        } while (badInput);
        return inputDouble; // Returns the inputDouble as a double
    }

    /**
     * Tries to parse a String into a Double.
     *
     * @param num The string to parse.
     * @return A Double if parsing is successful; null otherwise.
     */
    public static Double attemptParseDouble(String num) {

        try {
            return Double.parseDouble(num); // Tries to parse input String as a double number
        } catch (Exception e) {
            return null; // Returns null if parse attempt fails
        }
    }

    /**
     * Prompts the user until a non-empty string is entered.
     *
     * @return A non-empty, trimmed string input by the user.
     */
    public static String getValidString() {

        // Initializes String called string, set to null
        String string;

        // Uses do/while loop with a boolean badInput to get a non-empty String from user
        boolean badInput = false;
        do {
            // Sets badInput to false first, to ensure loop doesn't run continuously
            badInput = false;
            string = input.nextLine().trim(); // Accepts next user input as a String, trimming it

            if (string.isEmpty()) {
                System.out.println("You have not entered anything, please try again.");
                badInput = true; // Sets badInput to true if input is empty after trimming
            }
        } while (badInput);

        return string; // Returns string after input is confirmed to be non-empty
    }

}
