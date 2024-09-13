package com.example.mandar_app.perahu;

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

public class AdapterPerahu extends RecyclerView.Adapter<AdapterPerahu.HolderPerahu> {
    private Context ctx;
    private List<ModelPerahu> listPerahu;

    public AdapterPerahu(Context ctx, List<ModelPerahu> listPerahu) {
        this.ctx = ctx;
        this.listPerahu = listPerahu;
    }

    @NonNull
    @Override
    public HolderPerahu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perahu, parent, false);
        return new HolderPerahu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPerahu holder, int position) {
        ModelPerahu MP = listPerahu.get(position);

        holder.tvBoatName.setText(MP.getBoat_name());
        holder.tvBoatLength.setText(MP.getBoat_length());
        holder.tvBoatWidth.setText(MP.getBoat_width());
        holder.tvFishingMethod.setText(MP.getFishing_method());

        Glide.with(ctx)
                .load(MP.getBoat_photo())
                .placeholder(R.drawable.ic_boat)
                .into(holder.ivBoatPhoto);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Perhatian!");
                builder.setMessage("Operasi yang Anda inginkan?");
                builder.setCancelable(true);

//                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deletePerahu(MP.getId_boat());
//                        dialog.dismiss();
//                        ((PerahuActivity) ctx).retrieveAllPerahu();
//                    }
//                });

                builder.setNegativeButton("Kirim ke WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Nama Perahu: " + MP.getBoat_name() + "\n" +
                                "Panjang: " + MP.getBoat_length() + " meter" + "\n" +
                                "Lebar: " + MP.getBoat_width() + " meter" ;
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        ctx.startActivity(sendIntent);
                    }
                });

                builder.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPerahu.size();
    }

    public class HolderPerahu extends RecyclerView.ViewHolder {
        TextView tvBoatName, tvBoatLength, tvBoatWidth, tvFishingMethod;
        ImageView ivBoatPhoto;

        public HolderPerahu(@NonNull View itemView) {
            super(itemView);

            tvBoatName = itemView.findViewById(R.id.tv_namaperahu);
            tvBoatLength = itemView.findViewById(R.id.tv_panjangperahu);
            tvBoatWidth = itemView.findViewById(R.id.tv_lebarperahu);
            tvFishingMethod = itemView.findViewById(R.id.tv_fishingmethod);
            ivBoatPhoto = itemView.findViewById(R.id.gambar_perahu);
        }
    }
}
