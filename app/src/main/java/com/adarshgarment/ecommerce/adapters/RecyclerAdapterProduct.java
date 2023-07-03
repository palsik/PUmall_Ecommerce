package com.adarshgarment.ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.adarshgarment.ecommerce.Config;
import com.adarshgarment.ecommerce.R;
import com.adarshgarment.ecommerce.models.Product;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RecyclerAdapterProduct extends RecyclerView.Adapter<RecyclerAdapterProduct.MyViewHolder> implements Filterable {

    private Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;
    private ContactsAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, product_price;
        public ImageView product_image;

        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            product_image = view.findViewById(R.id.category_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                    listener.onContactSelected(productListFiltered.get(getBindingAdapterPosition()));
                }
            });
        }
    }

    public RecyclerAdapterProduct(Context context, List<Product> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = productListFiltered.get(position);
        holder.product_name.setText(product.getProductTitle());

        if (Config.ENABLE_DECIMAL_ROUNDING) {
            String price = String.format(Locale.GERMAN, "%1$,.0f", product.getPrice());
           // holder.product_price.setText(price + " " + product.getCurrency_code());
            holder.product_price.setText(price);
        } else {
           // holder.product_price.setText(product.getProductPrice() + " " + product.getCurrency_code());
            String price = String.format(Locale.GERMAN, "%1$,.0f", product.getPrice());
        }
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(10)
                .oval(false)
                .build();
//  this used image loaded from php
//        Picasso.with(context)
//                .load(Config.ADMIN_PANEL_URL + "/upload/product/" + product.getLinks())
//                .placeholder(R.drawable.ic_loading)
//                .resize(250, 250)
//                .centerCrop()
//                .transform(transformation)
//                .into(holder.product_image);
//        System.out.print("Print the first image link " + Arrays.toString(product.getLinks().split(System.lineSeparator(), 1)));
//        Picasso.with(context)
//                .load(Arrays.toString(product.getLinks().split(System.lineSeparator(), 1)))
//                .placeholder(R.drawable.ic_loading)
//                .resize(250, 250)
//                .centerCrop()
//                .transform(transformation)
//                .into(holder.product_image);

        //System.out.print("Print the first image link " + Arrays.toString(product.getLinks().split(System.lineSeparator(), 1)));
        Picasso.with(this.context).cancelRequest(holder.product_image);
        String[] imgLink = product.getLinks().split("\n");
        //imgLink.clear();
        String link =  imgLink[0].trim();
        Log.d("Printing Here the loading Image Link ", link + '\n');
       // String link = "https://firebasestorage.googleapis.com/v0/b/mkoroshoni-e5350.appspot.com/o/User%2FV8zIbZHIKZccp3J4PjLBAkXPFqF3%2FProduct%2Fimage%3A378049?alt=media&token=1c4d64b9-70c1-4361-809e-f1099501eb4e";
        Picasso.with(this.context)
                .load(link)
                .placeholder(R.drawable.ic_loading)
                .resize(320, 320)
                .centerInside()
                .transform(transformation)
                .into(holder.product_image);
    }

//    @Override
//    public int getItemCount() {
//        return 0;
//    }

     @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {
                        if (row.getProductTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Product product);
    }
}
