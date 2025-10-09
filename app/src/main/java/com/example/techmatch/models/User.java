package com.example.techmatch.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String department;
    private String bio;
    private String skills;

    // LinkedIn tarzı yeni alanlar
    private String university;
    private String graduationYear;
    private double gpa;
    private List<String> projects;
    private List<String> workExperience;
    private List<String> achievements;
    private List<String> certificates;

    public User(int id, String name, String email, String password, String department, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.bio = bio;
        this.skills = "";

        // Yeni alanları başlat
        this.university = "";
        this.graduationYear = "";
        this.gpa = 0.0;
        this.projects = new ArrayList<>();
        this.workExperience = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.certificates = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDepartment() { return department; }
    public String getBio() { return bio; }
    public String getSkills() { return skills; }
    public String getUniversity() { return university; }
    public String getGraduationYear() { return graduationYear; }
    public double getGpa() { return gpa; }
    public List<String> getProjects() { return projects; }
    public List<String> getWorkExperience() { return workExperience; }
    public List<String> getAchievements() { return achievements; }
    public List<String> getCertificates() { return certificates; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setDepartment(String department) { this.department = department; }
    public void setBio(String bio) { this.bio = bio; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setUniversity(String university) { this.university = university; }
    public void setGraduationYear(String graduationYear) { this.graduationYear = graduationYear; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    // Liste yönetimi
    public void addProject(String project) {
        if (this.projects == null) {
            this.projects = new ArrayList<>();
        }
        this.projects.add(project);
    }

    public void addWorkExperience(String experience) {
        if (this.workExperience == null) {
            this.workExperience = new ArrayList<>();
        }
        this.workExperience.add(experience);
    }

    public void addAchievement(String achievement) {
        if (this.achievements == null) {
            this.achievements = new ArrayList<>();
        }
        this.achievements.add(achievement);
    }

    public void addCertificate(String certificate) {
        if (this.certificates == null) {
            this.certificates = new ArrayList<>();
        }
        this.certificates.add(certificate);
    }

    // ⭐ Liste setter'ları (Gson için gerekli)
    public void setProjects(List<String> projects) {
        this.projects = (projects != null) ? projects : new ArrayList<>();
    }

    public void setWorkExperience(List<String> workExperience) {
        this.workExperience = (workExperience != null) ? workExperience : new ArrayList<>();
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = (achievements != null) ? achievements : new ArrayList<>();
    }

    public void setCertificates(List<String> certificates) {
        this.certificates = (certificates != null) ? certificates : new ArrayList<>();
    }
}