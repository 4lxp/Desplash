package com.example.alex.abstruct;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ImageClass> imageArrayList;
    private Context context;

    public RecyclerViewAdapter(Context context,ArrayList<ImageClass> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;

    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {


        Picasso.with(context)
                .load(imageArrayList.get(i).getImageSmallUrl())
                .fit().centerCrop()
                .into(viewHolder.rowImageView);

        viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);

                //Send url of the image clicked
                intent.putExtra("imageObject", imageArrayList.get(viewHolder.getAdapterPosition())); //I can pass the obkect with the intent because the class implement serializable
                v.getContext().startActivity(intent);

                Log.d("posizione",String.valueOf(viewHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rowImageView;

        public ViewHolder(View view) {
            super(view);

            rowImageView = (ImageView)view.findViewById(R.id.rowImageView);

        }
    }
}