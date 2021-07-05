package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullname, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullname = findViewById(R.id.Fullname);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mPhone = findViewById(R.id.Number);
        mRegisterBtn = findViewById(R.id.Register);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputdata();
            }

        });


    }
    private String email, fullname, password,phone;
    private void inputdata() {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        fullname = mFullname.getText().toString().trim();
        phone = mPhone.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Please enter Email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            mPassword.setError("Please enter a password");
            return;
        }

        if(password.length()<4){
            mPassword.setError("Password must be atleast 4 characters long");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // register the user in firebase
        createAccount();
    }

    private void createAccount() {
        fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saverFirebaseData();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,"Failed to register!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saverFirebaseData() {
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("email", ""+email);
        hm.put("fullname", ""+fullname);
        hm.put("password", ""+password);
        hm.put("phone", ""+phone);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(fAuth.getUid()).setValue(hm)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "User Registered.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,Dashboard1.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
