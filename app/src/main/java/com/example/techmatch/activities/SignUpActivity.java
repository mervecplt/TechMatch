package com.example.techmatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techmatch.R;
import com.example.techmatch.utils.DataManager;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etSurname, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvBackToLogin;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // DataManager instance
        dataManager = DataManager.getInstance(this);

        // Navigasyon kaydı
        dataManager.navigateTo("SignUpActivity");

        // View'ları bağla
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Sign Up butonu
        btnSignUp.setOnClickListener(v -> handleSignUp());

        // Giriş ekranına dön
        tvBackToLogin.setOnClickListener(v -> {
            dataManager.goBack();
            finish();
        });
    }

    private void handleSignUp() {
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validasyonlar
        if (name.isEmpty()) {
            etName.setError("Ad gerekli");
            etName.requestFocus();
            return;
        }

        if (surname.isEmpty()) {
            etSurname.setError("Soyad gerekli");
            etSurname.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email gerekli");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Geçerli bir email girin");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Şifre gerekli");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Şifre en az 6 karakter olmalı");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Şifreler eşleşmiyor");
            etConfirmPassword.requestFocus();
            return;
        }

        // Email zaten kayıtlı mı?
        if (dataManager.isEmailRegistered(email)) {
            etEmail.setError("Bu email zaten kayıtlı");
            etEmail.requestFocus();
            Toast.makeText(this, "Bu email zaten kullanılıyor. Giriş yapın.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kullanıcıyı kaydet
        boolean success = dataManager.registerUser(name, surname, email, password);

        if (success) {
            Toast.makeText(this, "Kayıt başarılı! Hoş geldiniz " + name, Toast.LENGTH_LONG).show();

            // Login ekranına dön
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.putExtra("registered_email", email); // Email'i Login'e gönder
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Kayıt başarısız. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        dataManager.goBack();
        super.onBackPressed();
    }
}