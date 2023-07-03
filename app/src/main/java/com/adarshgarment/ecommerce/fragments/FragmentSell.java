package com.adarshgarment.ecommerce.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adarshgarment.ecommerce.Config;
import com.adarshgarment.ecommerce.R;
import com.adarshgarment.ecommerce.activities.ModalClass;
import com.adarshgarment.ecommerce.activities.constants;
import com.adarshgarment.ecommerce.activities.sessionManager;
import com.adarshgarment.ecommerce.adapters.RecyclerAdapter;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//sessionManager sessionManager = new sessionManager(Context,Context.MODE_PRIVATE);


public class FragmentSell extends Fragment {

    ////////////////////////////////////////
    private EditText product_title, price, select_category, prd_description, quantity;
    private ImageButton backBtn;
    private ImageView addingImage;
    private ImageView addingImage1;
    private TextView addingImages, textView;
    LinearLayout gallery;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //Image pick Constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //working upload
    private static final int Read_Permission = 101;
    private static final int CODE = 100;

    ArrayList<Uri> uri  = new ArrayList<>();
    ArrayList<Uri> imageUri  = new ArrayList<>();
    RecyclerAdapter adapter;
    StringBuilder sb = new StringBuilder();
    String img[];
    // Button pick;

    //permission Arrays
    private  String[] cameraPermissions;
    private  String[] storagePermissions;

    //Image pick Uri
    private Uri image_uri;

    private int uploads = 0;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    ///////////////////////////////////////

    private sessionManager sessionManager;
    TextView txt_user_name;
    TextView txt_user_email;
    TextView txt_user_phone;
    TextView txt_user_address;
    MaterialRippleLayout btn_edit_user;
    MaterialRippleLayout btn_order_history, btn_rate, btn_share, btn_privacy;
    LinearLayout lyt_root;

    /////////////////////////
    ////////////////////////

    List<ModalClass> modalClassList;
    RecyclerView recyclerView;
    private StorageReference mStorageRef;
    private static final int IMAGE_CODE = 1;
    //////////////////////
    ////////////////////

    String uniqueID = UUID.randomUUID().toString();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
//        gallery = view.findViewById(R.id.Adding_Image1);
        sessionManager = new sessionManager(getActivity());

        //from working upload
        textView = view.findViewById(R.id.totalPhotos);
        recyclerView = view.findViewById(R.id.recyclerview_Gallery);
        ImageView pick = view.findViewById(R.id.Adding_Image);
        Button addBtn = view.findViewById(R.id.addBtn);
        adapter = new RecyclerAdapter(uri);

        //  recyclerView.setAdapter(adapter);

        lyt_root = view.findViewById(R.id.lyt_root);
        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                lyt_root.setRotationY(180);
            }
        }


/// My Text view input fields
        backBtn= view.findViewById(R.id.backBtn);
        product_title = view.findViewById(R.id.product_title);
        price = view.findViewById(R.id.price);
        select_category = view.findViewById(R.id.select_category);
        prd_description = view.findViewById(R.id.prd_description);
        quantity = view.findViewById(R.id.quantity);
        addingImages =view.findViewById(R.id.Adding_Images);
        addingImage =view.findViewById(R.id.Adding_Image);
//        addingImage1 =view.findViewById(R.id.Adding_Image1);

        firebaseAuth = FirebaseAuth.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);


        //Init permission arrays
        cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog tp pick image
               showImagePickDialog();
            }
        });

        select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pick category
                categoryDialog();


            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Flow:
                //1) Input data
                //2) Validate data
                //3) Add data to db

                inputData();
                //addProduct();
            }
        });


        return view;
    }

    private String productTitle, productCategory, productDescription, productQuantity, productPrice;

    private void inputData() {
//        String productTitle;
//        String productCategory;
//        String productDescription;
//        String productQuantity;
//       String productPrice;

        System.out.print("I should have checked input data first");
        //1) Input data
//        productTitle = product_title.getText().toString().trim();
//        productCategory = select_category.getText().toString().trim();
//        productDescription = prd_description.getText().toString().trim();
//        productQuantity = quantity.getText().toString().trim();
//        productPrice = price.getText().toString().trim();

        productTitle = product_title.getText().toString().trim();
        productCategory = select_category.getText().toString().trim();
        productDescription = prd_description.getText().toString().trim();
        productQuantity = quantity.getText().toString().trim();
        productPrice = price.getText().toString().trim();

        //2) Validate data

        if(TextUtils.isEmpty(productTitle)){
            Toast.makeText(getActivity(), "Title is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(getActivity(), "Category is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(getActivity(), "Description is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(productQuantity)){
            Toast.makeText(getActivity(), "Quantity is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }
        if(TextUtils.isEmpty(productCategory)){
            Toast.makeText(getActivity(), "Price is required...", Toast.LENGTH_SHORT).show();
            return; // don't proceed further
        }

        //add data to database

        addProduct();

    }

    //handle image pick results
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){

            //image picked from Gallery

           // save picked image uri
              //image_uri = data.getData();

          //  imported Statements
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                 //   String imageName = getFileName(imageUri);

                    uri.add(data.getClipData().getItemAt(i).getUri());
                    adapter.notifyDataSetChanged();
                    textView.setText("Photos("+uri.size()+")");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
//                    FirebaseStorage mStorage = FirebaseStorage.getInstance();
//                    StorageReference mStorageRef = mStorage.getInstance().getReference().child("image").child(imageName);
//                    //StorageReference mRef = mStorage.getReferenceFromUrl("image");
//                    //StorageReference mRef = mStorageRef.child("image");//.child(imageName);
//
//                    mStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getActivity(), "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });

                }

            } else if (data.getData() != null) {
                // Toast.makeText(getActivity(), "single", Toast.LENGTH_SHORT).show();
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));

            }

          // Imported Statements

           // set image
        }

        else if(requestCode == IMAGE_PICK_CAMERA_CODE){
            //image picked from camera
            addingImage1.setImageURI(image_uri);
        }

    }



    private void addProduct() {
        //3 add to database
        System.out.print("I want to test if my addproduct method is being called ");
//        progressDialog.setTitle("Adding Product...");
//        progressDialog.show(1);

        Long timestamp = System.currentTimeMillis();

        if (uri == null){
            //upload without image

            //setup data for upload
            HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("productId", ""+ timestamp);
                hashMap.put("product Title", ""+ productTitle);
                hashMap.put("product Category", ""+ productCategory);
                hashMap.put("product Description", ""+ productDescription);
                hashMap.put("product Quantity", Integer.parseInt(productQuantity));
                hashMap.put("Price", Integer.parseInt(productPrice));
                hashMap.put("timestamp", timestamp);
                hashMap.put("uid", " "+ firebaseAuth.getUid());
                hashMap.put("productIcon", ""+ "imagelink");

            // Add to db
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getInstance().getReference( "Users1");
            reference.child(firebaseAuth.getUid()).child(timestamp.toString()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            //added to database
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Product added", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed adding to db
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{

            final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("ImageFolder");
            for ( uploads=0; uploads < uri.size(); uploads++) {
                Uri Image  = uri.get(uploads);
                imageUri.add(Image);
                int y = imageUri.size();

                final StorageReference imageStore = ImageFolder.child(firebaseAuth.getUid()).child(timestamp.toString()).child("Img "+ (Image.getLastPathSegment()).toString().trim());

                imageStore.putFile(uri.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Toast.makeText(getActivity(), "Image added", Toast.LENGTH_SHORT).show();
                                String url = uri + System.getProperty("line.separator");
                                receiveLink2(url, y);

                            }
                        });

                    }

                    private void receiveLink2(String url, int Y) {
                        sb.append(url);

                        Log.d("uri size", String.valueOf(uri.size()));
                        Log.d("uploads count", String.valueOf(Y));

                        if(uri.size() == Y){
                            String sb1 = sb.toString();
                           addProduct1(sb1);


                        }
                   }
                });

            }

        }

    }

//    private void receiveLink(String sb1) {
//
//        sb.append(url);
//        Log.d("uri size", String.valueOf(uri.size()));
//        Log.d("SB Length", String.valueOf(sb.length()));
//
//
//        if(uri.size() == sb.length()){
//            String sb1 = sb.toString();
//           addProduct1(sb1);
//        }
//    }

    private void addProduct1(String sb1) {

        Long timestamp = System.currentTimeMillis();

        //setup data for upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("productId",  timestamp);
        hashMap.put("productTitle", ""+ productTitle);
        hashMap.put("productCategory", ""+ productCategory);
        hashMap.put("productDescription", ""+ productDescription);
        hashMap.put("productQuantity", Integer.parseInt(productQuantity));
        hashMap.put("Price", Integer.parseInt(productPrice));
        hashMap.put("timestamp", timestamp);
        hashMap.put("Links", " "+ sb1);
        hashMap.put("productIcon", " "+ sb1);
        hashMap.put("uid", ""+ firebaseAuth.getUid());

        // Add to db

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getInstance().getReference( "Users1");
        reference.child(firebaseAuth.getUid()).child(timestamp.toString()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //added to database
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Product added", Toast.LENGTH_SHORT).show();
                        clearData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding to db
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        clearData();

    }

    private void clearData() {
        //clear data after uploading product
        product_title.setText("");
        prd_description.setText("");
        select_category.setText("");
        quantity.setText("");
        price.setText("");
        addingImage.setImageResource(R.drawable.addproduct_foreground);
        image_uri = null;
       // uri.clear();
    }

    private void categoryDialog() {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("product Category")
                .setItems(constants.productCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = constants.productCategories[which];

                    }
                })
                .show();
    }

    private void showImagePickDialog1(){
        AlertDialog alertDialog = new AlertDialog.Builder((getActivity())).create();
        alertDialog.setTitle("Testing Dialoge  Alert");
        alertDialog.setMessage("I hope it has worked");
        alertDialog.setButton(alertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item Clicks
                        if(which==0){
                            //camera clicked
                            if(checkCameraPermission()){
                                //permission granted
                                pickFromCamera();
                            }
                            else{
                                //permission not granted, request
                                requestCameraPermission();
                            }
                        }
                        else{
                            //Gallery Clicked
                            if(checkStoragePermission()){
                                //permission granted
                                pickFromGallery();

                            }
                            else{
                                //permission not granted, request
                                requestStoragePermission();

                            }
                        }
                    }
                });
        builder.show();

    }

    private void pickFromGallery() {
        //intent to pick image from gallery
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //startActivityForResult(intent, IMAGE_CODE);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
    }

    private void pickFromCamera(){
        //intent to pick image from camera

        //using media store to pick high/original quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Images_Title");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result; //returns true/false
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, STORAGE_REQUEST_CODE);
    }

    // handle permission results
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        //both permissions granted
                        pickFromCamera();
                    }
                    else{
                        // both or one of the permissions denied
                        Toast.makeText(getActivity(), "camera & Storage Permission are required...", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else {
                        //permission denied
                        Toast.makeText(getActivity(), "Storage Permission is required...", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}


