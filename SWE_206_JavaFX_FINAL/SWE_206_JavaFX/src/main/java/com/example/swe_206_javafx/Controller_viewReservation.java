package com.example.swe_206_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_viewReservation implements Initializable {

    @FXML
    private AnchorPane viewReservationAnchor;
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
    private TableColumn<Reservation, Integer> attendeeNumber;

    @FXML
    private TableColumn<Reservation, String> status;

    @FXML
    private Button back_button = new Button();

    private ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();



    public ObservableList<Reservation> getReservationObservableList() {

        ArrayList<Reservation> userReservation = Person.getCurrentUser().reservationList;

        reservationObservableList.addAll(userReservation);

        return reservationObservableList;
    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        viewReservationAnchor.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));

        try{
            bookingID.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeFormat.setCellValueFactory(new PropertyValueFactory<>("timeFormat"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
            attendeeNumber.setCellValueFactory(new PropertyValueFactory<>("attendeeNumber"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            reservationData.setItems(getReservationObservableList());
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }


    }

    // switch scene:
    public void switchTo_home_page_scene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Home_page.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }


}
