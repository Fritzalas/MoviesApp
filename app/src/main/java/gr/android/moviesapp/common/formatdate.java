package gr.android.moviesapp.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class formatdate {
    public static String convertDateFormat(String inputDate) {
        // Define the input date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Define the output date format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        try {
            // Parse the input date string to a LocalDate object
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);

            // Format the LocalDate object to the desired output format
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Handle the case where the input date string is not in the expected format
            System.err.println("Invalid date format: " + inputDate);
            return null;
        }
    }
}
