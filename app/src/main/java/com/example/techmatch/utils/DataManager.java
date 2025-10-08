package com.example.techmatch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.techmatch.datastructures.*;
import com.example.techmatch.models.User;
import com.example.techmatch.models.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
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
        gson = new Gson();

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
                "Öğrenci", // Varsayılan departman
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

    // ID'ye göre kullanıcı getir
    public User getUserById(int userId) {
        return usersById.get(userId);
    }

    // ⭐ YENİ: Kullanıcı bilgilerini güncelle
    public void updateUser(User user) {
        if (user != null) {
            // HashMap'lerde güncelle
            users.put(user.getEmail(), user);
            usersById.put(user.getId(), user);

            // SharedPreferences'e kaydet (kalıcı)
            saveUsersToPrefs();
        }
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

    // Başarıları getir (PortfolioActivity için)
    public List<String> getAchievements(int userId) {
        User user = getUserById(userId);
        if (user != null) {
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

    public int getUserCount() {
        return users.size();
    }

    public int getProjectCount() {
        return projects.size();
    }

    // ==================== KALICI DEPOLAMA ====================

    // Kullanıcıları SharedPreferences'e kaydet
    private void saveUsersToPrefs() {
        List<User> userList = new ArrayList<>();

        for (User user : users.values()) {
            userList.add(user);
        }

        String json = gson.toJson(userList);
        sharedPreferences.edit().putString(KEY_USERS, json).apply();
    }

    // Kullanıcıları SharedPreferences'ten yükle
    private void loadUsersFromPrefs() {
        String json = sharedPreferences.getString(KEY_USERS, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> userList = gson.fromJson(json, listType);

            if (userList != null) {
                for (User user : userList) {
                    users.put(user.getEmail(), user);
                    usersById.put(user.getId(), user);
                }
            }
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
            Project p1 = new Project(1, "AI Chatbot", "Yapay Zeka",
                    "Doğal dil işleme ile chatbot", 5);
            Project p2 = new Project(2, "IoT Akıllı Ev", "IoT",
                    "Akıllı ev otomasyonu", 3);
            Project p3 = new Project(3, "Mobil Oyun", "Mobile",
                    "Unity ile mobil oyun", 4);

            addProject(p1);
            addProject(p2);
            addProject(p3);
        }
    }
}