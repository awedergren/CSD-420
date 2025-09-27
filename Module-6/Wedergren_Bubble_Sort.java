// Amanda Wedergren
// September 10, 2025
// Module 6.2 Assignment


 // This class demonstrates two generic bubble sort methods:
 // 1. One that sorts using the Comparable interface.
 // 2. One that sorts using a Comparator.
 // Includes test code to verify correctness.
 
import java.util.Comparator;
import java.util.Arrays;


public class Bubble_Sort {

    /**
     * Generic bubble sort using Comparable.
     * Sorts the array in ascending order.
     * @param <T> Type parameter that extends Comparable
     * @param array The array to be sorted
     */
    public static <T extends Comparable<T>> void bubbleSort(T[] array) {
        // Outer loop for each pass through the array
        for (int i = 0; i < array.length - 1; i++) {
            boolean swapped = false; // Track if any swaps occur in this pass
            // Inner loop compares adjacent elements
            for (int j = 0; j < array.length - 1 - i; j++) {
                // If the current element is greater than the next, swap them
                if (array[j].compareTo(array[j + 1]) > 0) {
                    // Swap elements
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true; // Mark that a swap occurred
                }
            }
            // If no swaps occurred, the array is already sorted
            if (!swapped) break;
        }
    }

    /**
     * Generic bubble sort using Comparator.
     * Sorts the array according to the provided comparator.
     * @param <T> Type parameter
     * @param array The array to be sorted
     * @param comp The comparator to determine order
     */
    public static <T> void bubbleSort(T[] array, Comparator<? super T> comp) {
        // Outer loop for each pass through the array
        for (int i = 0; i < array.length - 1; i++) {
            boolean swapped = false; // Track if any swaps occur in this pass
            // Inner loop compares adjacent elements
            for (int j = 0; j < array.length - 1 - i; j++) {
                // Use the comparator to determine order
                if (comp.compare(array[j], array[j + 1]) > 0) {
                    // Swap elements if out of order
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true; // Mark that a swap occurred
                }
            }
            // If no swaps occurred, the array is already sorted
            if (!swapped) break;
        }
    }

    /**
     * Utility method to check if an array is sorted in ascending order.
     * @param array The array to check
     * @param <T> Type parameter that extends Comparable
     * @return true if sorted ascending, false otherwise
     */
    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        // Loop through array and check if each element is <= the next
        for (int i = 1; i < array.length; i++) {
            if (array[i-1].compareTo(array[i]) > 0) return false;
        }
        return true;
    }

    /**
     * Utility method to check if an array is sorted in descending order.
     * @param array The array to check
     * @param <T> Type parameter that extends Comparable
     * @return true if sorted descending, false otherwise
     */
    public static <T extends Comparable<T>> boolean isSortedDescending(T[] array) {
        // Loop through array and check if each element is >= the next
        for (int i = 1; i < array.length; i++) {
            if (array[i-1].compareTo(array[i]) < 0) return false;
        }
        return true;
    }

    /**
     * Prints the array in a readable format.
     * @param array The array to print
     */
    public static <T> void printArray(T[] array) {
        System.out.print("{ ");
        for (T e : array) {
            System.out.print(e + " ");
        }
        System.out.println("}");
    }

    /**
     * Main method to test both bubble sort methods.
     */
    public static void main(String[] args) {
        // Test with Integer array using Comparable
        Integer[] intArray = {5, 3, 4, 9, 0, 1, 2, 7, 6, 8};
        System.out.println("Original Integer array:");
        printArray(intArray);

        // Sort using Comparable
        bubbleSort(intArray);
        System.out.println("Sorted with Comparable (ascending):");
        printArray(intArray);
        System.out.println("Is sorted: " + isSorted(intArray));

        // Test with String array using Comparable
        String[] strArray = {"pear", "apple", "orange", "banana"};
        System.out.println("\nOriginal String array:");
        printArray(strArray);

        // Sort using Comparable
        bubbleSort(strArray);
        System.out.println("Sorted with Comparable (ascending):");
        printArray(strArray);
        System.out.println("Is sorted: " + isSorted(strArray));

        // Test with Integer array using Comparator (descending order)
        Integer[] intArray2 = {3, 8, 2, 7, 4};
        System.out.println("\nOriginal Integer array:");
        printArray(intArray2);

        // Sort using Comparator for descending order
        bubbleSort(intArray2, Comparator.reverseOrder());
        System.out.println("Sorted with Comparator (descending):");
        printArray(intArray2);
        System.out.println("Is sorted descending: " + isSortedDescending(intArray2));

        // Test with custom objects using Comparator
        Person[] people = {
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        };
        System.out.println("\nOriginal Person array:");
        printArray(people);

        // Sort by age ascending using Comparator.comparingInt
        bubbleSort(people, Comparator.comparingInt(Person::getAge));
        System.out.println("Sorted by age (ascending):");
        printArray(people);

        // Sort by name descending using a lambda Comparator
        bubbleSort(people, (a, b) -> b.getName().compareTo(a.getName()));
        System.out.println("Sorted by name (descending):");
        printArray(people);
    }
}

/**
 * Simple Person class for testing custom object sorting.
 */
class Person {
    private String name; // Person's name
    private int age;     // Person's age

    // Constructor to initialize name and age
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter for name
    public String getName() { return name; }
    // Getter for age
    public int getAge() { return age; }

    // toString method for easy printing of Person objects
    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}