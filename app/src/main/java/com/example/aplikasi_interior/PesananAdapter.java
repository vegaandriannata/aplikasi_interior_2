package com.example.aplikasi_interior;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import com.example.aplikasi_interior.R;

import java.util.List;

public class PesananAdapter extends BaseAdapter {
    private Context context;
    private List<Pesanan> pesananList;
    private DecimalFormat decimalFormat;

    public PesananAdapter(Context context, List<Pesanan> pesananList) {
        this.context = context;
        this.pesananList = pesananList;
        decimalFormat = new DecimalFormat("#,###");
    }

    @Override
    public int getCount() {
        return pesananList.size();
    }

    @Override
    public Object getItem(int position) {
        return pesananList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setData(List<Pesanan> pesananList) {
        this.pesananList = pesananList;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_pesanan, null);
        }

        TextView textViewOrderId = convertView.findViewById(R.id.textViewOrderId);
        TextView textViewOrderDate = convertView.findViewById(R.id.textViewOrderDate);
        TextView textViewTotalOrder = convertView.findViewById(R.id.textViewTotalOrder);
        TextView textViewStatus = convertView.findViewById(R.id.txtStatus);

        Pesanan pesanan = pesananList.get(position);

        textViewOrderId.setText("Nomor Order: "+String.valueOf(pesanan.getOrder_id()));
        textViewOrderDate.setText("Tanggal Order: "+pesanan.getOrder_date());
        textViewTotalOrder.setText("Total Order: Rp" + decimalFormat.format(pesanan.getTotal_order()));

        textViewStatus.setText(pesanan.getStatus());

        return convertView;
    }
}
