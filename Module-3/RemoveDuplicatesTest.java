// Amanda Wedergren
// August 27, 2025
// Module 3.2 Assignment

// Write a Java program that removes duplicates from an ArrayList of integers.

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RemoveDuplicatesTest {
    // This static method takes an ArrayList of Integers and returns a new ArrayList with duplicates removed
    public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
        // Convert the list to a HashSet to remove duplicates, then back to an ArrayList
        return new ArrayList<>(new HashSet<>(list));
    }

    public static void main(String[] args) {
        // Create an ArrayList to hold the original random values
        ArrayList<Integer> originalList = new ArrayList<>();
        // Create a Random object to generate random numbers
        Random rand = new Random();
        // Fill the originalList with 50 random integers between 1 and 20 (inclusive)
        for (int i = 0; i < 50; i++) {
            // rand.nextInt(20) generates a number from 0 to 19, so add 1 to get 1 to 20
            originalList.add(rand.nextInt(20) + 1);
        }
        // Print the original list (may contain duplicates)
        System.out.println("Original List: " + originalList);
        // Call removeDuplicates to get a new list with duplicates removed
        ArrayList<Integer> noDuplicates = removeDuplicates(originalList);
        // Print the list with no duplicates
        System.out.println("List with no duplicates: " + noDuplicates);
    }
}
