package com.adarshgarment.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.adarshgarment.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter2  extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>{

    ArrayList<String> urls;
    Context context;


    //constructor
    public RecyclerAdapter2(ArrayList<String> ImgUrl, Context context_)
    {
        this.urls = ImgUrl;
        this.context = context_;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ViewHolder(View v)
        {
            super(v);
            image = v.findViewById(R.id.imageld2);
        }

        public ImageView getImage(){ return this.image;}
    }

    @Override
    public RecyclerAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_image2, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(1080,800));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
//        Glide.with(this.context)
//                .load(urls.get(position))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.getImage());


        Picasso
                .with(this.context)
                .load(urls.get(position))
                .placeholder(R.drawable.ic_loading)
                .resize(1200,1100)
                .centerInside()
                .into(holder.getImage());
    }


    @Override
    public int getItemCount()
    {
        return urls.size();
    }

}
