package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "SearchActivity";
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

    // ✅ YENİ METOD: Activity her göründüğünde arama sonuçlarını güncelle
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "=== onResume() çağrıldı ===");

        // Eğer daha önce arama yapılmışsa, sonuçları yenile
        String currentQuery = etSearchQuery.getText().toString().trim();
        if (!currentQuery.isEmpty()) {
            Log.d(TAG, "Arama yenileniyor: " + currentQuery);
            performSearch(); // Aramayı tekrar yap (güncel verilerle)
        } else {
            Log.d(TAG, "Arama kutusu boş, yenileme yapılmadı");
        }
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

        Log.d(TAG, "\n=== ARAMA BAŞLADI ===");
        Log.d(TAG, "Aranan kelime: " + query);

        searchResults.clear();

        // Geçici listeler
        List<String> tempUserResults = new ArrayList<>();
        List<String> tempProjectResults = new ArrayList<>();

        // ==================== KULLANICI ARAMA ====================
        List<User> matchedUsers = dataManager.searchUsers(query);
        Log.d(TAG, "Bulunan kullanıcı sayısı: " + matchedUsers.size());

        for (User user : matchedUsers) {
            int userProjectCount = (user.getProjects() != null) ? user.getProjects().size() : 0;

            String userInfo = "👤 " + user.getName() +
                    "\n📧 " + user.getEmail() +
                    "\n🎓 " + user.getDepartment() +
                    "\n📁 " + userProjectCount + " proje";
            tempUserResults.add(userInfo);
        }

        // ==================== PROJE ARAMA ====================
        // ÖNEMLİ: getAllProjects() DEĞİL, searchProjects(query) kullan!
        List<Project> matchedProjects = dataManager.searchProjects(query);
        Log.d(TAG, "Bulunan proje sayısı: " + matchedProjects.size());

        for (Project project : matchedProjects) {
            String projectInfo = "📁 " + project.getName() +
                    "\n🏷️ Kategori: " + project.getCategory() +
                    "\n👥 Takım: " + project.getCurrentParticipants() + "/" + project.getMaxParticipants() + " kişi";
            tempProjectResults.add(projectInfo);
        }

        // ==================== SONUÇLARI GÖSTER ====================
        int userCount = tempUserResults.size();
        int projectCount = tempProjectResults.size();

        Log.d(TAG, "---");
        Log.d(TAG, "Sonuç: " + userCount + " kullanıcı, " + projectCount + " proje");
        Log.d(TAG, "=== ARAMA BİTTİ ===\n");

        if (userCount == 0 && projectCount == 0) {
            // <<< BU BLOK DÖNGÜLERDEN SONRA, ADAPTER'DAN ÖNCE >>>
            searchResults.clear();
            searchResults.add("❌ Sonuç bulunamadı");
        } else {
            // Kullanıcı ve proje sonuçlarını ekle
            searchResults.addAll(tempUserResults);
            searchResults.addAll(tempProjectResults);
        }

        adapter.notifyDataSetChanged();


    }

}