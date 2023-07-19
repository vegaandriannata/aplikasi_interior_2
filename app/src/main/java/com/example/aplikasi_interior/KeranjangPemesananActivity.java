package com.example.aplikasi_interior;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KeranjangPemesananActivity extends AppCompatActivity {

    private List<CartProduct> cartProductList;
    private CartProductAdapter cartProductAdapter;
    private ListView listViewCartProducts;
    private TextView textViewTotalHarga;
    private EditText editTextAddress;
    private Button buyButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang_pemesanan);

        listViewCartProducts = findViewById(R.id.listViewCartProducts);
        textViewTotalHarga = findViewById(R.id.textViewTotalHarga);
        editTextAddress = findViewById(R.id.editTextAddress);
        buyButton = findViewById(R.id.buy_button);
        progressDialog = new ProgressDialog(this);

        cartProductList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("cartProductList")) {
            cartProductList = (List<CartProduct>) intent.getSerializableExtra("cartProductList");
            String alamat = intent.getStringExtra("alamat");
            editTextAddress.setText(alamat);
        }

        cartProductAdapter = new CartProductAdapter(this, cartProductList, null);
        listViewCartProducts.setAdapter(cartProductAdapter);

        calculateTotalPrice();

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartProductList.isEmpty()) {
                    Toast.makeText(KeranjangPemesananActivity.this, "Keranjang belanja kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String totalHarga = textViewTotalHarga.getText().toString();
                    String alamat = editTextAddress.getText().toString();

                    // Save the order to the server
                    saveOrderToServer(totalHarga, alamat);
                }
            }
        });
    }

    private void calculateTotalPrice() {
        double totalPrice = 0;

        for (CartProduct cartProduct : cartProductList) {
            try {
                double price = Double.parseDouble(cartProduct.getHarga_brg().replaceAll("[^\\d.]", ""));
                totalPrice += price * cartProduct.getQty();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        textViewTotalHarga.setText("Total Harga: \nRp " + formatter.format(totalPrice));
    }

    private void saveOrderToServer(String totalHarga, String alamat) {
        progressDialog.setMessage("Saving order...");
        progressDialog.show();

        // Get the current user ID
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        // Get the current order date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String orderDate = dateFormat.format(Calendar.getInstance().getTime());

        // Prepare the request parameters
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("order_date", orderDate);
        params.put("total_order", totalHarga.replace("Rp", "").replace(",", ""));
        params.put("alamat", alamat);

        JSONArray jsonArray = new JSONArray();
        for (CartProduct cartProduct : cartProductList) {
            try {
                JSONObject item = new JSONObject();
                item.put("id_brg", cartProduct.getId_brg());
                item.put("harga", cartProduct.getHarga_brg().replace("Rp", "").replace(",", ""));
                item.put("qty", cartProduct.getQty());
                jsonArray.put(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        params.put("items", jsonArray.toString());

        // Send the request to the server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DbContract.SERVER_POST_ORDER, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String status = response.getString("status");

                            if (status.equals("OK")) {
                                Toast.makeText(KeranjangPemesananActivity.this, "Order saved successfully", Toast.LENGTH_SHORT).show();

                                // Perform any desired action after successful order saving
                                // For example, navigate to the confirmation page
                                navigateToConfirmationPage();
                            } else {
                                Toast.makeText(KeranjangPemesananActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KeranjangPemesananActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(KeranjangPemesananActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        VolleyConnection.getInstance(this).addToRequestQueue(request);
    }

    private void navigateToConfirmationPage() {
        Intent intent = new Intent(KeranjangPemesananActivity.this, KonfirmasiPesananActivity.class);
        intent.putExtra("selectedProducts", (Serializable) cartProductList);
        intent.putExtra("address", editTextAddress.getText().toString());
        startActivity(intent);
        finish();
    }
}
