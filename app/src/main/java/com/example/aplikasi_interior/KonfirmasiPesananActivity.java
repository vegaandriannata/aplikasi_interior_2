package com.example.aplikasi_interior;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KonfirmasiPesananActivity extends AppCompatActivity {
    private TextView idFurniturTextView;
    private TextView userIdTextView;
    private TextView orderDateTextView;
    private TextView totalOrderTextView;
    private TextView alamatTextView;
    private TextView qtyTextView;
    private TextView hargaTextView;
    private ImageView backIcon;
    private Button confirmButton;
    private ProgressDialog progressDialog;
    private ArrayList<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pesanan);
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Mengakhiri activity saat ini dan kembali ke activity sebelumnya
            }
        });
        idFurniturTextView = findViewById(R.id.id_furnitur);
        userIdTextView = findViewById(R.id.user_id);
        orderDateTextView = findViewById(R.id.order_date);
        totalOrderTextView = findViewById(R.id.total_order);
        alamatTextView = findViewById(R.id.alamat);
        qtyTextView = findViewById(R.id.qty);
        hargaTextView = findViewById(R.id.harga);

        //menghilangkan tampilan id_brg dan user_id
        idFurniturTextView.setVisibility(View.GONE);
        userIdTextView.setVisibility(View.GONE);

        confirmButton = findViewById(R.id.confirm_button);
        progressDialog = new ProgressDialog(this);

        // Retrieve the selected products and address from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedProducts = (ArrayList<Product>) extras.getSerializable("selectedProducts");
            String address = extras.getString("address");
            String totalOrder = extras.getString("totalOrder");

            // Set the values to the respective TextViews
            if (selectedProducts != null && !selectedProducts.isEmpty()) {
                Product product = selectedProducts.get(0);
                idFurniturTextView.setText("ID Furnitur: " + product.getId_furnitur());

                // Get the User ID from the SessionManager
                SessionManager sessionManager = new SessionManager(this);
                String userId = sessionManager.getUserId();
                userIdTextView.setText("User ID: " + userId);

                // Get the current order date automatically
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String orderDate = dateFormat.format(calendar.getTime());
                orderDateTextView.setText("Order Date: " + orderDate);

                totalOrderTextView.setText("Total Order: " + totalOrder);
                alamatTextView.setText("Alamat: " + address);
                qtyTextView.setText("Quantity: " + product.getQuantity());
                hargaTextView.setText("Harga: " + product.getPrice());
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderToServer();
            }
        });
    }

    private void saveOrderToServer() {
        progressDialog.setMessage("Saving order...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_POST_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getJSONArray("server_response")
                                    .getJSONObject(0)
                                    .getString("status");

                            if (status.equals("OK")) {
                                Toast.makeText(getApplicationContext(), "Order saved successfully", Toast.LENGTH_SHORT).show();
                                // Reduce stock in the selected product
                                Product product = selectedProducts.get(0);
                                int newStock = product.getStok() - product.getQuantity();
                                product.setStok(newStock);

                                // Update stock in the database
                                updateStockOnServer(product);

                                // Redirect to success page or perform any desired action
                                Intent intent = new Intent(KonfirmasiPesananActivity.this, PesananBerhasilActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to save order", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to save order", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to save order", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Prepare the parameters to be sent in the request
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userIdTextView.getText().toString().split(":")[1].trim());
                params.put("order_date", orderDateTextView.getText().toString().split(":")[1].trim());
                params.put("total_order", totalOrderTextView.getText().toString().split(":")[1].trim().replace("Rp", "").replace(",", ""));
                params.put("alamat", alamatTextView.getText().toString().split(":")[1].trim());
                params.put("id_furnitur", idFurniturTextView.getText().toString().split(":")[1].trim());
                params.put("qty", qtyTextView.getText().toString().split(":")[1].trim());
                params.put("harga", hargaTextView.getText().toString().split(":")[1].trim().replace("Rp", "").replace(",", ""));

                return params;
            }
        };

        // Add the request to the RequestQueue
        VolleyConnection.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void updateStockOnServer(Product product) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_POST_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response from the server if needed
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_furnitur", String.valueOf(product.getId_furnitur()));
                params.put("stok", String.valueOf(product.getStok()));
                return params;
            }
        };

        VolleyConnection.getInstance(this).addToRequestQueue(stringRequest);
    }
}
