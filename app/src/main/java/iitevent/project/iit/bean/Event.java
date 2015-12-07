package iitevent.project.iit.bean;

import java.util.Date;

/**
 * Created by demo on 21-11-2015.
 */
public class Event {
    private String eventName;
    private int eventID;
    private String eventDescription;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    public Event(){

    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Event(String eventName, int eventID, String eventDescription, String eventLocation, String eventDate, String eventTime) {
        this.eventName = eventName;
        this.eventID = eventID;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }


}
