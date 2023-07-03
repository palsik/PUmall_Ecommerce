package com.adarshgarment.ecommerce.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adarshgarment.ecommerce.R;
import com.adarshgarment.ecommerce.adapters.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.security.ProtectionDomain;
import java.util.ArrayList;

public class ImageListing extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView;
    Button pick;

    ArrayList<Uri> uri  = new ArrayList<>();
    RecyclerAdapter adapter;

    private static final int Read_Permission = 101;
    private static final int CODE = 100;

    @Override

    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.recy_test);

        textView = findViewById(R.id.totalPhotos);
        recyclerView = findViewById(R.id.recyclerview_Gallery);
        pick = findViewById(R.id.Pick);

        adapter = new RecyclerAdapter(uri);
        //recyclerView.setLayoutManager(new GridLayoutManager(ImageListing.this,4));
        recyclerView.setLayoutManager(new LinearLayoutManager(ImageListing.this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView.setAdapter(adapter);
            //Asking permission from the user
        if(ContextCompat.checkSelfPermission(ImageListing.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){

        ActivityCompat.requestPermissions(ImageListing.this,
        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);

        }else askForPermission();


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);

            }

        });

     }





    private void askForPermission() {

        ActivityCompat.requestPermissions(ImageListing.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CODE);

    }
    //overide this method

    public  void onRequestPermissionResult(int requestCode, @Nullable String[] permissions, @Nullable int[] grantResults){

        if (CODE == requestCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else {
                Toast.makeText(ImageListing.this, "Please provide permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data.getClipData() !=null){
                int x = data.getClipData().getItemCount();

                for(int i=0; i<x; i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();
                textView.setText("Photos("+uri.size()+")");
            }else if(data.getData() !=null){
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));
            }
        }
    }
}
