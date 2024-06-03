package com.example.swe_206_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_modifyReservation implements Initializable {

    @FXML
    private AnchorPane modifyAnchorPane;

    @FXML
    private ChoiceBox booking_number = new ChoiceBox();

    @FXML
    private Button edit_button = new Button();

    @FXML
    private Button delete_button = new Button();

    @FXML
    private TableView<Reservation> reservationData;

    @FXML
    private TableColumn<Reservation, Integer> bookingID;

    @FXML
    private TableColumn<Reservation, String> location;

    @FXML
    private TableColumn<Reservation, LocalDate> date;

    @FXML
    private TableColumn<Reservation, String> timeFormat;

    @FXML
    private TableColumn<Reservation, String> reason;

    @FXML
    private TableColumn<Reservation, String> status;

    @FXML
    private Text messageLabel2 = new Text("");

    private ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList(); // ObservableList for reservations

    public static Reservation currentReservation;


    @FXML
    public void switch_scenes(ActionEvent event) throws IOException{

        String fxmlName = "Home_page.fxml";
        if (event.getSource() == edit_button){
            fxmlName = "Edit_reservation.fxml";

            int chosenID = (int) booking_number.getValue();

            ArrayList<Reservation> currentUserReservations = Person.getCurrentUser().reservationList;
            for (Reservation reservation : currentUserReservations) {
                int currentID = reservation.getBookingID();
                if (currentID == chosenID) {
                    currentReservation = reservation;
                }
            }
        }


        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlName)));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }


    public ObservableList<Reservation> getReservationObservableList() {

        ArrayList<Reservation> userReservation = Person.getCurrentUser().reservationList;

        reservationObservableList.addAll(userReservation);

        return reservationObservableList;
    }


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel2.setText("");
        modifyAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        edit_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        delete_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));

        try {
            // Initialize TableColumn cell value factories
            bookingID.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeFormat.setCellValueFactory(new PropertyValueFactory<>("timeFormat"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Set TableView items
            reservationData.setItems(getReservationObservableList());

            // Populate ChoiceBox with booking IDs
            ObservableList<Integer> bookingIDs = FXCollections.observableArrayList();
            for (Reservation reservation : reservationObservableList) {
                bookingIDs.add(reservation.getBookingID());
            }
            booking_number.getItems().addAll(bookingIDs);
        } catch(Exception exception) {
            System.out.println(exception.getMessage());
        }
    }






    public void deleteReservation(){
        try {

            if (messageLabel2.getText().equals("Your reservation is deleted successfully!")){
                messageLabel2.setText("You already deleted this reservation");
            }
            else{
                int chosenID = (int) booking_number.getValue();

                Reservation temp = null;
                for(Person person: RunApplication.people){
                    for (Reservation cuurentReservation : person.reservationList) {
                        int currentID = cuurentReservation.getBookingID();
                        if (currentID == chosenID) {
                            temp = cuurentReservation;
                            person.reservationList.remove(temp);
                            reservationObservableList.remove(temp);

                            // Remove the booking ID from the choice box
                            booking_number.getItems().remove(Integer.valueOf(String.valueOf(temp)));
                            booking_number.getSelectionModel().clearSelection();
                            break;
                        }
                    }
                }
                RunApplication.reservations.remove(temp);
                messageLabel2.setText("Your reservation is deleted successfully!");
            }




        } catch (Exception exception) {
            exception.printStackTrace();
            messageLabel2.setText("Something went wrong! Try again");
        }
    }







}
