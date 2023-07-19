package com.example.aplikasi_interior;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

import com.example.aplikasi_interior.Pesanan;
import com.example.aplikasi_interior.R;

public class DetailPemesananActivity extends AppCompatActivity {
    private TextView tvOrderId;
    private TextView tvUserId;
    private TextView tvOrderDate;
    private TextView tvTotalOrder;
    private TextView tvAlamat;
    private TextView tvIdFurnitur;
    private TextView tvQty;
    private TextView tvHarga;

    private ImageView backIcon;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);
        decimalFormat = new DecimalFormat("#,###");
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Mengakhiri activity saat ini dan kembali ke activity sebelumnya
            }
        });
        tvOrderId = findViewById(R.id.tv_order_id);
        tvUserId = findViewById(R.id.tv_user_id);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvTotalOrder = findViewById(R.id.tv_total_order);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvIdFurnitur = findViewById(R.id.tv_id_furnitur);
        tvQty = findViewById(R.id.tv_qty);
        tvHarga = findViewById(R.id.tv_harga);

        // Get the selected Pesanan object from Intent
        Pesanan pesanan = (Pesanan) getIntent().getSerializableExtra("pesanan");

        if (pesanan != null) {
            // Set the text views with the details of the selected pesanan
            tvOrderId.setText("ID Order: " + String.valueOf(pesanan.getOrder_id()));
            tvUserId.setText("User ID: " + String.valueOf(pesanan.getUser_id()));
            tvOrderDate.setText("Tanggal Order: " + pesanan.getOrder_date());
            tvTotalOrder.setText("Total Order: Rp" + decimalFormat.format(pesanan.getTotal_order()));
            tvAlamat.setText("Alamat: " + pesanan.getAlamat());

            // Retrieve the list of items from the selected pesanan
            List<Item> items = pesanan.getItems();
            if (items != null && !items.isEmpty()) {
                // Display the first item's details
                Item firstItem = items.get(0);
                tvIdFurnitur.setText("ID Furnitur: " + String.valueOf(firstItem.getidFurnitur()));
                tvQty.setText("Quantity: " + String.valueOf(firstItem.getQty()));
                tvHarga.setText("Harga: Rp" + decimalFormat.format(firstItem.getHarga()));
            }
        }
    }
}
