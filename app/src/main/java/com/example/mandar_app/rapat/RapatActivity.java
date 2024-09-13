package com.example.mandar_app.rapat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.example.mandar_app.kelompok.KelompokActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RapatActivity extends AppCompatActivity {
    public RecyclerView rvRapat;
    public ProgressBar pbRapat;
    public ApiRequestData apiRequestData;
    private RecyclerView.Adapter adRapat;
    private RecyclerView.LayoutManager lmRapat;
    public List<ModelRapat> listRapat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapat);

        rvRapat = findViewById(R.id.rv_rapat);
        pbRapat = findViewById(R.id.pb_rapat);

        lmRapat = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRapat.setLayoutManager(lmRapat);

        // Set OnClickListener for back button
        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RapatActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllRapat();
    }

    public void retrieveAllRapat() {
        pbRapat.setVisibility(View.VISIBLE);

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses = API.ardRetrieveAllData();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listRapat = response.body().getData();

                // Sort list by date in descending order
                Collections.sort(listRapat, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));

                adRapat = new AdapterRapat(RapatActivity.this, listRapat);
                rvRapat.setAdapter(adRapat);
                adRapat.notifyDataSetChanged();

                pbRapat.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(RapatActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbRapat.setVisibility(View.GONE);
            }
        });
    }
}
