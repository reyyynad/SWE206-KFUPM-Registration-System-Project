package com.example.swe_206_javafx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;

public class Controller_SignUp {
    @FXML
    private AnchorPane UserRegistration; // AnchorPane for the user registration section
    @FXML
    private PasswordField passwordTextField2; // PasswordField for entering the password
    @FXML
    private PasswordField confirmTextField2; // PasswordField for confirming the password
    @FXML
    private Label invalidLable; // Label to display invalid information messages
    @FXML
    private TextField nameTextField2; // TextField for entering the username
    @FXML
    private  TextField emailTextField2; // TextField for entering the email
    @FXML
    private ChoiceBox<String> genderChoiceBox; // ChoiceBox for selecting gender
    @FXML
    private ChoiceBox<String> positionChoiceBox; // ChoiceBox for selecting position
    @FXML
    private Button register; // Button to register new users

    private String[] gender = {"Female", "Male"}; // Array of gender options
    private String[] position = {"Staff", "Student", "club president", "resources management department employee"}; // Array of position options

    // Method to initialize the choice boxes and set the background color
    @FXML
    public void initialize() {
        genderChoiceBox.getItems().addAll(gender);
        positionChoiceBox.getItems().addAll(position);
        register.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
        UserRegistration.setBackground(new Background(new BackgroundFill(Color.rgb(0, 133, 64), null, null)));
    }

    // Method to handle the event of registering a new user
    @FXML
    public void registerButtonOnAction(ActionEvent event) throws IOException {
        String name = nameTextField2.getText();
        String email = emailTextField2.getText();
        String password = passwordTextField2.getText();
        String confirmPassword = confirmTextField2.getText();
        String gender = genderChoiceBox.getValue();
        String position = positionChoiceBox.getValue();
        boolean userExists = false;

        Person currentUser = new Person(nameTextField2.getText(), emailTextField2.getText(), genderChoiceBox.getValue(), passwordTextField2.getText(), positionChoiceBox.getValue());
        Person.setCurrentUser(currentUser);

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() &&
                gender != null && position != null) {

            if (!Character.isLetter(name.charAt(0))) {
                invalidLable.setText("Username should start with a letter");
                return;
            }

            for (Person person : RunApplication.people) {
                if (person.getUserName().equals(name) || person.getContactEmail().equals(email)) {
                    userExists = true;
                    break;
                }
            }

            if (userExists) {
                invalidLable.setText("Invalid information");
            } else if (!passwordTextField2.getText().equals(confirmTextField2.getText())) {
                invalidLable.setText("Password does not match");
            } else {
                if (position.equals("resources management department employee")) {
                    Person newPerson = new Person(name, email.toLowerCase(), gender, password, position);
                    RunApplication.people.add(newPerson);
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DepartmentPage.fxml"));
                        Common.scene = new Scene(fxmlLoader.load());
                        Common.stage.setTitle("Reservation Manager - Department Page");
                        Common.stage.setScene(Common.scene);
                        Common.stage.setResizable(false);
                        Common.stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Person newPerson = new Person(name, email, gender, password, position);
                    Person.setCurrentUser(newPerson);
                    RunApplication.people.add(newPerson);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Home_page.fxml"));
                    Common.scene = new Scene(fxmlLoader.load());
                    Common.stage.setTitle("Reservation Manager");
                    Common.stage.setScene(Common.scene);
                    Common.stage.setResizable(false);
                    Common.stage.show();
                }
            }
        } else {
            invalidLable.setText("Enter all information");
        }
    }

    // Method to switch to the sign-in page
    public void switchTo_singIn(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("signIn.fxml")));
        Common.scene = new Scene(fxmlLoader.load());
        Common.stage.setTitle("KFUPM Registration System");
        Common.stage.setScene(Common.scene);
        Common.stage.setResizable(false);
        Common.stage.show();
    }
    public String getEmail(){
        return  emailTextField2.getText();
    }
}