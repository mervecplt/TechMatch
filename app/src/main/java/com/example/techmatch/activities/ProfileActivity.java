package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private TextView btnBack;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private TextView tvUserBio, tvUserSkills;
    private TextView tvEducationInfo, tvGpaInfo;
    private LinearLayout llProjects, llWorkExperience, llAchievements;
    private Button btnEditProfile;

    private DataManager dataManager;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate() başladı");

        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDepartment = findViewById(R.id.tvUserDepartment);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserBio = findViewById(R.id.tvUserBio);
        tvUserSkills = findViewById(R.id.tvUserSkills);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Yeni alanlar
        tvEducationInfo = findViewById(R.id.tvEducationInfo);
        tvGpaInfo = findViewById(R.id.tvGpaInfo);
        llProjects = findViewById(R.id.llProjects);
        llWorkExperience = findViewById(R.id.llWorkExperience);
        llAchievements = findViewById(R.id.llAchievements);

        // Kullanıcı ID'sini al ve sakla
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            // ID gönderilmemişse mevcut kullanıcıyı al
            currentUser = dataManager.getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getId();
            }
        } else {
            currentUser = dataManager.getUserById(userId);
        }

        if (currentUser != null) {
            Log.d(TAG, "Kullanıcı yüklendi: " + currentUser.getName() + " (ID: " + userId + ")");
            displayUserInfo();
        } else {
            Log.e(TAG, "HATA: Kullanıcı bulunamadı!");
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // Profil düzenle butonu
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", currentUser.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "\n=== onResume() BAŞLADI ===");

        // ⭐ EditProfile'dan döndüğünde MUTLAKA yeniden yükle
        if (userId != -1) {
            Log.d(TAG, "Kullanıcı yeniden yükleniyor - ID: " + userId);

            // DataManager'dan GÜNCEL kullanıcıyı çek
            User updatedUser = dataManager.getUserById(userId);

            if (updatedUser != null) {
                currentUser = updatedUser;

                Log.d(TAG, "✅ Güncel kullanıcı alındı: " + currentUser.getName());
                Log.d(TAG, "Nesne referansı: " + System.identityHashCode(currentUser));

                // Liste durumlarını logla
                Log.d(TAG, "--- LİSTE DURUMLARI ---");
                Log.d(TAG, "Projeler: " + (currentUser.getProjects() != null ? currentUser.getProjects().size() : "NULL") + " adet");
                Log.d(TAG, "Proje listesi: " + currentUser.getProjects());
                Log.d(TAG, "İş deneyimleri: " + (currentUser.getWorkExperience() != null ? currentUser.getWorkExperience().size() : "NULL") + " adet");
                Log.d(TAG, "Başarılar: " + (currentUser.getAchievements() != null ? currentUser.getAchievements().size() : "NULL") + " adet");
                Log.d(TAG, "----------------------");

                // Ekranı yenile
                displayUserInfo();

                Log.d(TAG, "✅ Ekran yenilendi");
            } else {
                Log.e(TAG, "❌ HATA: Güncel kullanıcı alınamadı!");
            }
        }

        Log.d(TAG, "=== onResume() BİTTİ ===\n");
    }

    private void displayUserInfo() {
        Log.d(TAG, "\n=== displayUserInfo() BAŞLADI ===");

        // Temel bilgiler
        tvUserName.setText(currentUser.getName());
        tvUserDepartment.setText(currentUser.getDepartment());
        tvUserEmail.setText(currentUser.getEmail());

        // ⭐ Bio - null check
        String bio = currentUser.getBio();
        tvUserBio.setText((bio == null || bio.isEmpty()) ?
                "Henüz biyografi eklenmemiş. Profili düzenle butonuna tıklayarak ekleyebilirsin." : bio);

        // ⭐ Beceriler - String olarak (List değil!)
        String skillsString = currentUser.getSkills();
        tvUserSkills.setText((skillsString == null || skillsString.isEmpty()) ?
                "Beceri eklenmemiş" : skillsString);

        // ⭐ Eğitim bilgileri - null check
        String university = currentUser.getUniversity();
        String graduationYear = currentUser.getGraduationYear();

        if (university != null && !university.isEmpty()) {
            String eduText = university;
            if (graduationYear != null && !graduationYear.isEmpty()) {
                eduText += " - " + graduationYear;
            }
            tvEducationInfo.setText(eduText);
        } else {
            tvEducationInfo.setText("Eğitim bilgisi eklenmemiş");
        }

        // ⭐ GPA - null check
        if (currentUser.getGpa() > 0) {
            tvGpaInfo.setText("GPA: " + String.format("%.2f", currentUser.getGpa()));
        } else {
            tvGpaInfo.setText("GPA: Girilmemiş");
        }

        // ⭐ Projeler - null check ve detaylı log
        List<String> projects = currentUser.getProjects();
        Log.d(TAG, "--- PROJELER GÖSTERİLİYOR ---");
        Log.d(TAG, "Projects null mu?: " + (projects == null));
        Log.d(TAG, "Projects boyut: " + (projects != null ? projects.size() : "NULL"));
        if (projects != null && !projects.isEmpty()) {
            Log.d(TAG, "✅ Proje içeriği: " + projects);
            for (int i = 0; i < projects.size(); i++) {
                Log.d(TAG, "  [" + i + "] = " + projects.get(i));
            }
        } else {
            Log.d(TAG, "⚠️ Proje listesi boş veya null!");
        }
        displayList(llProjects, projects, "Henüz proje eklenmemiş");

        // ⭐ İş deneyimi - null check ve log
        List<String> workExperience = currentUser.getWorkExperience();
        Log.d(TAG, "--- İŞ DENEYİMLERİ GÖSTERİLİYOR ---");
        Log.d(TAG, "WorkExperience boyut: " + (workExperience != null ? workExperience.size() : "NULL"));
        displayList(llWorkExperience, workExperience, "Henüz iş deneyimi eklenmemiş");

        // ⭐ Başarılar - null check ve log
        List<String> achievements = currentUser.getAchievements();
        Log.d(TAG, "--- BAŞARILAR GÖSTERİLİYOR ---");
        Log.d(TAG, "Achievements boyut: " + (achievements != null ? achievements.size() : "NULL"));
        displayList(llAchievements, achievements, "Henüz başarı eklenmemiş");

        Log.d(TAG, "=== displayUserInfo() TAMAMLANDI ===\n");
    }

    private void displayList(LinearLayout container, List<String> items, String emptyMessage) {
        Log.d(TAG, "displayList() çağrıldı");

        container.removeAllViews();

        // ⭐ Null check eklendi
        if (items == null || items.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText(emptyMessage);
            emptyView.setTextColor(0xFF999999);
            emptyView.setPadding(16, 8, 16, 8);
            container.addView(emptyView);
            Log.d(TAG, "⚠️ Liste boş gösteriliyor: " + emptyMessage);
        } else {
            Log.d(TAG, "✅ Liste gösteriliyor - " + items.size() + " öğe");
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                TextView itemView = new TextView(this);
                itemView.setText("• " + item);
                itemView.setTextSize(14);
                itemView.setPadding(16, 8, 16, 8);
                container.addView(itemView);
                Log.d(TAG, "  Öğe [" + i + "] container'a eklendi: " + item);
            }
            Log.d(TAG, "✅ Tüm öğeler başarıyla eklendi");
        }
    }
}