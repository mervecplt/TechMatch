package com.example.techmatch.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.adapters.TeamListAdapter;
import com.example.techmatch.models.Project;
import com.example.techmatch.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {

    private TextView tvCategoryTitle;
    private TextView btnBack;
    private ListView lvTeams;
    private DataManager dataManager;
    private TeamListAdapter adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        // Intent'ten kategori al
        category = getIntent().getStringExtra("category");
        if (category == null) {
            category = "Tüm Projeler";
        }

        // View'ları bağla
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        btnBack = findViewById(R.id.btnBack);
        lvTeams = findViewById(R.id.lvTeams);

        dataManager = DataManager.getInstance(this);

        // Başlığı ayarla
        tvCategoryTitle.setText(category);

        // Geri butonu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Projeleri yükle
        loadProjects();
    }

    private void loadProjects() {
        List<Project> allProjects = dataManager.getAllProjects();
        List<Project> filteredProjects = new ArrayList<>();

        // Kategoriye göre filtrele
        for (Project project : allProjects) {
            if (project.getCategory().equalsIgnoreCase(category)) {
                filteredProjects.add(project);
            }
        }

        if (filteredProjects.isEmpty()) {
            Toast.makeText(this, "Bu kategoride henüz proje yok", Toast.LENGTH_SHORT).show();
        }

        // Adapter oluştur
        adapter = new TeamListAdapter(this, filteredProjects, new TeamListAdapter.OnJoinClickListener() {
            @Override
            public void onJoinClick(Project project) {
                joinTeam(project);
            }
        });

        lvTeams.setAdapter(adapter);
    }

    private void joinTeam(Project project) {
        if (project.isFull()) {
            Toast.makeText(this, "Bu takım dolu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kullanıcıyı takıma ekle
        project.addParticipant();

        // Adapter'ı güncelle
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Takıma katılma isteği gönderildi!", Toast.LENGTH_SHORT).show();
    }
}