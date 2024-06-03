package com.example.swe_206_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class Controller_CreateReservation {
    @FXML
    private AnchorPane homePageAnchorPane;
    @FXML
    private ChoiceBox locationsBox=new ChoiceBox<>();
    @FXML
    private Label attendeeText=new Label();
    @FXML
    private DatePicker datePicker=new DatePicker();
    @FXML
    private TextField reasonField =new TextField();
    @FXML
    private ChoiceBox durationChoiceBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox startingTimeChoiceBox=new ChoiceBox<>();
    @FXML
    private ChoiceBox attendeeNumberBox=new ChoiceBox<>() ;
    @FXML
    private Button confirm_button =new Button();
    @FXML
    private Button cancel_button=new Button();
    @FXML
    private RadioButton labs_classrooms_radioButton =new RadioButton();
    @FXML
    private RadioButton facilities_radioButton=new RadioButton();
    @FXML
    private Text timeText=new Text();
    @FXML
    private Text noteText=new Text();
    @FXML
    private Text currentUserName = new Text();
    @FXML
    private Text currentUserPosition = new Text();


    Integer[] attendeeNumberArray = IntStream.rangeClosed(2,20)
            .boxed()
            .toArray(Integer[]::new);

    ObservableList<String> startingTimesList = FXCollections.observableArrayList(
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
            "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
            "20:00", "21:00");

    Integer[] durationList = IntStream.rangeClosed(1,4)
            .boxed()
            .toArray(Integer[]::new);

    Person currentUser=Person.getCurrentUser();

    String[] locationArray;


    // switch scenes:
    public void switchTo_home_page_scene(ActionEvent event) throws Exception {
        if(event.getSource() == cancel_button)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Home_page.fxml")));
            Common.scene = new Scene(fxmlLoader.load());
            Common.stage.setTitle("KFUPM Registration System");
            Common.stage.setScene(Common.scene);
            Common.stage.setResizable(false);
            Common.stage.show();
        }
        else{
            // if it is confirmed
            try {
                //checking that there are no empty fields
                if (!(reasonField.getText().isEmpty() || reasonField.getText().isBlank() )) {
                    noteText.setText("");
                    timeText.setText("");
                    int duration = Integer.parseInt(String.valueOf(durationChoiceBox.getValue()));
                    String inputTime = String.valueOf(startingTimeChoiceBox.getValue());
                    if (isValidEndTime(duration, inputTime)) {
                        int startHour = Integer.parseInt(inputTime.substring(0, 2));
                        int endHour = (startHour + duration) % 24;

                        ArrayList<Integer> reservedHoursArrayList = returnReservedHours(startHour, endHour);
                        String locationString = (String) locationsBox.getValue();
                        LocalDate date = datePicker.getValue();
                        String reason = reasonField.getText();
                        if (labs_classrooms_radioButton.isSelected()) {
                            if (currentUser.getPosition().equals("Student")) {
                                noteText.setText("Not eligible to reserve labs and classrooms");
                            } else {
                                if (currentUser.reserveLabOrClassroom(date, reservedHoursArrayList, locationString, currentUser.getGender(), reason)) {
                                    noteText.setText("Reservation is made successfully");
                                    timeText.setText("Your reservation starts at : " + inputTime + " and last for : " + duration + " hours, and it will end at : " + endHour +":00");

                                    noteText.setFill(Color.GREEN);
                                    confirm_button.setDisable(true);

                                } else {
                                    noteText.setText("Reservation is not made successfully");
                                    noteText.setFill(Color.RED);
                                }
                            }
                        }

                        /// if facilities is selected
                        else {
                            int attendeeNum = (int) attendeeNumberBox.getValue();
                            if (currentUser.createEvent(date, reservedHoursArrayList, locationString, currentUser.getGender(), reason, attendeeNum)) {
                                noteText.setFill(Color.GREEN);
                                noteText.setText("Reservation is made successfully");
                                timeText.setText("Your reservation starts at : " + inputTime + " and last for : " + duration + " hours, and it will end at : " + endHour +":00");
                                confirm_button.setDisable(true);
                            } else {
                                noteText.setFill(Color.RED);
                                noteText.setText("Reservation is not made successfully");
                            }
                        }
                    } else {
                        noteText.setText("Please fill the information as required, remember that the maximum reservation duration is only 4 hours, and the reservations should start from 08:00 till 22:00");
                    }
                }
                else {
                    noteText.setText("Please fill all the field");
                }
            }
                catch (Exception e){
                noteText.setText("Please fill all the field");
            }


        }
    }
    //setting the UI for the user when pressing Labs & ClassRooms radioButton
    @FXML
    private void setLabs_classrooms_radioButton(ActionEvent event) throws Exception {
        if (!currentUser.getPosition().equals("Student")) {
            locationsBox.setValue("Building 63-301");
            noteText.setText("");
            timeText.setText("");
            noteText.setFill(Color.BLACK);
            locationsBox.getItems().clear();

            reasonField.clear();
            attendeeNumberBox.setVisible(false);
            attendeeText.setVisible(false);
            locationsBox.getItems().clear();
            locationArray = new String[]{"Building 63-301", "Building 5-321", "Building 4-211", "Building 5-100", "Building 22-201", "Building 7-111", "Building 11-113", "Building 24-009", "Building 4-211"};
            facilities_radioButton.setSelected(false);
            locationsBox.setDisable(false);
            locationsBox.getItems().addAll(locationArray);
            datePicker.setDisable(false);
            durationChoiceBox.setDisable(false);
            startingTimeChoiceBox.setDisable(false);
            reasonField.setDisable(false);
            confirm_button.setDisable(false);
        }
        else {
            labs_classrooms_radioButton.setDisable(true);
            facilities_radioButton.setSelected(false);
        }
    }

    //setting the UI for the user when pressing facilities radioButton
    @FXML
    private void setfacilities_radioButton(ActionEvent event) throws Exception{
        if(currentUser.getGender().equals("Female")){
            locationsBox.setValue("Gym-females");
        }
        else{
            locationsBox.setValue("Gym-males");
        }
        noteText.setText("");
        timeText.setText("");
        noteText.setFill(Color.BLACK);
        locationsBox.getItems().clear();
        reasonField.clear();
        if(currentUser.getGender().equals("Female")){
            locationArray = new String[]{"Gym-females", "Volleyball Court-females", "BasketBall Court-females ", "SwimmingPool-females", "FootBall Court-females"};
        }
        else{
            locationArray = new String[]{"Gym-males", "Volleyball Court-males", "BasketBall Court-males ", "SwimmingPool-males", "FootBall Court-males"};
        }
        attendeeNumberBox.setVisible(true);
        labs_classrooms_radioButton.setSelected(false);
        locationsBox.setDisable(false);
        locationsBox.getItems().addAll(locationArray);
        durationChoiceBox.setDisable(false);
        startingTimeChoiceBox.setDisable(false);
        datePicker.setDisable(false);
        reasonField.setDisable(false);
        confirm_button.setDisable(false);
        attendeeText.setDisable(false);
        attendeeNumberBox.setVisible(true);
        attendeeText.setVisible(true);
    }
    @FXML
    //initializing the UI for the user
    private void initialize() {
        homePageAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        confirm_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        if (currentUser.getPosition().equals("Student")) {
            labs_classrooms_radioButton.setDisable(true);
        }
        startingTimeChoiceBox.setItems(startingTimesList);
        startingTimeChoiceBox.setValue("08:00");
        attendeeNumberBox.getItems().addAll(attendeeNumberArray);
        locationsBox.setDisable(true);
        datePicker.setDisable(true);
        reasonField.setDisable(true);
        attendeeNumberBox.setVisible(false);
        confirm_button.setDisable(true);
        startingTimeChoiceBox.setDisable(true);
        attendeeText.setVisible(false);
        datePicker.setValue(LocalDate.now());
        attendeeNumberBox.setValue(2);
        durationChoiceBox.setValue(1);
        durationChoiceBox.getItems().addAll(durationList);
        durationChoiceBox.setDisable(true);
        noteText.setText("");
        timeText.setText("");
        noteText.setFill(Color.BLACK);
        currentUserName.setText(currentUser.getUserName());
        currentUserPosition.setText(currentUser.getPosition());
    }

    //checking that the end time does not exceed 22:00
    private boolean isValidEndTime(int duration, String inputStartingTime){
        int startingTime= Integer.parseInt(inputStartingTime.substring(0,2));
        int endHour = (startingTime + duration) % 24;
        if (endHour<=22 &&endHour>4){
            return true;
        }
        return false;
    }
    //store the times of the reservation in an arraylist
    private ArrayList<Integer> returnReservedHours(int starting, int ending){
        ArrayList<Integer> hoursArrayList=new ArrayList<>();
        for (int i=starting;i<ending;i++){
            hoursArrayList.add(i);
        }
        return hoursArrayList;
    }
}
