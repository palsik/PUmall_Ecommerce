package com.adarshgarment.ecommerce.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.adarshgarment.ecommerce.R;

import com.squareup.picasso.Picasso;

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;

    ImageView img_product_image;
    View view;

    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.activity_product_detail, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
        Activity rootview = new Activity();
        //img_product_image = findViewById(R.id.product_image1);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
          //  convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }

        Picasso
                .with(context)
                .load(imageUrls[position])
                .fit() // will explain later
                .into((img_product_image));

        return convertView;
    }
}