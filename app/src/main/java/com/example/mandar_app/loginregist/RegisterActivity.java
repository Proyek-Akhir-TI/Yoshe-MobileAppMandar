package com.example.mandar_app.loginregist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandar_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etUsername, etEmail, etPassword, etConfirmPassword, etKusuka; // Tambahkan etKusuka
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etKusuka = findViewById(R.id.etKusuka); // Inisialisasi etKusuka
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String kusuka = etKusuka.getText().toString().trim(); // Ambil nilai kusuka

        // Validasi kusuka harus diisi
        if (TextUtils.isEmpty(kusuka)) {
            etKusuka.setError("Nomor Kusuka wajib diisi");
            return;
        }

        // Validasi panjang kusuka harus 16 karakter
        if (kusuka.length() < 16) {
            etKusuka.setError("Nomor Kusuka harus 16 karakter");
            return;
        }

        // Validasi lainnya
        if (TextUtils.isEmpty(name)) {
            etName.setError("Nama lengkap wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Nama pengguna wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email tidak valid");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Kata sandi wajib diisi");
            return;
        }

        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?!.*[@!#$%^&*]).{8,}$")) {
            etPassword.setError("Kata sandi harus minimal 8 karakter dan terdiri dari huruf dan angka tanpa karakter spesial");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Kata sandi tidak sesuai");
            return;
        }

        ApiRequestData apiRequestData = RetroServer.getRetroServer().create(ApiRequestData.class);
        Call<ApiResponse> call = apiRequestData.register(name, username, email, password, kusuka); // Tambahkan kusuka ke API call

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getMessage().equals("Nomor Kusuka telah terdaftar")) {
                        etKusuka.setError("Nomor Kusuka telah terdaftar");
                        return;
                    }
                    Toast.makeText(RegisterActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if ("Pengguna Berhasil Membuat Akun Baru!".equals(apiResponse.getMessage())) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
