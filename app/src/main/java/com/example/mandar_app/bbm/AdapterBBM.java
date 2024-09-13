package com.example.mandar_app.bbm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.R;
import com.example.mandar_app.loginregist.ModelUser;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBBM extends RecyclerView.Adapter<AdapterBBM.HolderBBM> {
    private Context ctx;
    private List<ModelBBM> listBBM;
    private ModelUser currentUser;

    public AdapterBBM(Context ctx, List<ModelBBM> listBBM) {
        this.ctx = ctx;
        this.listBBM = listBBM;

        // Load current user
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("user", "");
        Gson gson = new Gson();
        currentUser = gson.fromJson(jsonUser, ModelUser.class);
    }

    @NonNull
    @Override
    public HolderBBM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bbm, parent, false);
        return new HolderBBM(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBBM holder, int position) {
        ModelBBM MB = listBBM.get(position);

        holder.tvTanggal.setText(MB.getDate());
        holder.tvNamaPerahu.setText(MB.getBoat_name());
        holder.tvJenisBBM.setText(MB.getFuel_species());
        holder.tvTotalBBM.setText(MB.getQuantity_liters());
        holder.tvHargaBBM.setText(MB.getPrice_of_liter());
        holder.tvTotalHargaBBM.setText(MB.getTotal_fuel());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                pesan.setTitle("Perhatian!");
                pesan.setMessage("Operasi Yang Anda Inginkan?");
                pesan.setCancelable(true);

                pesan.setNegativeButton("Kirim ke WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Nama Perahu: " + MB.getBoat_name() + "\n" +
                                "Tanggal: " + MB.getDate() + "\n" +
                                "Jenis BBM: " + MB.getFuel_species() + "\n" +
                                "Total Penggunaan BBM: " + MB.getQuantity_liters() + " liter" + "\n" +
                                "Harga per Liter: " + MB.getPrice_of_liter() + "\n" +
                                "Total Harga BBM: " + MB.getTotal_fuel();

                        // Nomor tujuan
                        String phoneNumber = "0816866354";

                        // Link WhatsApp dengan nomor tujuan
                        String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
                        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                        whatsappIntent.setData(Uri.parse(url));

                        try {
                            ctx.startActivity(whatsappIntent);
                        } catch (Exception e) {
                            Toast.makeText(ctx, "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                pesan.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listBBM != null) {
            return listBBM.size();
        } else {
            Toast.makeText(ctx, "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void deleteBBM(int id) {
        ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> proses = API.ardDeleteDataBBM(id);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                if (kode == 1) {
                    Toast.makeText(ctx, "Selamat! Sukses Menghapus Data BBM", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "Perhatian! Gagal Menghapus Data BBM", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(ctx, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class HolderBBM extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvNamaPerahu, tvJenisBBM, tvTotalBBM, tvHargaBBM, tvTotalHargaBBM;

        public HolderBBM(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvNamaPerahu = itemView.findViewById(R.id.tv_namaperahu);
            tvJenisBBM = itemView.findViewById(R.id.tv_jenisbbm);
            tvTotalBBM = itemView.findViewById(R.id.tv_totalbbm);
            tvHargaBBM = itemView.findViewById(R.id.tv_hargabbm);
            tvTotalHargaBBM = itemView.findViewById(R.id.tv_totalhargabbm);
        }
    }
}
