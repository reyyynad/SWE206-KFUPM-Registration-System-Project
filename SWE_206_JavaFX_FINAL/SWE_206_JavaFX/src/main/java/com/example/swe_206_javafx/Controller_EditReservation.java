package com.example.swe_206_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class Controller_EditReservation {

    @FXML
    private AnchorPane editAnchorPane;

    @FXML
    private ChoiceBox locationsBox = new ChoiceBox();

    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private ChoiceBox startingTimeChoiceBox=new ChoiceBox<>();

    @FXML
    private ChoiceBox durationChoiceBox = new ChoiceBox<>();

    ObservableList<String> startingTimesList = FXCollections.observableArrayList(
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
            "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
            "20:00", "21:00");

    Integer[] durationList = IntStream.rangeClosed(1,4)
            .boxed()
            .toArray(Integer[]::new);

    @FXML
    private TextField reasonField = new TextField();


    @FXML
    private Button confirm_button;

    @FXML
    private Text messageLabel;

    public static Reservation oldReservation;

    public static Reservation newReservation;




    @FXML
    public void switch_to_modify_page(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Modify_reservation.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }

    @FXML
    private void initialize(){
        messageLabel.setText("");
        editAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        confirm_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        oldReservation = Controller_modifyReservation.currentReservation;

        locationsBox.setValue(oldReservation.getLocation());
        datePicker.setValue(oldReservation.getDate());
        startingTimeChoiceBox.setItems(startingTimesList);
        System.out.println(oldReservation.getTime().get(0));
        if (oldReservation.getTime().get(0) < 10){
            System.out.println("Here000");
            startingTimeChoiceBox.setValue("0" + oldReservation.getTime().get(0) + ":00");
            System.out.println(startingTimeChoiceBox.getValue());
        }
        else{
            startingTimeChoiceBox.setValue(oldReservation.getTime().get(0) + ":00");
        }
        durationChoiceBox.setValue(oldReservation.getTime().size());
        durationChoiceBox.getItems().addAll(durationList);
        reasonField.setText(oldReservation.getReason());

    }

    @FXML
    public boolean editReservation(ActionEvent event) throws Exception {

        try {

            String newLocation = (String) locationsBox.getValue();
            LocalDate newDate = datePicker.getValue();
            System.out.println((startingTimeChoiceBox.getValue()));
            System.out.println(startingTimeChoiceBox.getValue().getClass());
            int startHour = Integer.parseInt(String.valueOf(startingTimeChoiceBox.getValue()).substring(0,2));
            int duration = Integer.parseInt(String.valueOf(durationChoiceBox.getValue()));
            int endHour = (startHour + duration) % 24;
            ArrayList<Integer> newTime =new ArrayList<>();
            for (int i=startHour;i<endHour;i++){
                newTime.add(i);
            }
            String newReason = reasonField.getText();

            newReservation = new Reservation(oldReservation.getBookingID(), newDate, newTime, newLocation, oldReservation.getGender(), oldReservation.getAttendeeNumber(), oldReservation.getMaximumAttendeeNumber(), newReason,oldReservation.getStatus());


            if (oldReservation.getLocation().equals(newLocation) && oldReservation.getDate().equals(newDate) && oldReservation.getTime().equals(newTime) && oldReservation.getReason().equals(newReason)) {
                messageLabel.setText("You have not change anything!");
                return false;
            }
            else{
                for (Person person: RunApplication.people){
                    for (Reservation current : person.reservationList) {
                        int currentID = current.getBookingID();

                        if (currentID == Controller_modifyReservation.currentReservation.getBookingID()) {
                            person.reservationList.remove(oldReservation);
                            person.reservationList.add(newReservation);

                        }
                    }
                }
                RunApplication.reservations.remove(oldReservation);
                RunApplication.reservations.add(newReservation);
                messageLabel.setText("Your reservation is updated successfully!");
                confirm_button.setDisable(true);
                return true;
            }

        } catch(Exception e) {
            messageLabel.setText("Something went wrong, choose an ID");
            e.printStackTrace();
            return false;
        }


    }




}
