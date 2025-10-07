package com.example.techmatch.models;

public class TeamRequest {
    private int id;
    private int userId;
    private String userName;
    private int projectId;
    private String projectTitle;
    private String message;
    private String date;
    private String status;

    public TeamRequest(int id, int userId, String userName,
                       int projectId, String projectTitle, String message) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.message = message;
        this.status = "Beklemede";
        this.date = getCurrentDate();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new java.util.Date());
    }

    @Override
    public String toString() {
        return userName + " → " + projectTitle;
    }
}