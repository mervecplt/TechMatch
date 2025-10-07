package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.datastructures.LinkedList;
import com.example.techmatch.models.Project;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvResults;
    private Button btnFilterUsers, btnFilterProjects;
    private Button btnCatAI, btnCatIoT, btnCatMobile, btnCatRobotics;
    private TextView btnBack;

    private DataManager dataManager;
    private boolean showingUsers = true;
    private ArrayAdapter<String> adapter;
    private List<String> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dataManager = DataManager.getInstance();

        // View'ları bağla
        etSearch = findViewById(R.id.etSearch);
        lvResults = findViewById(R.id.lvResults);
        btnFilterUsers = findViewById(R.id.btnFilterUsers);
        btnFilterProjects = findViewById(R.id.btnFilterProjects);
        btnCatAI = findViewById(R.id.btnCatAI);
        btnCatIoT = findViewById(R.id.btnCatIoT);
        btnCatMobile = findViewById(R.id.btnCatMobile);
        btnCatRobotics = findViewById(R.id.btnCatRobotics);
        btnBack = findViewById(R.id.btnBack);

        // Liste adapter'ı
        displayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvResults.setAdapter(adapter);

        // Başlangıçta tüm kullanıcıları göster
        showAllUsers();

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // Arama
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    if (showingUsers) {
                        showAllUsers();
                    } else {
                        showAllProjects();
                    }
                } else {
                    searchByName(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filtre butonları
        btnFilterUsers.setOnClickListener(v -> {
            showingUsers = true;
            showAllUsers();
        });

        btnFilterProjects.setOnClickListener(v -> {
            showingUsers = false;
            showAllProjects();
        });

        // Kategori butonları
        btnCatAI.setOnClickListener(v -> filterByCategory("Yapay Zeka"));
        btnCatIoT.setOnClickListener(v -> filterByCategory("IoT"));
        btnCatMobile.setOnClickListener(v -> filterByCategory("Mobil Uygulama"));
        btnCatRobotics.setOnClickListener(v -> filterByCategory("Robotik"));

        // Liste tıklama
        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            if (showingUsers) {
                LinkedList<User> users = dataManager.getAllUsers();
                User user = users.get(position);
                openProfile(user.getId());
            }
        });
    }

    private void showAllUsers() {
        displayList.clear();
        LinkedList<User> users = dataManager.getAllUsers();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String skillsStr = getSkillsString(user.getSkills());
            displayList.add(user.getName() + "\n" + user.getDepartment() + "\n" + skillsStr);
        }
        adapter.notifyDataSetChanged();
    }

    private void showAllProjects() {
        displayList.clear();
        LinkedList<Project> projects = dataManager.getAllProjects();

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            displayList.add(project.getTitle() + "\n" +
                    "Kategori: " + project.getCategory() + "\n" +
                    "Takım: " + project.getTeamSize() + "/" + project.getMaxTeamSize());
        }
        adapter.notifyDataSetChanged();
    }

    private void searchByName(String query) {
        displayList.clear();

        if (showingUsers) {
            LinkedList<User> results = dataManager.searchUsersByName(query);
            for (int i = 0; i < results.size(); i++) {
                User user = results.get(i);
                String skillsStr = getSkillsString(user.getSkills());
                displayList.add(user.getName() + "\n" + user.getDepartment() + "\n" + skillsStr);
            }
        } else {
            LinkedList<Project> projects = dataManager.getAllProjects();
            for (int i = 0; i < projects.size(); i++) {
                Project project = projects.get(i);
                if (project.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    displayList.add(project.getTitle() + "\n" +
                            "Kategori: " + project.getCategory() + "\n" +
                            "Takım: " + project.getTeamSize() + "/" + project.getMaxTeamSize());
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterByCategory(String category) {
        displayList.clear();

        if (showingUsers) {
            LinkedList<User> results = dataManager.searchUsersByCategory(category);
            for (int i = 0; i < results.size(); i++) {
                User user = results.get(i);
                String skillsStr = getSkillsString(user.getSkills());
                displayList.add(user.getName() + "\n" + user.getDepartment() + "\n" + skillsStr);
            }
        } else {
            LinkedList<Project> results = dataManager.searchProjectsByCategory(category);
            for (int i = 0; i < results.size(); i++) {
                Project project = results.get(i);
                displayList.add(project.getTitle() + "\n" +
                        "Kategori: " + project.getCategory() + "\n" +
                        "Takım: " + project.getTeamSize() + "/" + project.getMaxTeamSize());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getSkillsString(LinkedList<String> skills) {
        if (skills.isEmpty()) return "Beceri yok";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(3, skills.size()); i++) {
            if (i > 0) sb.append(", ");
            sb.append(skills.get(i));
        }
        return sb.toString();
    }

    private void openProfile(int userId) {
        Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}