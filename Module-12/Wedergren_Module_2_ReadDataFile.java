// Amanda Wedergren
// August 18, 2025
// Module 2.2 Assignment

// This program complements the WriteDataFile program. It opens the text file
// created by the writer program (AmandaWedergrendatafile.dat), reads it line by
// line, and prints each line to standard output. The file is opened using a
// BufferedReader wrapped around a FileReader for efficient line-based reading.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadDataFile {

    /**
     * Main method: reads the contents of AmandaWedergrendatafile.dat and prints
     * each line to the console. Uses try-with-resources to ensure the reader
     * is closed automatically.
     *
     * Notes on behavior:
     * - If the file does not exist, a FileNotFoundException (subclass of IOException)
     *   will be thrown and caught in the catch block below. We print a helpful
     *   error message rather than letting the exception propagate.
     * - try-with-resources guarantees that br.close() is called even if an
     *   exception occurs while reading.
     */
    public static void main(String[] args) {
        // The filename is the same one used by the writer program. If you move
        // the file to another directory, provide the correct relative or
        // absolute path here.
        String filename = "AmandaWedergrendatafile.dat";

        // Use try-with-resources so the BufferedReader is closed automatically.
        // BufferedReader provides the convenient readLine() method which reads
        // a line of text terminated by a newline, returning null at EOF.
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // Read until readLine() returns null (end-of-file).
            // Each non-null line is printed to the console exactly as it appears
            // in the file (including any labels added by the writer program).
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            // If an I/O error occurs (file not found, permission denied, etc.)
            // print a short, human-friendly message to stderr so the user can
            // diagnose the problem. In a more advanced program you might log the
            // full stack trace or recover from certain kinds of errors.
            System.err.println("An error occurred while reading '" + filename + "': " + e.getMessage());
        }
    }
}
