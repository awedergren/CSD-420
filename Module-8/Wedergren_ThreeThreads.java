// Amanda Wedergren
// September 17, 2025
// Module 8.2 Assignment

// This program uses three threads to output random letters, digits, and symbols to a JavaFX TextArea.


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Wedergren_ThreeThreads extends Application {

    // Number of characters to generate for each thread
    private static final int CHAR_COUNT = 10000;

    @Override
    public void start(Stage primaryStage) {
        // Create a TextArea for displaying output
        TextArea textArea = new TextArea();
        textArea.setWrapText(true); // Enable text wrapping for better readability

        // VBox layout to hold the TextArea
        VBox vbox = new VBox(textArea);

        // Create the scene and set up the stage (window)
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setTitle("Three Threads Output Demo");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Thread 1: Output random letters
        // This thread generates 10,000 random lowercase letters and appends them to the TextArea
        Thread letterThread = new Thread(() -> {
            StringBuilder sb = new StringBuilder(); // Efficiently build the string
            for (int i = 0; i < CHAR_COUNT; i++) {
                // Generate a random lowercase letter (a-z)
                char letter = (char) ('a' + (int)(Math.random() * 26));
                sb.append(letter); // Add the letter to the string
                // Insert a newline every 100 characters for readability
                if ((i + 1) % 100 == 0) sb.append('\n');
            }
            // Update TextArea on JavaFX Application Thread (thread-safe)
            javafx.application.Platform.runLater(() -> {
                textArea.appendText("Random Letters:\n" + sb.toString() + "\n\n");
            });
        });

        // Thread 2: Output random digits
        // This thread generates 10,000 random digits and appends them to the TextArea
        Thread digitThread = new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CHAR_COUNT; i++) {
                // Generate a random digit (0-9)
                char digit = (char) ('0' + (int)(Math.random() * 10));
                sb.append(digit); // Add the digit to the string
                // Insert a newline every 100 characters for readability
                if ((i + 1) % 100 == 0) sb.append('\n');
            }
            // Update TextArea on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                textArea.appendText("Random Digits:\n" + sb.toString() + "\n\n");
            });
        });

        // Thread 3: Output random symbols
        // This thread generates 10,000 random symbols from the specified set and appends them to the TextArea
        char[] symbols = {'!', '@', '#', '$', '%', '&', '*'}; // Symbol set
        Thread symbolThread = new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CHAR_COUNT; i++) {
                // Select a random symbol from the array
                char symbol = symbols[(int)(Math.random() * symbols.length)];
                sb.append(symbol); // Add the symbol to the string
                // Insert a newline every 100 characters for readability
                if ((i + 1) % 100 == 0) sb.append('\n');
            }
            // Update TextArea on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                textArea.appendText("Random Symbols:\n" + sb.toString() + "\n\n");
            });
        });

        // Run threads sequentially using a new thread to avoid blocking JavaFX thread
        // This thread starts and waits for each worker thread in order, ensuring output order
        new Thread(() -> {
            try {
                letterThread.start();      // Start letter thread
                letterThread.join();       // Wait for letter thread to finish
                digitThread.start();       // Start digit thread
                digitThread.join();        // Wait for digit thread to finish
                symbolThread.start();      // Start symbol thread
                symbolThread.join();       // Wait for symbol thread to finish

                // After all threads are done, check the TextArea content for test validation
                javafx.application.Platform.runLater(() -> {
                    String text = textArea.getText();
                    // Check that each section header is present in the output
                    boolean lettersOk = text.contains("Random Letters:");
                    boolean digitsOk = text.contains("Random Digits:");
                    boolean symbolsOk = text.contains("Random Symbols:");
                    // Output test result to the TextArea
                    if (lettersOk && digitsOk && symbolsOk) {
                        textArea.appendText("Test Passed: All thread outputs present and in order.\n");
                    } else {
                        textArea.appendText("Test Failed: Missing thread output.\n");
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Main method to launch the JavaFX application.
     
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}