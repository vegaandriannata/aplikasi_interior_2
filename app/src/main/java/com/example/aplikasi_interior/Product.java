package com.example.aplikasi_interior;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Product implements Serializable {
    private transient Bitmap image;
    private byte[] imageBytes;
    private String name;
    private String price;
    private String ket_furnitur; // Keterangan barang
    private int stok; // Stok barang
    private int quantity; // Jumlah produk yang dipilih
    private int id_furnitur; // ID barang

    public Product(Bitmap image, String name, String price, String ket_furnitur, int stok, int id_furnitur) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.ket_furnitur = ket_furnitur;
        this.stok = stok;
        this.quantity = 1; // Default quantity = 1
        this.id_furnitur = id_furnitur;
    }

    public Bitmap getImage() {
        if (image == null && imageBytes != null) {
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageBytes = byteArrayOutputStream.toByteArray();
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return formatPrice();
    }

    public String getKet_furnitur() {
        return ket_furnitur;
    }

    public int getStok() {
        return stok;
    }
    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId_furnitur() {
        return id_furnitur;
    }

    public void setIdBrg(int id_furnitur) {
        this.id_furnitur = id_furnitur;
    }

    private String formatPrice() {
        try {
            double amount = Double.parseDouble(price);
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###");
            return "Rp" + formatter.format(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return price;
        }
    }
}
