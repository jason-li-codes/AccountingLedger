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
                // rounds it to have 2 decimal points
                inputDouble = Math.round(inputDouble * 100) / 100.0;
                System.out.printf("Your input has been rounded to %f.", inputDouble);
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
