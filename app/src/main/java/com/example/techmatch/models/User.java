package com.example.techmatch.models;

import com.example.techmatch.datastructures.LinkedList;

public class User {
    private int id;
    private String name;
    private String department;
    private String email;
    private String phone;
    private LinkedList<String> skills;
    private LinkedList<String> categories;
    private LinkedList<Achievement> achievements;
    private String bio;
    private int projectCount;

    public User(int id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.skills = new LinkedList<>();
        this.categories = new LinkedList<>();
        this.achievements = new LinkedList<>();
        this.bio = "";
        this.projectCount = 0;
    }

    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
        projectCount++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LinkedList<String> getSkills() {
        return skills;
    }

    public LinkedList<String> getCategories() {
        return categories;
    }

    public LinkedList<Achievement> getAchievements() {
        return achievements;
    }

    public String getBio() {
        return bio;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean hasSkill(String skill) {
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).equalsIgnoreCase(skill)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCategory(String category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " - " + department;
    }
}