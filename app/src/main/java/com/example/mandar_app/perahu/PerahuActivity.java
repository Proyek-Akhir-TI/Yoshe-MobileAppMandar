package com.example.mandar_app.perahu;

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

public class PerahuActivity extends AppCompatActivity {
    private RecyclerView rvPerahu;
    private ProgressBar pbPerahu;
    private TextView tvTotalPerahu, tvAktifPerahu, tvAvgPanjangPerahu, tvAvgLebarPerahu, tvInfoTangkap, tvInfoPerahu;

    private RecyclerView.Adapter adPerahu;
    private RecyclerView.LayoutManager lmPerahu;
    private List<ModelPerahu> listPerahu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perahu);

        rvPerahu = findViewById(R.id.rv_perahu);
        pbPerahu = findViewById(R.id.pb_perahu);
        tvTotalPerahu = findViewById(R.id.tv_total_perahu);
        tvAktifPerahu = findViewById(R.id.tv_aktif_perahu);
        tvAvgPanjangPerahu = findViewById(R.id.tv_avg_panjang_perahu);
        tvAvgLebarPerahu = findViewById(R.id.tv_avg_lebar_perahu);

        lmPerahu = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPerahu.setLayoutManager(lmPerahu);

        // Set OnClickListener untuk tombol kembali
        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerahuActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllPerahu();
    }

    public void retrieveAllPerahu() {
        pbPerahu.setVisibility(View.VISIBLE);

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses = API.ardRetrieveAllData();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listPerahu = response.body().getData();

                // Mengurutkan listPerahu berdasarkan abjad nama perahu
                Collections.sort(listPerahu, new Comparator<ModelPerahu>() {
                    @Override
                    public int compare(ModelPerahu p1, ModelPerahu p2) {
                        return p1.getBoat_name().compareToIgnoreCase(p2.getBoat_name());
                    }
                });

                // Menghitung jumlah perahu dan jumlah perahu aktif
                int totalPerahu = listPerahu.size();
                int aktifPerahu = 0;
                double totalPanjang = 0;
                double totalLebar = 0;
                for (ModelPerahu perahu : listPerahu) {
                    totalPanjang += Double.parseDouble(perahu.getBoat_length());
                    totalLebar += Double.parseDouble(perahu.getBoat_width());
                    if (isBoatActive(perahu.getId_boat())) {
                        aktifPerahu++;
                    }
                }
                double avgPanjang = totalPanjang / totalPerahu;
                double avgLebar = totalLebar / totalPerahu;

                // Menampilkan hasil dengan animasi
                animateCount(tvTotalPerahu, 0, totalPerahu, 1000);
                animateCount(tvAktifPerahu, 0, aktifPerahu, 1000);
                animateCount(tvAvgPanjangPerahu, 0, (int) avgPanjang , 1000);
                animateCount(tvAvgLebarPerahu, 0, (int) avgLebar, 1000);

                adPerahu = new AdapterPerahu(PerahuActivity.this, listPerahu);
                rvPerahu.setAdapter(adPerahu);
                adPerahu.notifyDataSetChanged();

                pbPerahu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(PerahuActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbPerahu.setVisibility(View.GONE);
            }
        });
    }

    private boolean isBoatActive(int id_boat) {
        return true;
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
