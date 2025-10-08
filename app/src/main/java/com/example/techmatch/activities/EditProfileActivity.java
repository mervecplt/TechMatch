package com.example.techmatch.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;

public class EditProfileActivity extends AppCompatActivity {

    private TextView btnBack;
    private EditText etBio, etSkills, etUniversity, etGraduationYear, etGpa;
    private EditText etProject, etWorkExperience, etAchievement;
    private Button btnAddProject, btnAddWorkExperience, btnAddAchievement;
    private Button btnSaveProfile;

    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        etBio = findViewById(R.id.etBio);
        etSkills = findViewById(R.id.etSkills);
        etUniversity = findViewById(R.id.etUniversity);
        etGraduationYear = findViewById(R.id.etGraduationYear);
        etGpa = findViewById(R.id.etGpa);
        etProject = findViewById(R.id.etProject);
        etWorkExperience = findViewById(R.id.etWorkExperience);
        etAchievement = findViewById(R.id.etAchievement);
        btnAddProject = findViewById(R.id.btnAddProject);
        btnAddWorkExperience = findViewById(R.id.btnAddWorkExperience);
        btnAddAchievement = findViewById(R.id.btnAddAchievement);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Kullanıcıyı al
        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            currentUser = dataManager.getCurrentUser();
        } else {
            currentUser = dataManager.getUserById(userId);
        }

        if (currentUser != null) {
            loadCurrentData();
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // Proje ekle
        btnAddProject.setOnClickListener(v -> {
            String project = etProject.getText().toString().trim();
            if (!project.isEmpty()) {
                currentUser.addProject(project);
                etProject.setText("");
                Toast.makeText(this, "Proje eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Proje bilgisi boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        // İş deneyimi ekle
        btnAddWorkExperience.setOnClickListener(v -> {
            String experience = etWorkExperience.getText().toString().trim();
            if (!experience.isEmpty()) {
                currentUser.addWorkExperience(experience);
                etWorkExperience.setText("");
                Toast.makeText(this, "İş deneyimi eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "İş deneyimi boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        // Başarı ekle
        btnAddAchievement.setOnClickListener(v -> {
            String achievement = etAchievement.getText().toString().trim();
            if (!achievement.isEmpty()) {
                currentUser.addAchievement(achievement);
                etAchievement.setText("");
                Toast.makeText(this, "Başarı eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Başarı bilgisi boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        // Profili kaydet
        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        // Mevcut verileri form'a yükle
        etBio.setText(currentUser.getBio());
        etSkills.setText(currentUser.getSkills());
        etUniversity.setText(currentUser.getUniversity());
        etGraduationYear.setText(currentUser.getGraduationYear());
        if (currentUser.getGpa() > 0) {
            etGpa.setText(String.valueOf(currentUser.getGpa()));
        }
    }

    private void saveProfile() {
        // Temel bilgileri kaydet
        currentUser.setBio(etBio.getText().toString().trim());
        currentUser.setSkills(etSkills.getText().toString().trim());
        currentUser.setUniversity(etUniversity.getText().toString().trim());
        currentUser.setGraduationYear(etGraduationYear.getText().toString().trim());

        // GPA'yı kaydet
        String gpaStr = etGpa.getText().toString().trim();
        if (!gpaStr.isEmpty()) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (gpa >= 0 && gpa <= 4.0) {
                    currentUser.setGpa(gpa);
                } else {
                    Toast.makeText(this, "GPA 0-4.0 arasında olmalı", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Geçersiz GPA değeri", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // DataManager'a kaydet
        dataManager.updateUser(currentUser);

        Toast.makeText(this, "Profil güncellendi!", Toast.LENGTH_SHORT).show();
        finish(); // ProfileActivity'ye geri dön
    }
}