package com.example.mandar_app.kelompok;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelompokActivity extends AppCompatActivity {
    private RecyclerView rvKelompok;
    private ProgressBar pbKelompok;
    private TextView tvTotalKelompok, tvTotalAnggota, tvInfoKelompok;

    private RecyclerView.Adapter adKelompok;
    private RecyclerView.LayoutManager lmKelompok;
    private List<ModelKelompok> listKelompok = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelompok);

        rvKelompok = findViewById(R.id.rv_kelompok);
        pbKelompok = findViewById(R.id.pb_kelompok);
        tvTotalKelompok = findViewById(R.id.tv_total_kelompok);
        tvTotalAnggota = findViewById(R.id.tv_total_anggota);
        tvInfoKelompok = findViewById(R.id.tv_info_kelompok);

        lmKelompok = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false
        );
        rvKelompok.setLayoutManager(lmKelompok);

        // Set OnClickListener for back button
        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KelompokActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllKelompok();
    }

    public void retrieveAllKelompok() {
        pbKelompok.setVisibility(View.VISIBLE);

        ApiRequestData API = RetroServer.konekRetrofit().create(
                ApiRequestData.class
        );
        Call<ModelResponse> proses = API.ardRetrieveAllData();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listKelompok = response.body().getData();

                // Sorting list based on group name
                Collections.sort(listKelompok, new Comparator<ModelKelompok>() {
                    @Override
                    public int compare(ModelKelompok o1, ModelKelompok o2) {
                        return o1.getGroup_name().compareTo(o2.getGroup_name());
                    }
                });

                int totalAnggota = 0;
                for (ModelKelompok kelompok : listKelompok) {
                    totalAnggota += kelompok.getJumlah_anggota();
                }

                animateCount(tvTotalKelompok, 0, listKelompok.size(), 1000);
                animateCount(tvTotalAnggota, 0, totalAnggota, 1000);

                // Update additional information
                tvInfoKelompok.setText("Kelompok nelayan adalah grup yang terdiri dari para nelayan yang bekerja bersama dalam kegiatan perikanan dan aktivitas terkait. Kegiatan yang dilakukan oleh kelompok nelayan termasuk menangkap ikan, mengelola hasil tangkapan, dan berpartisipasi dalam pelatihan dan pengembangan kapasitas.");

                adKelompok = new AdapterKelompok(
                        KelompokActivity.this,
                        listKelompok
                );
                rvKelompok.setAdapter(adKelompok);
                adKelompok.notifyDataSetChanged();

                pbKelompok.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(KelompokActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbKelompok.setVisibility(View.GONE);
            }
        });
    }

    private void animateCount(TextView textView, int start, int end, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        animator.start();
    }
}
