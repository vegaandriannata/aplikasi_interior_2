package com.example.aplikasi_interior;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Interior implements Serializable {
    private transient Bitmap image;
    private byte[] imageBytes;
    private String idInterior; // ID Interior
    private String name;
    private String price;
    private String ketInterior;

    public Interior(Bitmap image, String idInterior, String name, String price, String ketInterior) {
        this.image = image;
        this.idInterior = idInterior;
        this.name = name;
        this.price = price;
        this.ketInterior = ketInterior;
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

    public String getIdInterior() {
        return idInterior;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return formatPrice();
    }

    public String getKetInterior() {
        return ketInterior;
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
