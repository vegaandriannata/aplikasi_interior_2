package com.example.aplikasi_interior.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasi_interior.R;

import java.util.ArrayList;
import java.util.List;

import com.example.aplikasi_interior.Interior;
import com.example.aplikasi_interior.InteriorAdapter;
import com.example.aplikasi_interior.Product;
import com.example.aplikasi_interior.ProductAdapter;
import com.example.aplikasi_interior.DbContract;
import com.example.aplikasi_interior.VolleyConnection;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private InteriorAdapter interiorAdapter;
    private List<Product> productList;
    private List<Interior> interiorList;
    private boolean isInteriorMenuClicked = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a list of products and interiors
        productList = new ArrayList<>();
        interiorList = new ArrayList<>();

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Set the initial adapter to product adapter
        productAdapter = new ProductAdapter(productList);
        interiorAdapter = new InteriorAdapter(interiorList);
        recyclerView.setAdapter(productAdapter);

        // Set click listeners on menu items
        TextView furnitureMenu = view.findViewById(R.id.menu_furniture);
        TextView interiorMenu = view.findViewById(R.id.menu_interior);

        furnitureMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInteriorMenuClicked = false;
                recyclerView.setAdapter(productAdapter);
                fetchProductDataFromDatabase();
            }
        });

        interiorMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInteriorMenuClicked = true;
                recyclerView.setAdapter(interiorAdapter);
                fetchInteriorDataFromDatabase();
            }
        });

        // Set up the search functionality
        EditText searchEditText = view.findViewById(R.id.search_edittext);
        ImageButton searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = searchEditText.getText().toString().trim();
                searchItemByName(itemName);
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String itemName = searchEditText.getText().toString().trim();
                    searchItemByName(itemName);
                    return true;
                }
                return false;
            }
        });

        // Fetch initial data from the database
        fetchProductDataFromDatabase();
    }

    private void fetchProductDataFromDatabase() {
        String url = DbContract.SERVER_GET_PRODUCT;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("server_response");
                            productList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productObject = jsonArray.getJSONObject(i);
                                String name = productObject.getString("nama_furnitur");
                                String price = productObject.getString("harga_furnitur");
                                String imageString = productObject.getString("gambar");
                                String ket_furnitur = productObject.getString("ket_furnitur");
                                int stok = productObject.getInt("stok");
                                int id_furnitur = productObject.getInt("id_furnitur");
                                // Convert the image string to byte array
                                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);

                                // Convert the byte array to Bitmap
                                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                Product product = new Product(imageBitmap, name, price, ket_furnitur, stok, id_furnitur);
                                productList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleyConnection.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void fetchInteriorDataFromDatabase() {
        String url = DbContract.SERVER_GET_INTERIOR;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("server_response");
                            interiorList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject interiorObject = jsonArray.getJSONObject(i);
                                String idInterior = interiorObject.getString("id_interior");
                                String name = interiorObject.getString("nama_interior");
                                String price = interiorObject.getString("harga_interior");
                                String imageString = interiorObject.getString("gambar_interior");
                                String ket_interior = interiorObject.getString("ket_interior");
                                // Decode the image string into a Bitmap
                                byte[] decodedBytes = Base64.decode(imageString, Base64.DEFAULT);
                                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                                // Create an Interior object and add it to the list
                                Interior interior = new Interior(imageBitmap, idInterior, name, price, ket_interior);
                                interiorList.add(interior);
                            }

                            // Notify the adapter that the data has changed
                            interiorAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleyConnection.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void searchItemByName(String itemName) {
        if (isInteriorMenuClicked) {
            List<Interior> filteredInteriors = new ArrayList<>();
            for (Interior interior : interiorList) {
                if (interior.getName().toLowerCase().contains(itemName.toLowerCase())) {
                    filteredInteriors.add(interior);
                }
            }
            interiorAdapter.setInteriorList(filteredInteriors);
            interiorAdapter.notifyDataSetChanged();
        } else {
            List<Product> filteredProducts = new ArrayList<>();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(itemName.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
            productAdapter.setProductList(filteredProducts);
            productAdapter.notifyDataSetChanged();
        }
    }
}
