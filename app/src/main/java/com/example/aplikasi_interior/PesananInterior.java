package com.example.aplikasi_interior;

import java.io.Serializable;
import java.util.List;

public class PesananInterior implements Serializable {
    private int order_int_id;
    private int user_id;
    private String order_date;
    private int total_order;
    private String alamat;
    private String status;
    private List<ItemPesananInterior> items;

    public PesananInterior(int order_int_id, int user_id, String order_date, int total_order, String alamat, String status, List<ItemPesananInterior> items) {
        this.order_int_id = order_int_id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.total_order = total_order;
        this.alamat = alamat;
        this.status = status;
        this.items = items;
    }

    public int getOrderIntId() {
        return order_int_id;
    }

    public int getUserId() {
        return user_id;
    }

    public String getOrderDate() {
        return order_date;
    }

    public int getTotalOrder() {
        return total_order;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getStatus() {
        return status;
    }

    public List<ItemPesananInterior> getItems() {
        return items;
    }
}
