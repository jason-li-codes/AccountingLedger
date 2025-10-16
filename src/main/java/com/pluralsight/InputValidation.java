package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static com.pluralsight.LedgerApp.input;

public class InputValidation {

    public static LocalDate getValidDate() {

        // initializes LocalDate and boolean badInput
        LocalDate inputDate = null;

        boolean badInput = false;
        // uses do/while loop
        do {
            // sets badInput to false first
            badInput = false;
            String userInputDate = getValidString();
            // if input contains letter N at the start, assume LocalDate is now
            if (Character.toUpperCase(userInputDate.charAt(0)) == 'N') {
                inputDate = LocalDate.now();
                System.out.println("Date set as current date, " + inputDate);
                return inputDate;
            } else {
                inputDate = validateDate(userInputDate);
                if (inputDate == null) {
                    System.out.println("You have not entered a valid date, please try again.");
                    badInput = true;
                }
            }
            // conditional checks badInput boolean
        } while (badInput);

        return inputDate;
    }

    public static LocalDate validateDate(String date) {

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
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // tries the next format
            }
        }
        return null;
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
                inputTime = LocalTime.now().withNano(0);
                System.out.println("Time set as current time, " + inputTime);
                return inputTime;
            } else {
                inputTime = validateTime(userInputTime);
                if (inputTime == null) {
                    System.out.println("You have not entered a valid date, please try again.");
                    badInput = true;
                }
            }
            // conditional checks badInput boolean
        } while (badInput);

        return inputTime;
    }

    public static LocalTime validateTime(String time) {

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
                return LocalTime.parse(time, formatter);
            } catch (DateTimeParseException e) {
                // tries the next format
            }
        }
        return null;
    }

    public static double getValidDouble() {

        // initializes inputDouble and boolean badInput
        String userInputDouble = null;
        Double inputDouble = null;
        boolean badInput = false;

        // uses do/while loop
        do {
            // sets badInput to false first
            badInput = false;

            //tries to get a double
            userInputDouble = input.nextLine();
            inputDouble = validateDouble(userInputDouble);
            // rounds it to have 2 decimal points
            if (inputDouble != null) {
                inputDouble = Math.round(inputDouble * 100) / 100.0;
                return inputDouble;
            } else {
                System.out.println("Sorry I don't know what you mean, please try again.");
                badInput = true;
            }
            // conditional checks badInput boolean
        } while (badInput);

        // returns the correct inputDouble as a double
        return inputDouble;
    }

    public static Double validateDouble(String num) {

        try {
            return Double.parseDouble(num);
        } catch (Exception e) {
            return null;
        }
    }


    public static String getValidString() {

        String string;
        boolean badInput = false;

        do {
            badInput = false;
            string = input.nextLine().trim();

            if (string.isEmpty()) {
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
