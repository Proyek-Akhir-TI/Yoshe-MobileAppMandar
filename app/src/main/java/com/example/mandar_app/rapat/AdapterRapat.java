package com.example.mandar_app.rapat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRapat extends RecyclerView.Adapter<AdapterRapat.HolderRapat> {
    private Context ctx;
    private List<ModelRapat> listRapat;

    public AdapterRapat(Context ctx, List<ModelRapat> listRapat) {
        this.ctx = ctx;
        this.listRapat = listRapat;
    }

    @NonNull
    @Override
    public HolderRapat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rapat, parent, false);
        return new HolderRapat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRapat holder, int position) {
        ModelRapat MR = listRapat.get(position);

        holder.tvDate.setText(MR.getDate());
        holder.tvGroupName.setText(MR.getGroup_name());
        holder.tvNotes.setText(MR.getNotes());

        if (MR.getDocumentation_photo() != null && !MR.getDocumentation_photo().isEmpty()) {
            Glide.with(ctx)
                    .load(MR.getDocumentation_photo())
                    .placeholder(R.drawable.ic_boat)
                    .into(holder.ivDocumentationPhoto);
        } else {
            holder.ivDocumentationPhoto.setImageResource(R.drawable.ic_boat);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                pesan.setTitle("Perhatian!");
                pesan.setMessage("Operasi Yang Anda Inginkan?");
                pesan.setCancelable(true);

                pesan.setPositiveButton("Kirim ke WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Nama Kelompok: " + MR.getGroup_name() + "\n" +
                                "Tanggal: " + MR.getDate() + "\n" +
                                "Catatan: " + MR.getNotes();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        ctx.startActivity(sendIntent);
                    }
                });

                pesan.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listRapat != null) {
            return listRapat.size();
        } else {
            Toast.makeText(ctx, "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderRapat extends RecyclerView.ViewHolder {
        TextView tvDate, tvGroupName, tvNotes;
        ImageView ivDocumentationPhoto;

        public HolderRapat(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_tanggal);
            tvGroupName = itemView.findViewById(R.id.tv_namakelompok);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            ivDocumentationPhoto = itemView.findViewById(R.id.iv_gambar_rapat);
        }

    }
}
