package com.example.aplikasi_interior;
import java.util.List;

import java.io.Serializable;

public class Pesanan implements Serializable {
    private int order_id;
    private int user_id;
    private String order_date;
    private int total_order;
    private String alamat;
    private String status;
    private List<Item> items;

    public Pesanan(int order_id, int user_id, String order_date, int total_order, String alamat, String status, List<Item> items) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.total_order = total_order;
        this.alamat = alamat;
        this.status = status;
        this.items = items;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public int getTotal_order() {
        return total_order;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getStatus() {
        return status;
    }

    public List<Item> getItems() {
        return items;
    }

}
