package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.datastructures.LinkedList;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private TextView tvUserBio, tvUserSkills;
    private Button btnViewPortfolio;

    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataManager = DataManager.getInstance();

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDepartment = findViewById(R.id.tvUserDepartment);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserBio = findViewById(R.id.tvUserBio);
        tvUserSkills = findViewById(R.id.tvUserSkills);
        btnViewPortfolio = findViewById(R.id.btnViewPortfolio);

        // Kullanıcı ID'sini al
        int userId = getIntent().getIntExtra("USER_ID", 1);
        currentUser = dataManager.getUserById(userId);

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
    }

    private void displayUserInfo() {
        tvUserName.setText(currentUser.getName());
        tvUserDepartment.setText(currentUser.getDepartment());
        tvUserEmail.setText(currentUser.getEmail());
        tvUserBio.setText(currentUser.getBio().isEmpty() ?
                "Henüz biyografi eklenmemiş" : currentUser.getBio());

        // Becerileri göster
        LinkedList<String> skills = currentUser.getSkills();
        StringBuilder skillsStr = new StringBuilder();

        for (int i = 0; i < skills.size(); i++) {
            if (i > 0) skillsStr.append(", ");
            skillsStr.append(skills.get(i));
        }

        tvUserSkills.setText(skillsStr.length() > 0 ?
                skillsStr.toString() : "Henüz beceri eklenmemiş");
    }
}