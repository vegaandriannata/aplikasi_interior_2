package com.example.aplikasi_interior;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectedProductAdapter extends RecyclerView.Adapter<SelectedProductAdapter.SelectedProductViewHolder> {
    private List<Product> selectedProducts;

    public SelectedProductAdapter(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @NonNull
    @Override
    public SelectedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_product, parent, false);
        return new SelectedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedProductViewHolder holder, int position) {
        Product product = selectedProducts.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return selectedProducts.size();
    }

    public class SelectedProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView productQuantityTextView;

        public SelectedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
            productQuantityTextView = itemView.findViewById(R.id.tvQuantity);
        }

        public void bind(Product product) {
            productNameTextView.setText(product.getName());
            productPriceTextView.setText(product.getPrice());
            productQuantityTextView.setText(" X " + String.valueOf(product.getQuantity()));
        }
    }
}
