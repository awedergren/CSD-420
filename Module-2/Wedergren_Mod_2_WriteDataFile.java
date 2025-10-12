// Amanda Wedergren
// August 18, 2025
// Module 2.2 Assignment

// This program demonstrates basic file output in Java. It does the following:
// 1) Generates an array of five random integers and an array of five random doubles.
// 2) Writes these values to a text file named "AmandaWedergrendatafile.dat".
//    - If the file does not exist it will be created.
//    - If the file exists the program appends the new data to the end of the file.
// 3) Uses try-with-resources to ensure the FileWriter is always closed cleanly.

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class WriteDataFile {

    /**
     * Entry point for the program. No command-line arguments are required.
     * The method generates random values, writes them to a file, and prints
     * a confirmation message or an error message if something goes wrong.
     */
    public static void main(String[] args) {
        // We will store five integers and five doubles as required by the assignment.
        int[] intArray = new int[5];
        double[] doubleArray = new double[5];

        // java.util.Random is used to produce pseudo-random numbers.
        // In production or tests you may want to seed the generator to get repeatable results.
        Random rand = new Random();

        // ---------------------------------------------------------------------
        // Populate arrays with random values
        // - For integers: use nextInt(100) to get values from 0 (inclusive) to 100 (exclusive).
        // - For doubles: use nextDouble() which returns a value in [0.0, 1.0) and scale it.
        // ---------------------------------------------------------------------
        for (int i = 0; i < 5; i++) {
            // Random integer between 0 and 99
            intArray[i] = rand.nextInt(100);
            // Random double between 0.0 and 100.0
            doubleArray[i] = rand.nextDouble() * 100.0;
        }

        // ---------------------------------------------------------------------
        // Prepare to write data to disk.
        // FileWriter is constructed with the 'append' flag set to true so the
        // program will add to the file if it already exists rather than overwriting it.
        // We use try-with-resources to ensure the FileWriter is closed automatically
        // when the try block completes (including when an exception is thrown).
        // ---------------------------------------------------------------------
        try (FileWriter fw = new FileWriter("AmandaWedergrendatafile.dat", true)) {

            // Write a label for integers
            fw.write("Integers: ");

            // Write each integer separated by commas, add a newline after the last
            for (int i = 0; i < intArray.length; i++) {
                fw.write(Integer.toString(intArray[i]));
                if (i < intArray.length - 1) {
                    // add comma and space between values
                    fw.write(", ");
                } else {
                    // final value: terminate the line
                    fw.write(System.lineSeparator());
                }
            }

            // Write a label for doubles
            fw.write("Doubles: ");

            // Format doubles to two decimal places for readability and write them
            for (int i = 0; i < doubleArray.length; i++) {
                // String.format is used to control decimal places ("%.2f" -> two decimals)
                fw.write(String.format("%.2f", doubleArray[i]));
                if (i < doubleArray.length - 1) {
                    fw.write(", ");
                } else {
                    fw.write(System.lineSeparator());
                }
            }

            // Write a separator so multiple program runs are easy to distinguish
            fw.write("---" + System.lineSeparator());

            // Inform the user that the write completed successfully
            System.out.println("Data written to file: AmandaWedergrendatafile.dat");

        } catch (IOException e) {
            // Any IOException (file permission, disk full, etc.) will be caught here.
            // Print a brief message to help the user diagnose the problem.
            System.err.println("An error occurred while writing the file: " + e.getMessage());
            // For debugging you might also print the full stack trace:
            // e.printStackTrace();
        }
    }
}
