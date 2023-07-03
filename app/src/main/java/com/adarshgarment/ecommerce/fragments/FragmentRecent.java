package com.adarshgarment.ecommerce.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adarshgarment.ecommerce.Config;
import com.adarshgarment.ecommerce.R;
import com.adarshgarment.ecommerce.activities.ActivityProductDetail;
import com.adarshgarment.ecommerce.adapters.RecyclerAdapterProduct;
import com.adarshgarment.ecommerce.models.Product;
import com.adarshgarment.ecommerce.utilities.ItemOffsetDecoration;
import com.adarshgarment.ecommerce.utilities.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import static com.adarshgarment.ecommerce.utilities.Constant.GET_RECENT_PRODUCT;

public class FragmentRecent extends Fragment implements RecyclerAdapterProduct.ContactsAdapterListener {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private RecyclerAdapterProduct mAdapter;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout = null;
    LinearLayout lyt_root;
    DatabaseReference database;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        setHasOptionsMenu(true);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        lyt_root = view.findViewById(R.id.lyt_root);
        if (Config.ENABLE_RTL_MODE) {
            lyt_root.setRotationY(180);
        }
//        recyclerView = view.findViewById(R.id.recycler_view);
//        mAdapter = new RecyclerAdapterProduct(getActivity(), productList, this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
//        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);



        // checking to see if there is still need for  running the methods below
        onRefresh();
        fetchData();

        return view;
    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(getActivity())) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }
//this is the initial fetchdata function working with PHP
//    private void fetchData() {
//
//        JsonArrayRequest request = new JsonArrayRequest(GET_RECENT_PRODUCT, new Response.Listener<JSONArray>() {
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
//                        }.getType());
//
//                        // adding contacts to contacts list
//                        productList.clear();
//                        productList.addAll(items);
//
//                        // refreshing recycler view
//                        mAdapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e("INFO", "Error: " + error.getMessage());
//               // Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }

    private void fetchData(){

        //I insert the firebase database snapshot instead f php

        database = FirebaseDatabase.getInstance().getReference("Users1");
        //productList = new ArrayList<>();

        //  I fit here my access to database instead of using Json array results from php
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList = new ArrayList<Product>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot dsnapshot : dataSnapshot.getChildren()){
                        Product product = dsnapshot.getValue(Product.class);

                        productList.add(product);

                        System.out.print("An attempt the size of the Array " + productList.size()+" " + '\n');
                        String[] imgLink = product.getLinks().split("\n");
                        System.out.print("An attempt the size of the Image Array " + imgLink[0] + '\n');

                        // refreshing recycler view
                        //mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                //created a function to send the List with the product details outside the loop to access outer methods and
                //the product list to the recyclerAdapter
                fetchData2((ArrayList<Product>) productList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private ArrayList<Product> fetchData2(ArrayList<Product> productList) {
        //System.out.print(productList.toString());
        mAdapter = new RecyclerAdapterProduct(getActivity(), productList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //System.out.print("An attempt to print price after export" + product.getPrice());
        System.out.print("Checking prdlist after exporting it " + productList.size() + " " + '\n');

        // refreshing recycler view
        // mAdapter.notifyDataSetChanged(); // adding notifyDataSetChanged causes the app to crush due to null object error
        swipeRefreshLayout.setRefreshing(false);



        return productList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onContactSelected(Product product) {
        Intent intent = new Intent(getActivity(), ActivityProductDetail.class);
        intent.putExtra("product_id", product.getProductId());
        intent.putExtra("title", product.getProductTitle());
        intent.putExtra("image", product.getLinks());
        intent.putExtra("product_price", product.getPrice());
        intent.putExtra("product_description", product.getProductDescription());
        intent.putExtra("product_quantity", product.getProductQuantity());
        intent.putExtra("product_Category", product.getProductCategory());
        intent.putExtra("product_Icon", product.getLinks());
        intent.putExtra("product_timeStamp", product.gettimestamp());
        intent.putExtra("product_UID", product.getuid());
        // intent.putExtra("product_status", product.getUserId());
        // intent.putExtra("currency_code", product.getCurrency_code());
        //  intent.putExtra("category_name", product.getCategory_name());
        startActivity(intent);
    }
}