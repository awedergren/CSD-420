// Amanda Wedergren
// August 15, 2025
// Module 1.2 Assignment

// Write a JavaFX program that displays four images randomly selected from a deck of 52 cards.


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDisplayApp extends Application {
    private static final int CARD_COUNT = 52;
    private static final int DISPLAY_COUNT = 4;
    private static final String CARD_PATH = "AssignmentCards/";

    private final ImageView[] cardViews = new ImageView[DISPLAY_COUNT];

    @Override
    public void start(Stage primaryStage) {
        HBox cardBox = new HBox(10);
        for (int i = 0; i < DISPLAY_COUNT; i++) {
            cardViews[i] = new ImageView();
            cardViews[i].setFitWidth(100);
            cardViews[i].setFitHeight(150);
            cardBox.getChildren().add(cardViews[i]);
        }

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> showRandomCards()); // Lambda expression

        VBox root = new VBox(10, cardBox, refreshButton);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        showRandomCards();

        primaryStage.setTitle("Random Card Display");
        primaryStage.setScene(new Scene(root, 480, 260));
        primaryStage.show();
    }

    private void showRandomCards() {
        // Create a list of card numbers and shuffle
        List<Integer> cards = new ArrayList<>();
        for (int i = 1; i <= CARD_COUNT; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);

        for (int i = 0; i < DISPLAY_COUNT; i++) {
            int cardNum = cards.get(i);
            try {
                java.nio.file.Path imagePath = java.nio.file.Paths.get(CARD_PATH + cardNum + ".png");
                String uri = imagePath.toAbsolutePath().toUri().toString();
                Image img = new Image(uri, 100, 150, true, true);
                if (img.isError()) {
                    throw new Exception("Image not found: " + uri);
                }
                cardViews[i].setImage(img);
            } catch (Exception ex) {
                // If image is missing, set to null or a placeholder
                cardViews[i].setImage(null);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}