package com.example.aplikasi_interior;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;

import android.content.Context;
import android.content.Intent;

import java.util.List;

public class InteriorAdapter extends RecyclerView.Adapter<InteriorAdapter.InteriorViewHolder> {
    private List<Interior> interiorList;
    private Context context;

    public InteriorAdapter(List<Interior> interiorList) {
        this.interiorList = interiorList;
    }

    @NonNull
    @Override
    public InteriorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_interior, parent, false);
        return new InteriorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InteriorViewHolder holder, int position) {
        final Interior interior = interiorList.get(position);

        // Set the image bitmap to the ImageView
        holder.interiorImage.setImageBitmap(interior.getImage());

        // Set the other views as needed
        holder.interiorName.setText(interior.getName());
        holder.interiorPrice.setText(interior.getPrice());

        // Set a click listener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                // Convert the image Bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                interior.getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // Set the byte array to the Interior object
                interior.setImageBytes(byteArray);

                // Create an intent to launch InteriorDetailActivity
                Intent intent = new Intent(context, InteriorDetailActivity.class);
                intent.putExtra("interior", interior);
                context.startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        return interiorList.size();
    }
    public void setInteriorList(List<Interior> interiorList) {
        this.interiorList = interiorList;
    }
    public class InteriorViewHolder extends RecyclerView.ViewHolder {
        private ImageView interiorImage;
        private TextView interiorName;
        private TextView interiorPrice;

        public InteriorViewHolder(@NonNull View itemView) {
            super(itemView);
            interiorImage = itemView.findViewById(R.id.interior_image);
            interiorName = itemView.findViewById(R.id.interior_name);
            interiorPrice = itemView.findViewById(R.id.interior_price);
        }
    }
}
