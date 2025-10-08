package com.example.techmatch.activities;

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
    private TextView btnBack;  // TextView olarak tanımlandı!
    private ListView lvSearchResults;

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