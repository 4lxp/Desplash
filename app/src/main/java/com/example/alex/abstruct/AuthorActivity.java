package com.example.alex.abstruct;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AuthorActivity extends AppCompatActivity {

    private ImageClass imageObject;
    private ArrayList imageArrayList = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private CircularImageView authorImageView;
    private String url;
    private int dominantColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        //Get object from intent
        imageObject = (ImageClass) getIntent().getSerializableExtra("imageObject");

        initViews();

        setViewsData();

        setViewsColors();

        firstRecyclerViewLoad();

        nextRecyclerViewLoads();

    }

    private void initViews(){

        // TODO: Fix E/RecyclerView: No adapter attached; skipping layout

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        //Setup Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        authorImageView = (CircularImageView) findViewById(R.id.authorImageView);

    }

    private void setViewsData(){

        collapsingToolbarLayout.setTitle(imageObject.getAuthorName());
        Picasso.with(this).load(imageObject.getAuthorImage()).into(authorImageView);

    }

    private void setViewsColors(){

        //Get and  edit colors
        dominantColor = Color.parseColor(imageObject.getColor());

        //Code to make a color darker or brighter
        float[] hsv;

        //Make dominant color Brighter
        hsv = new float[3];
        Color.colorToHSV(dominantColor, hsv);
        hsv[2] *= 6; //change this to change brightness
        int brightDominantColor = Color.HSVToColor(hsv);

        //Make dominant color Darker
        hsv = new float[3];
        Color.colorToHSV(dominantColor, hsv);
        hsv[2] *= 0.7; //change this to change brightness
        int darkDominantColor = Color.HSVToColor(hsv);

        //Make dominant color Darker
        hsv = new float[3];
        Color.colorToHSV(darkDominantColor, hsv);
        hsv[2] *= 0.7; //change this to change brightness
        int darkDarkDominantColor = Color.HSVToColor(hsv);


        //Set color to various elements
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkDarkDominantColor);
        //window.setNavigationBarColor(darkDominantColor);

        collapsingToolbarLayout.setBackgroundColor(darkDominantColor);
        collapsingToolbarLayout.setCollapsedTitleTextColor(brightDominantColor);
        collapsingToolbarLayout.setExpandedTitleColor(brightDominantColor);
    }

    private void firstRecyclerViewLoad(){
        url = "https://api.unsplash.com/users/"+imageObject.getAuthorUserName()+"/photos?order_by=latest&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

        boolean isFirstPage = true;

        getJsonArray(url,isFirstPage); //Make the request for the json, using the url, and the boolean isFirstPage, to check if set the adapter or not
    }

    private void nextRecyclerViewLoads(){
        //What to do when recycler view reach the end of the page
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                //Update url whit the new page number
                String url = "https://api.unsplash.com//users/"+imageObject.getAuthorUserName()+"/photos?per_page=20&page="+String.valueOf(current_page)+"&order_by=latest&client_id=42af43eb53971d07987caea0f6474d8592f3633d1f810a92ba3468f36fa44a25";

                //Set the boolean to false, because there is no need to reset the adapter
                boolean isFirstPage = false;

                getJsonArray(url,isFirstPage); //Make the request for the json

            }
        });
    }

    private void setRecyclerViewAdapter(){

        recyclerViewAdapter = new RecyclerViewAdapter(this, imageArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void getJsonArray(String url, final boolean isFirstPage){

        JsonRequest jsonRequest = new JsonRequest(url, this);

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
                JSONObject author = photo.getJSONObject("user");
                JSONObject authorImages = author.getJSONObject("profile_image");

                //Put data into variables
                String imageSmallUrl = photoUrls.getString("small"); //thumb,small,regular,full,raw
                String imageRegularUrl = photoUrls.getString("regular");
                String imageFullUrl = photoUrls.getString("full");
                String imageRawUrl = photoUrls.getString("raw");
                String color= photo.getString("color");
                int likes= photo.getInt("likes");
                String authorUserName = author.getString("username");
                String authorName = author.getString("name");
                String authorImage = authorImages.getString("large");

                //Send data to the ImageCLass
                ImageClass imageClass = new ImageClass();
                imageClass.setImageSmallUrl(imageSmallUrl);
                imageClass.setImageRegularUrl(imageRegularUrl);
                imageClass.setImageFullUrl(imageFullUrl);
                imageClass.setImageRawUrl(imageRawUrl);
                imageClass.setColor(color);
                imageClass.setLikes(likes);
                imageClass.setAuthorUserName(authorUserName);
                imageClass.setAuthorName(authorName);
                imageClass.setAuthorImage(authorImage);

                //Add ImageClass object to the imageArrayList
                imageArrayList.add(imageClass);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
