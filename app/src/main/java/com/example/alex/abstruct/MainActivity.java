package com.example.alex.abstruct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList imageArrayList = new ArrayList<>();
    RecyclerViewAdapter recyclerViewAdapter;

    String sort="Latest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Setup Recycler View
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String url = "https://api.unsplash.com/photos?per_page=20&page=1&order_by="+sort+"&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

        boolean isFirstPage = true;

        getJsonArray(url,isFirstPage); //Make the request for the json, using the url, and the boolean isFirstPage, to check if set the adapter or not

        //What to do when recycler view reach the end of the page
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                //Update url whit the new page number
                String url = "https://api.unsplash.com/photos?per_page=20&page="+String.valueOf(current_page)+"&order_by="+sort+"&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

                //Set the boolean to false, because there is no need to reset the adapter
                boolean isFirstPage = false;

                getJsonArray(url,isFirstPage); //Make the request for the json

            }
        });


    }

    private void getJsonArray(String url, final boolean isFirstPage){

        JsonRequest jsonRequest = new JsonRequest(url, MainActivity.this);

        jsonRequest.getJsonData(new VolleyCallback(){

            @Override
            public void onSuccess(JSONArray jsonResponse) {

                parseJsonIntoArrayList(jsonResponse); //Parse the json returned from the class, and add it to the arraylist

                if(isFirstPage){
                    setRecyclerViewAdapter(); //Initizialize Recycler View and set adapter only the first time, i do this here because i need to wait for the data to be downloaded
                }

            }
        });

    }

    private void parseJsonIntoArrayList(JSONArray jsonResponse){


        try {

            //Loop trough all the node in json
            for (int i = 0; i < jsonResponse.length(); i++) {

                //Parse json, get photo's data
                JSONObject photo = (JSONObject) jsonResponse.get(i);
                JSONObject photoUrls = photo.getJSONObject("urls");

                String imageSmallUrl = photoUrls.getString("small"); //thumb,small,regular,full,raw
                String imageRegularUrl = photoUrls.getString("regular");
                String imageFullUrl = photoUrls.getString("full");
                String imageRawUrl = photoUrls.getString("raw");

                //Send data to the ImageCLass
                ImageClass imageClass = new ImageClass();
                imageClass.setImageSmallUrl(imageSmallUrl);
                imageClass.setImageRegularUrl(imageRegularUrl);
                imageClass.setImageFullUrl(imageFullUrl);
                imageClass.setImageRawUrl(imageRawUrl);

                //Add ImageClass object to the imageArrayList
                imageArrayList.add(imageClass);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setRecyclerViewAdapter(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), imageArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

}
