package com.example.mandar_app.nelayan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.example.mandar_app.rapat.RapatActivity;

public class DetailNelayanActivity extends AppCompatActivity {
    private TextView tvFishermanName, tvNib, tvGroupName, tvBoatName, tvNpwp, tvKusuka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nelayan);

        tvFishermanName = findViewById(R.id.tv_fisherman_name);
        tvNib = findViewById(R.id.tv_nib);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvBoatName = findViewById(R.id.tv_boat_name);
        tvNpwp = findViewById(R.id.tv_npwp);
        tvKusuka = findViewById(R.id.tv_kusuka);

        ModelNelayan nelayan = getIntent().getParcelableExtra("nelayan");

        if (nelayan != null) {
            tvFishermanName.setText(nelayan.getFisherman_name());
            tvNib.setText(nelayan.getNib());
            tvGroupName.setText(nelayan.getGroup_name());
            tvBoatName.setText(nelayan.getBoat_name());
            tvNpwp.setText(nelayan.getNpwp());
            tvKusuka.setText(nelayan.getKusuka());
        }

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailNelayanActivity.this, NelayanActivity.class));
            }
        });
    }
}
