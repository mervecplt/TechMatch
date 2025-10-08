package com.example.techmatch.models;

public class Project {
    private int id;
    private String name;
    private String category;
    private String description;
    private int maxParticipants;
    private int currentParticipants;

    public Project(int id, String name, String category, String description, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getMaxParticipants() { return maxParticipants; }
    public int getCurrentParticipants() { return currentParticipants; }

    // ⭐ YENİ: SearchActivity için eklenen metodlar
    public String getTitle() {
        return name; // name alanını title olarak döndür
    }

    public int getTeamSize() {
        return currentParticipants; // Mevcut katılımcı sayısı
    }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void addParticipant() { this.currentParticipants++; }

    // Yardımcı metodlar
    public boolean isFull() {
        return currentParticipants >= maxParticipants;
    }

    public int getAvailableSlots() {
        return maxParticipants - currentParticipants;
    }
}