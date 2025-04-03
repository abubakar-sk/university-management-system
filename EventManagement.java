package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

public class EventManagement extends Application {
    private boolean isAdmin;
    private ObservableList<Event> events = FXCollections.observableArrayList();
    private String currentUser;

    public EventManagement(boolean isAdmin, String currentUser) {
        this.isAdmin = isAdmin;
        this.currentUser = currentUser;
    }

    public EventManagement(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public EventManagement(){
        this.isAdmin = false;
    }

    @Override
    public void start(Stage stage) {
        // Load events from the data file
        loadEventsFromData();

        if (isAdmin) {
            showAdminPanel(stage);
        } else {
            showUserPanel(stage);
        }
    }

    // Load events from the Excel data (converted to text format)
    private void loadEventsFromData() {
        // Sample data - in a real application, this would come from parsing the Excel file
        events.add(new Event("EV001", "Welcome Seminar", "Orientation week", "Auditorium",
                LocalDateTime.parse("2025-09-01T10:00"), 100, "Free", "default",
                "Alice Smith, Bob Johnson, Jennifer Davis, Helen Jones"));

        events.add(new Event("EV002", "Research Workshop", "Graduate workshop", "Lab 301",
                LocalDateTime.parse("2025-10-05T14:00"), 50, "Paid ($20)", "default",
                "Alice Smith, Bob Johnson, Lucka Racki, Helen Jones, David Lee"));
    }

    // Admin Panel with all features
    private void showAdminPanel(Stage stage) {
        Label titleLabel = new Label("Event Management (Admin)");
        titleLabel.setFont(new Font(18));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Create tabs for different functionalities
        TabPane tabPane = new TabPane();

        // Tab 1: Add/Edit Events
        Tab manageEventsTab = new Tab("Manage Events");
        manageEventsTab.setClosable(false);
        manageEventsTab.setContent(createManageEventsPanel());

        // Tab 2: View All Events
        Tab viewEventsTab = new Tab("View Events");
        viewEventsTab.setClosable(false);
        viewEventsTab.setContent(createViewEventsPanel(true));

        // Tab 3: Manage Registrations
        Tab manageRegistrationsTab = new Tab("Manage Registrations");
        manageRegistrationsTab.setClosable(false);
        manageRegistrationsTab.setContent(createManageRegistrationsPanel());

        tabPane.getTabs().addAll(manageEventsTab, viewEventsTab, manageRegistrationsTab);

        VBox layout = new VBox(10, titleLabel, tabPane);
        layout.setPadding(new Insets(10));
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Event Management - Admin");
        stage.show();
    }

    private GridPane createManageEventsPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Event Code
        Label codeLabel = new Label("Event Code:");
        TextField codeField = new TextField();
        codeField.setPromptText("EVXXX");
        grid.add(codeLabel, 0, 0);
        grid.add(codeField, 1, 0);

        // Event Name
        Label nameLabel = new Label("Event Name:");
        TextField nameField = new TextField();
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);

        // Description
        Label descLabel = new Label("Description:");
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        grid.add(descLabel, 0, 2);
        grid.add(descArea, 1, 2);

        // Location
        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();
        grid.add(locationLabel, 0, 3);
        grid.add(locationField, 1, 3);

        // Date and Time
        Label dateLabel = new Label("Date and Time:");
        TextField dateField = new TextField();
        dateField.setPromptText("YYYY-MM-DDTHH:MM");
        grid.add(dateLabel, 0, 4);
        grid.add(dateField, 1, 4);

        // Capacity
        Label capacityLabel = new Label("Capacity:");
        TextField capacityField = new TextField();
        grid.add(capacityLabel, 0, 5);
        grid.add(capacityField, 1, 5);

        // Cost
        Label costLabel = new Label("Cost:");
        TextField costField = new TextField();
        costField.setPromptText("Free or Paid ($XX)");
        grid.add(costLabel, 0, 6);
        grid.add(costField, 1, 6);

        // Header Image
        Label imageLabel = new Label("Header Image:");
        Button uploadImageBtn = new Button("Upload Image");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        HBox imageBox = new HBox(10, uploadImageBtn, imageView);
        grid.add(imageLabel, 0, 7);
        grid.add(imageBox, 1, 7);

        // Buttons
        Button addButton = new Button("Add Event");
        Button updateButton = new Button("Update Event");
        Button clearButton = new Button("Clear");
        HBox buttonBox = new HBox(10, addButton, updateButton, clearButton);
        grid.add(buttonBox, 1, 8);

        // Event List
        ListView<Event> eventListView = new ListView<>(events);
        eventListView.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getEventCode() + " - " + item.getEventName() + " (" +
                            item.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")) + ")");
                }
            }
        });
        grid.add(eventListView, 2, 0, 1, 9);

        // Button actions
        uploadImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Header Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                imageView.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        addButton.setOnAction(e -> {
            try {
                Event newEvent = new Event(
                        codeField.getText(),
                        nameField.getText(),
                        descArea.getText(),
                        locationField.getText(),
                        LocalDateTime.parse(dateField.getText()),
                        Integer.parseInt(capacityField.getText()),
                        costField.getText(),
                        "custom", // Will be replaced with actual image path
                        ""
                );
                events.add(newEvent);
                clearFields(codeField, nameField, descArea, locationField, dateField,
                        capacityField, costField, imageView);
            } catch (Exception ex) {
                showAlert("Error", "Invalid input. Please check all fields.");
            }
        });

        updateButton.setOnAction(e -> {
            Event selected = eventListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setEventCode(codeField.getText());
                    selected.setEventName(nameField.getText());
                    selected.setDescription(descArea.getText());
                    selected.setLocation(locationField.getText());
                    selected.setDateTime(LocalDateTime.parse(dateField.getText()));
                    selected.setCapacity(Integer.parseInt(capacityField.getText()));
                    selected.setCost(costField.getText());
                    if (imageView.getImage() != null) {
                        selected.setHeaderImage(imageView.getImage());
                    }
                    eventListView.refresh();
                } catch (Exception ex) {
                    showAlert("Error", "Invalid input. Please check all fields.");
                }
            }
        });

        clearButton.setOnAction(e -> {
            clearFields(codeField, nameField, descArea, locationField, dateField,
                    capacityField, costField, imageView);
        });

        eventListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                codeField.setText(newVal.getEventCode());
                nameField.setText(newVal.getEventName());
                descArea.setText(newVal.getDescription());
                locationField.setText(newVal.getLocation());
                dateField.setText(newVal.getDateTime().toString());
                capacityField.setText(String.valueOf(newVal.getCapacity()));
                costField.setText(newVal.getCost());
                imageView.setImage(newVal.getHeaderImage());
            }
        });

        return grid;
    }

    private VBox createViewEventsPanel(boolean showRegistered) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ListView<Event> eventListView = new ListView<>(events);
        eventListView.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getEventCode() + " - " + item.getEventName() + " (" +
                            item.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")) + ")");
                }
            }
        });

        // Details panel
        Label detailTitle = new Label("Event Details");
        detailTitle.setFont(new Font(16));
        detailTitle.setTextFill(Color.DARKBLUE);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label();
        Label dateLabel = new Label();
        Label locationLabel = new Label();
        Label descLabel = new Label();
        descLabel.setWrapText(true);
        Label capacityLabel = new Label();
        Label costLabel = new Label();
        Label registeredLabel = new Label();
        registeredLabel.setWrapText(true);

        Button registerButton = new Button("Register");
        registerButton.setVisible(!isAdmin && showRegistered);

        eventListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                imageView.setImage(newVal.getHeaderImage());
                nameLabel.setText("Name: " + newVal.getEventName());
                dateLabel.setText("Date: " + newVal.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")));
                locationLabel.setText("Location: " + newVal.getLocation());
                descLabel.setText("Description: " + newVal.getDescription());
                capacityLabel.setText("Capacity: " + newVal.getCapacity() + " (Available: " +
                        (newVal.getCapacity() - newVal.getRegisteredStudents().size()) + ")");
                costLabel.setText("Cost: " + newVal.getCost());
                registeredLabel.setText("Registered: " + String.join(", ", newVal.getRegisteredStudents()));

                // Check if current user is already registered
                if (!isAdmin && showRegistered) {
                    registerButton.setDisable(newVal.getRegisteredStudents().contains(currentUser) ||
                            newVal.getRegisteredStudents().size() >= newVal.getCapacity());
                }
            }
        });

        registerButton.setOnAction(e -> {
            Event selected = eventListView.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.getRegisteredStudents().contains(currentUser)) {
                selected.addRegisteredStudent(currentUser);
                registeredLabel.setText("Registered: " + String.join(", ", selected.getRegisteredStudents()));
                registerButton.setDisable(true);
                showAlert("Success", "You have successfully registered for " + selected.getEventName());
                // In a real app, send confirmation email here
            }
        });

        VBox detailsBox = new VBox(5, detailTitle, imageView, nameLabel, dateLabel,
                locationLabel, descLabel, capacityLabel, costLabel,
                registeredLabel, registerButton);
        detailsBox.setPadding(new Insets(10));

        HBox contentBox = new HBox(10, eventListView, detailsBox);
        vbox.getChildren().add(contentBox);

        return vbox;
    }

    private VBox createManageRegistrationsPanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label titleLabel = new Label("Manage Event Registrations");
        titleLabel.setFont(new Font(16));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Event selection
        ComboBox<Event> eventCombo = new ComboBox<>(events);
        eventCombo.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getEventCode() + " - " + item.getEventName());
                }
            }
        });
        eventCombo.setButtonCell(new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getEventCode() + " - " + item.getEventName());
                }
            }
        });

        // Registered students list
        ListView<String> registeredList = new ListView<>();
        registeredList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Buttons
        Button removeButton = new Button("Remove Selected");
        Button exportButton = new Button("Export Attendee List");

        HBox buttonBox = new HBox(10, removeButton, exportButton);

        // Update registered students when event selection changes
        eventCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                registeredList.getItems().setAll(newVal.getRegisteredStudents());
            } else {
                registeredList.getItems().clear();
            }
        });

        removeButton.setOnAction(e -> {
            Event selectedEvent = eventCombo.getValue();
            if (selectedEvent != null) {
                List<String> selectedStudents = new ArrayList<>(registeredList.getSelectionModel().getSelectedItems());
                selectedEvent.getRegisteredStudents().removeAll(selectedStudents);
                registeredList.getItems().removeAll(selectedStudents);
            }
        });

        exportButton.setOnAction(e -> {
            // In a real app, this would export to a file
            showAlert("Export", "Attendee list exported (simulated)");
        });

        vbox.getChildren().addAll(titleLabel, eventCombo, registeredList, buttonBox);
        return vbox;
    }

    // User Panel with limited features
    private void showUserPanel(Stage stage) {
        Label titleLabel = new Label("Event Management");
        titleLabel.setFont(new Font(18));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Create tabs for different functionalities
        TabPane tabPane = new TabPane();

        // Tab 1: View All Events
        Tab viewEventsTab = new Tab("View Events");
        viewEventsTab.setClosable(false);
        viewEventsTab.setContent(createViewEventsPanel(true));

        // Tab 2: My Events
        Tab myEventsTab = new Tab("My Events");
        myEventsTab.setClosable(false);

        // Filter events where current user is registered
        ObservableList<Event> myEvents = FXCollections.observableArrayList();
        for (Event event : events) {
            if (event.getRegisteredStudents().contains(currentUser)) {
                myEvents.add(event);
            }
        }

        ListView<Event> myEventsList = new ListView<>(myEvents);
        myEventsList.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getEventCode() + " - " + item.getEventName() + " (" +
                            item.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")) + ")");
                }
            }
        });

        // Details panel for my events
        Label detailTitle = new Label("Event Details");
        detailTitle.setFont(new Font(16));
        detailTitle.setTextFill(Color.DARKBLUE);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label();
        Label dateLabel = new Label();
        Label locationLabel = new Label();
        Label descLabel = new Label();
        descLabel.setWrapText(true);
        Label costLabel = new Label();

        Button unregisterButton = new Button("Unregister");

        myEventsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                imageView.setImage(newVal.getHeaderImage());
                nameLabel.setText("Name: " + newVal.getEventName());
                dateLabel.setText("Date: " + newVal.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")));
                locationLabel.setText("Location: " + newVal.getLocation());
                descLabel.setText("Description: " + newVal.getDescription());
                costLabel.setText("Cost: " + newVal.getCost());
            }
        });

        unregisterButton.setOnAction(e -> {
            Event selected = myEventsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.getRegisteredStudents().remove(currentUser);
                myEvents.remove(selected);
                showAlert("Success", "You have unregistered from " + selected.getEventName());
            }
        });

        VBox detailsBox = new VBox(5, detailTitle, imageView, nameLabel, dateLabel,
                locationLabel, descLabel, costLabel, unregisterButton);
        detailsBox.setPadding(new Insets(10));

        HBox contentBox = new HBox(10, myEventsList, detailsBox);
        myEventsTab.setContent(contentBox);

        tabPane.getTabs().addAll(viewEventsTab, myEventsTab);

        VBox layout = new VBox(10, titleLabel, tabPane);
        layout.setPadding(new Insets(10));
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Event Management");
        stage.show();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void clearFields(TextField codeField, TextField nameField, TextArea descArea,
                             TextField locationField, TextField dateField,
                             TextField capacityField, TextField costField, ImageView imageView) {
        codeField.clear();
        nameField.clear();
        descArea.clear();
        locationField.clear();
        dateField.clear();
        capacityField.clear();
        costField.clear();
        imageView.setImage(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addReturnToDashboardButton(VBox layout, Stage stage) {
        Button returnToDashboardBtn = new Button("Return to Dashboard");
        returnToDashboardBtn.setOnAction(e -> {
            try {
                new UserDashboard().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                System.err.println("Error returning to dashboard: " + ex.getMessage());
            }
        });
        layout.getChildren().add(returnToDashboardBtn);
    }

    // Enhanced Event Class with all required attributes
    public static class Event {
        private String eventCode;
        private String eventName;
        private String description;
        private String location;
        private LocalDateTime dateTime;
        private int capacity;
        private String cost;
        private Image headerImage;
        private List<String> registeredStudents;

        public Event(String eventCode, String eventName, String description, String location,
                     LocalDateTime dateTime, int capacity, String cost, String imagePath,
                     String registeredStudents) {
            this.eventCode = eventCode;
            this.eventName = eventName;
            this.description = description;
            this.location = location;
            this.dateTime = dateTime;
            this.capacity = capacity;
            this.cost = cost;
            this.headerImage = new Image(imagePath.equals("default") ? "default_event.png" : imagePath);
            this.registeredStudents = new ArrayList<>();
            if (!registeredStudents.isEmpty()) {
                for (String student : registeredStudents.split(",")) {
                    this.registeredStudents.add(student.trim());
                }
            }
        }

        // Getters and Setters
        public String getEventCode() { return eventCode; }
        public void setEventCode(String eventCode) { this.eventCode = eventCode; }

        public String getEventName() { return eventName; }
        public void setEventName(String eventName) { this.eventName = eventName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public LocalDateTime getDateTime() { return dateTime; }
        public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }

        public String getCost() { return cost; }
        public void setCost(String cost) { this.cost = cost; }

        public Image getHeaderImage() { return headerImage; }
        public void setHeaderImage(Image headerImage) { this.headerImage = headerImage; }

        public List<String> getRegisteredStudents() { return registeredStudents; }

        public void addRegisteredStudent(String studentName) {
            if (!registeredStudents.contains(studentName)) {
                registeredStudents.add(studentName);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
