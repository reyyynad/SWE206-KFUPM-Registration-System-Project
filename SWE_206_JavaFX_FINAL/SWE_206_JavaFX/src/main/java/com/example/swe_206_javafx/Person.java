package com.example.swe_206_javafx;
import java.time.LocalDate;
import java.util.ArrayList;

public class Person {
    // Variables
    public static Person currentUser;
    private String contactEmail;
    private String gender;
    public ArrayList<Reservation> reservationList = new ArrayList<>();
    private String userName;
    private String password;
    private String position;


    // constructor
    public Person(String userName,String contactEmail,String gender,String password,String position){
        this.userName = userName;
        this.contactEmail=contactEmail;
        this.gender=gender;
        this.password=password;
        this.position=position;
    }

    // getters and setters:
    public String getPosition(){
        return this.position;
    }
    public String getGender(){
        return this.gender;
    }
    public String getContactEmail(){
        return this.contactEmail;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public static void setCurrentUser(Person person){
        currentUser = person;
    }
    public static Person getCurrentUser(){
        return currentUser;
    }


    //a method to join other's events
    public boolean joinASportEvent(int chosenID){
        // go through the system list
        ArrayList<Reservation> systemReservation= RunApplication.reservations;
        for(Reservation reservation:systemReservation){
            // finding a reservation with a given booking ID
            if(reservation.getBookingID() == chosenID){
                reservation.increaseAttendeeNumber();
                currentUser.reservationList.add(reservation);
                if (reservation.getAttendeeNumber() == reservation.getMaximumAttendeeNumber()){
                    reservation.setStatus("Confirmed");
                }
                return true;
            }
        }

        // Otherwise
        return false;
    }


    // Facilities:
    public Boolean createEvent(LocalDate date, ArrayList<Integer> time, String location, String gender, String reason,int maximumAttendeeNumber){
        // create a reservation object
        Reservation reservation=new Reservation(date,time,location,gender,maximumAttendeeNumber, reason, "Not Confirmed");

        // calling the system's reservation list
        ArrayList<Reservation> systemReservation= RunApplication.reservations;
        for (int i=0; i < systemReservation.size(); i++) {
            // checking conflict
            if (reservation.checkSystemConflict(systemReservation.get(i))) {
                return false;
            }
        }

        // calling the current user list
        ArrayList<Reservation> currentUserReservationList = currentUser.reservationList;
        for(int i=0; i < currentUserReservationList.size();i++) {
            // checking conflict
            if (reservation.checkTimeAndDateConflict(currentUserReservationList.get(i))) {
                return false;
            }
        }

        //the reservation now is added to the person's reservationsList
        reservationList.add(reservation);
        //the reservation should be added to the system reservationsList
        RunApplication.reservations.add(reservation);

        // to confirm creating event
        return true;

    }

    public Boolean reserveLabOrClassroom(LocalDate date, ArrayList<Integer> time, String location, String gender, String reason){
        // create a reservation object
        Reservation reservation=new Reservation(date,time,location,reason);

        //should return false indicating that the reservation is not made
        ArrayList<Reservation> systemReservation= RunApplication.reservations;
        for (int i=0;i<systemReservation.size();i++) {
            if (reservation.checkSystemConflict(systemReservation.get(i))) {
                return false;
            }
        }


        ArrayList<Reservation> currentUserReservationList=currentUser.reservationList;
        for(int i=0;i<currentUserReservationList.size();i++) {
            if (reservation.checkTimeAndDateConflict(currentUserReservationList.get(i))) {
                return false;
            }
        }

        //the reservation now is added to the person's reservationsList
        reservationList.add(reservation);
        //the reservation should be added to the system reservationsList
        RunApplication.reservations.add(reservation);

        return true;
    }


    public boolean cancelReservation(Reservation reservation){
        for (int i = 0; i < reservationList.size(); i++){

            if(reservationList.get(i).equalReservation(reservation)){

                this.reservationList.remove(reservation);

                //it should also cancel it from the system's reservations
                //renad
                RunApplication.reservations.remove(reservation);

                //it should sent email
                //SendEmail.email(contactEmail, "cancel");
                return true;
            }
        }
        return false;
    }

    //after i confirm a reservation should i add it to the reservation list of the person?
    //or just change its status
    // i think we should remove the method

    public boolean confirmReservation(Reservation reservation) {
        if (reservation.getAttendeeNumber() == reservation.getMaximumAttendeeNumber()) {
            //SendEmail.email(contactEmail, "confirm");
            return true;
        }
        return false;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getUserName()).append(",");
        sb.append(getPassword()).append(",");
        sb.append(getContactEmail()).append(",");
        sb.append(getPosition()).append(",");
        sb.append(getGender()).append(",");



        for (Reservation reservation : reservationList) {
            sb.append(reservation.getBookingID()).append(",");
            sb.append(reservation.getLocation()).append(",");
            sb.append(reservation.getDate()).append(",");
            sb.append(reservation.getTime()).append(",");
            sb.append(reservation.getAttendeeNumber()).append(",");
            sb.append(reservation.getMaximumAttendeeNumber()).append(",");
            sb.append(reservation.getGender()).append(",");
            sb.append(reservation.getReason()).append(",");
        }

        return sb.toString();
    }




}
