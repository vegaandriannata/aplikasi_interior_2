package com.example.aplikasi_interior.fragment.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aplikasi_interior.CartProduct;
import com.example.aplikasi_interior.CartProductAdapter;
import com.example.aplikasi_interior.KeranjangPemesananActivity;
import com.example.aplikasi_interior.DbContract;
import com.example.aplikasi_interior.R;
import com.example.aplikasi_interior.SessionManager;
import com.example.aplikasi_interior.VolleyConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment implements CartProductAdapter.DeleteCartItemListener {

    private List<CartProduct> cartProductList;
    private CartProductAdapter cartProductAdapter;
    private ListView listViewCartProducts;
    private SessionManager sessionManager;
    private int currentUserId;
    private TextView textViewTotalHarga;
    private EditText editTextAddress;
    private Button buyButton;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        listViewCartProducts = view.findViewById(R.id.listViewCartProducts);
        textViewTotalHarga = view.findViewById(R.id.textViewTotalHarga);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        buyButton = view.findViewById(R.id.buy_button);

        cartProductList = new ArrayList<>();
        cartProductAdapter = new CartProductAdapter(getActivity(), cartProductList, this);
        listViewCartProducts.setAdapter(cartProductAdapter);

        sessionManager = new SessionManager(getActivity());

        String userId = sessionManager.getUserId();
        if (!userId.isEmpty()) {
            currentUserId = Integer.parseInt(userId);
            loadCartProducts();
            calculateTotalPrice();
        } else {
            Toast.makeText(getActivity(), "Keranjang sedang loading", Toast.LENGTH_SHORT).show();
        }

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartProductList.isEmpty()) {
                    Toast.makeText(getActivity(), "Keranjang belanja kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String totalHarga = textViewTotalHarga.getText().toString();
                    String alamat = editTextAddress.getText().toString();

                    Intent intent = new Intent(getActivity(), KeranjangPemesananActivity.class);
                    intent.putExtra("cartProductList", (Serializable) cartProductList);
                    intent.putExtra("totalHarga", totalHarga);
                    intent.putExtra("alamat", alamat);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

    private void loadCartProducts() {
        String url = DbContract.SERVER_GET_CART + "?user_id=" + currentUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");

                            cartProductList.clear();

                            for (int i = 0; i < serverResponse.length(); i++) {
                                JSONObject cartProductObj = serverResponse.getJSONObject(i);

                                int id_keranjang = cartProductObj.getInt("id_keranjang");
                                int id_brg = cartProductObj.getInt("id_brg");
                                int user_id = cartProductObj.getInt("user_id");
                                int qty = cartProductObj.getInt("qty");
                                String nama_brg = cartProductObj.getString("nama_brg");
                                String harga_brg = cartProductObj.getString("harga_brg");

                                if (user_id == currentUserId) {
                                    CartProduct cartProduct = new CartProduct(id_keranjang, id_brg, user_id, qty, nama_brg, harga_brg);
                                    cartProductList.add(cartProduct);
                                }
                            }

                            cartProductAdapter.notifyDataSetChanged();
                            calculateTotalPrice();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to load cart products.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to connect to server.", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyConnection.getInstance(getActivity()).addToRequestQueue(request);
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

    @Override
    public void onDeleteCartItem(int itemId) {
        deleteCartItem(itemId);
    }

    private void deleteCartItem(int itemId) {
        String deleteUrl = DbContract.SERVER_POST_CART + "?delete_id=" + itemId;

        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.GET, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");
                            JSONObject statusObj = serverResponse.getJSONObject(0);
                            String status = statusObj.getString("status");

                            if (status.equals("OK")) {
                                // Item deleted successfully
                                Toast.makeText(getActivity(), "Item berhasil di hapus dari keranjang", Toast.LENGTH_SHORT).show();

                                // Refresh the cart items
                                loadCartProducts();
                            } else {
                                // Failed to delete item
                                Toast.makeText(getActivity(), "Gagal menghapus item dari keranjang", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                                Toast.makeText(getActivity(), "Gagal menghapus item dari keranjang", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Terjadi kesalahan jaringanr", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyConnection.getInstance(getActivity()).addToRequestQueue(deleteRequest);
    }
}
