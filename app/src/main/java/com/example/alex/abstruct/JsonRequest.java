package com.example.alex.abstruct;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by Alex on 22/07/16.
 */
public class JsonRequest {

    String url;

    Context context;

    JsonRequest(String url, Context context){
        this.url=url;
        this.context=context;
    }

    public void getJsonData(final VolleyCallback callback) {

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonArrayRequest getRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            callback.onSuccess(response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    });

            queue.add(getRequest);

    }
}



interface VolleyCallback{
    void onSuccess(JSONArray response);
}
