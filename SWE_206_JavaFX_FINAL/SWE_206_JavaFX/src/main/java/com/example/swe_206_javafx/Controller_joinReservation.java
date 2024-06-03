package com.example.swe_206_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_joinReservation implements Initializable {

    @FXML
    private AnchorPane joinEventAnchorPane;

    @FXML
    private TableView<Reservation> tableView; // TableView to display reservations

    @FXML
    private TableColumn<Reservation, Integer> attendeeNumber; // TableColumn for attendee number

    @FXML
    private TableColumn<Reservation, String> bookingId; // TableColumn for booking ID

    @FXML
    private TableColumn<Reservation, LocalDate> date; // TableColumn for date

    @FXML
    private TableColumn<Reservation, String> location; // TableColumn for location

    @FXML
    private TableColumn<Reservation, LocalTime> timeFormat; // TableColumn for time format

    @FXML
    private TableColumn<Reservation, String> reason;

    @FXML
    private Label messageLable = new Label(""); // Label for displaying messages

    @FXML
    private ChoiceBox<Integer> boockingID_choiceBox = new ChoiceBox(); // ChoiceBox for selecting booking IDs

    @FXML
    private Button join_button = new Button(); // Button for joining events

    @FXML
    private Button back_button = new Button(); // Button for navigating back


    private ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList(); // ObservableList for reservations

    Person currentUser = Person.getCurrentUser(); // Current user

    // Method to initialize the controller
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinEventAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        join_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));

        try {
            // Initialize TableColumn cell value factories
            bookingId.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeFormat.setCellValueFactory(new PropertyValueFactory<>("timeFormat"));
            attendeeNumber.setCellValueFactory(new PropertyValueFactory<>("attendeeNumber"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));

            // Set TableView items
            tableView.setItems(getReservationObservableList());

            // Populate ChoiceBox with booking IDs
            ObservableList<Integer> bookingIDs = FXCollections.observableArrayList();
            for (Reservation reservation : reservationObservableList) {
                bookingIDs.add(reservation.getBookingID());
            }
            boockingID_choiceBox.getItems().addAll(bookingIDs);
        } catch(Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    // Method to switch to the home page scene
    public void switchTo_home_page_scene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Home_page.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }

    // Method to retrieve valid reservations for the current user
    public ObservableList<Reservation> getReservationObservableList() {
        ArrayList<Reservation> validReservations = new ArrayList<>();
        ArrayList<Reservation> currentUserReservations = currentUser.reservationList;

        for (Reservation reservation : RunApplication.reservations) {
            boolean conflicting = false;
            for (Reservation currentUserReservation : currentUserReservations) {
                if (reservation.checkTimeAndDateConflict(currentUserReservation)) {
                    conflicting = true;
                    break;
                }
            }
            if (!conflicting && reservation.getAttendeeNumber() < reservation.getMaximumAttendeeNumber() && Person.getCurrentUser().getGender().equals(reservation.getGender())) {
                validReservations.add(reservation);
            }
        }

        reservationObservableList.addAll(validReservations);
        return reservationObservableList;
    }

    // Method to handle joining an event
    public void joinEvent(ActionEvent event) throws Exception {
        try {
            int choosedID = boockingID_choiceBox.getValue();

            // Check if the user is already assigned to the event
            ArrayList<Reservation> currentUserReservations = Person.getCurrentUser().reservationList;
            for (Reservation cuurentReservation : currentUserReservations) {
                int currentID = cuurentReservation.getBookingID();

                if (currentID == choosedID) {
                    messageLable.setText("You are already assigned in this event!");
                }
            }

            if (!messageLable.getText().equals("You are already assigned in this event!")){
                // Join the event and display appropriate message
                if (currentUser.joinASportEvent(choosedID)) {
                    messageLable.setText("You have successfully joined the event");
                }
            }


        } catch(Exception e) {
            messageLable.setText("Something went wrong, choose an ID");
        }
    }
}










