package com.example.aplikasi_interior;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import java.util.List;

public class PesananInteriorAdapter extends BaseAdapter {
    private Context context;
    private List<PesananInterior> pesananInteriorList;
    private DecimalFormat decimalFormat;

    public PesananInteriorAdapter(Context context, List<PesananInterior> pesananInteriorList) {
        this.context = context;
        this.pesananInteriorList = pesananInteriorList;
        decimalFormat = new DecimalFormat("#,###");
    }

    @Override
    public int getCount() {
        return pesananInteriorList.size();
    }

    @Override
    public Object getItem(int position) {
        return pesananInteriorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPesananInteriorList(List<PesananInterior> pesananInteriorList) {
        this.pesananInteriorList = pesananInteriorList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_pesanan_interior, parent, false);
        }

        TextView textViewOrderId = convertView.findViewById(R.id.txtOrderId);
        TextView textViewOrderDate = convertView.findViewById(R.id.txtOrderDate);
        TextView textViewTotalOrder = convertView.findViewById(R.id.txtTotalOrder);

        TextView textViewStatus = convertView.findViewById(R.id.txtStatus);

        PesananInterior pesananInterior = pesananInteriorList.get(position);

        textViewOrderId.setText("Nomor Order: "+String.valueOf(pesananInterior.getOrderIntId()));
        textViewOrderDate.setText("Tanggal Order: "+pesananInterior.getOrderDate());
        textViewTotalOrder.setText("Total Order: Rp" + decimalFormat.format(pesananInterior.getTotalOrder()));

        textViewStatus.setText(pesananInterior.getStatus());

        return convertView;
    }
}
