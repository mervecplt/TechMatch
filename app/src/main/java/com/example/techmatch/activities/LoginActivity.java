package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.MainActivity;
import com.example.techmatch.R;
import com.example.techmatch.models.User;
import com.example.techmatch.utils.DataManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // DataManager instance
        dataManager = DataManager.getInstance(this);

        // Navigasyon kaydı
        dataManager.navigateTo("LoginActivity");

        // View'ları bağla
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // SignUp'tan gelen email varsa doldur
        String registeredEmail = getIntent().getStringExtra("registered_email");
        if (registeredEmail != null) {
            etEmail.setText(registeredEmail);
            etPassword.requestFocus();
        }

        // NOT: Otomatik giriş kontrolü KALDIRILDI
        // Artık her seferinde şifre istenecek

        // Login butonu
        btnLogin.setOnClickListener(v -> handleLogin());

        // Sign Up'a git
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validasyonlar
        if (email.isEmpty()) {
            etEmail.setError("Email gerekli");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Şifre gerekli");
            etPassword.requestFocus();
            return;
        }

        // Giriş denemesi
        User user = dataManager.loginUser(email, password);

        if (user != null) {
            // Giriş başarılı
            Toast.makeText(this, "Hoş geldiniz " + user.getName(), Toast.LENGTH_SHORT).show();
            goToMainActivity();
        } else {
            // Giriş başarısız
            if (!dataManager.isEmailRegistered(email)) {
                Toast.makeText(this, "Bu email kayıtlı değil. Önce kayıt olun.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Yanlış şifre. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
                etPassword.setError("Yanlış şifre");
                etPassword.requestFocus();
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Login ekranına geri dönülmesin
    }

    @Override
    public void onBackPressed() {
        // Login ekranında geri tuşu uygulamadan çıkar
        finishAffinity(); // Tüm activity'leri kapat
    }
}