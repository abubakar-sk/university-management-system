package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EventManagement extends Application {
    private boolean isAdmin;
    private ObservableList<Event> events = FXCollections.observableArrayList();

    public EventManagement(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public EventManagement() {
        this.isAdmin = false;
    }

    @Override
    public void start(Stage stage) {
        if (isAdmin) {
            showAdminPanel(stage);
        } else {
            showUserPanel(stage);
        }
    }

    // Admin Panel
    private void showAdminPanel(Stage stage) {
        Label titleLabel = new Label("Event Management (Admin)");
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Event Name");
        TextField eventDateField = new TextField();
        eventDateField.setPromptText("Event Date");
        Button addEventButton = new Button("Add Event");
        Button deleteEventButton = new Button("Delete Event");
        ListView<String> eventListView = new ListView<>();
        updateEventListView(eventListView);

        addEventButton.setOnAction(e -> {
            String name = eventNameField.getText();
            String date = eventDateField.getText();

            if (validateInput(name, date)) {
                events.add(new Event(name, date));
                eventNameField.clear();
                eventDateField.clear();
                updateEventListView(eventListView);
                System.out.println("Added Event: " + name + " - " + date);
            } else {
                showAlert("Invalid Input", "Event name and date cannot be empty.");
            }
        });

        deleteEventButton.setOnAction(e -> {
            String selectedEvent = eventListView.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                events.removeIf(event -> event.getEventName().equals(parseEventName(selectedEvent)));
                updateEventListView(eventListView);
                System.out.println("Deleted Event: " + selectedEvent);
            } else {
                showAlert("No Selection", "Please select an event to delete.");
            }
        });

        VBox layout = new VBox(10, titleLabel, eventNameField, eventDateField, addEventButton, deleteEventButton, eventListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Event Management");
        stage.show();
    }

    // User Panel
    private void showUserPanel(Stage stage) {
        Label titleLabel = new Label("Upcoming Events");
        ListView<String> eventListView = new ListView<>();
        updateEventListView(eventListView);

        VBox layout = new VBox(10, titleLabel, eventListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Event Management");
        stage.show();
    }

    private void updateEventListView(ListView<String> eventListView) {
        eventListView.getItems().clear();
        events.forEach(event -> eventListView.getItems().add(event.getEventName() + " - " + event.getEventDate()));
    }

    private boolean validateInput(String name, String date) {
        return name != null && !name.isEmpty() && date != null && !date.isEmpty();
    }

    private String parseEventName(String eventDisplay) {
        return eventDisplay.split(" - ")[0];
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
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

    // Event Class
    public static class Event {
        private String eventName;
        private String eventDate;
        private Image headerImage;

        public Event(String eventName, String eventDate) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.headerImage = new Image("default_event.png"); // Default image placeholder
        }

        public String getEventName() { return eventName; }
        public String getEventDate() { return eventDate; }
        public Image getHeaderImage() { return headerImage; }
        public void setHeaderImage(Image headerImage) { this.headerImage = headerImage; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
