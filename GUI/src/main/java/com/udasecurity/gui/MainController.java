package com.udasecurity.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import com.udasecurity.security.Sensor;

public class MainController {

    @FXML private Label controlstatusLabel;
    @FXML private Label systemStatusLabel;
    @FXML private ImageView cat_placeholder;
    @FXML private Button refreshButton;
    @FXML private ComboBox<String> sensorTypeCombo;
    @FXML private TextField sensorNameField;
    @FXML private Button addSensorButton;
    @FXML private Button removeSensorButton;
    @FXML private TableView<Sensor> sensorTable;
    @FXML private TableColumn<Sensor, String> sensorNameColumn;
    @FXML private TableColumn<Sensor, String> sensorTypeColumn;

    private boolean systemArmed = false;
    private boolean showingCat = false;

    private final Image catImage = new Image(getClass().getResource("/images/cat_placeholder.png").toExternalForm());
    private final Image noCatImage = new Image(getClass().getResource("/images/no_cat_placeholder.png").toExternalForm());

    // ObservableList for TableView
    private final ObservableList<Sensor> sensors = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize with disarmed state
        updateSystemControlStatus(false);

        // Populate sensor type ComboBox
        sensorTypeCombo.getItems().addAll("Door", "Window", "Motion");
        sensorTypeCombo.setPromptText("Type");

        // Setup refresh button
        refreshButton.setOnAction(e -> refreshCamera());

        // Setup TableView columns
        sensorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        sensorTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        sensorTable.setItems(sensors);

        // Setup Add/Remove buttons
        addSensorButton.setOnAction(e -> addSensor());
        removeSensorButton.setOnAction(e -> removeSensor());
    }

    @FXML
    private void armSystem() {
        systemArmed = true;
        updateSystemControlStatus(true);
    }

    @FXML
    private void disarmSystem() {
        systemArmed = false;
        updateSystemControlStatus(false);
    }

    private void updateSystemControlStatus(boolean armed) {
        controlstatusLabel.setText("System Control Status: " + (armed ? "ARMED" : "DISARMED"));

        if (armed) {
            controlstatusLabel.setTextFill(Color.GREEN);
            refreshButton.setDisable(false);
        } else {
            controlstatusLabel.setTextFill(Color.RED);
            refreshButton.setDisable(true);
            showAlert("Disarmed!");
            systemStatusLabel.setText("System Status: Not available");
            systemStatusLabel.setTextFill(Color.GRAY);
            cat_placeholder.setImage(noCatImage);
        }
    }

    private void refreshCamera() {
        if (!systemArmed) {
            showAlert("Disarmed!");
            return;
        }

        showingCat = !showingCat;

        if (showingCat) {
            cat_placeholder.setImage(catImage);
            systemStatusLabel.setText("System Status: Danger – Cat Detected!");
            systemStatusLabel.setTextFill(Color.RED);
            showAlert("Cat is detected!");
        } else {
            cat_placeholder.setImage(noCatImage);
            systemStatusLabel.setText("System Status: Cool and Good");
            systemStatusLabel.setTextFill(Color.GREEN);
            showAlert("Everything is fine!");
        }
    }

    private void addSensor() {
        String name = sensorNameField.getText().trim();
        String type = sensorTypeCombo.getValue();

        if (name.isEmpty() || type == null) {
            showAlert("Please enter sensor name and select a type.");
            return;
        }

        sensors.add(new Sensor(name, type));
        sensorNameField.clear();
        sensorTypeCombo.getSelectionModel().clearSelection();
    }

    private void removeSensor() {
        Sensor selected = sensorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sensors.remove(selected);
        } else {
            showAlert("Please select a sensor to remove.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
