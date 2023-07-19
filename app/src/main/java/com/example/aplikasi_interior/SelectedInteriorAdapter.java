package com.example.aplikasi_interior;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectedInteriorAdapter extends RecyclerView.Adapter<SelectedInteriorAdapter.SelectedInteriorViewHolder> {
    private List<Interior> selectedInteriors;

    public SelectedInteriorAdapter(List<Interior> selectedInteriors) {
        this.selectedInteriors = selectedInteriors;
    }

    @NonNull
    @Override
    public SelectedInteriorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_interior, parent, false);
        return new SelectedInteriorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedInteriorViewHolder holder, int position) {
        Interior interior = selectedInteriors.get(position);
        holder.bind(interior);
    }

    @Override
    public int getItemCount() {
        return selectedInteriors.size();
    }

    public class SelectedInteriorViewHolder extends RecyclerView.ViewHolder {
        private TextView interiorNameTextView;
        private TextView interiorPriceTextView;


        public SelectedInteriorViewHolder(@NonNull View itemView) {
            super(itemView);
            interiorNameTextView = itemView.findViewById(R.id.interior_name);
            interiorPriceTextView = itemView.findViewById(R.id.interior_price);

        }

        public void bind(Interior interior) {
            interiorNameTextView.setText(interior.getName());
            interiorPriceTextView.setText(interior.getPrice());

        }
    }
}
