package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView ivSearchIcon;
    private TextView btnBack;
    private ListView lvSearchResults;

    // Kategori butonları
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
        ivSearchIcon = findViewById(R.id.ivSearchIcon);
        btnBack = findViewById(R.id.btnBack);
        lvSearchResults = findViewById(R.id.lvSearchResults);

        // Kategori butonlarını bağla
        btnCategoryAI = findViewById(R.id.btnCategoryAI);
        btnCategoryIoT = findViewById(R.id.btnCategoryIoT);
        btnCategoryMobile = findViewById(R.id.btnCategoryMobile);

        searchResults = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        lvSearchResults.setAdapter(adapter);

        // 🔍 Arama ikonuna tıklayınca arama yap
        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // ⌨️ Enter tuşuna basınca arama yap
        etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN)) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        // Geri butonu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Kategori buton click listener'ları
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

    // TeamListActivity'yi aç
    private void openTeamList(String category) {
        Intent intent = new Intent(SearchActivity.this, TeamListActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    // 🎯 Birleşik arama fonksiyonu - hem kullanıcılar hem projeler
    private void performSearch() {
        String query = etSearchQuery.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Lütfen arama kelimesi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        searchResults.clear();

        // Geçici listeler oluştur
        List<String> tempUserResults = new ArrayList<>();
        List<String> tempProjectResults = new ArrayList<>();

        // Önce kullanıcıları ara
        List<User> allUsers = dataManager.getAllUsers();
        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                    user.getDepartment().toLowerCase().contains(query.toLowerCase())) {

                String userInfo = "👤 " + user.getName() +
                        "\n📧 " + user.getEmail() +
                        "\n🎓 " + user.getDepartment();
                tempUserResults.add(userInfo);
            }
        }

        // Sonra projeleri ara
        List<Project> allProjects = dataManager.getAllProjects();
        for (Project project : allProjects) {
            if (project.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    project.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                    project.getDescription().toLowerCase().contains(query.toLowerCase())) {

                String projectInfo = "📁 " + project.getTitle() +
                        "\n🏷️ Kategori: " + project.getCategory() +
                        "\n👥 Takım: " + project.getTeamSize() + " kişi";
                tempProjectResults.add(projectInfo);
            }
        }

        // Sonuç mesajı
        int userCount = tempUserResults.size();
        int projectCount = tempProjectResults.size();

        if (userCount == 0 && projectCount == 0) {
            searchResults.add("❌ Sonuç bulunamadı");
        } else {
            // Başa özet ekle
            searchResults.add("📊 " + userCount + " kullanıcı, " + projectCount + " proje bulundu\n");

            // Kullanıcıları ekle
            searchResults.addAll(tempUserResults);

            // Projeleri ekle
            searchResults.addAll(tempProjectResults);
        }

        adapter.notifyDataSetChanged();

        // Toast mesajı göster
        Toast.makeText(this, userCount + " kullanıcı, " + projectCount + " proje bulundu", Toast.LENGTH_SHORT).show();
    }
}