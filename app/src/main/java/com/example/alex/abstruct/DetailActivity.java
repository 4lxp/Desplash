package com.example.alex.abstruct;

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

    boolean isFirstTouch=true;
    FrameLayout frameLayout;
    int dominantColor;
    LinearLayout authorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Set the animation when activity is opened
        overridePendingTransition(R.transition.in_from_right, R.transition.stay_in_place);

        initViews();

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

        ImageView detailImageView = (ImageView) findViewById(R.id.detailImageView);
        CircularImageView authorImageView = (CircularImageView) findViewById(R.id.authorImageView);
        TextView authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        authorLayout = (LinearLayout) findViewById(R.id.authorLayout);


        //Get the onject from the intent
        ImageClass imageObject = (ImageClass) getIntent().getSerializableExtra("imageObject");

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


        //Set data
        Picasso.with(this).load(imageObject.getImageRegularUrl()).into(detailImageView);
        Picasso.with(this).load(imageObject.getAuthorImage()).into(authorImageView);
        authorNameTextView.setText(imageObject.getAuthorName());

        //Add pan and zoom funcionality to detailImageView
        PhotoViewAttacher attacher = new PhotoViewAttacher(detailImageView);
        //Intercept Click on image view to hide and show navigation
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if(isFirstTouch) {
                    isFirstTouch=false;

                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

                    frameLayout.setBackgroundColor(Color.BLACK);
                    authorLayout.setVisibility(View.INVISIBLE);

                }else{
                    isFirstTouch=true;

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    frameLayout.setBackgroundColor(dominantColor);
                    authorLayout.setVisibility(View.VISIBLE);
                }
            }
        });


    }

}
