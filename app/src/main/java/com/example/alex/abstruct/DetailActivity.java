package com.example.alex.abstruct;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailActivity extends AppCompatActivity {

    private boolean isFirstTouch=true;
    private FrameLayout frameLayout;
    private int dominantColor;
    private LinearLayout authorLayout;
    private ImageClass imageObject;
    private CircularImageView authorImageView;
    private ImageView detailImageView;
    private TextView authorNameTextView;
    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Set the animation when activity is opened
        overridePendingTransition(R.transition.in_from_right, R.transition.stay_in_place);

        //Get the object from the intent
        imageObject = (ImageClass) getIntent().getSerializableExtra("imageObject");

        initViews();

        manageClicks();

        //Set the data to the various elements of the ui
        setViewsData();

        //Set colors to the various elements of the ui
        setViewsColors();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Set the animation when activity is closed, using onBackPressed
        overridePendingTransition(R.transition.stay_in_place,R.transition.out_to_right);

    }


    //If thh arrow on toolbar is clicked, call the same method of the back button
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.onBackPressed();

        }
        return super.onOptionsItemSelected(menuItem);
    }


    private void initViews(){

        detailImageView = (ImageView) findViewById(R.id.detailImageView);
        //Add pan and zoom functionality to detailImageView
        attacher = new PhotoViewAttacher(detailImageView);

        authorImageView = (CircularImageView) findViewById(R.id.authorImageView);
        authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        authorLayout = (LinearLayout) findViewById(R.id.authorLayout);

    }

    private void manageClicks(){

        //DetailImageView click
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                setDarkUi();

            }

        });

        //AuthorLayout click
        authorLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Start AuthorActivity
                Intent intent = new Intent(DetailActivity.this , AuthorActivity.class);

                //Send the current imageObject
                intent.putExtra("imageObject", imageObject); //I can pass the object with the intent because the class implement serializable
                v.getContext().startActivity(intent);

            }
        });
    }

    private void setViewsData(){
        Picasso.with(this).load(imageObject.getImageRegularUrl()).into(detailImageView);
        Picasso.with(this).load(imageObject.getAuthorImage()).into(authorImageView);
        authorNameTextView.setText(imageObject.getAuthorName());
    }

    private void setViewsColors(){

        //Get and  edit colors
        dominantColor =Color.parseColor(imageObject.getColor());

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


        //Set color to various elements
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkDominantColor);
        window.setNavigationBarColor(darkDominantColor);

        frameLayout.setBackgroundColor(dominantColor);
        authorLayout.setBackgroundColor(darkDominantColor);
        authorNameTextView.setTextColor(brightDominantColor);
    }

    private void setDarkUi() {

        if (isFirstTouch) {

            //If is the first time the imageview is touched, hide the bars, and make the background dark
            isFirstTouch = false;

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

            frameLayout.setBackgroundColor(Color.BLACK);
            authorLayout.setVisibility(View.INVISIBLE);

        } else {

            //If is the second time the imageview is touched, restore the bars and the previous colors
            isFirstTouch = true;

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            frameLayout.setBackgroundColor(dominantColor);
            authorLayout.setVisibility(View.VISIBLE);
        }
    }

}
