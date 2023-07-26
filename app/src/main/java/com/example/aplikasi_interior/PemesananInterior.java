package com.example.aplikasi_interior;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PemesananInterior extends AppCompatActivity {
    private ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan_interior);

        final List<Interior> selectedInteriors = new ArrayList<>();

        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Mengakhiri activity saat ini dan kembali ke activity sebelumnya
            }
        });

        // Retrieve the selected interiors from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedInteriors.addAll((List<Interior>) extras.getSerializable("selectedInteriors"));
        }

        RecyclerView selectedInteriorsRecyclerView = findViewById(R.id.selected_interior_recycler_view);
        SelectedInteriorAdapter selectedInteriorAdapter = new SelectedInteriorAdapter(selectedInteriors);
        selectedInteriorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedInteriorsRecyclerView.setAdapter(selectedInteriorAdapter);

        TextView totalOrderTextView = findViewById(R.id.total_order);
        EditText addressInput = findViewById(R.id.address_input);
        EditText catatanInput = findViewById(R.id.catatan_input); // EditText for catatan
        Button checkoutButton = findViewById(R.id.checkout_button);

        updateTotalOrder(selectedInteriors, totalOrderTextView);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                String catatan = catatanInput.getText().toString(); // Get the value of catatan

                if (TextUtils.isEmpty(address)) {
                    addressInput.setError("Address is required");
                    return;
                }

                // Create intent to start KonfirmasiPemesananActivity
                Intent intent = new Intent(PemesananInterior.this, KonfirmasiPesananInterior.class);
                intent.putExtra("selectedInteriors", new ArrayList<>(selectedInteriors)); // Convert to ArrayList
                intent.putExtra("address", address);
                intent.putExtra("catatan", catatan); // Add the value of catatan to intent

                // Save the total order value in the Intent
                double totalOrder = calculateTotalOrder(selectedInteriors);
                String formattedTotal = formatPrice(totalOrder);
                intent.putExtra("totalOrder", formattedTotal);

                startActivity(intent);
            }
        });
    }

    private void updateTotalOrder(List<Interior> selectedInteriors, TextView totalOrderTextView) {
        double total = calculateTotalOrder(selectedInteriors);
        String formattedTotal = formatPrice(total);
        String totalOrderText = "Total Order: " + formattedTotal;
        totalOrderTextView.setText(totalOrderText);
    }

    private double calculateTotalOrder(List<Interior> selectedInteriors) {
        double total = 0;
        for (Interior interior : selectedInteriors) {
            double price = Double.parseDouble(interior.getPrice().replace("Rp", "").replace(",", ""));
            total += price;
        }
        return total;
    }

    private String formatPrice(double price) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return "Rp" + formatter.format(price);

    }
}
