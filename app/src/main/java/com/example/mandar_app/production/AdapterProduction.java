package com.example.mandar_app.production;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandar_app.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProduction extends RecyclerView.Adapter<AdapterProduction.HolderProduction> {
    private Context ctx;
    private List<ModelProduction> listProduction;

    // Nomor WhatsApp yang dituju (dalam format internasional tanpa '+')
    private static final String WHATSAPP_NUMBER = "62816866354"; // Ganti dengan nomor yang diinginkan

    public AdapterProduction(Context ctx, List<ModelProduction> listProduction) {
        this.ctx = ctx;
        this.listProduction = listProduction != null ? listProduction : Collections.emptyList();
    }

    @NonNull
    @Override
    public HolderProduction onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tangkapan, parent, false);
        return new HolderProduction(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProduction holder, int position) {
        ModelProduction MP = listProduction.get(position);

        holder.tvTanggal.setText(MP.getDate());
        holder.tvNamaPerahu.setText(MP.getBoat_name());
        holder.tvFishSpecies.setText(getFishDetails(MP));
        holder.tvTotal.setText("Jumlah Total Tangkapan : " + MP.getTotal_weight() + " kg");
    }

    @Override
    public int getItemCount() {
        return listProduction.size();
    }

    private String getFishDetails(ModelProduction MP) {
        StringBuilder fishDetailsBuilder = new StringBuilder();
        for (ModelProductionDetail detail : MP.getFish_details()) {
            fishDetailsBuilder.append(detail.getFish_species())
                    .append(" : ")
                    .append(detail.getWeight())
                    .append(" kg\n");
        }
        return fishDetailsBuilder.toString();
    }

    public class HolderProduction extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvNamaPerahu, tvFishSpecies, tvTotal;

        public HolderProduction(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvNamaPerahu = itemView.findViewById(R.id.tv_namaperahu);
            tvFishSpecies = itemView.findViewById(R.id.tv_fishspecies);
            tvTotal = itemView.findViewById(R.id.tv_total);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Perhatian!");
                    pesan.setMessage("Operasi Yang Anda Inginkan?");
                    pesan.setCancelable(true);

                    // Opsi Hapus (dapat diaktifkan kembali jika diperlukan)
                    // pesan.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                    //     @Override
                    //     public void onClick(DialogInterface dialog, int which) {
                    //         ModelProduction MP = listProduction.get(getAdapterPosition());
                    //         deleteProduction(MP.getId_production(), getAdapterPosition());
                    //         dialog.dismiss();
                    //     }
                    // });

                    // Opsi Kirim ke WhatsApp
                    pesan.setNeutralButton("Kirim ke Whatsapp", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ModelProduction MP = listProduction.get(getAdapterPosition());
                            StringBuilder message = new StringBuilder();
                            message.append("Tanggal: ").append(MP.getDate()).append("\n")
                                    .append("Total Tangkapan: ").append(MP.getTotal_weight()).append(" kg").append("\n")
                                    .append("Perahu: ").append(MP.getBoat_name()).append("\n");

                            for (ModelProductionDetail detail : MP.getFish_details()) {
                                message.append(detail.getFish_species())
                                        .append(": ")
                                        .append(detail.getWeight())
                                        .append(" kg\n");
                            }

                            try {
                                // Encode pesan untuk URL
                                String urlMessage = URLEncoder.encode(message.toString(), "UTF-8");
                                String url = "https://wa.me/" + WHATSAPP_NUMBER + "?text=" + urlMessage;

                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse(url));
                                sendIntent.setPackage("com.whatsapp");

                                ctx.startActivity(sendIntent);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Toast.makeText(ctx, "Terjadi kesalahan saat mengirim pesan.", Toast.LENGTH_SHORT).show();
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(ctx, "WhatsApp tidak terpasang.", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });

                    pesan.show();
                    return false;
                }
            });
        }

        private void deleteProduction(int id, int position) {
            ApiRequestData API = RetroServer.konekRetrofit().create(ApiRequestData.class);
            Call<ModelResponse> proses = API.ardDeleteProduction(id);

            proses.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    if (response.body() != null) {
                        int kode = response.body().getKode();
                        String pesan = response.body().getPesan();

                        if (kode == 1) {
                            listProduction.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(ctx, "Berhasil menghapus data.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ctx, "Gagal menghapus data: " + pesan, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ctx, "Gagal menghapus data: Respon kosong.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
