package com.quadriyanney.moviesdisplay;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by quadriy on 6/5/17.
 */

public class Controller extends Application {

    public static final String TAG = Controller.class.getSimpleName();

    private RequestQueue requestQueue;
    private static Controller controller;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
    }

    public static synchronized Controller getInstance() {
        return controller;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (requestQueue != null) {
//            requestQueue.cancelAll(tag);
//        }
//    }
}