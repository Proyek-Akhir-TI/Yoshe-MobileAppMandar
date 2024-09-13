package com.example.mandar_app.production;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.example.mandar_app.loginregist.ModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionActivity extends AppCompatActivity {
    private RecyclerView rvProduction;
    private FloatingActionButton fabAddProduction;
    private ProgressBar pbProduction;
    private TextView tvCurrentMonth, tvTotalCatch, tvCurrentDay, tvCurrentDate, tvTotalDaily, tvMostFishSpecies;
    private Button btnDatePicker, btnFilter;
    private String selectedDate = "";
    private RecyclerView.Adapter adProduction;
    private RecyclerView.LayoutManager lmProduction;
    private List<ModelProduction> listProduction = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        rvProduction = findViewById(R.id.rv_production);
        fabAddProduction = findViewById(R.id.fab_add_production);
        pbProduction = findViewById(R.id.pb_production);
        tvCurrentMonth = findViewById(R.id.tv_current_month);
        tvTotalCatch = findViewById(R.id.tv_total_catch);
        tvCurrentDay = findViewById(R.id.tv_current_day);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvTotalDaily = findViewById(R.id.tv_total_daily_catch);
        tvMostFishSpecies = findViewById(R.id.tv_most_fish_species);

        lmProduction = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduction.setLayoutManager(lmProduction);

        btnDatePicker = findViewById(R.id.btn_date_picker);
        btnFilter = findViewById(R.id.btn_filter);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        ModelUser currentUser = gson.fromJson(jsonUser, ModelUser.class);

        if (currentUser.getRole().equals("pengelola")) {
            fabAddProduction.setVisibility(View.GONE);
        } else if (currentUser.getRole().equals("nelayan")) {
            fabAddProduction.setVisibility(View.VISIBLE);
            fabAddProduction.setOnClickListener(view -> startActivity(new Intent(ProductionActivity.this, AddProductionActivity.class)));
        }

        updateCurrentDateInfo();

        findViewById(R.id.icon_back).setOnClickListener(view -> startActivity(new Intent(ProductionActivity.this, MainActivity.class)));

        btnDatePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ProductionActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        btnDatePicker.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnFilter.setOnClickListener(v -> filterProductionByDate(selectedDate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllProduction();
    }

    private void filterProductionByDate(String date) {
        if (date.isEmpty()) {
            Toast.makeText(this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<ModelProduction> filteredList = new ArrayList<>();
        for (ModelProduction production : listProduction) {
            try {
                Date productionDate = sdf.parse(production.getDate());
                Date selectedParsedDate = sdf.parse(date);
                if (productionDate.equals(selectedParsedDate)) {
                    filteredList.add(production);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data untuk tanggal yang dipilih", Toast.LENGTH_SHORT).show();
            adProduction = new AdapterProduction(ProductionActivity.this, listProduction);
        } else {
            adProduction = new AdapterProduction(ProductionActivity.this, filteredList);
        }
        rvProduction.setAdapter(adProduction);
        adProduction.notifyDataSetChanged();
    }

    private void updateCurrentDateInfo() {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", locale);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", locale);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
        Date currentDate = new Date();

        tvCurrentMonth.setText(monthFormat.format(currentDate));
        tvCurrentDay.setText(dayFormat.format(currentDate));
        tvCurrentDate.setText(dateFormat.format(currentDate));
    }

    public void retrieveAllProduction() {
        pbProduction.setVisibility(View.VISIBLE);

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses;

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        ModelUser currentUser = gson.fromJson(jsonUser, ModelUser.class);

        if (currentUser.getRole().equals("nelayan")) {
            proses = API.ardRetrieveProductionByFisherman(currentUser.getName());
        } else {
            proses = API.ardRetrieveAllProduction();
        }

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listProduction = response.body().getData();

                if (listProduction == null || listProduction.isEmpty()) {
                    // Jika data kosong, tampilkan toast dan jangan lanjutkan
                    Toast.makeText(ProductionActivity.this, "Data tangkapan kosong", Toast.LENGTH_SHORT).show();
                    pbProduction.setVisibility(View.GONE);
                    return;
                }

                // Jika data tidak kosong, lanjutkan dengan proses sorting dan lainnya
                Collections.sort(listProduction, (o1, o2) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        Date date1 = sdf.parse(o1.getDate());
                        Date date2 = sdf.parse(o2.getDate());
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });

                adProduction = new AdapterProduction(ProductionActivity.this, listProduction);
                rvProduction.setAdapter(adProduction);
                adProduction.notifyDataSetChanged();

                calculateAndUpdateTotals();

                pbProduction.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(ProductionActivity.this, "Gagal menghubungi server.", Toast.LENGTH_SHORT).show();
                pbProduction.setVisibility(View.GONE);
            }
        });
    }

    private void calculateAndUpdateTotals() {
        int totalCatchForMonth = 0;
        int totalCatchForToday = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        String currentMonth = currentDate.substring(0, 7);

        for (ModelProduction production : listProduction) {
            int weight = Integer.parseInt(production.getTotal_weight());
            String productionDate = production.getDate();

            if (productionDate.startsWith(currentMonth)) {
                totalCatchForMonth += weight;
            }

            if (productionDate.equals(currentDate)) {
                totalCatchForToday += weight;
            }
        }

        tvTotalCatch.setText(totalCatchForMonth + " kg");
        tvTotalDaily.setText(totalCatchForToday + " kg");

        Map<String, Integer> fishSpeciesCount = new HashMap<>();
        for (ModelProduction production : listProduction) {
            if (production.getDate().equals(currentDate)) {
                for (ModelProductionDetail detail : production.getFish_details()) {
                    String fishSpecies = detail.getFish_species();
                    int fishWeight = Integer.parseInt(detail.getWeight());
                    fishSpeciesCount.put(fishSpecies, fishSpeciesCount.getOrDefault(fishSpecies, 0) + fishWeight);
                }
            }
        }

        String mostCaughtFish = "";
        int maxWeight = 0;
        for (Map.Entry<String, Integer> entry : fishSpeciesCount.entrySet()) {
            if (entry.getValue() > maxWeight) {
                maxWeight = entry.getValue();
                mostCaughtFish = entry.getKey();
            }
        }

        if (!mostCaughtFish.isEmpty()) {
            tvMostFishSpecies.setText(mostCaughtFish + " (" + maxWeight + " kg)");
        } else {
            tvMostFishSpecies.setText("Hari Ini Kosong");
        }
    }
}
