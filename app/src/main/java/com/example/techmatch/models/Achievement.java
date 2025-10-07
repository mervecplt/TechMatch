package com.example.techmatch.models;

public class Achievement {
    private int id;
    private String title;
    private String category;
    private String description;
    private String date;
    private String type;
    private String rank;

    public Achievement(int id, String title, String category, String type) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.type = type;
        this.description = "";
        this.rank = "";
        this.date = getCurrentDate();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getRank() {
        return rank;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new java.util.Date());
    }

    @Override
    public String toString() {
        return title + " - " + type;
    }
}