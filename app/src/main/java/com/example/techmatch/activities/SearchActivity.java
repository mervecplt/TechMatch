package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.models.Project;
import com.example.techmatch.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DataManager dataManager;

    private EditText etSearchQuery;
    private Button btnSearchUsers;
    private Button btnSearchProjects;
    private TextView btnBack;
    private ListView lvSearchResults;

    // ⭐ YENİ: Kategori butonları
    private Button btnCategoryAI;
    private Button btnCategoryIoT;
    private Button btnCategoryMobile;

    private ArrayAdapter<String> adapter;
    private List<String> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dataManager = DataManager.getInstance(this);

        etSearchQuery = findViewById(R.id.etSearchQuery);
        btnSearchUsers = findViewById(R.id.btnSearchUsers);
        btnSearchProjects = findViewById(R.id.btnSearchProjects);
        btnBack = findViewById(R.id.btnBack);
        lvSearchResults = findViewById(R.id.lvSearchResults);

        // ⭐ YENİ: Kategori butonlarını bağla
        btnCategoryAI = findViewById(R.id.btnCategoryAI);
        btnCategoryIoT = findViewById(R.id.btnCategoryIoT);
        btnCategoryMobile = findViewById(R.id.btnCategoryMobile);

        searchResults = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        lvSearchResults.setAdapter(adapter);

        btnSearchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });

        btnSearchProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProjects();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ⭐ YENİ: Kategori buton click listener'ları
        btnCategoryAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeamList("Yapay Zeka");
            }
        });

        btnCategoryIoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeamList("IoT");
            }
        });

        btnCategoryMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeamList("Mobil Uygulama");
            }
        });
    }

    // ⭐ YENİ: TeamListActivity'yi aç
    private void openTeamList(String category) {
        Intent intent = new Intent(SearchActivity.this, TeamListActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void searchUsers() {
        String query = etSearchQuery.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Lütfen arama kelimesi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        searchResults.clear();

        List<User> allUsers = dataManager.getAllUsers();

        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                String userInfo = "👤 " + user.getName() +
                        "\n📧 " + user.getEmail() +
                        "\n🎓 " + user.getDepartment();
                searchResults.add(userInfo);
            }
        }

        if (searchResults.isEmpty()) {
            searchResults.add("Sonuç bulunamadı");
        }

        adapter.notifyDataSetChanged();
    }

    private void searchProjects() {
        String query = etSearchQuery.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Lütfen arama kelimesi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        searchResults.clear();

        List<Project> allProjects = dataManager.getAllProjects();

        for (Project project : allProjects) {
            if (project.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    project.getCategory().toLowerCase().contains(query.toLowerCase())) {

                String projectInfo = "📁 " + project.getTitle() +
                        "\n🏷️ Kategori: " + project.getCategory() +
                        "\n👥 Takım: " + project.getTeamSize() + " kişi";
                searchResults.add(projectInfo);
            }
        }

        if (searchResults.isEmpty()) {
            searchResults.add("Sonuç bulunamadı");
        }

        adapter.notifyDataSetChanged();
    }
}