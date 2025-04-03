package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventManagement extends Application {
    private boolean isAdmin;
    private ObservableList<Event> events = FXCollections.observableArrayList();
    private String currentUser;
    private TabPane userTabPane;

    public EventManagement(boolean isAdmin, String currentUser) {
        this.isAdmin = isAdmin;
        this.currentUser = this.currentUser;
    }

    @Override
    public void start(Stage stage) {
        loadSampleEvents();

        if (isAdmin) {
            showAdminPanel(stage);
        } else {
            showUserPanel(stage);
        }
    }

    private void loadSampleEvents() {
        events.add(new Event("EV001", "Welcome Seminar", "Orientation week", "Auditorium",
                LocalDateTime.parse("2025-09-01T10:00"), 100, "Free",
                "Alice Smith, Bob Johnson"));

        events.add(new Event("EV002", "Research Workshop", "Graduate workshop", "Lab 301",
                LocalDateTime.parse("2025-10-05T14:00"), 50, "Paid ($20)",
                "Carol Williams, David Lee"));
    }

    private void showAdminPanel(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab manageTab = new Tab("Manage Events");
        manageTab.setContent(createManageEventsPanel());

        Tab calendarTab = new Tab("Calendar View");
        calendarTab.setContent(createCalendarView());

        tabPane.getTabs().addAll(manageTab, calendarTab);

        // Back to Dashboard Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            try {
                new UserDashboard().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to return to dashboard");
            }
        });

        VBox layout = new VBox(10,
                new Label("Event Management (Admin)"),
                tabPane,
                backButton
        );
        layout.setPadding(new Insets(15));
        stage.setScene(new Scene(layout, 800, 600));
        stage.setTitle("Admin Event Manager");
        stage.show();
    }

    private void showUserPanel(Stage stage) {
        userTabPane = new TabPane();

        Tab allEventsTab = new Tab("All Events");
        allEventsTab.setContent(createUserEventsView(false));

        Tab myEventsTab = new Tab("My Events");
        myEventsTab.setContent(createUserEventsView(true));

        userTabPane.getTabs().addAll(allEventsTab, myEventsTab);

        // Back to Dashboard Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            try {
                new UserDashboard().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                showAlert("Error", "Failed to return to dashboard");
            }
        });

        VBox layout = new VBox(10,
                new Label("Event Registration"),
                userTabPane,
                backButton
        );
        stage.setScene(new Scene(layout, 700, 500));
        stage.setTitle("Event Portal");
        stage.show();
    }

    private GridPane createManageEventsPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form Fields
        TextField codeField = new TextField();
        TextField nameField = new TextField();
        TextArea descArea = new TextArea();
        TextField locField = new TextField();
        TextField dateField = new TextField();
        TextField capField = new TextField();
        TextField costField = new TextField();

        addFormField(grid, "Event Code:", codeField, 0);
        addFormField(grid, "Event Name:", nameField, 1);
        addFormField(grid, "Description:", descArea, 2);
        addFormField(grid, "Location:", locField, 3);
        addFormField(grid, "Date/Time:", dateField, 4);
        addFormField(grid, "Capacity:", capField, 5);
        addFormField(grid, "Cost:", costField, 6);

        // Event List
        ListView<Event> eventList = new ListView<>(events);
        eventList.setCellFactory(lv -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : formatEventSummary(item));
            }
        });
        grid.add(eventList, 2, 0, 1, 8);

        // Buttons
        Button addBtn = new Button("Add Event");
        Button updateBtn = new Button("Update Event");
        Button deleteBtn = new Button("Delete Event");
        HBox buttonBox = new HBox(10, addBtn, updateBtn, deleteBtn);
        grid.add(buttonBox, 1, 7);

        // Button Actions
        addBtn.setOnAction(e -> {
            try {
                Event newEvent = new Event(
                        codeField.getText(),
                        nameField.getText(),
                        descArea.getText(),
                        locField.getText(),
                        LocalDateTime.parse(dateField.getText()),
                        Integer.parseInt(capField.getText()),
                        costField.getText(),
                        ""
                );
                events.add(newEvent);
                clearFormFields(grid);
            } catch (Exception ex) {
                showAlert("Input Error", "Please check all fields have valid values");
            }
        });

        deleteBtn.setOnAction(e -> {
            Event selected = eventList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                events.remove(selected);
            }
        });

        return grid;
    }

    private VBox createUserEventsView(boolean onlyRegistered) {
        ObservableList<Event> filteredEvents = onlyRegistered ? getRegisteredEvents() : events;
        ListView<Event> eventList = new ListView<>(filteredEvents);
        eventList.setCellFactory(lv -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : formatEventDetails(item));
            }
        });

        Button actionBtn = new Button(onlyRegistered ? "Unregister" : "Register");
        actionBtn.setOnAction(e -> {
            Event selected = eventList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (onlyRegistered) {
                    selected.getRegisteredStudents().remove(currentUser);
                    filteredEvents.remove(selected);
                } else {
                    if (!selected.isFull()) {
                        selected.getRegisteredStudents().add(currentUser);
                        refreshAllTabs();
                    } else {
                        showAlert("Event Full", "This event has reached maximum capacity");
                    }
                }
                eventList.refresh();
            }
        });

        return new VBox(10, eventList, actionBtn);
    }

    private void refreshAllTabs() {
        if (userTabPane != null) {
            ((Tab)userTabPane.getTabs().get(0)).setContent(createUserEventsView(false));
            ((Tab)userTabPane.getTabs().get(1)).setContent(createUserEventsView(true));
        }
    }

    private void addFormField(GridPane grid, String labelText, Control inputControl, int row) {
        Label label = new Label(labelText);
        grid.add(label, 0, row);
        grid.add(inputControl, 1, row);
        inputControl.setPrefWidth(250);
        if (inputControl instanceof TextArea) {
            ((TextArea)inputControl).setPrefRowCount(3);
        }
    }

    private void clearFormFields(GridPane grid) {
        grid.getChildren().forEach(node -> {
            if (node instanceof TextInputControl) {
                ((TextInputControl)node).clear();
            }
        });
    }

    private VBox createCalendarView() {
        ListView<Event> calendarList = new ListView<>(events);
        calendarList.setCellFactory(lv -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%s\n%s at %s",
                        item.getEventName(),
                        item.getDateTime().format(DateTimeFormatter.ISO_DATE),
                        item.getLocation()));
            }
        });
        return new VBox(10, new Label("Upcoming Events Calendar"), calendarList);
    }

    private ObservableList<Event> getRegisteredEvents() {
        ObservableList<Event> registered = FXCollections.observableArrayList();
        for (Event e : events) {
            if (e.getRegisteredStudents().contains(currentUser)) {
                registered.add(e);
            }
        }
        return registered;
    }

    private String formatEventSummary(Event e) {
        return String.format("%s: %s\n%s | %s/%s spots",
                e.getEventCode(),
                e.getEventName(),
                e.getDateTime().format(DateTimeFormatter.ofPattern("MMM d, h:mm a")),
                e.getRegisteredStudents().size(),
                e.getCapacity());
    }

    private String formatEventDetails(Event e) {
        return String.format(
                "» %s (%s)\n" +
                        "Date: %s\n" +
                        "Location: %s\n" +
                        "Cost: %s\n" +
                        "Spots: %d/%d\n" +
                        "Description: %s",
                e.getEventName(), e.getEventCode(),
                e.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")),
                e.getLocation(),
                e.getCost(),
                e.getRegisteredStudents().size(), e.getCapacity(),
                e.getDescription());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Event {
        private final String eventCode;
        private final String eventName;
        private final String description;
        private final String location;
        private final LocalDateTime dateTime;
        private final int capacity;
        private final String cost;
        private final List<String> registeredStudents;

        public Event(String code, String name, String desc, String loc,
                     LocalDateTime dt, int cap, String cost, String registered) {
            this.eventCode = code;
            this.eventName = name;
            this.description = desc;
            this.location = loc;
            this.dateTime = dt;
            this.capacity = cap;
            this.cost = cost;
            this.registeredStudents = new ArrayList<>();
            for (String s : registered.split(",")) {
                this.registeredStudents.add(s.trim());
            }
        }

        public String getEventCode() { return eventCode; }
        public String getEventName() { return eventName; }
        public String getDescription() { return description; }
        public String getLocation() { return location; }
        public LocalDateTime getDateTime() { return dateTime; }
        public int getCapacity() { return capacity; }
        public String getCost() { return cost; }
        public List<String> getRegisteredStudents() { return registeredStudents; }
        public boolean isFull() { return registeredStudents.size() >= capacity; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
