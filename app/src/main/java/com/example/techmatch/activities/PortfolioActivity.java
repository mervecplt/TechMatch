package com.example.techmatch.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.datastructures.LinkedList;
import com.example.techmatch.models.Achievement;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class PortfolioActivity extends AppCompatActivity {

    private TextView btnBack, tvPortfolioTitle;
    private ListView lvAchievements;

    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        tvPortfolioTitle = findViewById(R.id.tvPortfolioTitle);
        lvAchievements = findViewById(R.id.lvAchievements);

        // Kullanıcı ID'sini al
        int userId = getIntent().getIntExtra("USER_ID", 1);
        currentUser = dataManager.getUserById(userId);

        if (currentUser != null) {
            displayAchievements();
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());
    }

    private void displayAchievements() {
        tvPortfolioTitle.setText(currentUser.getName() + " - Başarılar");

        // ⭐ DÜZELTİLDİ: List<String> olarak al
        List<String> achievements = dataManager.getAchievements(currentUser.getId());
        List<String> displayList = new ArrayList<>();

        if (achievements.isEmpty()) {
            displayList.add("Henüz başarı eklenmemiş");
        } else {
            for (int i = 0; i < achievements.size(); i++) {
                String display = "🏆 " + achievements.get(i);
                displayList.add(display);
            }
        }

        // Adapter'ı ayarla
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        lvAchievements.setAdapter(adapter);
    }
}