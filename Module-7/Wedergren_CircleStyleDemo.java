// Amanda Wedergren
// September 17, 2025
// Module 7.2 Assignment

// JavaFX program that displays four circles styled with external CSS

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class Wedergren_CircleStyleDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create four circles with radius 50
        Circle circle1 = new Circle(50); // First circle
        Circle circle2 = new Circle(50); // Second circle
        Circle circle3 = new Circle(50); // Third circle
        Circle circle4 = new Circle(50); // Fourth circle

        // Apply the CSS class to the first two circles (white fill, black stroke)
        circle1.getStyleClass().add("white-black");
        circle2.getStyleClass().add("white-black");

        // Apply the CSS IDs to the last two circles (red and green fill)
        circle3.setId("red-fill");
        circle4.setId("green-fill");

        // Place circles in an HBox for horizontal layout, with spacing between them
        HBox hbox = new HBox(20, circle1, circle2, circle3, circle4);
        hbox.setStyle("-fx-padding: 30; -fx-alignment: center;"); // Add padding and center alignment

        // Create the scene and attach the external CSS stylesheet
        Scene scene = new Scene(hbox, 400, 150);
        // Attach the CSS file (must be in the same directory or resource path)
        scene.getStylesheets().add("Wedergren_mystyle.css");

        // Set up the stage (window)
        primaryStage.setTitle("Circle Style Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Main method to launch the JavaFX application.
    // Test code: If the window displays four circles with correct colors and strokes, the code works.
     
    public static void main(String[] args) {
        launch(args);
    }
}