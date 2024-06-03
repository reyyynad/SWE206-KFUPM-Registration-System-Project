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

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_Department implements Initializable {

    @FXML
    private AnchorPane reservationDataAnchorPane;

    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, String> bookingID;

    @FXML
    private Button cancel_button;

    @FXML
    private TableColumn<Reservation, LocalDate> date;

    @FXML
    private TableColumn<Reservation, String> gender;

    @FXML
    private TableColumn<Reservation, String> location;

    @FXML
    private ChoiceBox<Integer> bookingID_choiceBox;

    @FXML
    private TableColumn<Reservation, String> reason;

    @FXML
    private TableColumn<Reservation, String> timeFormat;

    @FXML
    private TableColumn<Reservation, String> status;

    @FXML
    private Label messageLable;



    private final ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

    // Switches to the sign-in screen
    @FXML
    public void switchTo_singIn(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("signIn.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }

    public ObservableList<Reservation> getReservationObservableList() {
        ArrayList<Reservation> userReservation = RunApplication.reservations;
        reservationObservableList.addAll(userReservation);
        return reservationObservableList;
    }

    // Initializes the controller
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        reservationDataAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        cancel_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));

        try {
            // Set up the table columns
            gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            bookingID.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeFormat.setCellValueFactory(new PropertyValueFactory<>("timeFormat"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Set the data in the table view
            tableView.setItems(getReservationObservableList());

            // Populate the choice box with booking IDs
            ObservableList<Integer> bookingIDs = FXCollections.observableArrayList();
            for (Reservation reservation : reservationObservableList) {
                bookingIDs.add(reservation.getBookingID());
            }
            bookingID_choiceBox.getItems().addAll(bookingIDs);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    // Cancels a reservation
    @FXML
    public void cancelReservation(ActionEvent event) {

        try {
            int canceledBookingID = bookingID_choiceBox.getValue();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Cancel Confirmation");
            alert.setContentText("Are you sure you want to cancel this reservation?");
            ButtonType confirmButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("No");
            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            // If confirmed, delete the profile and update friend lists
            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    Reservation canceledReservation = null;

                    // Find the reservation to be canceled
                    for (Reservation reservation : reservationObservableList) {
                        if (canceledBookingID == reservation.getBookingID()) {
                            canceledReservation = reservation;
                            break;
                        }
                    }

                    if (canceledReservation != null) {
                        // Remove the reservation from the list
                        reservationObservableList.remove(canceledReservation);

                        // Remove the booking ID from the choice box
                        bookingID_choiceBox.getItems().remove(Integer.valueOf(canceledBookingID));
                        bookingID_choiceBox.getSelectionModel().clearSelection();

                        // Remove the reservation from the person's reservation list and the global reservation list
                        for (Person person : RunApplication.people) {
                            Iterator<Reservation> iterator = person.reservationList.iterator();
                            while (iterator.hasNext()) {
                                Reservation reservation = iterator.next();
                                if (reservation.getBookingID() == canceledBookingID) {
                                    // Remove the reservation using the iterator
                                    iterator.remove();
                                    // Send an email notification to the user
                                    String recipient = person.getContactEmail();
                                    String subject = "Reservation Cancellation";
                                    String body = "Dear User, your reservation has been canceled.";
                                    RunApplication.sendEmail(recipient, subject, body);
                                }
                            }
                        }

// Remove the reservation from the global reservation list
                        Iterator<Reservation> globalIterator = RunApplication.reservations.iterator();
                        while (globalIterator.hasNext()) {
                            Reservation reservation = globalIterator.next();
                            if (reservation.getBookingID() == canceledBookingID) {
                                globalIterator.remove();
                            }
                        }

                    }

                }
            });

        } catch (Exception exception) {
            messageLable.setText("Something went wrong, choose an ID");
            exception.printStackTrace();
        }
    }


}