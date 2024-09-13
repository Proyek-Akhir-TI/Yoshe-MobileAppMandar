package com.example.mandar_app.bbm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mandar_app.R;
import com.example.mandar_app.loginregist.ModelUser;
import com.example.mandar_app.production.AdapterProduction;
import com.example.mandar_app.production.ModelProduction;
import com.example.mandar_app.production.ProductionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BBMActivity extends AppCompatActivity {
    private RecyclerView rvBBM;
    private FloatingActionButton fabTambahBBM;
    private ProgressBar pbBBM;
    private TextView tvBbmBulanIni, tvBulanIni, tvBbmHariIni, tvHariIni, tvTanggalIni;
    private Button btnDatePicker, btnFilter;
    private String selectedDate = "";

    private RecyclerView.Adapter adBBM;
    private RecyclerView.LayoutManager lmBBM;
    private List<ModelBBM> listBBM = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbm);

        rvBBM = findViewById(R.id.rv_bbm);
        fabTambahBBM = findViewById(R.id.fab_tambah_bbm);
        pbBBM = findViewById(R.id.pb_bbm);
        tvBbmBulanIni = findViewById(R.id.tv_bbm_bulan_ini);
        tvBulanIni = findViewById(R.id.tv_bulan_ini);
        tvBbmHariIni = findViewById(R.id.tv_bbm_hari_ini);
        tvHariIni = findViewById(R.id.tv_hari_ini);
        tvTanggalIni = findViewById(R.id.tv_tanggal_ini);

        lmBBM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBBM.setLayoutManager(lmBBM);

        btnDatePicker = findViewById(R.id.btn_date_picker);
        btnFilter = findViewById(R.id.btn_filter);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        ModelUser currentUser = gson.fromJson(jsonUser, ModelUser.class);

        if (currentUser.getRole().equals("pengelola")) {
            fabTambahBBM.setVisibility(View.GONE);
        } else if (currentUser.getRole().equals("nelayan")) {
            fabTambahBBM.setVisibility(View.VISIBLE);
            fabTambahBBM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BBMActivity.this, AddBBMActivity.class));
                }
            });
        }

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDatePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BBMActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        btnDatePicker.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnFilter.setOnClickListener(v -> filterBBMByDate(selectedDate));

        updateCurrentDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveAllBBM();
    }

    private void filterBBMByDate(String date) {
        if (date.isEmpty()) {
            Toast.makeText(this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<ModelBBM> filteredList = new ArrayList<>();
        for (ModelBBM bbm : listBBM) {
            try {
                Date bbmDate = sdf.parse(bbm.getDate());
                Date selectedParsedDate = sdf.parse(date);
                if (bbmDate.equals(selectedParsedDate)) {
                    filteredList.add(bbm);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data untuk tanggal yang dipilih", Toast.LENGTH_SHORT).show();
            adBBM = new AdapterBBM(BBMActivity.this, listBBM);
        } else {
            adBBM = new AdapterBBM(BBMActivity.this, filteredList);
        }
        rvBBM.setAdapter(adBBM);
        adBBM.notifyDataSetChanged();
    }

    public void retrieveAllBBM() {
        pbBBM.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        ModelUser currentUser = gson.fromJson(jsonUser, ModelUser.class);

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);

        Call<ModelResponse> proses;
        if (currentUser.getRole().equals("pengelola")) {
            proses = API.ardRetrieveAllData("", currentUser.getRole()); // Kirimkan string kosong atau sesuai jika role pengelola
        } else if (currentUser.getRole().equals("nelayan")) {
            // Mengirimkan nama boat yang terkait dengan nelayan
            proses = API.ardRetrieveAllData(currentUser.getBoatName(), currentUser.getRole());
        } else {
            Toast.makeText(this, "Role tidak diketahui", Toast.LENGTH_SHORT).show();
            pbBBM.setVisibility(View.GONE);
            return;
        }

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                if (response.body() != null) {
                    listBBM = response.body().getData();

                    if (listBBM != null && !listBBM.isEmpty()) {
                        Collections.sort(listBBM, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                        adBBM = new AdapterBBM(BBMActivity.this, listBBM);
                        rvBBM.setAdapter(adBBM);
                        adBBM.notifyDataSetChanged();
                        calculateStatistics();
                    } else {
                        Toast.makeText(BBMActivity.this, "Data BBM tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BBMActivity.this, "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show();
                }
                pbBBM.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(BBMActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbBBM.setVisibility(View.GONE);
            }
        });
    }


    private void calculateStatistics() {
        int currentMonthUsage = 0;
        int currentDayUsage = 0;
        double totalUsage = 0;
        double totalPrice = 0;
        int count = listBBM.size();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        for (ModelBBM bbm : listBBM) {
            String date = bbm.getDate();
            String month = date.split("-")[1];

            if (month.equals(getCurrentMonth())) {
                currentMonthUsage += Integer.parseInt(bbm.getQuantity_liters());
            }

            if (date.equals(today)) {
                currentDayUsage += Integer.parseInt(bbm.getQuantity_liters());
            }

            totalUsage += Integer.parseInt(bbm.getQuantity_liters());
            totalPrice += Double.parseDouble(bbm.getTotal_fuel());
        }

        double avgUsage = totalUsage / count;
        double avgPrice = totalPrice / count;

        tvBbmBulanIni.setText(String.format("%d Liter", currentMonthUsage));
        tvBbmHariIni.setText(String.format("%d Liter", currentDayUsage));
        tvBulanIni.setText(getCurrentMonthName());
        tvHariIni.setText(getCurrentDayName());
    }

    private String getCurrentMonthName() {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", locale);
        return sdf.format(new Date());
    }

    private String getCurrentDayName() {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", locale);
        return sdf.format(new Date());
    }

    private String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(new Date());
    }

    private void updateCurrentDate() {
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", locale);
        String currentDate = sdf.format(new Date());
        tvTanggalIni.setText(currentDate);
    }
}
