package com.example.mandar_app.loginregist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Periksa apakah sudah login sebelumnya
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Jika sudah login, langsung ke MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("role", sharedPreferences.getString("role", ""));
            intent.putExtra("user", sharedPreferences.getString("user", ""));
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Nama pengguna wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Kata sandi wajib diisi");
            return;
        }

        ApiRequestData apiRequestData = RetroServer.getRetroServer().create(ApiRequestData.class);
        Call<ApiResponse> call = apiRequestData.login(username, password);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    String message = apiResponse.getMessage();

                    switch (message) {
                        case "Login successful":
                            // Simpan status login ke SharedPreferences
                            Gson gson = new Gson();
                            String jsonUser = gson.toJson(apiResponse.getUser());

                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("role", apiResponse.getRole());
                            editor.putString("user", jsonUser); // Simpan objek user sebagai JSON
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("role", apiResponse.getRole()); // Kirim role ke MainActivity
                            intent.putExtra("user", jsonUser);
                            startActivity(intent);
                            finish();
                            break;

                        case "Password salah":
                            Toast.makeText(LoginActivity.this, "Kata Sandi Salah!", Toast.LENGTH_SHORT).show();
                            break;

                        case "Username tidak ada":
                            Toast.makeText(LoginActivity.this, "Nama Pengguna Tidak Ada!", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(LoginActivity.this, "Nama Pengguna dan Kata Sandi Salah", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Gagal login, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
