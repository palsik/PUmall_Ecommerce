package com.adarshgarment.ecommerce.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adarshgarment.ecommerce.Config;
import com.adarshgarment.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {

    TextView register, forgotPassword;
    EditText inputEmail, inputPassword, enterphone;
    Button btnLogin;
    String emailPattern= "[a-zA-Z0-9._-]+[a-z]+\\.+[a-z]";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override

    protected void  onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_login);
        forgotPassword = findViewById(R.id.Forgot_Password);
        register=findViewById(R.id.add);
        inputEmail=findViewById(R.id.Product_title);
        inputPassword=findViewById(R.id.Enter_password);
        enterphone=findViewById(R.id.enterPhone);
        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        btnLogin=findViewById(R.id.btnLogin);

        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this, ActivityRegister.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performAuth();


            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));

            }
        });

    }

    private void performAuth() {

        String email=inputEmail.getText().toString();
        String  password=inputPassword.getText().toString();
        String _phone=enterphone.getText().toString();
              if(!email.matches(emailPattern)){
            inputEmail.setError("Enter Correct Email");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Enter correct password format");
          }else if(_phone.charAt(0)=='0'){
            _phone = _phone.substring(1);
        }

        progressDialog.setMessage("We are login you in....");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() ){
                    progressDialog.dismiss();
                    checkProfile();
                    Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    sendUserToNextActivity();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ActivityLogin.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void checkProfile(){
        // Getting users data from firebase database
        Query checkuser = FirebaseDatabase.getInstance().getReference("users").orderByChild(String.valueOf(mAuth.getUid()));
        String uid = String.valueOf(mAuth.getUid());
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //final_phone.seterror
                    //final_phone.setErrorEnabled(false);
                    String firstName = snapshot.child(uid).child("First Name").getValue(String.class);
                    String middleName = snapshot.child(uid).child("Middle Name").getValue(String.class);
                    String username = snapshot.child(uid).child("username").getValue(String.class);
                    String email = snapshot.child(uid).child("Email").getValue(String.class);
                    String final_phone= snapshot.child(uid).child("Phone").getValue(String.class);;

                    Toast.makeText(ActivityLogin.this, firstName+"\n"+middleName+"\n"+username+"\n"+email, Toast.LENGTH_SHORT).show();

                    //Create Session

                    sessionManager sessionManager = new sessionManager(ActivityLogin.this);
                    sessionManager.createLoginSession(username, firstName, middleName,email,final_phone);

                    // startActivity(new Intent(getApplicationContext(), FragmentProfile.class));

                }

                else {
                    progressDialog.dismiss();
                    Toast.makeText( ActivityLogin.this, "Data does not exist",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(ActivityLogin.this, MainActivity.class);
       // Intent intent=new Intent(ActivityLogin.this, ImageListing.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


