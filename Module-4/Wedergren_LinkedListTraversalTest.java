// Amanda Wedergren
// August 27, 2025
// Module 4.2 Assignment

// Write a Java program that demonstrates the difference in performance between
// traversing a LinkedList using an iterator versus using get(index).

import java.util.LinkedList;
import java.util.Iterator;

public class LinkedListTraversalTest {

    // Fills a LinkedList with integers from 0 to n-1
    // This method creates a new LinkedList and adds integers sequentially.
    public static LinkedList<Integer> fillList(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            list.add(i); // Add each integer to the end of the list
        }
        return list;
    }

    // Traverses the list using an iterator and returns the sum of all elements
    // This is the efficient way to traverse a LinkedList (O(n) time)
    public static long traverseWithIterator(LinkedList<Integer> list) {
        long sum = 0;
        Iterator<Integer> it = list.iterator(); // Get an iterator for the list
        while (it.hasNext()) {
            sum += it.next(); // Access next element and add to sum
        }
        return sum;
    }

    // Traverses the list using get(index) and returns the sum of all elements
    // This is an inefficient way for LinkedList (O(n^2) time)
    public static long traverseWithGet(LinkedList<Integer> list) {
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i); // get(i) is O(i) for LinkedList, so this is slow
        }
        return sum;
    }

    // Runs the test for a given list size
    // Measures and prints the time for both traversal methods, and checks correctness
    public static void runTest(int n) {
        System.out.println("Testing with " + n + " elements:");
        LinkedList<Integer> list = fillList(n); // Fill the list with n integers

        // Test iterator traversal
        long start = System.nanoTime(); // Record start time
        long sumIterator = traverseWithIterator(list); // Traverse with iterator
        long timeIterator = System.nanoTime() - start; // Calculate elapsed time
        System.out.println("Iterator traversal time: " + timeIterator / 1_000_000.0 + " ms");

        // Test get(index) traversal
        start = System.nanoTime(); // Record start time
        long sumGet = traverseWithGet(list); // Traverse with get(index)
        long timeGet = System.nanoTime() - start; // Calculate elapsed time
        System.out.println("get(index) traversal time: " + timeGet / 1_000_000.0 + " ms");

        // Test correctness: both methods should produce the same sum
        if (sumIterator != sumGet) {
            System.out.println("ERROR: Sums do not match!");
        } else {
            System.out.println("Sums match: " + sumIterator);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Run the test with 50,000 elements
        runTest(50_000);
        // Run the test with 500,000 elements
        runTest(500_000);

        /*
         * Results and Discussion:
         * 
         * Traversing a LinkedList using an iterator is much faster than using get(index).
         * This is because get(index) in a LinkedList is O(n) for each access, so traversing
         * the whole list with get(index) is O(n^2). In contrast, using an iterator is O(n)
         * because it simply follows the next pointers.
         * 
         * For 50,000 elements, iterator traversal is very fast, while get(index) is much slower.
         * For 500,000 elements, the difference is even more dramatic: get(index) traversal
         * can take orders of magnitude longer than iterator traversal.
         * 
         * This demonstrates why you should always use an iterator (or enhanced for-loop)
         * to traverse a LinkedList, never get(index).
         */
    }
}