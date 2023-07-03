package com.adarshgarment.ecommerce.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adarshgarment.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPassword extends AppCompatActivity {
    EditText inputMailForgot;
    Button forgotButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputMailForgot = findViewById(R.id.inputMailforgot);
        forgotButton = findViewById(R.id.forgot);
        auth = FirebaseAuth.getInstance();

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputMailForgot.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(ActivityForgotPassword.this,"Please enter your Email", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ActivityForgotPassword.this, "Please check your mail for password Reset", Toast.LENGTH_LONG).show();
                                inputMailForgot.setText("Reset link sent!");
                            } else
                            {
                               Toast.makeText(ActivityForgotPassword.this, "Error occured please try Again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
}

}