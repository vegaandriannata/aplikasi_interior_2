package com.example.aplikasi_interior;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

import com.example.aplikasi_interior.CartProduct;
import com.example.aplikasi_interior.R;

import java.util.List;

public class CartProductAdapter extends ArrayAdapter<CartProduct> {

    private Context context;
    private List<CartProduct> cartProductList;

    public interface DeleteCartItemListener {
        void onDeleteCartItem(int itemId);
    }

    private DeleteCartItemListener deleteCartItemListener;

    public CartProductAdapter(Context context, List<CartProduct> cartProductList, DeleteCartItemListener listener) {
        super(context, 0, cartProductList);
        this.context = context;
        this.cartProductList = cartProductList;
        this.deleteCartItemListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_cart_product, parent, false);
        }

        CartProduct cartProduct = cartProductList.get(position);

        TextView textViewIdBrg = view.findViewById(R.id.textViewIdBrg);
        TextView textViewUserId = view.findViewById(R.id.textViewUserId);
        TextView textViewQty = view.findViewById(R.id.textViewQty);
        TextView textViewNamaBrg = view.findViewById(R.id.textViewNamaBrg);
        TextView textViewHargaBrg = view.findViewById(R.id.textViewHargaBrg);

        ImageView deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the item from the cartProductList
                cartProductList.remove(position);
                // Notify the adapter about the data change
                notifyDataSetChanged();

                // Delete the item from the database
                int itemId = cartProduct.getId_keranjang();
                deleteCartItemListener.onDeleteCartItem(itemId);
            }
        });




        textViewIdBrg.setText("ID Barang: " + String.valueOf(cartProduct.getId_brg()));
        textViewUserId.setText("User ID: " + String.valueOf(cartProduct.getUser_id()));
        textViewQty.setText("Quantity: " +String.valueOf(cartProduct.getQty()));
        textViewNamaBrg.setText("Nama: " + String.valueOf(cartProduct.getNama_brg()));
        textViewHargaBrg.setText("Harga: " + String.valueOf(cartProduct.getHarga_brg()));




        return view;
    }

}

