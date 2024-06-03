package com.example.swe_206_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;


public class Controller_HomePage {

    // variables
    @FXML
    private AnchorPane homePageAnchorPane;
    @FXML
    private Button create_button = new Button();
    @FXML
    private Button edit_delete_button = new Button();
    @FXML
    private Button view_button = new Button();
    @FXML
    private Button join_button = new Button();
    @FXML
    private Button log_out_button = new Button();
    @FXML
    private Text currentUserName = new Text();
    @FXML
    private Text currentUserPosition = new Text();

    // methods
    // initialize: to set colors and texts once this controller is activated
    @FXML
    private void initialize(){
        currentUserName.setText(Person.getCurrentUser().getUserName());
        currentUserPosition.setText(Person.getCurrentUser().getPosition());
        homePageAnchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        create_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        edit_delete_button.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
    }

    // switch_scenes: this method is used to switch scenes based on the button pressed while running the application
    @FXML
    public void switch_scenes(ActionEvent event) throws IOException{
        String fxmlName = "Home_page.fxml";

        if (event.getSource() == create_button)
            fxmlName = "Create_reservation.fxml";
        if (event.getSource() == edit_delete_button)
            fxmlName = "Modify_reservation.fxml";
        if (event.getSource() == view_button)
            fxmlName = "View_reservation.fxml";
        if (event.getSource() == join_button)
            fxmlName = "Join_reservation.fxml";
        if (event.getSource() == log_out_button)
            fxmlName = "signIn.fxml";


        // load and display the scene after taking the fxmlName
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlName)));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }

}
