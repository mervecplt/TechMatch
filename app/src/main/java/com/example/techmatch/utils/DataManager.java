package com.example.techmatch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.techmatch.datastructures.*;
import com.example.techmatch.models.User;
import com.example.techmatch.models.Project;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class DataManager {
    private static final String TAG = "DataManager";
    private static DataManager instance;
    private CustomHashMap<String, User> users; // Email -> User
    private CustomHashMap<Integer, User> usersById; // ID -> User
    private CustomHashMap<Integer, Project> projects; // ProjectID -> Project
    private Stack<String> navigationStack; // Geri tuşu için
    private Queue<String> activityQueue; // İleri tuşu için
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final String PREFS_NAME = "TechMatchPrefs";
    private static final String KEY_USERS = "users";
    private static final String KEY_CURRENT_USER = "current_user_email";

    // Singleton Pattern
    private DataManager(Context context) {
        users = new CustomHashMap<>();
        usersById = new CustomHashMap<>();
        projects = new CustomHashMap<>();
        navigationStack = new Stack<>();
        activityQueue = new Queue<>();
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // ⭐ Gson'u daha güvenli şekilde yapılandır
        gson = new GsonBuilder()
                .serializeNulls()
                .create();

        loadUsersFromPrefs(); // Kayıtlı kullanıcıları yükle
        addSampleData(); // Örnek veriler
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }
        return instance;
    }

    // ==================== KULLANICI YÖNETİMİ ====================

    // Kullanıcı kaydet
    public boolean registerUser(String name, String surname, String email, String password) {
        // Email zaten var mı kontrol et
        if (users.containsKey(email)) {
            return false; // Bu email zaten kayıtlı
        }

        // Yeni kullanıcı ID'si
        int newId = users.size() + 1;

        // Yeni kullanıcı oluştur
        User newUser = new User(
                newId,
                name + " " + surname,
                email,
                password,
                "", // Varsayılan departman
                ""  // Bio boş
        );

        // HashMap'e ekle (hem email hem ID ile)
        users.put(email, newUser);
        usersById.put(newId, newUser);

        // SharedPreferences'e kaydet (kalıcı)
        saveUsersToPrefs();

        return true;
    }

    // Kullanıcı girişi
    public User loginUser(String email, String password) {
        User user = users.get(email);

        if (user != null && user.getPassword().equals(password)) {
            // Giriş başarılı - Mevcut kullanıcıyı kaydet
            setCurrentUser(email);
            return user;
        }

        return null; // Giriş başarısız
    }

    // Mevcut kullanıcıyı kaydet
    private void setCurrentUser(String email) {
        sharedPreferences.edit().putString(KEY_CURRENT_USER, email).apply();
    }

    // Mevcut kullanıcıyı al
    public User getCurrentUser() {
        String email = sharedPreferences.getString(KEY_CURRENT_USER, null);
        if (email != null) {
            return users.get(email);
        }
        return null;
    }

    // ⭐ DÜZELTME: ID'ye göre kullanıcı getir - AYNI REFERANSI DÖNDÜR
    public User getUserById(int userId) {
        User user = usersById.get(userId);
        Log.d(TAG, "getUserById(" + userId + ") çağrıldı");
        Log.d(TAG, "Kullanıcı bulundu: " + (user != null ? user.getName() : "NULL"));
        if (user != null) {
            Log.d(TAG, "Proje sayısı: " + (user.getProjects() != null ? user.getProjects().size() : "NULL"));
        }
        return user;
    }

    // 🔹 YENİ METOD EKLENDI: Email'e göre kullanıcı getir
    public User getUserByEmail(String email) {
        return users.get(email);
    }

    // ⭐ DÜZELTME: Kullanıcı bilgilerini güncelle
    public void updateUser(User user) {
        if (user != null) {
            Log.d(TAG, "=== updateUser BAŞLADI ===");
            Log.d(TAG, "Kullanıcı: " + user.getName() + " (ID: " + user.getId() + ")");
            Log.d(TAG, "Projeler: " + (user.getProjects() != null ? user.getProjects().size() : "NULL"));
            Log.d(TAG, "Proje listesi: " + user.getProjects());

            // ⚠ ÖNEMLİ: HashMap'de AYNI REFERANSI kullan
            // Yeni nesne oluşturma, mevcut nesneyi güncelle
            User existingUser = usersById.get(user.getId());
            if (existingUser != null && existingUser != user) {
                Log.w(TAG, "UYARI: Farklı User nesnesi tespit edildi! Senkronize ediliyor...");
                // Mevcut nesneyi güncelle
                existingUser.setBio(user.getBio());
                existingUser.setSkills(user.getSkills());
                existingUser.setUniversity(user.getUniversity());
                existingUser.setGraduationYear(user.getGraduationYear());
                existingUser.setGpa(user.getGpa());
                existingUser.setProjects(user.getProjects());
                existingUser.setWorkExperience(user.getWorkExperience());
                existingUser.setAchievements(user.getAchievements());
                existingUser.setCertificates(user.getCertificates());

                // HashMap'leri güncelle
                users.put(existingUser.getEmail(), existingUser);
                usersById.put(existingUser.getId(), existingUser);
            } else {
                // Zaten aynı referans, direkt güncelle
                users.put(user.getEmail(), user);
                usersById.put(user.getId(), user);
            }

            Log.d(TAG, "HashMap'e kaydedildi");

            // SharedPreferences'e kaydet (kalıcı)
            saveUsersToPrefs();

            Log.d(TAG, "SharedPreferences'e kaydedildi");
            Log.d(TAG, "=== updateUser BİTTİ ===\n");
        }
    }

    // ⭐ YENİ METOD - Mevcut kullanıcıyı sil
    public boolean deleteCurrentUser() {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            Log.w(TAG, "deleteCurrentUser: Giriş yapılmamış!");
            return false; // Giriş yapılmamış
        }

        Log.d(TAG, "=== deleteCurrentUser BAŞLADI ===");
        Log.d(TAG, "Silinecek kullanıcı: " + currentUser.getName() + " (" + currentUser.getEmail() + ")");

        // 1. HashMap'lerden sil
        users.remove(currentUser.getEmail());      // Email ile sil
        usersById.remove(currentUser.getId());     // ID ile sil

        Log.d(TAG, "HashMap'lerden silindi");
        Log.d(TAG, "Kalan kullanıcı sayısı: " + users.size());

        // 2. SharedPreferences'ten currentUser'ı kaldır
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CURRENT_USER);  // Mevcut kullanıcı bilgisini sil
        editor.apply();

        Log.d(TAG, "CurrentUser SharedPreferences'ten temizlendi");

        // 3. Güncel kullanıcı listesini kaydet
        saveUsersToPrefs();

        Log.d(TAG, "Güncel liste SharedPreferences'e kaydedildi");
        Log.d(TAG, "=== deleteCurrentUser BİTTİ ===\n");

        return true;  // Başarılı
    }

    // ⭐ YENİ METOD (BONUS) - Belirli bir kullanıcıyı sil (Admin için kullanılabilir)
    public boolean deleteUser(String email) {
        if (!users.containsKey(email)) {
            Log.w(TAG, "deleteUser: Kullanıcı bulunamadı: " + email);
            return false; // Kullanıcı bulunamadı
        }

        Log.d(TAG, "=== deleteUser BAŞLADI ===");
        Log.d(TAG, "Silinecek email: " + email);

        User user = users.get(email);

        // HashMap'lerden sil
        users.remove(email);
        usersById.remove(user.getId());

        Log.d(TAG, "Kullanıcı silindi: " + user.getName());
        Log.d(TAG, "Kalan kullanıcı sayısı: " + users.size());

        // Eğer silinen kullanıcı şu anki kullanıcıysa, çıkış yap
        User currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getEmail().equals(email)) {
            logout();
            Log.d(TAG, "Silinen kullanıcı mevcut kullanıcıydı, çıkış yapıldı");
        }

        // Kaydet
        saveUsersToPrefs();

        Log.d(TAG, "=== deleteUser BİTTİ ===\n");

        return true;
    }

    // Çıkış yap
    public void logout() {
        sharedPreferences.edit().remove(KEY_CURRENT_USER).apply();
    }

    // Email kontrolü
    public boolean isEmailRegistered(String email) {
        return users.containsKey(email);
    }

    // Tüm kullanıcıları getir
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        for (User user : users.values()) {
            userList.add(user);
        }
        return userList;
    }

    // ✅ YENİ METOD: Kullanıcı arama (isim, email, departman)
    public List<User> searchUsers(String query) {
        if (query == null) return Collections.emptyList();
        String q = query.trim();
        if (q.isEmpty() || q.length() < 3) { // En az 3 harf
            Log.d("Arama", "Geçersiz sorgu (çok kısa): " + q);
            return Collections.emptyList();
        }

        if (!q.matches("^[\\p{L}\\p{N}@._\\-\\s]{3,}$")) {
            Log.d("Arama", "Geçersiz karakter içeriyor: " + q);
            return Collections.emptyList();
        }

        List<User> allUsers = getAllUsers();
        List<User> searchResults = new ArrayList<>();
        String lowerQuery = q.toLowerCase();

        for (User user : allUsers) {
            if (user == null) continue;

            String name = user.getName() != null ? user.getName().toLowerCase() : "";
            String email = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
            String department = user.getDepartment() != null ? user.getDepartment().toLowerCase() : "";

            // ✅ sadece başında veya kelime sınırında eşleşen sonuçları al
            boolean matchesName = name.startsWith(lowerQuery) || name.contains(" " + lowerQuery);
            boolean matchesEmail = email.startsWith(lowerQuery);
            boolean matchesDepartment = department.startsWith(lowerQuery);

            if (matchesName || matchesEmail || matchesDepartment) {
                searchResults.add(user);
            }
        }

        return searchResults;
    }



    // Başarıları getir (PortfolioActivity için)
    public List<String> getAchievements(int userId) {
        User user = getUserById(userId);
        if (user != null && user.getAchievements() != null) {
            return user.getAchievements();
        }
        return new ArrayList<>();
    }

    // ==================== NAVİGASYON YÖNETİMİ ====================

    // Yeni sayfaya git (Stack'e ekle)
    public void navigateTo(String activityName) {
        navigationStack.push(activityName);
    }

    // Geri git
    public String goBack() {
        if (!navigationStack.isEmpty()) {
            String current = navigationStack.pop();
            activityQueue.enqueue(current); // İleri için kaydet

            if (!navigationStack.isEmpty()) {
                return navigationStack.peek(); // Bir önceki sayfa
            }
        }
        return null;
    }

    // İleri git
    public String goForward() {
        if (!activityQueue.isEmpty()) {
            String next = activityQueue.dequeue();
            navigationStack.push(next);
            return next;
        }
        return null;
    }

    // Geri gidilebilir mi?
    public boolean canGoBack() {
        return navigationStack.size() > 1;
    }

    // İleri gidilebilir mi?
    public boolean canGoForward() {
        return !activityQueue.isEmpty();
    }

    // ==================== PROJE YÖNETİMİ ====================

    public void addProject(Project project) {
        projects.put(project.getId(), project);
    }

    public Project getProjectById(int id) {
        return projects.get(id);
    }

    // Tüm projeleri getir
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        for (Project project : projects.values()) {
            projectList.add(project);
        }
        return projectList;
    }
    // ✅ YENİ METOD: Proje arama (isim, kategori, açıklama)
    public List<Project> searchProjects(String query) {
        if (query == null) return Collections.emptyList();
        String q = query.trim();
        if (q.isEmpty() || q.length() < 3) return Collections.emptyList();

        List<Project> allProjects = getAllProjects();
        List<Project> results = new ArrayList<>();
        String lowerQuery = q.toLowerCase();

        for (Project project : allProjects) {
            if (project == null) continue;

            String title = project.getTitle() != null ? project.getTitle().toLowerCase() : "";
            String category = project.getCategory() != null ? project.getCategory().toLowerCase() : "";
            String desc = project.getDescription() != null ? project.getDescription().toLowerCase() : "";

            // sadece başında veya kelime içinde geçen ama kısa olmayanları al
            boolean matchesTitle = title.startsWith(lowerQuery) || title.contains(" " + lowerQuery);
            boolean matchesCategory = category.startsWith(lowerQuery);
            boolean matchesDesc = desc.contains(" " + lowerQuery);

            if (matchesTitle || matchesCategory || matchesDesc) {
                results.add(project);
            }
        }

        return results;
    }



    public int getUserCount() {
        return users.size();
    }

    public int getProjectCount() {
        return projects.size();
    }

    // ==================== KALICI DEPOLAMA ====================

    // ⭐ DÜZELTİLDİ: Kullanıcıları SharedPreferences'e kaydet
    private void saveUsersToPrefs() {
        try {
            Log.d(TAG, "=== saveUsersToPrefs BAŞLADI ===");
            List<User> userList = new ArrayList<>();

            for (User user : users.values()) {
                // ⭐ Null kontrolü ekle
                if (user != null) {
                    Log.d(TAG, "Kaydediliyor: " + user.getName() + " - Projeler: " +
                            (user.getProjects() != null ? user.getProjects().size() : "NULL"));

                    // Liste alanlarının null olmamasını garanti et
                    if (user.getProjects() == null) {
                        user.setProjects(new ArrayList<>());
                    }
                    if (user.getWorkExperience() == null) {
                        user.setWorkExperience(new ArrayList<>());
                    }
                    if (user.getAchievements() == null) {
                        user.setAchievements(new ArrayList<>());
                    }
                    if (user.getCertificates() == null) {
                        user.setCertificates(new ArrayList<>());
                    }
                    userList.add(user);
                }
            }

            String json = gson.toJson(userList);
            sharedPreferences.edit().putString(KEY_USERS, json).apply();
            Log.d(TAG, "SharedPreferences'e yazıldı - " + userList.size() + " kullanıcı");
            Log.d(TAG, "=== saveUsersToPrefs BİTTİ ===\n");
        } catch (Exception e) {
            Log.e(TAG, "HATA: saveUsersToPrefs başarısız", e);
            e.printStackTrace();
        }
    }

    // ⭐ DÜZELTİLDİ: Kullanıcıları SharedPreferences'ten yükle
    private void loadUsersFromPrefs() {
        try {
            Log.d(TAG, "=== loadUsersFromPrefs BAŞLADI ===");
            String json = sharedPreferences.getString(KEY_USERS, null);
            if (json != null && !json.isEmpty()) {
                Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                List<User> userList = gson.fromJson(json, listType);

                if (userList != null) {
                    Log.d(TAG, "Yüklenen kullanıcı sayısı: " + userList.size());
                    for (User user : userList) {
                        if (user != null) {
                            // ⭐ Liste alanlarının null olmamasını garanti et
                            if (user.getProjects() == null) {
                                user.setProjects(new ArrayList<>());
                            }
                            if (user.getWorkExperience() == null) {
                                user.setWorkExperience(new ArrayList<>());
                            }
                            if (user.getAchievements() == null) {
                                user.setAchievements(new ArrayList<>());
                            }
                            if (user.getCertificates() == null) {
                                user.setCertificates(new ArrayList<>());
                            }

                            Log.d(TAG, "Yüklendi: " + user.getName() + " - Projeler: " + user.getProjects().size());

                            users.put(user.getEmail(), user);
                            usersById.put(user.getId(), user);
                        }
                    }
                }
            }
            Log.d(TAG, "=== loadUsersFromPrefs BİTTİ ===\n");
        } catch (Exception e) {
            Log.e(TAG, "HATA: loadUsersFromPrefs başarısız", e);
            e.printStackTrace();
        }
    }

    // ==================== ÖRNEK VERİLER ====================

    private void addSampleData() {
        // Eğer hiç kullanıcı yoksa örnek kullanıcı ekle
        if (users.size() == 0) {
            registerUser("Ahmet", "Yılmaz", "ahmet@test.com", "123456");
            registerUser("Ayşe", "Demir", "ayse@test.com", "123456");
            registerUser("Mehmet", "Kaya", "mehmet@test.com", "123456");
        }

        // Örnek projeler
        if (projects.size() == 0) {
            // Yapay Zeka projeleri
            Project p1 = new Project(1, "AI Chatbot", "Yapay Zeka",
                    "Doğal dil işleme ile chatbot geliştirme projesi", 5, "Prof. Dr. Ali Yılmaz");
            p1.addParticipant();
            p1.addParticipant(); // 2 kişi var

            Project p2 = new Project(2, "Görüntü İşleme Sistemi", "Yapay Zeka",
                    "Derin öğrenme ile nesne tanıma", 4, "Doç. Dr. Zeynep Kara");
            p2.addParticipant(); // 1 kişi var

            // IoT projeleri
            Project p3 = new Project(3, "Akıllı Ev Otomasyonu", "IoT",
                    "Akıllı ev sistemleri ve IoT sensörleri", 3, "Dr. Mehmet Demir");
            p3.addParticipant();
            p3.addParticipant(); // 2 kişi var

            Project p4 = new Project(4, "Akıllı Tarım Sistemi", "IoT",
                    "Tarımda IoT ve sensör teknolojileri", 4, "Prof. Dr. Ayşe Öztürk");
            // 0 kişi var

            // Mobil Uygulama projeleri
            Project p5 = new Project(5, "Eğitim Uygulaması", "Mobil Uygulama",
                    "Kotlin ile Android eğitim uygulaması", 4, "Dr. Can Yıldız");
            p5.addParticipant();
            p5.addParticipant();
            p5.addParticipant(); // 3 kişi var

            Project p6 = new Project(6, "Sağlık Takip Uygulaması", "Mobil Uygulama",
                    "React Native ile sağlık takip uygulaması", 3, "Doç. Dr. Elif Aslan");
            p6.addParticipant(); // 1 kişi var

            addProject(p1);
            addProject(p2);
            addProject(p3);
            addProject(p4);
            addProject(p5);
            addProject(p6);
        }
    }

    // ==================== PROJE KATILIM YÖNETİMİ ====================
    // Kullanıcıyı projeye ekle
    public boolean addUserToProject(int userId, int projectId) {
        User user = getUserById(userId);
        Project project = getProjectById(projectId);

        if (user == null || project == null) {
            Log.e(TAG, "Kullanici veya proje bulunamadi!");
            return false;
        }

        // Kullanıcı zaten bu projede mi kontrol et
        if (user.getProjects() == null) {
            user.setProjects(new ArrayList<String>());
        }

        String projectIdStr = String.valueOf(projectId);

        for (String existingProjectId : user.getProjects()) {
            if (existingProjectId.equals(projectIdStr)) {
                Log.w(TAG, "Kullanici zaten bu projede!");
                return false; // Zaten katılmış
            }
        }

        // Proje dolu mu kontrol et
        if (project.getCurrentParticipants() >= project.getMaxParticipants()) {
            Log.w(TAG, "Proje dolu!");
            return false; // Proje dolu
        }

        // Kullanıcıyı projeye ekle
        user.getProjects().add(projectIdStr);
        project.addParticipant(); // Katılımcı sayısını artır

        // Kaydet
        updateUser(user);

        Log.d(TAG, user.getName() + " projeye eklendi: " + project.getName());
        Log.d(TAG, "Yeni katilimci sayisi: " + project.getCurrentParticipants());

        return true; // Başarılı
    }
}