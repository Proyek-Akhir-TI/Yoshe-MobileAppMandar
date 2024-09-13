package com.example.mandar_app.nelayan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mandar_app.R;
import java.util.List;

public class AdapterNelayan extends RecyclerView.Adapter<AdapterNelayan.HolderNelayan> {
    private Context ctx;
    private List<ModelNelayan> listNelayan;
    private OnNelayanClickListener onNelayanClickListener;

    public interface OnNelayanClickListener {
        void onNelayanClick(ModelNelayan nelayan);
    }

    public AdapterNelayan(Context ctx, List<ModelNelayan> listNelayan, OnNelayanClickListener listener) {
        this.ctx = ctx;
        this.listNelayan = listNelayan;
        this.onNelayanClickListener = listener;
    }

    @NonNull
    @Override
    public HolderNelayan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nelayan, parent, false);
        return new HolderNelayan(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNelayan holder, int position) {
        ModelNelayan MN = listNelayan.get(position);

        holder.tvFishermanName.setText(MN.getFisherman_name());
        holder.tvGroupName.setText(MN.getGroup_name());

        holder.itemView.setOnClickListener(v -> onNelayanClickListener.onNelayanClick(MN));

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
                        String message = "Nama Nelayan: " + MN.getFisherman_name() + "\n" +
                                "Nama Kelompok Nelayan: " + MN.getGroup_name()  + "\n" +
                                "Nama Perahu Nelayan: " + MN.getBoat_name()  + "\n" +
                                "Nomor Induk Berusaha: " + MN.getNib()  + "\n" +
                                "NPWP: " + MN.getNpwp()  + "\n" +
                                "KUSUKA " + MN.getKusuka()  + "\n" ;
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
        return listNelayan != null ? listNelayan.size() : 0;
    }

    public class HolderNelayan extends RecyclerView.ViewHolder {
        TextView tvFishermanName, tvGroupName;

        public HolderNelayan(@NonNull View itemView) {
            super(itemView);
            tvFishermanName = itemView.findViewById(R.id.tv_nelayan);
            tvGroupName = itemView.findViewById(R.id.tv_namakelompok);
        }
    }
}
