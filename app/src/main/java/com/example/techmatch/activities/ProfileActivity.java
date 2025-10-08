package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private TextView tvUserBio, tvUserSkills;
    private TextView tvEducationInfo, tvGpaInfo;
    private LinearLayout llProjects, llWorkExperience, llAchievements;
    private Button btnViewPortfolio, btnEditProfile;

    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDepartment = findViewById(R.id.tvUserDepartment);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserBio = findViewById(R.id.tvUserBio);
        tvUserSkills = findViewById(R.id.tvUserSkills);
        btnViewPortfolio = findViewById(R.id.btnViewPortfolio);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Yeni alanlar
        tvEducationInfo = findViewById(R.id.tvEducationInfo);
        tvGpaInfo = findViewById(R.id.tvGpaInfo);
        llProjects = findViewById(R.id.llProjects);
        llWorkExperience = findViewById(R.id.llWorkExperience);
        llAchievements = findViewById(R.id.llAchievements);

        // Kullanıcı ID'sini al
        int userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            // ID gönderilmemişse mevcut kullanıcıyı al
            currentUser = dataManager.getCurrentUser();
        } else {
            currentUser = dataManager.getUserById(userId);
        }

        if (currentUser != null) {
            displayUserInfo();
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // Portfolyo butonu
        btnViewPortfolio.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PortfolioActivity.class);
            intent.putExtra("USER_ID", currentUser.getId());
            startActivity(intent);
        });

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
        // Profil düzenlemeden döndüğünde yenile
        if (currentUser != null) {
            currentUser = dataManager.getUserById(currentUser.getId());
            if (currentUser != null) {
                displayUserInfo();
            }
        }
    }

    private void displayUserInfo() {
        // Temel bilgiler
        tvUserName.setText(currentUser.getName());
        tvUserDepartment.setText(currentUser.getDepartment());
        tvUserEmail.setText(currentUser.getEmail());

        // ⭐ Bio - null check
        String bio = currentUser.getBio();
        tvUserBio.setText((bio == null || bio.isEmpty()) ?
                "Henüz biyografi eklenmemiş. Profili düzenle butonuna tıklayarak ekleyebilirsin." : bio);

        // ⭐ Beceriler - null check
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

        // ⭐ Projeler - null check
        List<String> projects = currentUser.getProjects();
        displayList(llProjects, projects, "Henüz proje eklenmemiş");

        // ⭐ İş deneyimi - null check
        List<String> workExperience = currentUser.getWorkExperience();
        displayList(llWorkExperience, workExperience, "Henüz iş deneyimi eklenmemiş");

        // ⭐ Başarılar - null check
        List<String> achievements = currentUser.getAchievements();
        displayList(llAchievements, achievements, "Henüz başarı eklenmemiş");
    }

    private void displayList(LinearLayout container, List<String> items, String emptyMessage) {
        container.removeAllViews();

        // ⭐ Null check eklendi
        if (items == null || items.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText(emptyMessage);
            emptyView.setTextColor(0xFF999999);
            emptyView.setPadding(16, 8, 16, 8);
            container.addView(emptyView);
        } else {
            for (String item : items) {
                TextView itemView = new TextView(this);
                itemView.setText("• " + item);
                itemView.setTextSize(14);
                itemView.setPadding(16, 8, 16, 8);
                container.addView(itemView);
            }
        }
    }
}