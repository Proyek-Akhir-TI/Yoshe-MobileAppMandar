package com.example.mandar_app.nelayan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NelayanActivity extends AppCompatActivity {
    private RecyclerView rvNelayan;
    private ProgressBar pbNelayan;
    private RecyclerView.Adapter adNelayan;
    private RecyclerView.LayoutManager lmNelayan;
    private List<ModelNelayan> listNelayan = new ArrayList<>();
    private SearchView searchView;
    private Spinner spinnerFilterGroup;
    private List<String> groupNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nelayan);

        rvNelayan = findViewById(R.id.rv_nelayan);
        pbNelayan = findViewById(R.id.pb_nelayan);
        searchView = findViewById(R.id.search_view);
        spinnerFilterGroup = findViewById(R.id.spinner_filter_group);

        lmNelayan = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNelayan.setLayoutManager(lmNelayan);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query, spinnerFilterGroup.getSelectedItem() != null ? spinnerFilterGroup.getSelectedItem().toString() : "Semua Kelompok");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText, spinnerFilterGroup.getSelectedItem() != null ? spinnerFilterGroup.getSelectedItem().toString() : "Semua Kelompok");
                return false;
            }
        });

        spinnerFilterGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                retrieveAllNelayan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });

        retrieveGroups();

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NelayanActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllNelayan();
    }

    private void retrieveGroups() {
        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponseGroups> proses = API.retrieveGroups();

        proses.enqueue(new Callback<ModelResponseGroups>() {
            @Override
            public void onResponse(Call<ModelResponseGroups> call, Response<ModelResponseGroups> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getKode() == 1) {
                    groupNames.clear();
                    groupNames.add("Semua Kelompok");
                    for (ModelGroup group : response.body().getData()) {
                        if (!groupNames.contains(group.getGroup_name())) {
                            groupNames.add(group.getGroup_name());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NelayanActivity.this,
                            android.R.layout.simple_spinner_item, groupNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFilterGroup.setAdapter(adapter);
                } else {
                    Toast.makeText(NelayanActivity.this, "Data Kelompok Tidak Tersedia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponseGroups> call, Throwable t) {
                Toast.makeText(NelayanActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String query, String groupName) {
        List<ModelNelayan> filteredList = new ArrayList<>();
        for (ModelNelayan nelayan : listNelayan) {
            if (nelayan.getFisherman_name().toLowerCase().contains(query.toLowerCase())) {
                if (groupName.equals("Semua Kelompok") || nelayan.getGroup_name().equalsIgnoreCase(groupName)) {
                    filteredList.add(nelayan);
                }
            }
        }

        Collections.sort(filteredList, new Comparator<ModelNelayan>() {
            @Override
            public int compare(ModelNelayan o1, ModelNelayan o2) {
                return o1.getFisherman_name().compareToIgnoreCase(o2.getFisherman_name());
            }
        });

        adNelayan = new AdapterNelayan(NelayanActivity.this, filteredList, nelayan -> {
            Intent detailIntent = new Intent(NelayanActivity.this, DetailNelayanActivity.class);
            detailIntent.putExtra("nelayan", nelayan);
            startActivity(detailIntent);
        });
        rvNelayan.setAdapter(adNelayan);
    }

    public void retrieveAllNelayan() {
        pbNelayan.setVisibility(View.VISIBLE);

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        String selectedGroupName = spinnerFilterGroup.getSelectedItem() != null ? spinnerFilterGroup.getSelectedItem().toString() : "Semua Kelompok";
        Call<ModelResponse> proses = API.ardRetrieveAllData(selectedGroupName);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getKode() == 1) {
                    listNelayan = response.body().getData();

                    Collections.sort(listNelayan, new Comparator<ModelNelayan>() {
                        @Override
                        public int compare(ModelNelayan o1, ModelNelayan o2) {
                            return o1.getFisherman_name().compareToIgnoreCase(o2.getFisherman_name());
                        }
                    });

                    filter(searchView.getQuery().toString(), selectedGroupName);

                    pbNelayan.setVisibility(View.GONE);
                } else {
                    Toast.makeText(NelayanActivity.this, "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
                    pbNelayan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(NelayanActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbNelayan.setVisibility(View.GONE);
            }
        });
    }
}