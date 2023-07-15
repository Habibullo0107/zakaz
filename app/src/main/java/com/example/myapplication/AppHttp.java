package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppHttp {

    private static AppHttp instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private AppHttp(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized AppHttp getInstance(Context context) {
        if (instance == null) {
            instance = new AppHttp(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
