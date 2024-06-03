package com.example.swe_206_javafx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class RunApplication extends Application {
    public static ArrayList<Person> people = new ArrayList<>();
    public static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            // Load the sign-in screen
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("signIn.fxml")));
            Common.scene = new Scene(fxmlLoader.load());
            Common.stage.setTitle("KFUPM Registration System");
            Common.stage.setScene(Common.scene);
            Common.stage.setResizable(false);
            Common.stage.show();

            // Load data from file
            loadData();
        } catch (IOException e) {
            // Handle exception appropriately (e.g., show error dialog, log error)
            e.printStackTrace();
        }
    }

    private void loadData() throws IOException {
        File file = new File("people.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] information = line.split("=");

                    if (!(information[0].matches("\\d+"))) {
                        // Load person information
                        String userName = information[0];
                        String password = information[1];
                        String email = information[2];
                        String position = information[3];
                        String gender = information[4];

                        Person person = new Person(userName, email, gender, password, position);
                        people.add(person);
                    } else {
                        // Load reservation information
                        int bookingID = Integer.parseInt(information[0]);
                        String location = information[1];
                        LocalDate date = LocalDate.parse(information[2]);
                        String[] time = information[3].substring(1, information[3].length() - 1).split(",");
                        ArrayList<Integer> newTime = new ArrayList<>();
                        for (int i = 0; i < time.length; i++)
                            newTime.add(Integer.parseInt(time[i].trim()));

                        int attendeeNumber = Integer.parseInt(information[4]);
                        int maximumAttendeeNumber = Integer.parseInt(information[5]);
                        String gender = information[6];
                        String reason = information[7];
                        String status = information[8];

                        Reservation reservation = new Reservation(bookingID, date, newTime, location, gender, attendeeNumber, maximumAttendeeNumber, reason, status);
                        people.get(people.size() - 1).reservationList.add(reservation);
                        reservations.add(reservation);
                    }
                }
            }
        }
    }

    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("people.txt"))) {
            for (Person person : people) {
                // Save person information
                writer.write(person.getUserName() + "=" +
                        person.getPassword() + "=" +
                        person.getContactEmail() + "=" +
                        person.getPosition() + "=" +
                        person.getGender());
                writer.newLine();

                for (Reservation reservation : person.reservationList) {
                    // Save reservation information
                    writer.write(reservation.getBookingID() + "=" +
                            reservation.getLocation() + "=" +
                            reservation.getDate() + "=" +
                            reservation.getTime() + "=" +
                            reservation.getAttendeeNumber() + "=" +
                            reservation.getMaximumAttendeeNumber() + "=" +
                            reservation.getGender() + "=" +
                            reservation.getReason() + "=" +
                            reservation.getStatus());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            // Handle exception appropriately (e.g., show error dialog, log error)
            e.printStackTrace();
        }
    }
    public static void sendEmail(String recipient, String subject, String body) {
        try {
            String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            String encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            String uriString = String.format("mailto:%s?subject=%s&body=%s", recipient, encodedSubject, encodedBody);
            URI uri = new URI(uriString);

            // Open the default email client with the pre-filled recipient, subject, and body
            Desktop.getDesktop().mail(uri);

            System.out.println("Emails sent successfully.");
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            System.err.println("Desktop is not supported on this platform.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void verificationEmail(String recipient, String subject, int code) {
        try {
            String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            String encodedBody = URLEncoder.encode(String.valueOf(code), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            String uriString = String.format("mailto:%s?subject=%s&body=%s", recipient, encodedSubject, encodedBody);
            URI uri = new URI(uriString);

            // Open the default email client with the pre-filled recipient, subject, and body
            Desktop.getDesktop().mail(uri);

            System.out.println("Emails sent successfully.");
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            System.err.println("Desktop is not supported on this platform.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void stop() {
        saveData(); // Save data to file before exiting the application
    }

}