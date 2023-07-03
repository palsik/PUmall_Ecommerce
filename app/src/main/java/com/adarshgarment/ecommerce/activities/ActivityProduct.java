package com.adarshgarment.ecommerce.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.adarshgarment.ecommerce.Config;
import com.adarshgarment.ecommerce.R;
import com.adarshgarment.ecommerce.adapters.RecyclerAdapterProduct;
import com.adarshgarment.ecommerce.models.Product;
import com.adarshgarment.ecommerce.utilities.ItemOffsetDecoration;
import com.adarshgarment.ecommerce.utilities.Utils;
//import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

//import static com.adarshgarment.ecommerce.utilities.Constant.GET_CATEGORY_DETAIL;

public class ActivityProduct extends AppCompatActivity implements RecyclerAdapterProduct.ContactsAdapterListener {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private RecyclerAdapterProduct mAdapter;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private String category_id, category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        category_name = intent.getStringExtra("category_name");

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category_name);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new RecyclerAdapterProduct(this, productList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //fetchData();
        onRefresh();

    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(ActivityProduct.this)) {
                            swipeRefreshLayout.setRefreshing(false);
                            //fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }

//    private void fetchData() {
//        JsonArrayRequest request = new JsonArrayRequest(GET_CATEGORY_DETAIL + category_id, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
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
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e("INFO", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onContactSelected(Product product) {
        Intent intent = new Intent(getApplicationContext(), ActivityProductDetail.class);
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
