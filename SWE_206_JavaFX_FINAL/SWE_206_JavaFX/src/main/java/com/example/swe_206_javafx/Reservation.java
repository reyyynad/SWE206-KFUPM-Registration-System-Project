package com.example.swe_206_javafx;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Reservation {
    private int bookingID;
    private String location;
    private LocalDate date;
    private ArrayList<Integer> time;
    private String timeFormat;
    private int attendeeNumber;
    private int maximumAttendeeNumber;
    private String gender;
    private String reason;
    private String status;

    //constructor for labs/classrooms reservations
    //should not contain gender or max attendee number///

    /// labs and classrooms
    public Reservation(LocalDate date, ArrayList<Integer> time, String location, String reason){
        this(date, time, location, "Both",-1, reason, "Confirmed");
    }


    //////facilities reservation
    public Reservation(LocalDate date, ArrayList<Integer> time, String location, String gender, int maximumAttendeeNumber, String reason, String status){
        this.bookingID = this.hashCode();
        this.location = location;
        this.date = date;
        this.time = time;
        this.gender = gender;
        this.attendeeNumber = 1;
        this.maximumAttendeeNumber = maximumAttendeeNumber;
        this.reason = reason;
        this.timeFormat = "" + time.get(0)+ ":00 - " + (time.get(time.size()-1) + 1) + ":00";
        this.status = status;
        if (this.maximumAttendeeNumber == 1)
            this.status = "Not Confirmed";

    }

    // for reading the file:
    public Reservation(int bookingID, LocalDate date, ArrayList<Integer> time, String location, String gender, int attendeeNumber, int maximumAttendeeNumber, String reason, String status){
        this.bookingID = bookingID;
        this.location = location;
        this.date = date;
        this.time = time;
        this.gender = gender;
        this.attendeeNumber = attendeeNumber;
        this.maximumAttendeeNumber = maximumAttendeeNumber;
        this.reason = reason;
        this.timeFormat = "" + time.get(0)+ ":00 - " + (time.get(time.size()-1) + 1) + ":00";
        this.status = status;

    }

    public int getBookingID(){
        return this.bookingID;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate date){
        this.date=date;
    }
    public ArrayList<Integer> getTime(){
        return this.time;
    }
    public void setTime(ArrayList<Integer> time ){
        this.time=time;
    }
    public int getAttendeeNumber(){ return this.attendeeNumber;  }
    public void setAttendeeNumber(int attendeeNumber){ this.attendeeNumber=attendeeNumber;}
    public void increaseAttendeeNumber(){
        this.attendeeNumber++;
    }
    public int getMaximumAttendeeNumber(){
        return this.maximumAttendeeNumber;
    }
    public void setMaximumAttendeeNumber(int maximumAttendeeNumber){ this.maximumAttendeeNumber=maximumAttendeeNumber;}
    public String getGender(){
        return this.gender;
    }
    public String getReason(){
        return this.reason;
    }
    public void setReason(String reason){
        this.reason=reason;
    }
    public String getTimeFormat(){return this.timeFormat; }
    public void setTimeFormat(String timeFormat){this.timeFormat = timeFormat;}
    public void setStatus(String status){this.status = status;}
    public String getStatus(){return this.status;}



    //to check that two reservation have the same id
    public boolean equalReservation(Object that){
        if(that instanceof Reservation){
            return (this.bookingID == ((Reservation) that).bookingID);
        }
        else {
            return false;
        }
    }

    //return true if they conflict
    //false if they dont
    public boolean checkConflictingTime(ArrayList<Integer> otherArrayList){
        try {
            Set<Integer> theSet=new HashSet<>(otherArrayList);
                ArrayList<Integer> thisReservationTimes=this.time;
                for(Integer i:thisReservationTimes){
                    if(theSet.contains(i)){
                        return true;
                    }
                }


            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean checkSystemConflict(Reservation systemReservation){
        return this.getDate().equals(systemReservation.getDate()) &&
                this.getLocation().equals(systemReservation.getLocation()) &&
                checkConflictingTime(systemReservation.time);

    }


    public boolean checkTimeAndDateConflict(Reservation currentUserReservationList){
        Boolean checkDateConflict=false;
        Boolean checkTimeConflict=false;
        LocalDate myCurrentDate=currentUserReservationList.getDate();
        ArrayList<Integer> myCurrentTime=currentUserReservationList.time;
            if(this.getDate().equals(myCurrentDate)){
                checkDateConflict=true;
                if(checkConflictingTimeForSameUser(myCurrentTime)){
                    checkTimeConflict=true;
                    return true;
                }
            }
        return false;
    }

    public boolean checkConflictingTimeForSameUser(ArrayList<Integer> otherArrayList){
        try {
            Set<Integer> theSet=new HashSet<>(otherArrayList);
                ArrayList<Integer> thisTime=this.time;
                for(Integer i:thisTime){
                    if(theSet.contains(i)){
                        return true;
                    }
                }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }
}







