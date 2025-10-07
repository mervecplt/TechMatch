package com.example.techmatch.utils;

import com.example.techmatch.datastructures.CustomHashMap;
import com.example.techmatch.datastructures.LinkedList;
import com.example.techmatch.datastructures.Queue;
import com.example.techmatch.datastructures.Stack;
import com.example.techmatch.models.Achievement;
import com.example.techmatch.models.Project;
import com.example.techmatch.models.TeamRequest;
import com.example.techmatch.models.User;

public class DataManager {
    private static DataManager instance;

    // Veri yapıları
    private LinkedList<User> users;
    private LinkedList<Project> projects;
    private Queue<TeamRequest> teamRequests;
    private Stack<String> searchHistory;
    private CustomHashMap<String, LinkedList<User>> categoryMap;

    private DataManager() {
        users = new LinkedList<>();
        projects = new LinkedList<>();
        teamRequests = new Queue<>();
        searchHistory = new Stack<>();
        categoryMap = new CustomHashMap<>();

        initializeSampleData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Örnek Kullanıcılar
        User user1 = new User(1, "Ahmet Yılmaz", "Bilgisayar Mühendisliği", "ahmet@email.com");
        user1.setBio("Yapay zeka ve makine öğrenmesi ile ilgileniyorum");
        user1.addSkill("Python");
        user1.addSkill("Machine Learning");
        user1.addSkill("TensorFlow");
        user1.addCategory("Yapay Zeka");
        user1.addCategory("Yazılım");

        Achievement ach1 = new Achievement(1, "Teknofest 2023", "Yapay Zeka", "Yarışma");
        ach1.setRank("2. Yer");
        ach1.setDescription("Görüntü işleme projesi ile finalist");
        user1.addAchievement(ach1);

        User user2 = new User(2, "Ayşe Demir", "Elektrik-Elektronik Mühendisliği", "ayse@email.com");
        user2.setBio("Gömülü sistemler ve IoT projelerinde deneyimliyim");
        user2.addSkill("Arduino");
        user2.addSkill("C++");
        user2.addSkill("IoT");
        user2.addCategory("Donanım");
        user2.addCategory("IoT");

        Achievement ach2 = new Achievement(2, "Akıllı Ev Sistemi", "IoT", "Proje");
        ach2.setDescription("Üniversite projesi - Tam otomasyon");
        user2.addAchievement(ach2);

        User user3 = new User(3, "Mehmet Kaya", "Yazılım Mühendisliği", "mehmet@email.com");
        user3.setBio("Full-stack developer, mobil uygulama geliştiriyorum");
        user3.addSkill("Java");
        user3.addSkill("Android");
        user3.addSkill("React");
        user3.addCategory("Mobil Uygulama");
        user3.addCategory("Yazılım");

        Achievement ach3 = new Achievement(3, "Google Developer Sertifikası", "Android", "Sertifika");
        ach3.setDescription("Associate Android Developer");
        user3.addAchievement(ach3);

        User user4 = new User(4, "Zeynep Şahin", "Endüstri Mühendisliği", "zeynep@email.com");
        user4.setBio("Veri analizi ve optimizasyon");
        user4.addSkill("Python");
        user4.addSkill("Data Analysis");
        user4.addSkill("Excel");
        user4.addCategory("Veri Bilimi");

        User user5 = new User(5, "Can Arslan", "Makine Mühendisliği", "can@email.com");
        user5.setBio("Robotik ve otomasyon sistemleri");
        user5.addSkill("ROS");
        user5.addSkill("Python");
        user5.addSkill("CAD");
        user5.addCategory("Robotik");
        user5.addCategory("Donanım");

        Achievement ach5 = new Achievement(5, "Robot Yarışması", "Robotik", "Yarışma");
        ach5.setRank("1. Yer");
        user5.addAchievement(ach5);

        // Kullanıcıları ekle
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        // Örnek Projeler
        Project project1 = new Project(1, "Yapay Zeka Tabanlı Sağlık Asistanı",
                "Yapay Zeka", "Hastalık teşhisi için ML modeli", 1, "Ahmet Yılmaz");
        project1.addRequiredSkill("Python");
        project1.addRequiredSkill("Machine Learning");
        project1.setMaxTeamSize(4);

        Project project2 = new Project(2, "Akıllı Tarım Sistemi",
                "IoT", "Sensörlerle tarla izleme", 2, "Ayşe Demir");
        project2.addRequiredSkill("Arduino");
        project2.addRequiredSkill("IoT");
        project2.setMaxTeamSize(3);

        Project project3 = new Project(3, "Eğitim Uygulaması",
                "Mobil Uygulama", "Öğrenciler için interaktif platform", 3, "Mehmet Kaya");
        project3.addRequiredSkill("Java");
        project3.addRequiredSkill("Android");
        project3.setMaxTeamSize(5);

        Project project4 = new Project(4, "Otonom Araç Simülasyonu",
                "Robotik", "ROS tabanlı simülasyon", 5, "Can Arslan");
        project4.addRequiredSkill("ROS");
        project4.addRequiredSkill("Python");
        project4.setMaxTeamSize(4);

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projects.add(project4);

        // Kategoriye göre kullanıcıları grupla
        updateCategoryMap();

        // Örnek takım başvuruları
        TeamRequest req1 = new TeamRequest(1, 3, "Mehmet Kaya", 1,
                "Yapay Zeka Tabanlı Sağlık Asistanı", "Android geliştirme konusunda yardımcı olabilirim");
        TeamRequest req2 = new TeamRequest(2, 4, "Zeynep Şahin", 1,
                "Yapay Zeka Tabanlı Sağlık Asistanı", "Veri analizi yapabilirim");

        teamRequests.enqueue(req1);
        teamRequests.enqueue(req2);
    }

    private void updateCategoryMap() {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            LinkedList<String> categories = user.getCategories();

            for (int j = 0; j < categories.size(); j++) {
                String category = categories.get(j);
                LinkedList<User> categoryUsers = categoryMap.get(category);

                if (categoryUsers == null) {
                    categoryUsers = new LinkedList<>();
                    categoryMap.put(category, categoryUsers);
                }
                categoryUsers.add(user);
            }
        }
    }

    // Kullanıcı arama (isme göre)
    public LinkedList<User> searchUsersByName(String query) {
        LinkedList<User> results = new LinkedList<>();
        searchHistory.push(query);

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(user);
            }
        }
        return results;
    }

    // Kategoriye göre kullanıcı arama
    public LinkedList<User> searchUsersByCategory(String category) {
        searchHistory.push("Kategori: " + category);
        LinkedList<User> result = categoryMap.get(category);
        return result != null ? result : new LinkedList<>();
    }

    // Beceriye göre kullanıcı arama
    public LinkedList<User> searchUsersBySkill(String skill) {
        LinkedList<User> results = new LinkedList<>();
        searchHistory.push("Beceri: " + skill);

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.hasSkill(skill)) {
                results.add(user);
            }
        }
        return results;
    }

    // Proje arama
    public LinkedList<Project> searchProjectsByCategory(String category) {
        LinkedList<Project> results = new LinkedList<>();

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            if (project.getCategory().equalsIgnoreCase(category)) {
                results.add(project);
            }
        }
        return results;
    }

    // ID'ye göre kullanıcı bul
    public User getUserById(int id) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    // ID'ye göre proje bul
    public Project getProjectById(int id) {
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            if (project.getId() == id) {
                return project;
            }
        }
        return null;
    }

    // Tüm getters
    public LinkedList<User> getAllUsers() {
        return users;
    }

    public LinkedList<Project> getAllProjects() {
        return projects;
    }

    public Queue<TeamRequest> getTeamRequests() {
        return teamRequests;
    }

    public Stack<String> getSearchHistory() {
        return searchHistory;
    }

    // Takım başvurusu ekle
    public void addTeamRequest(TeamRequest request) {
        teamRequests.enqueue(request);
    }

    // Son arama
    public String getLastSearch() {
        return searchHistory.peek();
    }
}