package com.example.swe_206_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;

public class Controller_SignIn {


    @FXML
    private TextField UserNameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label invalidLable;
    @FXML
    private Button LoginButton;
    @FXML
    private Button singUpButton;

    @FXML
    private void initialize(){
        LoginButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));

    }


    public void switchTo_Home_page(javafx.event.ActionEvent actionEvent) throws IOException {
        String name = UserNameTextField.getText();
        String password = passwordTextField.getText();


        if (!UserNameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            for (Person person : RunApplication.people) {
                if (person.getUserName().equals(name) && person.getPassword().equals(password)) {
                    Person.setCurrentUser(person);

                    if (person.getPosition().equals("resources management department employee")) {

                        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("DepartmentPage.fxml")));
                        Common.scene = new Scene(fxmlLoader.load());
                        Common.stage.setTitle("KFUPM Registration System");
                        Common.stage.setScene(Common.scene);
                        Common.stage.setResizable(false);
                        Common.stage.show();

                    } else {
                        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Home_page.fxml")));
                        Common.scene = new Scene(fxmlLoader.load());
                        Common.stage.setTitle("KFUPM Registration System");
                        Common.stage.setScene(Common.scene);
                        Common.stage.setResizable(false);
                        Common.stage.show();

                    }

                }
                else {
                    invalidLable.setText(" Incorrect Username or password ");
                }

            }
        }
        else {
            invalidLable.setText(" Enter Information");
        }

    }

    public void switchTo_singUp(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("signUp.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }
}