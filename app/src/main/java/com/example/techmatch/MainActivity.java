package com.example.techmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.activities.ProfileActivity;
import com.example.techmatch.activities.SearchActivity;
import com.example.techmatch.utils.DataManager;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch, btnProfile;
    private TextView tvUserCount, tvProjectCount;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DataManager instance
        dataManager = DataManager.getInstance();

        // View'ları bağla
        btnSearch = findViewById(R.id.btnSearch);
        btnProfile = findViewById(R.id.btnProfile);
        tvUserCount = findViewById(R.id.tvUserCount);
        tvProjectCount = findViewById(R.id.tvProjectCount);

        // İstatistikleri güncelle
        updateStats();

        // Buton tıklama olayları
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", 1); // Örnek kullanıcı ID
            startActivity(intent);
        });
    }

    private void updateStats() {
        int userCount = dataManager.getAllUsers().size();
        int projectCount = dataManager.getAllProjects().size();

        tvUserCount.setText(String.valueOf(userCount));
        tvProjectCount.setText(String.valueOf(projectCount));
    }
}