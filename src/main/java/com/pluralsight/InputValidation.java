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
            inputDate = validateDate(userInputDate);
            if (inputDate == null) {
                System.out.println("You have not entered a valid date, please try again.");
                badInput = true;
            }
        } while(badInput);

        return inputDate;
}

public static LocalDate validateDate(String date) {

    if (Character.toUpperCase(date.charAt(0)) == 'N') {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Date set as current date, " + currentDate);
        return currentDate;
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
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // tries the next format
            }
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
        inputTime = validateTime(userInputTime);
        if (inputTime == null) {
            System.out.println("You have not entered a valid time, please try again.");
            badInput = true;
        }
        // conditional checks badInput boolean
    } while (badInput);

    return inputTime;
}

public static LocalTime validateTime(String time) {

    if (Character.toUpperCase(time.charAt(0)) == 'N') {
        LocalTime currentTime = LocalTime.now();
        System.out.println("Time set as current time, " + currentTime);
        return currentTime;
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
                return LocalTime.parse(time, formatter);
            } catch (DateTimeParseException e) {
                // tries the next format
            }
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

}
