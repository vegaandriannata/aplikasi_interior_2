package com.example.aplikasi_interior;
import java.io.Serializable;
public class Item implements Serializable {
    private String idFurnitur;
    private int qty;
    private double harga;

    public Item(String idFurnitur, int qty, double harga) {
        this.idFurnitur = idFurnitur;
        this.qty = qty;
        this.harga = harga;
    }

    public String getidFurnitur() {
        return idFurnitur;
    }

    public int getQty() {
        return qty;
    }

    public double getHarga() {
        return harga;
    }
}
