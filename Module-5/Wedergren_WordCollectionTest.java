// Amanda Wedergren
// September 2, 2025
// Module 5.2 Assignment

// Write a Java program that reads a collection of words from a text file,
// removes duplicates, and displays the non-duplicate words in both ascending
// and descending order. Include test code to ensure correctness.

import java.io.*;
import java.util.*;

public class Wedergren_WordCollectionTest {

    // Reads words from the specified file and returns a Set of unique words
    // Uses a HashSet to automatically filter out duplicate words
    public static Set<String> readWordsFromFile(String filename) {
        Set<String> words = new HashSet<>(); // HashSet to store unique words
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                String word = line.trim(); // Remove leading/trailing whitespace
                if (!word.isEmpty()) { // Ignore empty lines
                    words.add(word); // Add word to set (duplicates ignored)
                }
            }
        } catch (IOException e) {
            // If there is an error reading the file, print an error message
            System.out.println("Error reading file: " + e.getMessage());
        }
        // Return the set of unique words
        return words;
    }

    // Displays the words in ascending (alphabetical) order
    // Converts the set to a list, sorts it, and prints each word
    public static void displayAscending(Set<String> words) {
        List<String> sorted = new ArrayList<>(words); // Convert set to list
        Collections.sort(sorted); // Sort list in ascending order
        System.out.println("Non-duplicate words in ascending order:");
        for (String word : sorted) {
            System.out.println(word); // Print each word
        }
    System.out.println(); // Blank line after ascending order output
    }

    // Displays the words in descending (reverse alphabetical) order
    // Converts the set to a list, sorts it in reverse, and prints each word
    public static void displayDescending(Set<String> words) {
        List<String> sorted = new ArrayList<>(words); // Convert set to list
        Collections.sort(sorted, Collections.reverseOrder()); // Sort list in descending order
        System.out.println("Non-duplicate words in descending order:");
        for (String word : sorted) {
            System.out.println(word); // Print each word
        }
    }

    // Test code to ensure correctness of the program
    // Checks for duplicates and verifies sorting order
    public static void runTests(Set<String> words) {
        // Test: No duplicates in the set
        List<String> wordList = new ArrayList<>(words); // Convert set to list
        Set<String> testSet = new HashSet<>(wordList); // Create a new set from the list
        if (wordList.size() != testSet.size()) {
            // If sizes differ, there are duplicates
            System.out.println("Test failed: Duplicates found in set!");
        } else {
            System.out.println("Test passed: No duplicates in set.");
        }

        // Test: Ascending order is correct
        List<String> asc = new ArrayList<>(words);
        Collections.sort(asc); // Sort in ascending order
        for (int i = 1; i < asc.size(); i++) {
            // Check that each word is less than or equal to the next
            if (asc.get(i-1).compareTo(asc.get(i)) > 0) {
                System.out.println("Test failed: Ascending order incorrect!");
                return;
            }
        }
        System.out.println("Test passed: Ascending order correct.");

        // Test: Descending order is correct
        List<String> desc = new ArrayList<>(words);
        Collections.sort(desc, Collections.reverseOrder()); // Sort in descending order
        for (int i = 1; i < desc.size(); i++) {
            // Check that each word is greater than or equal to the next
            if (desc.get(i-1).compareTo(desc.get(i)) < 0) {
                System.out.println("Test failed: Descending order incorrect!");
                return;
            }
        }
        System.out.println("Test passed: Descending order correct.");
    }

    public static void main(String[] args) {
        // Read words from the file (file must be in the same directory as the program)
        Set<String> words = readWordsFromFile("collection_of_words.txt");

        // Display the unique words in ascending order
        displayAscending(words);

        // Display the unique words in descending order
        displayDescending(words);

    // Print a blank line before test results
    System.out.println();
        // Run tests to ensure the program works correctly
        runTests(words);
    }
}