package com.example.alex.abstruct;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailActivity extends AppCompatActivity {

    private boolean isFirstTouch=true;
    private FrameLayout frameLayout;
    private int dominantColor;
    private ImageClass imageObject;
    private CircularImageView authorImageView;
    private ImageView detailImageView;
    private TextView authorNameTextView;
    private PhotoViewAttacher attacher;
    private Toolbar toolbar;
    private Drawable overflowIcon;

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


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle item selection
        switch (menuItem.getItemId()) {

            //Back Arrow
            case R.id.home:
                this.onBackPressed();
                return true;

            case R.id.set_wallpaper:
                setWallpaper();
                return true;

            case R.id.share_photo:
                sharePhoto();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    private void initViews(){

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        detailImageView = (ImageView) findViewById(R.id.detailImageView);
        //Add pan and zoom functionality to detailImageView
        attacher = new PhotoViewAttacher(detailImageView);

        authorImageView = (CircularImageView) findViewById(R.id.authorImageView);
        authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

    }

    private void manageClicks(){

        //DetailImageView click
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                setDarkUi();

            }

        });

        //toolbar click
        toolbar.setOnClickListener(new View.OnClickListener() {
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

        //Make dominant color Darker
        hsv = new float[3];
        Color.colorToHSV(darkDominantColor, hsv);
        hsv[2] *= 0.7; //change this to change brightness
        int darkDarkDominantColor = Color.HSVToColor(hsv);


        //Set color to various elements
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkDarkDominantColor);
        window.setNavigationBarColor(darkDarkDominantColor);

        toolbar.setBackgroundColor(darkDominantColor);

        //Set color to the overflow icon
        overflowIcon = toolbar.getOverflowIcon();
        overflowIcon = DrawableCompat.wrap(overflowIcon);
        DrawableCompat.setTint(overflowIcon.mutate(), brightDominantColor);
        toolbar.setOverflowIcon(overflowIcon);

        frameLayout.setBackgroundColor(dominantColor);
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
            toolbar.setVisibility(View.INVISIBLE);

        } else {

            //If is the second time the imageview is touched, restore the bars and the previous colors
            isFirstTouch = true;

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            frameLayout.setBackgroundColor(dominantColor);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private void setWallpaper(){
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                try {
                    myWallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this).load(imageObject.getImageRegularUrl()).into(target);

    }

    private void sharePhoto(){
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                try {

                    //Save the image locally
                    File file = new File(DetailActivity.this.getCacheDir(), "photo" + ".png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);

                    //Share the photo
                    Uri uri = Uri.fromFile(file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, "Share this photo"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this).load(imageObject.getImageRegularUrl()).into(target);

    }

}
