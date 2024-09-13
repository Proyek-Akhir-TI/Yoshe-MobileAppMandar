package com.example.mandar_app.kelompok;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mandar_app.R;

import java.util.List;

public class AdapterKelompok extends RecyclerView.Adapter<AdapterKelompok.HolderKelompok> {
    private Context ctx;
    private List<ModelKelompok> listKelompok;

    public AdapterKelompok(
            Context ctx,
            List<ModelKelompok> listKelompok
    ) {
        this.ctx = ctx;
        this.listKelompok = listKelompok;
    }

    @NonNull
    @Override
    public HolderKelompok onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(R.layout.item_kelompok,
                parent,
                false);
        HolderKelompok HK = new HolderKelompok(view);

        return HK;
    }

    @Override
    public void onBindViewHolder(
            @NonNull HolderKelompok holder, int position) {
        ModelKelompok MK = listKelompok.get(position);

        holder.tvIDFishermanGroup.setText(String.valueOf(MK.getId_fisherman_group()));
        holder.tvChairName.setText(MK.getChair_name());
        holder.tvGroupName.setText(MK.getGroup_name());
        holder.tvJumlahAnggota.setText(String.valueOf(MK.getJumlah_anggota()));

        Glide
                .with(ctx)
                .load(MK.getOffice_photo())
                .placeholder(R.drawable.ic_kelompok)
                .into(holder.ivOfficePhoto);
    }

    @Override
    public int getItemCount() {
        if (listKelompok != null) {
            return listKelompok.size();
        } else {
            Toast.makeText(ctx, "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderKelompok extends
            RecyclerView.ViewHolder {
        TextView tvIDFishermanGroup,
                tvChairName,
                tvGroupName,
                tvJumlahAnggota;

        ImageView ivOfficePhoto;

        public HolderKelompok(@NonNull View itemView) {
            super(itemView);

            tvIDFishermanGroup = itemView.findViewById(R.id.tv_idkelompok);
            tvChairName = itemView.findViewById(R.id.tv_ketuakelompok);
            tvGroupName = itemView.findViewById(R.id.tv_namakelompok);
            tvJumlahAnggota = itemView.findViewById(R.id.tv_jumlahanggota);

            ivOfficePhoto = itemView.findViewById(R.id.iv_gambarkelompok);

            itemView.setOnLongClickListener(
                    view -> {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Nama Kelompok: " + tvGroupName.getText().toString() + "\nNama Ketua: " + tvChairName.getText().toString());
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");

                        try {
                            ctx.startActivity(sendIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ctx, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
            );
        }
    }
}
