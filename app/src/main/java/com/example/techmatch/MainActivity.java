package com.example.techmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.activities.ProfileActivity;
import com.example.techmatch.activities.SearchActivity;
import com.example.techmatch.models.User;
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
        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnSearch = findViewById(R.id.btnSearch);
        btnProfile = findViewById(R.id.btnProfile);


        // İstatistikleri güncelle


        // Buton tıklama olayları
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            // ⭐ DÜZELTİLDİ: Giriş yapan kullanıcının ID'sini gönder
            User currentUser = dataManager.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USER_ID", currentUser.getId());
                startActivity(intent);
            }
        });
    }


}