package com.example.techmatch.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

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
            Log.d(TAG, "Kullanıcı yüklendi: " + currentUser.getName() + " (ID: " + currentUser.getId() + ")");
            loadCurrentData();
        } else {
            Log.e(TAG, "Kullanıcı bulunamadı!");
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // ⭐ PROJE EKLE - Geliştirilmiş Debug
        btnAddProject.setOnClickListener(v -> {
            String project = etProject.getText().toString().trim();
            if (!project.isEmpty()) {
                Log.d(TAG, "=== PROJE EKLEME DETAYLI TEST ===");
                Log.d(TAG, "Eklenecek proje: " + project);
                Log.d(TAG, "currentUser null mu?: " + (currentUser == null));
                Log.d(TAG, "getProjects() null mu?: " + (currentUser.getProjects() == null));

                // Liste null ise boş liste oluştur
                if (currentUser.getProjects() == null) {
                    Log.e(TAG, "HATA: Proje listesi NULL! Yeni liste oluşturuluyor...");
                    currentUser.setProjects(new java.util.ArrayList<>());
                }

                Log.d(TAG, "Ekleme öncesi boyut: " + currentUser.getProjects().size());
                Log.d(TAG, "Ekleme öncesi içerik: " + currentUser.getProjects());

                // Listeye ekle
                currentUser.addProject(project);

                Log.d(TAG, "addProject() çağrıldı");
                Log.d(TAG, "Ekleme sonrası boyut: " + currentUser.getProjects().size());
                Log.d(TAG, "Ekleme sonrası içerik: " + currentUser.getProjects());

                // Kaydet
                dataManager.updateUser(currentUser);
                Log.d(TAG, "updateUser() çağrıldı");

                // Kontrol: Gerçekten kaydedildi mi?
                User checkUser = dataManager.getUserById(currentUser.getId());
                if (checkUser != null) {
                    Log.d(TAG, "Kayıt sonrası kontrol - Liste boyutu: " + checkUser.getProjects().size());
                    Log.d(TAG, "Kayıt sonrası kontrol - İçerik: " + checkUser.getProjects());
                    Log.d(TAG, "Kayıt sonrası kontrol - Liste null mu?: " + (checkUser.getProjects() == null));
                } else {
                    Log.e(TAG, "HATA: Kullanıcı kaydedildikten sonra bulunamadı!");
                }

                etProject.setText("");
                Toast.makeText(this, "Proje eklendi ✓", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "=== PROJE EKLEME BİTTİ ===\n");
            } else {
                Toast.makeText(this, "Proje bilgisi boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        // ⭐ İŞ DENEYİMİ EKLE - Geliştirilmiş Debug
        btnAddWorkExperience.setOnClickListener(v -> {
            String experience = etWorkExperience.getText().toString().trim();
            if (!experience.isEmpty()) {
                Log.d(TAG, "=== İŞ DENEYİMİ EKLEME BAŞLADI ===");
                Log.d(TAG, "Eklenecek deneyim: " + experience);

                // Liste null ise boş liste oluştur
                if (currentUser.getWorkExperience() == null) {
                    Log.e(TAG, "HATA: İş deneyimi listesi NULL! Yeni liste oluşturuluyor...");
                    currentUser.setWorkExperience(new java.util.ArrayList<>());
                }

                Log.d(TAG, "Ekleme öncesi boyut: " + currentUser.getWorkExperience().size());

                currentUser.addWorkExperience(experience);
                Log.d(TAG, "Ekleme sonrası boyut: " + currentUser.getWorkExperience().size());

                dataManager.updateUser(currentUser);
                Log.d(TAG, "updateUser() çağrıldı");

                User checkUser = dataManager.getUserById(currentUser.getId());
                if (checkUser != null) {
                    Log.d(TAG, "Kayıt sonrası kontrol - Liste boyutu: " + checkUser.getWorkExperience().size());
                }

                etWorkExperience.setText("");
                Toast.makeText(this, "İş deneyimi eklendi ✓", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "=== İŞ DENEYİMİ EKLEME BİTTİ ===\n");
            } else {
                Toast.makeText(this, "İş deneyimi boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        // ⭐ BAŞARI EKLE - Geliştirilmiş Debug
        btnAddAchievement.setOnClickListener(v -> {
            String achievement = etAchievement.getText().toString().trim();
            if (!achievement.isEmpty()) {
                Log.d(TAG, "=== BAŞARI EKLEME BAŞLADI ===");
                Log.d(TAG, "Eklenecek başarı: " + achievement);

                // Liste null ise boş liste oluştur
                if (currentUser.getAchievements() == null) {
                    Log.e(TAG, "HATA: Başarı listesi NULL! Yeni liste oluşturuluyor...");
                    currentUser.setAchievements(new java.util.ArrayList<>());
                }

                Log.d(TAG, "Ekleme öncesi boyut: " + currentUser.getAchievements().size());

                currentUser.addAchievement(achievement);
                Log.d(TAG, "Ekleme sonrası boyut: " + currentUser.getAchievements().size());

                dataManager.updateUser(currentUser);
                Log.d(TAG, "updateUser() çağrıldı");

                User checkUser = dataManager.getUserById(currentUser.getId());
                if (checkUser != null) {
                    Log.d(TAG, "Kayıt sonrası kontrol - Liste boyutu: " + checkUser.getAchievements().size());
                }

                etAchievement.setText("");
                Toast.makeText(this, "Başarı eklendi ✓", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "=== BAŞARI EKLEME BİTTİ ===\n");
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

        Log.d(TAG, "=== MEVCUT VERİLER YÜKLENDI ===");
        Log.d(TAG, "Projeler null mu?: " + (currentUser.getProjects() == null));
        Log.d(TAG, "Projeler boyut: " + (currentUser.getProjects() != null ? currentUser.getProjects().size() : "NULL"));
        Log.d(TAG, "İş deneyimleri boyut: " + (currentUser.getWorkExperience() != null ? currentUser.getWorkExperience().size() : "NULL"));
        Log.d(TAG, "Başarılar boyut: " + (currentUser.getAchievements() != null ? currentUser.getAchievements().size() : "NULL"));
    }

    private void saveProfile() {
        Log.d(TAG, "=== PROFİL KAYDETME BAŞLADI ===");

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
        Log.d(TAG, "Profil kaydedildi - updateUser() çağrıldı");
        Log.d(TAG, "=== PROFİL KAYDETME BİTTİ ===\n");

        Toast.makeText(this, "Profil güncellendi! ✓", Toast.LENGTH_SHORT).show();
        finish(); // ProfileActivity'ye geri dön
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() - Activity durduruluyor");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - Activity kapatılıyor");
    }
}