package com.example.aplikasi_interior;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import com.example.aplikasi_interior.PesananInterior;
import com.example.aplikasi_interior.R;

import java.util.List;

public class DetailPemesananInteriorActivity extends AppCompatActivity {
    private TextView tvOrderIntId;
    private TextView tvUserId;
    private TextView tvOrderDate;
    private TextView tvTotalOrder;
    private TextView tvAlamat;
    private TextView tvStatus;
    private ImageView backIcon;
    private TextView tvIdInt;
    private TextView tvCatatan;
    private TextView tvHarga;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan_interior);
        decimalFormat = new DecimalFormat("#,###");
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Mengakhiri activity saat ini dan kembali ke activity sebelumnya
            }
        });
        tvOrderIntId = findViewById(R.id.tv_order_int_id);
        tvUserId = findViewById(R.id.tv_user_id);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvTotalOrder = findViewById(R.id.tv_total_order);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvStatus = findViewById(R.id.tv_status);
        tvIdInt = findViewById(R.id.tv_id_int);
        tvCatatan = findViewById(R.id.tv_catatan);
        tvHarga = findViewById(R.id.tv_harga);

        // Get the selected PesananInterior object from Intent
        PesananInterior pesananInterior = (PesananInterior) getIntent().getSerializableExtra("pesananInterior");

        if (pesananInterior != null) {
            // Set the text views with the details of the selected pesananInterior
            tvOrderIntId.setText("ID Order Interior: " + String.valueOf(pesananInterior.getOrderIntId()));
            tvUserId.setText("User ID: " + String.valueOf(pesananInterior.getUserId()));
            tvOrderDate.setText("Tanggal Order: " + pesananInterior.getOrderDate());
            tvTotalOrder.setText("Total Order: " + decimalFormat.format(pesananInterior.getTotalOrder()));
            tvAlamat.setText("Alamat: " + pesananInterior.getAlamat());
            tvStatus.setText("Status: " + pesananInterior.getStatus());

            List<ItemPesananInterior> items = pesananInterior.getItems();
            if (items != null && !items.isEmpty()) {
                // Display the first item's details
                ItemPesananInterior firstItem = items.get(0);
                tvIdInt.setText("ID Interior: " + String.valueOf(firstItem.getIdInterior()));
                tvCatatan.setText("Catatan: " + String.valueOf(firstItem.getCatatan()));
                tvHarga.setText("Harga: Rp" + decimalFormat.format(firstItem.getHarga()));
            }
        }
    }
}
