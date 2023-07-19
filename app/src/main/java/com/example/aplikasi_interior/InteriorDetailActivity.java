package com.example.aplikasi_interior;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InteriorDetailActivity extends AppCompatActivity {
    private ImageView interiorImage;
    private TextView interiorName;
    private TextView interiorPrice;
    private TextView interiorKet;
    private Button buyButton;
    private List<Interior> selectedInteriors;
    private Interior interior;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_detail);

        selectedInteriors = new ArrayList<>();

        interiorImage = findViewById(R.id.interior_image);
        interiorName = findViewById(R.id.interior_name);
        interiorPrice = findViewById(R.id.interior_price);
        interiorKet = findViewById(R.id.interior_ket);
        buyButton = findViewById(R.id.interior_buy_button);

        interior = (Interior) getIntent().getSerializableExtra("interior");

        if (interior != null) {
            interior.setImageBytes(interior.getImageBytes());

            interiorImage.setImageBitmap(interior.getImage());
            interiorName.setText(interior.getName());
            interiorPrice.setText(interior.getPrice());
            interiorKet.setText(interior.getKetInterior());
        }

        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Mengakhiri activity saat ini dan kembali ke activity sebelumnya
            }
        });
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedInteriors.add(interior);

                Intent intent = new Intent(InteriorDetailActivity.this, PemesananInterior.class);
                intent.putExtra("selectedInteriors", (Serializable) selectedInteriors);
                startActivity(intent);
            }
        });
    }
}
