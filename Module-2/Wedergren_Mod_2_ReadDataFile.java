// Amanda Wedergren
// August 18, 2025
// Module 2.2 Assignment

// Write a second program that will read the file and display the data from the AmandaWedergrendatafile.dat.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadDataFile {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("AmandaWedergrendatafile.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
