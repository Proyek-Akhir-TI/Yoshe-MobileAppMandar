package com.example.mandar_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mandar_app.bbm.BBMActivity;
import com.example.mandar_app.kelompok.KelompokActivity;
import com.example.mandar_app.loginregist.LoginActivity;
import com.example.mandar_app.loginregist.ModelUser;
import com.example.mandar_app.loginregist.ProfileActivity;
import com.example.mandar_app.nelayan.NelayanActivity;
import com.example.mandar_app.perahu.PerahuActivity;
import com.example.mandar_app.production.ProductionActivity;
import com.example.mandar_app.rapat.RapatActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    CardView cardKelompok, cardTangkapan, cardPerahu, cardNelayan, cardBBM, cardRapat;
    ImageView iconProfile, iconLogout;
    ViewPager2 viewPager2;
    BannerAdapter bannerAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    List<ModelBanner> bannerList = new ArrayList<>();
    private ModelUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Ambil user dari SharedPreferences
        String jsonUser = sharedPreferences.getString("user", "");
        if (!jsonUser.isEmpty()) {
            Gson gson = new Gson();
            user = gson.fromJson(jsonUser, ModelUser.class); // Deserialisasi dari JSON ke ModelUser
        }

        // Terima role dari SharedPreferences
        String role = sharedPreferences.getString("role", "");

        // Inisialisasi ViewPager2
        viewPager2 = findViewById(R.id.view_pager);
        bannerAdapter = new BannerAdapter(this, bannerList);
        viewPager2.setAdapter(bannerAdapter);
        retrieveBanners();

        // Inisialisasi CardView dan Icon Profile
        cardKelompok = findViewById(R.id.card_kelompok);
        cardTangkapan = findViewById(R.id.card_tangkapan);
        cardPerahu = findViewById(R.id.card_perahu);
        cardNelayan = findViewById(R.id.card_nelayan);
        cardBBM = findViewById(R.id.card_bbm);
        cardRapat = findViewById(R.id.card_rapat);
        iconProfile = findViewById(R.id.icon_profile);
        iconLogout = findViewById(R.id.icon_logout); // Inisialisasi ikon logout

        // Tentukan visibilitas CardView dan Icon Profile berdasarkan role
        if ("nelayan".equals(role)) {
            cardKelompok.setVisibility(View.GONE);
            cardPerahu.setVisibility(View.GONE);
            cardRapat.setVisibility(View.GONE);
            cardNelayan.setVisibility(View.GONE);
            iconProfile.setVisibility(View.VISIBLE);
        } else if ("pengelola".equals(role)) {
            iconProfile.setVisibility(View.GONE);
        }

        // OnClickListener untuk setiap CardView dan Icon Profile
        cardKelompok.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, KelompokActivity.class)));
        cardTangkapan.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProductionActivity.class)));
        cardPerahu.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PerahuActivity.class)));
        cardNelayan.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NelayanActivity.class)));
        cardBBM.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BBMActivity.class)));
        cardRapat.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RapatActivity.class)));
        iconProfile.setOnClickListener(view -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.putExtra("user", user);
            startActivity(profileIntent);
        });

        iconLogout.setOnClickListener(view -> {
            // Hapus status login di SharedPreferences dan kembali ke LoginActivity
            editor.clear();
            editor.apply();

            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void retrieveBanners() {
        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelBannerResponse> call = API.ardRetrieveAllBanners();

        call.enqueue(new Callback<ModelBannerResponse>() {
            @Override
            public void onResponse(Call<ModelBannerResponse> call, Response<ModelBannerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getKode() == 1) {
                        bannerList.addAll(response.body().getData());
                        bannerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelBannerResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
}
