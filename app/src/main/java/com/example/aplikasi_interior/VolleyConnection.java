package com.example.aplikasi_interior;

import android.content.Context;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyConnection {

    private static VolleyConnection vInstance;
    private RequestQueue requestQueue;
    private static Context vCtx;

    private VolleyConnection(Context context) {
        vCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(vCtx.getApplicationContext());
        }
        return requestQueue;
    }


    public <T> void addToRequestQueue(JsonObjectRequest request) {
        getRequestQueue().add(request);
    }
    public static synchronized VolleyConnection getInstance(Context context) {
        if (vInstance == null) {
            vInstance = new VolleyConnection(context);
        }
        return vInstance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueueInstance() {
        return getRequestQueue();
    }
}
