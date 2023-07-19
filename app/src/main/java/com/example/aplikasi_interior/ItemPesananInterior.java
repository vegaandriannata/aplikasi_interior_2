package com.example.aplikasi_interior;

import java.io.Serializable;

public class ItemPesananInterior implements Serializable {
    private String idInterior;
    private String catatan;
    private double harga;

    public ItemPesananInterior(String idInterior, String catatan, double harga) {
        this.idInterior = idInterior;
        this.catatan = catatan;
        this.harga = harga;
    }

    public String getIdInterior() {
        return idInterior;
    }

    public String getCatatan() {
        return catatan;
    }

    public double getHarga() {
        return harga;
    }
}
