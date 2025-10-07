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

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUp, tvForgotPassword;

    // Test hesabı (şimdilik - sonra Firebase ile değiştirilecek)
    private static final String TEST_EMAIL = "test@techmatch.com";
    private static final String TEST_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View'ları bağla
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Sign In butonu
        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            // Validasyon
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen email ve şifrenizi girin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email formatı kontrolü
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Geçerli bir email adresi girin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Giriş kontrolü (şimdilik test hesabı ile)
            // Debug için kontrol edelim
            if (email.equalsIgnoreCase(TEST_EMAIL) && password.equals(TEST_PASSWORD)) {
                // Başarılı giriş
                Toast.makeText(this, "Giriş başarılı! Hoş geldiniz", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Hatalı giriş - Debug mesajı
                Toast.makeText(this, "Email veya şifre hatalı!\nTest: test@techmatch.com / 123456", Toast.LENGTH_LONG).show();
            }
        });

        // Sign Up linki
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Şifremi Unuttum
        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Lütfen email adresinizi girin", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Geçerli bir email adresi girin", Toast.LENGTH_SHORT).show();
            } else {
                // Şifre sıfırlama maili gönderildi (simülasyon)
                Toast.makeText(this, "Şifre sıfırlama bağlantısı " + email + " adresine gönderildi", Toast.LENGTH_LONG).show();
            }
        });
    }
}