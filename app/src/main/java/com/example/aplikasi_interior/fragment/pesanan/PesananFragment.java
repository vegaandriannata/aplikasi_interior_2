package com.example.aplikasi_interior.fragment.pesanan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;

import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aplikasi_interior.DbContract;
import com.example.aplikasi_interior.R;
import com.example.aplikasi_interior.Item;
import com.example.aplikasi_interior.ItemPesananInterior;
import com.example.aplikasi_interior.SessionManager;
import com.example.aplikasi_interior.VolleyConnection;
import com.example.aplikasi_interior.Pesanan;
import com.example.aplikasi_interior.PesananAdapter;
import com.example.aplikasi_interior.PesananInteriorAdapter;
import com.example.aplikasi_interior.PesananInterior;
import com.example.aplikasi_interior.DetailPemesananActivity;
import com.example.aplikasi_interior.DetailPemesananInteriorActivity;

import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PesananFragment extends Fragment {
    private List<Pesanan> pesananList;
    private PesananAdapter pesananAdapter;
    private ListView listViewPesanan;

    private List<PesananInterior> pesananInteriorList;
    private PesananInteriorAdapter pesananInteriorAdapter;
    private ListView listViewPesananInterior;

    private SessionManager sessionManager;
    private int currentUserId;
    private Spinner spinnerBulan;
    private Spinner spinnerTahun;
    private Button btnFilter;
    private boolean isInteriorMenuClicked = false;
    private LinearLayout pesananLayout;
    private LinearLayout pesananInteriorLayout;

    public PesananFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesanan, container, false);

        listViewPesanan = view.findViewById(R.id.listViewPesanan);
        listViewPesananInterior = view.findViewById(R.id.listViewPesananInterior);
        pesananInteriorList = new ArrayList<>();
        pesananInteriorAdapter = new PesananInteriorAdapter(getActivity(), pesananInteriorList);
        listViewPesananInterior.setAdapter(pesananInteriorAdapter);
        spinnerBulan = view.findViewById(R.id.spinnerBulan);
        spinnerTahun = view.findViewById(R.id.spinnerTahun);
        btnFilter = view.findViewById(R.id.btnFilter);
        pesananList = new ArrayList<>();
        pesananAdapter = new PesananAdapter(getActivity(), pesananList);
        listViewPesanan.setAdapter(pesananAdapter);

        TextView furnitureMenu = view.findViewById(R.id.menu_furniture);
        TextView interiorMenu = view.findViewById(R.id.menu_interior);

        pesananLayout = view.findViewById(R.id.pesananLayout);
        pesananInteriorLayout = view.findViewById(R.id.pesananInteriorLayout);

        sessionManager = new SessionManager(getActivity());

        String userId = sessionManager.getUserId();
        if (!userId.isEmpty()) {
            currentUserId = Integer.parseInt(userId);
            loadPesanan();
        } else {
            Toast.makeText(getActivity(), "Pesanan sedang loading", Toast.LENGTH_SHORT).show();
        }
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil bulan dan tahun yang dipilih
                int selectedBulan = spinnerBulan.getSelectedItemPosition() + 1; // Karena array dimulai dari 0
                String selectedTahun = spinnerTahun.getSelectedItem().toString();

                // Panggil metode untuk memuat pesanan berdasarkan filter
                loadPesananByFilter(selectedBulan, selectedTahun);
                loadPesananInteriorByFilter(selectedBulan, selectedTahun);
            }
        });
        furnitureMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInteriorMenuClicked = false;
                pesananLayout.setVisibility(View.VISIBLE);
                pesananInteriorLayout.setVisibility(View.GONE);
                listViewPesanan.setAdapter(pesananAdapter);
                loadPesanan();
            }
        });

        interiorMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInteriorMenuClicked = true;
                pesananLayout.setVisibility(View.GONE);
                pesananInteriorLayout.setVisibility(View.VISIBLE);
                listViewPesananInterior.setAdapter(pesananInteriorAdapter);
                loadPesananInterior();
            }
        });
        listViewPesanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pesanan pesanan = pesananList.get(position);

                Intent intent = new Intent(getActivity(), DetailPemesananActivity.class);
                intent.putExtra("pesanan", pesanan);
                startActivity(intent);
            }
        });
        listViewPesananInterior.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PesananInterior pesananInterior = pesananInteriorList.get(position);

                Intent intent = new Intent(getActivity(), DetailPemesananInteriorActivity.class);
                intent.putExtra("pesananInterior", pesananInterior);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadPesanan() {
        String url = DbContract.SERVER_GET_ORDER + "?user_id=" + currentUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");

                            pesananList.clear();

                            for (int i = 0; i < serverResponse.length(); i++) {
                                JSONObject pesananObj = serverResponse.getJSONObject(i);

                                // Check if the "order_id" key exists
                                if (pesananObj.has("order_id")) {
                                    int order_id = pesananObj.getInt("order_id");
                                    int user_id = pesananObj.getInt("user_id");
                                    String order_date = pesananObj.getString("order_date");
                                    int total_order = pesananObj.getInt("total_order");
                                    String alamat = pesananObj.getString("alamat");
                                    String status = pesananObj.getString("status");

                                    if (user_id == currentUserId) {
                                        JSONArray itemsArray = pesananObj.getJSONArray("items");
                                        List<Item> items = new ArrayList<>();

                                        for (int j = 0; j < itemsArray.length(); j++) {
                                            JSONObject itemObj = itemsArray.getJSONObject(j);
                                            String id_furnitur = itemObj.getString("id_furnitur");
                                            int qty = itemObj.getInt("qty");
                                            double harga = itemObj.getDouble("harga");
                                            Item item = new Item(id_furnitur, qty, harga);
                                            items.add(item);
                                        }

                                        Pesanan pesanan = new Pesanan(order_id, user_id, order_date, total_order, alamat,status, items);
                                        pesananList.add(pesanan);
                                    }
                                }
                            }

                            pesananAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to load pesanan.", Toast.LENGTH_SHORT).show();
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

    private void loadPesananInterior() {
        String url = DbContract.SERVER_GET_INT_ORDER + "?user_id=" + currentUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");

                            pesananInteriorList.clear();

                            for (int i = 0; i < serverResponse.length(); i++) {
                                JSONObject pesananintObj = serverResponse.getJSONObject(i);

                                // Check if the "order_int_id" key exists
                                if (pesananintObj.has("order_int_id")) {
                                    int order_int_id = pesananintObj.getInt("order_int_id");
                                    int user_id = pesananintObj.getInt("user_id");
                                    String order_date = pesananintObj.getString("order_date");
                                    int total_order = pesananintObj.getInt("total_order");
                                    String alamat = pesananintObj.getString("alamat");
                                    String status = pesananintObj.getString("status");

                                    if (user_id == currentUserId) {
                                        // Retrieve items for the current order from 'item_interior' table
                                        JSONArray itemsArray = pesananintObj.getJSONArray("items");
                                        List<ItemPesananInterior> items = new ArrayList<>();

                                        for (int j = 0; j < itemsArray.length(); j++) {
                                            JSONObject itemObj = itemsArray.getJSONObject(j);
                                            String idInterior = itemObj.getString("id_interior");
                                            String catatan = itemObj.getString("catatan");
                                            double harga = itemObj.getDouble("harga");

                                            ItemPesananInterior item = new ItemPesananInterior(idInterior, catatan, harga);
                                            items.add(item);
                                        }

                                        PesananInterior pesananInterior = new PesananInterior(order_int_id, user_id, order_date, total_order, alamat, status, items);
                                        pesananInteriorList.add(pesananInterior);
                                    }
                                }
                            }

                            pesananInteriorAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to load pesanan interior.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to connect to server interior.", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyConnection.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void loadPesananByFilter(int bulan, String tahun) {
        String url = DbContract.SERVER_GET_ORDER + "?user_id=" + currentUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");

                            pesananList.clear();

                            for (int i = 0; i < serverResponse.length(); i++) {
                                JSONObject pesananObj = serverResponse.getJSONObject(i);

                                if (pesananObj.has("order_id")) {
                                    int order_id = pesananObj.getInt("order_id");
                                    int user_id = pesananObj.getInt("user_id");
                                    String order_date = pesananObj.getString("order_date");
                                    int total_order = pesananObj.getInt("total_order");
                                    String alamat = pesananObj.getString("alamat");
                                    String status = pesananObj.getString("status");

                                    // Parsing tanggal pesanan untuk mendapatkan bulan dan tahun
                                    String[] orderDateParts = order_date.split("-");
                                    int orderMonth = Integer.parseInt(orderDateParts[1]);
                                    String orderYear = orderDateParts[0];

                                    // Cek apakah pesanan sesuai dengan filter bulan dan tahun
                                    if (orderMonth == bulan && orderYear.equals(tahun) && user_id == currentUserId) {
                                        List<Item> items = new ArrayList<>();
                                        JSONArray itemsArray = pesananObj.getJSONArray("items");

                                        for (int j = 0; j < itemsArray.length(); j++) {
                                            JSONObject itemObj = itemsArray.getJSONObject(j);
                                            String id_furnitur = itemObj.getString("id_furnitur");
                                            int qty = itemObj.getInt("qty");
                                            double harga = itemObj.getDouble("harga");
                                            Item item = new Item(id_furnitur, qty, harga);
                                            items.add(item);
                                        }

                                        Pesanan pesanan = new Pesanan(order_id, user_id, order_date, total_order, alamat,status, items);
                                        pesananList.add(pesanan);
                                    }
                                }
                            }

                            pesananAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to load pesanan.", Toast.LENGTH_SHORT).show();
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


    private void loadPesananInteriorByFilter(int bulan, String tahun) {
        String url = DbContract.SERVER_GET_INT_ORDER + "?user_id=" + currentUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray serverResponse = response.getJSONArray("server_response");

                            pesananInteriorList.clear();

                            for (int i = 0; i < serverResponse.length(); i++) {
                                JSONObject pesananintObj = serverResponse.getJSONObject(i);

                                if (pesananintObj.has("order_int_id")) {
                                    int order_int_id = pesananintObj.getInt("order_int_id");
                                    int user_id = pesananintObj.getInt("user_id");
                                    String order_date = pesananintObj.getString("order_date");
                                    int total_order = pesananintObj.getInt("total_order");
                                    String alamat = pesananintObj.getString("alamat");
                                    String status = pesananintObj.getString("status");

                                    // Parsing tanggal pesanan untuk mendapatkan bulan dan tahun
                                    String[] orderDateParts = order_date.split("-");
                                    int orderMonth = Integer.parseInt(orderDateParts[1]);
                                    String orderYear = orderDateParts[0];

                                    // Cek apakah pesanan sesuai dengan filter bulan dan tahun
                                    if (orderMonth == bulan && orderYear.equals(tahun) && user_id == currentUserId) {
                                        // Retrieve items for the current order from 'item_interior' table
                                        JSONArray itemsArray = pesananintObj.getJSONArray("items");
                                        List<ItemPesananInterior> items = new ArrayList<>();

                                        for (int j = 0; j < itemsArray.length(); j++) {
                                            JSONObject itemObj = itemsArray.getJSONObject(j);
                                            String idInterior = itemObj.getString("id_interior");
                                            String catatan = itemObj.getString("catatan");
                                            double harga = itemObj.getDouble("harga");

                                            ItemPesananInterior item = new ItemPesananInterior(idInterior, catatan, harga);
                                            items.add(item);
                                        }

                                        PesananInterior pesananInterior = new PesananInterior(order_int_id, user_id, order_date, total_order, alamat, status, items);
                                        pesananInteriorList.add(pesananInterior);
                                    }
                                }
                            }

                            pesananInteriorAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to load pesanan interior.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to connect to server interior.", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyConnection.getInstance(getActivity()).addToRequestQueue(request);
    }


}
