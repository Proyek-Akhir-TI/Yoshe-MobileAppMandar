package com.example.mandar_app.loginregist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandar_app.MainActivity;
import com.example.mandar_app.R;
import com.example.mandar_app.rapat.RapatActivity;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvUsername, tvEmail, tvRole, tvGroupName, tvBoatName, tvNib, tvKusuka, tvNpwp;
    private ModelUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        tvGroupName = findViewById(R.id.tv_groupname);
        tvBoatName = findViewById(R.id.tv_boatname);
        tvKusuka = findViewById(R.id.tv_kusuka);
        tvNib = findViewById(R.id.tv_nib);
        tvNpwp = findViewById(R.id.tv_npwp);

        user = (ModelUser) getIntent().getSerializableExtra("user");
        if (user != null) {
            tvName.setText(user.getName());
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            tvRole.setText(user.getRole());
            tvGroupName.setText(user.getGroupName() != null ? user.getGroupName() : "Belum ada nama kelompok");
            tvBoatName.setText(user.getBoatName() != null ? user.getBoatName() : "Belum ada nama perahu");
            tvNib.setText(user.getNib() != null ? user.getNib() : "Belum ada NIB");
            tvKusuka.setText(user.getKusuka() != null ? user.getKusuka() : "Belum ada KUSUKA");
            tvNpwp.setText(user.getNpwp() != null ? user.getNpwp() : "Belum ada NPWP");
        }

        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }
}
