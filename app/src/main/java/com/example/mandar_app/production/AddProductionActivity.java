package com.example.mandar_app.production;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.example.mandar_app.loginregist.ModelUser;
import com.example.mandar_app.rapat.RapatActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductionActivity extends AppCompatActivity {
    private EditText etWeight, etDate;
    private Spinner spinnerBoat, spinnerFishSpecies;
    private Button btnAddFish, btnSimpan;
    private List<ModelBoat> listBoats = new ArrayList<>();
    private List<ModelFishSpecies> listFishSpecies = new ArrayList<>();
    private List<ModelProductionDetail> productionDetails = new ArrayList<>();
    private ModelUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_production);

        initView();
        loadUser();
        getBoatData();
        getFishSpeciesData();

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnAddFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = etWeight.getText().toString();
                int idFishSpecies = listFishSpecies.get(spinnerFishSpecies.getSelectedItemPosition()).getId_fish_species();

                if (validateInputs(weight)) {
                    // Tambahkan detail jenis ikan ke daftar
                    ModelProductionDetail detail = new ModelProductionDetail(idFishSpecies, weight);
                    productionDetails.add(detail);
                    Toast.makeText(AddProductionActivity.this, "Ikan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = etDate.getText().toString();
                int idBoat = listBoats.get(spinnerBoat.getSelectedItemPosition()).getId_boat();

                if (validateInputsForSave(date)) {
                    createProduction(date, idBoat, productionDetails);
                }
            }
        });

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddProductionActivity.this, ProductionActivity.class));
            }
        });
    }

    private void initView() {
        etWeight = findViewById(R.id.et_weight);
        etDate = findViewById(R.id.et_date);
        spinnerBoat = findViewById(R.id.spinner_boat);
        spinnerFishSpecies = findViewById(R.id.spinner_fish_species);
        btnAddFish = findViewById(R.id.btn_add_fish);
        btnSimpan = findViewById(R.id.btn_simpan);
    }

    private void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        currentUser = gson.fromJson(jsonUser, ModelUser.class);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void getBoatData() {
        String fishermanName = currentUser.getName();

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelBoatResponse> call = API.ardRetrieveAllBoats(fishermanName);

        call.enqueue(new Callback<ModelBoatResponse>() {
            @Override
            public void onResponse(Call<ModelBoatResponse> call, Response<ModelBoatResponse> response) {
                if (response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    if (kode == 1) {
                        listBoats = response.body().getData();
                        List<String> boatNames = new ArrayList<>();

                        for (ModelBoat boat : listBoats) {
                            boatNames.add(boat.getBoat_name());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductionActivity.this, android.R.layout.simple_spinner_item, boatNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerBoat.setAdapter(adapter);
                    } else {
                        Toast.makeText(AddProductionActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddProductionActivity.this, "Gagal Mengambil Data Perahu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBoatResponse> call, Throwable t) {
                Log.e("AddProductionActivity", "Gagal Menghubungi Server: " + t.getMessage());
                Toast.makeText(AddProductionActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFishSpeciesData() {
        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelFishSpeciesResponse> call = API.ardRetrieveAllFishSpecies();

        call.enqueue(new Callback<ModelFishSpeciesResponse>() {
            @Override
            public void onResponse(Call<ModelFishSpeciesResponse> call, Response<ModelFishSpeciesResponse> response) {
                if (response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    if (kode == 1) {
                        listFishSpecies = response.body().getData();
                        List<String> fishSpeciesNames = new ArrayList<>();
                        for (ModelFishSpecies fishSpecies : listFishSpecies) {
                            fishSpeciesNames.add(fishSpecies.getFish_species());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductionActivity.this, android.R.layout.simple_spinner_item, fishSpeciesNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFishSpecies.setAdapter(adapter);
                    } else {
                        Toast.makeText(AddProductionActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddProductionActivity.this, "Gagal ambil data jenis ikan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelFishSpeciesResponse> call, Throwable t) {
                Log.e("AddProductionActivity", "Gagal menghubungi server: " + t.getMessage());
                Toast.makeText(AddProductionActivity.this, "Gagal menghubungi server:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String weight) {
        if (weight.trim().isEmpty()) {
            etWeight.setError("Berat harus diisi!");
            return false;
        }
        return true;
    }

    private boolean validateInputsForSave(String date) {
        if (productionDetails.isEmpty()) {
            Toast.makeText(this, "Tambahkan minimal satu jenis ikan", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.trim().isEmpty()) {
            etDate.setError("Tanggal harus diisi!");
            return false;
        }
        return true;
    }

    // Method in AddProductionActivity.java to send data
    private void createProduction(String date, int idBoat, List<ModelProductionDetail> productionDetails) {
        ModelProductionRequest productionRequest = new ModelProductionRequest(date, idBoat, productionDetails, currentUser.getBoatName());

        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses = API.ardCreateProduction(productionRequest);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                if (response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    if (kode == 1) {
                        Toast.makeText(AddProductionActivity.this, "Selamat! Sukses Menambah Data Hasil Tangkapan", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (kode == 0) {
                        Toast.makeText(AddProductionActivity.this, pesan, Toast.LENGTH_SHORT).show(); // Menampilkan pesan dari server
                    }
                } else {
                    Toast.makeText(AddProductionActivity.this, "Gagal Tambah Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(AddProductionActivity.this, "Gagal menghubungi server:", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
