package com.example.lootjesapp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiveawayApp extends Application {

    private List<String> entries = new ArrayList<>();

    private Button startButton;
    private MediaView mediaView;
    private MediaView mediaView2;
    private StackPane root;
    private Text winnerText;

    @Override
    public void start(Stage primaryStage) {
        // Create the start button
        startButton = new Button("Start Giveaway");
        startButton.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setOnAction(event -> startGiveaway());

        // Create the media player and media view
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("countdown.mp4").toURI().toString()));
        mediaView = new MediaView(mediaPlayer);
        mediaView.setVisible(false);

        MediaPlayer mediaPlayer2 = new MediaPlayer(new Media(new File("fireworks.mp4").toURI().toString()));
        mediaView2 = new MediaView(mediaPlayer2);
        mediaView2.setVisible(false);

        loadEntriesFromFile();

        String winner = selectRandomWinner();
        winnerText = new Text(winner);
        winnerText.setFill(Color.WHITE);
        winnerText.setStyle("-fx-font-size: 120px;");
        winnerText.setVisible(false);

        // Create the root layout
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().addAll(mediaView, startButton, mediaView2, winnerText);

        // Create the scene
        Scene scene = new Scene(root, 800, 600);

        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Giveaway App");
        primaryStage.show();
    }

    private void startGiveaway() {
        // Play the video
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        mediaPlayer.play();

        // Hide the button
        startButton.setVisible(false);

        // Make the mediaView visible and play the video in full-screen mode
        mediaView.setVisible(true);
        mediaView.setFitWidth(root.getWidth());
        mediaView.setFitHeight(root.getHeight());

        // Show the text after the video has finished playing
        mediaPlayer.setOnEndOfMedia(() -> {
            winnerText.setVisible(true);

            MediaPlayer mediaPlayer2 = mediaView2.getMediaPlayer();
            mediaPlayer2.play();

            mediaView2.setVisible(true);
            mediaView2.setFitWidth(root.getWidth());
            mediaView2.setFitHeight(root.getHeight());
        });
    }

    private void loadEntriesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("entries.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int numberOfEntries = Integer.parseInt(parts[1].trim());
                    for (int i = 0; i < numberOfEntries; i++) {
                        entries.add(name);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String selectRandomWinner() {
        if (entries.isEmpty()) {
            return "No entries";
        }
        int index = (int) (Math.random() * entries.size());
        return entries.get(index);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
