package com.example.aplikasi_interior;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.Serializable;

public class CartProduct implements Serializable {
    private int id_keranjang;
    private int id_brg;
    private int user_id;
    private int qty;
    private String nama_brg;
    private String harga_brg;


    public CartProduct(int id_keranjang,int id_brg, int user_id, int qty, String nama_brg, String harga_brg) {
        this.id_keranjang = id_keranjang;
        this.id_brg = id_brg;
        this.user_id = user_id;
        this.qty = qty;
        this.nama_brg = nama_brg;
        this.harga_brg = harga_brg;

    }
    public int getId_keranjang() {
        return id_keranjang;
    }
    public int getId_brg() {
        return id_brg;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getNama_brg() {
        return nama_brg;
    }

    public String getHarga_brg() {
        return formatPrice();
    }

    private String formatPrice() {
        try {
            double amount = Double.parseDouble(harga_brg);
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###");
            return "Rp" + formatter.format(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return harga_brg;
        }
    }
}

