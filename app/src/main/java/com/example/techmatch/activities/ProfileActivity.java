package com.example.techmatch.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private TextView btnBack;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private TextView tvUserBio, tvUserSkills;
    private TextView tvEducationInfo, tvGpaInfo;
    private LinearLayout llProjects, llWorkExperience, llAchievements;
    private Button btnEditProfile;
    private Button btnDeleteAccount; // ⭐ YENİ - Hesap silme butonu

    private DataManager dataManager;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate() başladı");

        dataManager = DataManager.getInstance(this);

        // View'ları bağla
        btnBack = findViewById(R.id.btnBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDepartment = findViewById(R.id.tvUserDepartment);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserBio = findViewById(R.id.tvUserBio);
        tvUserSkills = findViewById(R.id.tvUserSkills);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount); // ⭐ YENİ

        // Yeni alanlar
        tvEducationInfo = findViewById(R.id.tvEducationInfo);
        tvGpaInfo = findViewById(R.id.tvGpaInfo);
        llProjects = findViewById(R.id.llProjects);
        llWorkExperience = findViewById(R.id.llWorkExperience);
        llAchievements = findViewById(R.id.llAchievements);

        // ⭐ DÜZELTİLDİ: Önce currentUser'ı kontrol et
        currentUser = dataManager.getCurrentUser();

        // Intent'ten USER_ID geliyorsa (başka kullanıcı profili görmek için)
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId != -1) {
            // Başka bir kullanıcının profili isteniyor
            Log.d(TAG, "Intent'ten USER_ID alındı: " + userId);
            User requestedUser = dataManager.getUserById(userId);
            if (requestedUser != null) {
                currentUser = requestedUser;
            }
        } else if (currentUser != null) {
            // Kendi profilim
            userId = currentUser.getId();
            Log.d(TAG, "Kendi profilim gösteriliyor: " + userId);
        }

        if (currentUser != null) {
            Log.d(TAG, "Kullanıcı yüklendi: " + currentUser.getName() + " (ID: " + userId + ")");
            displayUserInfo();
        } else {
            Log.e(TAG, "HATA: Kullanıcı bulunamadı!");
        }

        // Geri butonu
        btnBack.setOnClickListener(v -> finish());

        // Profil düzenle butonu
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", currentUser.getId());
            startActivity(intent);
        });

        // ⭐ YENİ - Hesap silme butonu
        btnDeleteAccount.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "\n=== onResume() BAŞLADI ===");

        // ⭐ DÜZELTİLDİ: Her zaman güncel currentUser'ı kontrol et
        User latestCurrentUser = dataManager.getCurrentUser();

        if (latestCurrentUser != null) {
            // Eğer userId set edilmemişse veya kendi profilimse, güncel currentUser'ı kullan
            if (userId == -1 || userId == latestCurrentUser.getId()) {
                currentUser = latestCurrentUser;
                userId = currentUser.getId();
                Log.d(TAG, "✅ Güncel currentUser kullanılıyor: " + currentUser.getName());
            } else {
                // Başka bir kullanıcının profili gösteriliyorsa, onu yeniden yükle
                User updatedUser = dataManager.getUserById(userId);
                if (updatedUser != null) {
                    currentUser = updatedUser;
                    Log.d(TAG, "✅ Başka kullanıcı profili yenilendi: " + currentUser.getName());
                }
            }

            Log.d(TAG, "Nesne referansı: " + System.identityHashCode(currentUser));

            // Liste durumlarını logla
            Log.d(TAG, "--- LİSTE DURUMLARI ---");
            Log.d(TAG, "Projeler: " + (currentUser.getProjects() != null ? currentUser.getProjects().size() : "NULL") + " adet");
            Log.d(TAG, "İş deneyimleri: " + (currentUser.getWorkExperience() != null ? currentUser.getWorkExperience().size() : "NULL") + " adet");
            Log.d(TAG, "Başarılar: " + (currentUser.getAchievements() != null ? currentUser.getAchievements().size() : "NULL") + " adet");
            Log.d(TAG, "----------------------");

            // Ekranı yenile
            displayUserInfo();

            Log.d(TAG, "✅ Ekran yenilendi");
        } else {
            Log.e(TAG, "❌ HATA: getCurrentUser() null döndü!");
        }

        Log.d(TAG, "=== onResume() BİTTİ ===\n");
    }

    private void displayUserInfo() {
        Log.d(TAG, "\n=== displayUserInfo() BAŞLADI ===");

        // Temel bilgiler
        tvUserName.setText(currentUser.getName());
        tvUserDepartment.setText(currentUser.getDepartment());
        tvUserEmail.setText(currentUser.getEmail());

        // ⭐ Bio - null check
        String bio = currentUser.getBio();
        tvUserBio.setText((bio == null || bio.isEmpty()) ?
                "Henüz biyografi eklenmemiş. Profili düzenle butonuna tıklayarak ekleyebilirsin." : bio);

        // ⭐ Beceriler - String olarak (List değil!)
        String skillsString = currentUser.getSkills();
        tvUserSkills.setText((skillsString == null || skillsString.isEmpty()) ?
                "Beceri eklenmemiş" : skillsString);

        // ⭐ Eğitim bilgileri - null check
        String university = currentUser.getUniversity();
        String graduationYear = currentUser.getGraduationYear();

        if (university != null && !university.isEmpty()) {
            String eduText = university;
            if (graduationYear != null && !graduationYear.isEmpty()) {
                eduText += " - " + graduationYear;
            }
            tvEducationInfo.setText(eduText);
        } else {
            tvEducationInfo.setText("Eğitim bilgisi eklenmemiş");
        }

        // ⭐ GPA - null check
        if (currentUser.getGpa() > 0) {
            tvGpaInfo.setText("GPA: " + String.format("%.2f", currentUser.getGpa()));
        } else {
            tvGpaInfo.setText("GPA: Girilmemiş");
        }

        // ⭐ Projeler - null check ve detaylı log
        List<String> projects = currentUser.getProjects();
        Log.d(TAG, "--- PROJELER GÖSTERİLİYOR ---");
        Log.d(TAG, "Projects null mu?: " + (projects == null));
        Log.d(TAG, "Projects boyut: " + (projects != null ? projects.size() : "NULL"));
        if (projects != null && !projects.isEmpty()) {
            Log.d(TAG, "✅ Proje içeriği: " + projects);
            for (int i = 0; i < projects.size(); i++) {
                Log.d(TAG, "  [" + i + "] = " + projects.get(i));
            }
        } else {
            Log.d(TAG, "⚠️ Proje listesi boş veya null!");
        }
        displayList(llProjects, projects, "Henüz proje eklenmemiş");

        // ⭐ İş deneyimi - null check ve log
        List<String> workExperience = currentUser.getWorkExperience();
        Log.d(TAG, "--- İŞ DENEYİMLERİ GÖSTERİLİYOR ---");
        Log.d(TAG, "WorkExperience boyut: " + (workExperience != null ? workExperience.size() : "NULL"));
        displayList(llWorkExperience, workExperience, "Henüz iş deneyimi eklenmemiş");

        // ⭐ Başarılar - null check ve log
        List<String> achievements = currentUser.getAchievements();
        Log.d(TAG, "--- BAŞARILAR GÖSTERİLİYOR ---");
        Log.d(TAG, "Achievements boyut: " + (achievements != null ? achievements.size() : "NULL"));
        displayList(llAchievements, achievements, "Henüz başarı eklenmemiş");

        Log.d(TAG, "=== displayUserInfo() TAMAMLANDI ===\n");
    }

    private void displayList(LinearLayout container, List<String> items, String emptyMessage) {
        Log.d(TAG, "displayList() çağrıldı");

        container.removeAllViews();

        // ⭐ Null check eklendi
        if (items == null || items.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText(emptyMessage);
            emptyView.setTextColor(0xFF999999);
            emptyView.setPadding(16, 8, 16, 8);
            container.addView(emptyView);
            Log.d(TAG, "⚠️ Liste boş gösteriliyor: " + emptyMessage);
        } else {
            Log.d(TAG, "✅ Liste gösteriliyor - " + items.size() + " öğe");
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                TextView itemView = new TextView(this);
                itemView.setText("• " + item);
                itemView.setTextSize(14);
                itemView.setPadding(16, 8, 16, 8);
                container.addView(itemView);
                Log.d(TAG, "  Öğe [" + i + "] container'a eklendi: " + item);
            }
            Log.d(TAG, "✅ Tüm öğeler başarıyla eklendi");
        }
    }

    // ⭐ YENİ METOD - İlk onay dialogu
    private void showDeleteConfirmationDialog() {
        Log.d(TAG, "showDeleteConfirmationDialog() çağrıldı");

        new AlertDialog.Builder(this)
                .setTitle("⚠️ Hesabı Sil")
                .setMessage("Hesabınızı silmek istediğinizden emin misiniz?\n\n" +
                        "Bu işlem:\n" +
                        "• Tüm verilerinizi silecek\n" +
                        "• Projelerinizden çıkacak\n" +
                        "• Başarılarınızı kaldıracak\n" +
                        "• GERİ ALINAMAZ!\n\n" +
                        "Email: " + currentUser.getEmail())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("EVET, SİL", (dialog, which) -> {
                    // İkinci onay göster (çift güvenlik)
                    showFinalConfirmation();
                })
                .setNegativeButton("İPTAL", (dialog, which) -> {
                    Log.d(TAG, "Kullanıcı hesap silmeyi iptal etti");
                    Toast.makeText(this, "İşlem iptal edildi", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(true)
                .show();
    }

    // ⭐ YENİ METOD - Son onay dialogu (çift güvenlik)
    private void showFinalConfirmation() {
        Log.d(TAG, "showFinalConfirmation() çağrıldı");

        new AlertDialog.Builder(this)
                .setTitle("🚨 Son Onay")
                .setMessage("Bu işlem GERİ ALINAMAZ!\n\n" +
                        "Tüm verileriniz kalıcı olarak silinecek.\n\n" +
                        "Devam etmek istiyor musunuz?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("EVET, EMİNİM", (dialog, which) -> {
                    // Hesabı sil
                    deleteAccount();
                })
                .setNegativeButton("HAYIR", (dialog, which) -> {
                    Log.d(TAG, "Kullanıcı son onayda iptal etti");
                    Toast.makeText(this, "Hesabınız güvende 😊", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false) // Dışarı tıklayarak kapatamaz
                .show();
    }

    // ⭐ YENİ METOD - Hesabı sil
    private void deleteAccount() {
        Log.d(TAG, "\n=== deleteAccount() BAŞLADI ===");
        Log.d(TAG, "Silinecek kullanıcı: " + currentUser.getName() + " (" + currentUser.getEmail() + ")");

        // Loading mesajı göster
        Toast.makeText(this, "Hesap siliniyor...", Toast.LENGTH_SHORT).show();

        try {
            // DataManager üzerinden hesabı sil
            boolean success = dataManager.deleteCurrentUser();

            if (success) {
                Log.d(TAG, "✅ Hesap başarıyla silindi!");

                // Başarı mesajı
                Toast.makeText(this,
                        "✅ Hesabınız başarıyla silindi.\nGörüşmek üzere!",
                        Toast.LENGTH_LONG).show();

                // LoginActivity'ye yönlendir (tüm geçmişi temizle)
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                Log.d(TAG, "Login ekranına yönlendirildi");
            } else {
                Log.e(TAG, "❌ HATA: Hesap silinemedi!");

                // Hata mesajı
                Toast.makeText(this,
                        "❌ Hesap silinirken bir hata oluştu!\nLütfen tekrar deneyin.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ EXCEPTION: deleteAccount başarısız", e);
            e.printStackTrace();

            // Hata mesajı
            Toast.makeText(this,
                    "❌ Beklenmeyen bir hata oluştu!",
                    Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "=== deleteAccount() BİTTİ ===\n");
    }
}