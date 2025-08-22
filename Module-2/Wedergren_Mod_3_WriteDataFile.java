// Amanda Wedergren
// August 18, 2025
// Module 2.2 Assignment

// Write a program that stores: An array of five random integers 
// and an array of five random double values. Write the data in a file named
// AmandaWedergrendatafile.dat. If there is no file, the file will be created.
// If there is a file, the data will be appended.


import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class WriteDataFile {
    public static void main(String[] args) {
        int[] intArray = new int[5];
        double[] doubleArray = new double[5];
        Random rand = new Random();

        // Fill arrays with random values
        for (int i = 0; i < 5; i++) {
            intArray[i] = rand.nextInt(100); // random int 0-99
            doubleArray[i] = rand.nextDouble() * 100; // random double 0-100
        }

        try (FileWriter fw = new FileWriter("AmandaWedergrendatafile.dat", true)) {
            fw.write("Integers: ");
            for (int i = 0; i < 5; i++) {
                fw.write(intArray[i] + (i < 4 ? ", " : "\n"));
            }
            fw.write("Doubles: ");
            for (int i = 0; i < 5; i++) {
                fw.write(String.format("%.2f", doubleArray[i]) + (i < 4 ? ", " : "\n"));
            }
            fw.write("---\n");
            System.out.println("Data written to file.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
