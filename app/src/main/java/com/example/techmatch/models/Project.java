package com.example.techmatch.models;

import com.example.techmatch.datastructures.LinkedList;

public class Project {
    private int id;
    private String title;
    private String category;
    private String description;
    private LinkedList<String> requiredSkills;
    private int creatorId;
    private String creatorName;
    private String date;
    private String status;
    private int teamSize;
    private int maxTeamSize;

    public Project(int id, String title, String category, String description,
                   int creatorId, String creatorName) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.requiredSkills = new LinkedList<>();
        this.status = "Açık";
        this.teamSize = 1;
        this.maxTeamSize = 5;
        this.date = getCurrentDate();
    }

    public void addRequiredSkill(String skill) {
        requiredSkills.add(skill);
    }

    public boolean addTeamMember() {
        if (teamSize < maxTeamSize) {
            teamSize++;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return teamSize >= maxTeamSize;
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

    public LinkedList<String> getRequiredSkills() {
        return requiredSkills;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new java.util.Date());
    }

    @Override
    public String toString() {
        return title + " (" + category + ")";
    }
}