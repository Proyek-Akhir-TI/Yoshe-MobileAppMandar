package com.example.mandar_app.bbm;

import android.app.DatePickerDialog;
import android.content.Context;
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

import com.example.mandar_app.R;
import com.example.mandar_app.loginregist.ModelUser;
import com.example.mandar_app.production.ModelBoat;
import com.example.mandar_app.production.ModelBoatResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBBMActivity extends AppCompatActivity {
    private EditText etJenisBBM, etQuantity, etPricePerLiter, etDate;
    private Spinner spinnerBoat;
    private Button btnSimpan;
    private List<ModelBoat> listBoats = new ArrayList<ModelBoat>();
    private ModelUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bbm);

        initView();
        loadUser();
        getBoatData();

        // Set default value for fuel_species
        etJenisBBM.setText("Solar");

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jenisBBM = etJenisBBM.getText().toString();
                String date = etDate.getText().toString();
                String quantity = etQuantity.getText().toString();
                String pricePerLiter = etPricePerLiter.getText().toString();

                // Calculate total fuel cost
                double totalFuel = Double.parseDouble(quantity) * Double.parseDouble(pricePerLiter);

                int idBoat = listBoats.get(spinnerBoat.getSelectedItemPosition()).getId_boat();

                if (validateInputs(jenisBBM, date, quantity, pricePerLiter)) {
                    createBBM(jenisBBM, date, quantity, pricePerLiter, String.valueOf(totalFuel), idBoat);
                }
            }
        });

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to MainActivity
            }
        });
    }

    private void initView() {
        etJenisBBM = findViewById(R.id.et_jenisbbm);
        etQuantity = findViewById(R.id.et_quantity);
        etPricePerLiter = findViewById(R.id.et_priceperliter);
        etDate = findViewById(R.id.et_date);
        spinnerBoat = findViewById(R.id.spinner_boat);
        btnSimpan = findViewById(R.id.btn_simpan);
    }

    private void loadUser() {
        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
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
        com.example.mandar_app.production.ApiRequestData API = com.example.mandar_app.production.RetroServer.konekRetrofit().create(com.example.mandar_app.production.ApiRequestData.class);
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

                        for (com.example.mandar_app.production.ModelBoat boat : listBoats) {
                            boatNames.add(boat.getBoat_name());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddBBMActivity.this, android.R.layout.simple_spinner_item, boatNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerBoat.setAdapter(adapter);
                    } else {
                        Toast.makeText(AddBBMActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddBBMActivity.this, "Gagal Mengambil Data Perahu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBoatResponse> call, Throwable t) {
                Log.e("AddProductionActivity", "Gagal Menghubungi Server: " + t.getMessage());
                Toast.makeText(AddBBMActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
//        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
//        Call<ModelBoatResponse> call = API.ardRetrieveBoatForFisherman(currentUser.getIdAccount());
//
//        call.enqueue(new Callback<ModelBoatResponse>() {
//            @Override
//            public void onResponse(Call<ModelBoatResponse> call, Response<ModelBoatResponse> response) {
//                if (response.body() != null && response.body().getKode() == 1) {
//                    List<ModelBoat> boats = response.body().getData();
//                    if (!boats.isEmpty()) {
//                        ModelBoat boat = boats.get(0);  // Get the first boat
//                        spinnerBoat.setAdapter(new ArrayAdapter<>(AddBBMActivity.this,
//                                android.R.layout.simple_spinner_item,
//                                Collections.singletonList(boat.getBoat_name())));
//                    } else {
//                        Toast.makeText(AddBBMActivity.this, "Tidak ada data perahu", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(AddBBMActivity.this, "Gagal Mengambil Data Perahu", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ModelBoatResponse> call, Throwable t) {
//                Log.e("AddBBMActivity", "Gagal Menghubungi Server: " + t.getMessage());
//                Toast.makeText(AddBBMActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean validateInputs(String jenisBBM, String date, String quantity, String pricePerLiter) {
        if (jenisBBM.trim().isEmpty()) {
            etJenisBBM.setError("Jenis BBM Harus Diisi!");
            return false;
        } else if (date.trim().isEmpty()) {
            etDate.setError("Tanggal Harus Diisi!");
            return false;
        } else if (quantity.trim().isEmpty()) {
            etQuantity.setError("Jumlah Liter Harus Diisi!");
            return false;
        } else if (pricePerLiter.trim().isEmpty()) {
            etPricePerLiter.setError("Harga per Liter Harus Diisi!");
            return false;
        }
        return true;
    }

    private void createBBM(String jenisBBM, String date, String quantity, String pricePerLiter, String totalFuel, int idBoat) {
        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses = API.ardCreateDataBBM(jenisBBM, date, quantity, pricePerLiter, totalFuel, idBoat);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                if (response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan(); // Ambil pesan dari response

                    if (kode == 1) {
                        Toast.makeText(AddBBMActivity.this, "Selamat! Sukses Menambah Data BBM", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (kode == 0) {
                        // Tampilkan pesan error jika data BBM sudah ada
                        Toast.makeText(AddBBMActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddBBMActivity.this, "Gagal Menambah Data BBM", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(AddBBMActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
