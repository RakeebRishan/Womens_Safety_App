package com.nibm.sos_app;

public class Story {
    private String title;
    private String description;

    // Default constructor required for Firebase
    public Story() { }

    // Constructor
    public Story(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

