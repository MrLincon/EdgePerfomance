package io.edgeperformance.edge.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Notification {

    private String title, details;
    private @ServerTimestamp
    Date timestamp;

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
