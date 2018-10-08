package com.example.sarahtang.midtermelections;

import android.app.Application;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by sarahtang on 10/3/18.
 */

//Pro Public API Key: W9mgP0QOnz5VmuBmb4j5x6IHrGdoOHDlTaQyagK7
//https://androidclarified.com/android-volley-example/

//GEOCODIO
//    String api_key = "1696e0bfe56b6b7011be886e88e567b45e0760b";
//    String url = "https://api.geocod.io/v1.3/api_endpoint_here?api_key=" + api_key;

public class Volley extends Application {

    //Declare a private RequestQueue variable
    private RequestQueue requestQueue;
    private static Volley mInstance;
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized Volley getInstance() {
        return mInstance;
    }
    /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instantiated only once and the same
    instance is used throughout the application
    */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }
    /*
    public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }
    /*
    Cancel all the requests matching with the given tag
    */
    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }
}
