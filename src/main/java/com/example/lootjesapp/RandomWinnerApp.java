package com.example.lootjesapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomWinnerApp extends Application {
    private List<String> entries = new ArrayList<>();
    private ListView<String> entryListView;

    @Override
    public void start(Stage primaryStage) {
        loadEntriesFromFile();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Text title = new Text("Entry Input");
        title.setFont(Font.font(30));

        Text title2 = new Text("List of Entries");
        title2.setFont(Font.font(30));

        Region spacer1 = new Region();
        spacer1.setPrefHeight(40);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setStyle("-fx-pref-width: 200px;");

        TextField numEntriesField = new TextField();
        numEntriesField.setPromptText("Number of Entries");
        numEntriesField.setStyle("-fx-pref-width: 200px;");

        // Create a TextFormatter that filters out non-numeric characters
        TextFormatter<String> numEntriesFormatter = new TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*")) {
                return change; // Accept the change if it consists of only numeric characters
            } else {
                return null; // Reject the change if it contains non-numeric characters
            }
        });

        // Apply the TextFormatter to the numEntriesField
        numEntriesField.setTextFormatter(numEntriesFormatter);

        Button addEntryButton = new Button("Add Entry");
        addEntryButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addEntryButton.setOnAction(e -> {
            String name = nameField.getText();
            String numEntries = numEntriesField.getText();
            if (!name.isEmpty() && !numEntries.isEmpty()) {
                entries.add(name + " - " + numEntries);
                updateEntryList();
                saveEntriesToFile();
                nameField.clear();
                numEntriesField.clear();
            } else {
                showAlert("Please enter both name and number of entries.");
            }
        });

        Button removeEntryButton = new Button("Remove Entry");
        removeEntryButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        removeEntryButton.setOnAction(e -> {
            String selectedEntry = entryListView.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                showRemoveConfirmation(selectedEntry);
            } else {
                showAlert("Please select an entry to remove.");
            }
        });

        entryListView = new ListView<>();
        updateEntryList();

        root.getChildren().addAll(title, nameField, numEntriesField, addEntryButton, spacer1, title2, entryListView, removeEntryButton);

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("Random Winner App");
        primaryStage.show();
    }

    private void loadEntriesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("entries.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                entries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEntriesToFile() {
        try (FileWriter writer = new FileWriter("entries.txt")) {
            for (String entry : entries) {
                writer.write(entry + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEntryList() {
        entryListView.getItems().setAll(entries);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showRemoveConfirmation(String entry) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Removal");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this entry?\n\n" + entry);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            entries.remove(entry);
            updateEntryList();
            saveEntriesToFile();
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
