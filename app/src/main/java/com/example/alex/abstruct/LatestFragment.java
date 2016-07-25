package com.example.alex.abstruct;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LatestFragment extends Fragment {

    private View view;
    private ArrayList imageArrayList = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest, container, false);

        //Setup Recycler View
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String url = "https://api.unsplash.com/photos?per_page=20&page=1&order_by=latest&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

        boolean isFirstPage = true;

        getJsonArray(url,isFirstPage); //Make the request for the json, using the url, and the boolean isFirstPage, to check if set the adapter or not

        //What to do when recycler view reach the end of the page
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                //Update url whit the new page number
                String url = "https://api.unsplash.com/photos?per_page=20&page="+String.valueOf(current_page)+"&order_by=latest&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

                //Set the boolean to false, because there is no need to reset the adapter
                boolean isFirstPage = false;

                getJsonArray(url,isFirstPage); //Make the request for the json

            }
        });

        return view;

    }

    private void getJsonArray(String url, final boolean isFirstPage){

        JsonRequest jsonRequest = new JsonRequest(url, getContext());

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), imageArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


}