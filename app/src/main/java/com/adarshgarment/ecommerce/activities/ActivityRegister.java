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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActivityRegister extends AppCompatActivity {

    TextView alreadyhaveAccount,editPhone;
    EditText inputEmail, inputPassword, inputConfirmPassword,phoneNo,userName,firstName,middleName;
    Button btnRegister;
    String emailPattern= "[a-zA-Z0-9._-]+[a-z]+\\.+[a-z]";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alreadyhaveAccount =findViewById(R.id.add);
        inputEmail=findViewById(R.id.Product_title);
        inputPassword=findViewById(R.id.Enter_password);
        inputConfirmPassword=findViewById(R.id.input_Confirm_password);
        btnRegister=findViewById(R.id.btnRegister);
        phoneNo=findViewById(R.id.editPhone);
        userName=findViewById(R.id.editUsername);
        firstName=findViewById(R.id.editFirstName);
        middleName=findViewById(R.id.editMiddleName);
        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        //hooks


            if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        alreadyhaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityRegister.this, ActivityLogin.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();

            }
        });
        

    }

    private void performAuth() {
        String email=inputEmail.getText().toString();
        String  password=inputPassword.getText().toString();
        String confirmPassword =inputConfirmPassword.getText().toString();
        String _phone=phoneNo.getText().toString();


        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter Correct Email");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Enter correct password format");
          }else if(!password.equals(confirmPassword)){
           inputPassword.setError("Password does not match");
         }else if (_phone.matches("")){
            Toast.makeText(this, "You did not enter a Phone number", Toast.LENGTH_SHORT).show();
        }

        // Getting users data from firebase database
        Query checkuser = FirebaseDatabase.getInstance().getReference("users").orderByChild("uid").equalTo(mAuth.getUid());
        String uid1 = mAuth.getUid();
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // storeUserData();
                if(snapshot.exists()){
                    //  final_phone.seterror
                    //  final_phone.setErrorEnabled(false);
                    String firstName = snapshot.child(uid1).child("First Name").getValue(String.class);
                    String middleName = snapshot.child(uid1).child("Middle Name").getValue(String.class);
                    String username = snapshot.child(uid1).child("username").getValue(String.class);
                    String email = snapshot.child(uid1).child("Email").getValue(String.class);
                    String phone = snapshot.child(uid1).child("Phone").getValue(String.class);

                    Toast.makeText(ActivityRegister.this, firstName+"\n"+middleName+"\n"+username+"\n"+email+"\n"+phone, Toast.LENGTH_SHORT).show();

                    //Create Session

                    sessionManager sessionManager = new sessionManager(ActivityRegister.this);

                    sessionManager.createLoginSession(username, firstName, middleName,email,phone);

                    // startActivity(new Intent(getApplicationContext(), FragmentProfile.class));

                }

                else {
                    progressDialog.dismiss();
                    Toast.makeText( ActivityRegister.this, "Data does not exist",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        progressDialog.setMessage("Please Wait while Registration....");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    storeUserData();
                    sendUserToNextActivity();
                    Toast.makeText(ActivityRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ActivityRegister.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    private void storeUserData() {

        String username = userName.getText().toString();
        String firstname = firstName.getText().toString();
        String middlename = middleName.getText().toString();
        String phone = phoneNo.getText().toString();
        String email= inputEmail.getText().toString();
        String uid = mAuth.getUid();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        HashMap hashMap=new HashMap();
        hashMap.put("username",username);
        hashMap.put("First Name",firstname);
        hashMap.put("Middle Name",middlename);
        hashMap.put("Phone", phone);
        hashMap.put("Email", email);
        hashMap.put("Uid", uid);

       // UserHelperClass addNewUser = new UserHelperClass( username,firstname,middlename, phone);

        reference.child(uid).setValue(hashMap);


    }
    private void sendUserToNextActivity() {
        Intent intent=new Intent(ActivityRegister.this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}