package com.example.alex.abstruct;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Set the animation when activity is opened
        overridePendingTransition(R.transition.in_from_right, R.transition.stay_in_place);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //Hide activity title
        getSupportActionBar().setTitle("Immagine 1");

        initViews();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Set the animation when activity is closed, using onBackPressed
        overridePendingTransition(R.transition.stay_in_place,R.transition.out_to_right);

    }


    private void initViews(){

        final ImageView detailImageView = (ImageView) findViewById(R.id.detailImageView);

        //Get the onject from the intent
        ImageClass imageObject = (ImageClass) getIntent().getSerializableExtra("imageObject");

        //Set the title of the image
        TextView titleTw = (TextView) findViewById(R.id.titleTw);
        //titleTw.setText(imageObject.getTitle);

        //Load image in imageview with picasso, and get image dominant color with Picasso Palette Library
        Picasso.with(this).load(imageObject.getImageRegularUrl()).into(detailImageView,
                PicassoPalette.with(imageObject.getImageSmallUrl(), detailImageView)
                        .intoCallBack(
                                new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {

                                        //palette.getMutedColor(0x00000) This is to get the doinant color

                                        float[] hsv;
                                        //Make dominant color Brighter
                                        hsv = new float[3];
                                        Color.colorToHSV(palette.getMutedColor(0x00000), hsv);
                                        hsv[2] *= 1.2; //change this to change brightness
                                        int dominantColor = Color.HSVToColor(hsv);

                                        //Make dominant color Darker
                                        hsv = new float[3];
                                        Color.colorToHSV(palette.getMutedColor(0x00000), hsv);
                                        hsv[2] *= 0.9; //change this to change brightness
                                        int darkDominantColor = Color.HSVToColor(hsv);


                                        //Set color to status bar
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(Color.TRANSPARENT);
                                        window.setNavigationBarColor(darkDominantColor);


                                        LinearLayout titleLayout =(LinearLayout) findViewById(R.id.titleLayout);
                                        titleLayout.setBackgroundColor(dominantColor);
                                    }
                                })
        );
    }

}
