package com.example.complaintt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private EditText Email;
    private EditText Phone;
    private CheckBox RegisterAdmin;
    private CheckBox RegisterUser;
    private Button CreateAccount;
    private Button Login;


    boolean valid = true;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Username = findViewById(R.id.registerName);
        Password = findViewById(R.id.registerPassword);
        Email = findViewById(R.id.registerEmail);
        Phone = findViewById(R.id.registerPhone);
        RegisterAdmin = findViewById(R.id.isAdmin);
        RegisterUser = findViewById(R.id.isUser);
        CreateAccount = findViewById(R.id.RegisterBtn);
        Login = findViewById(R.id.gotoLogin);


        RegisterUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    RegisterAdmin.setChecked(false);
                }
            }
        });

     RegisterAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (buttonView.isChecked()) {
                 RegisterUser.setChecked(false);
             }
         }
         });





        CreateAccount.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkField(Username);
            checkField(Password);
            checkField(Email);
            checkField(Phone);

            if (valid) {
                fAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();

                        Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                        DocumentReference df = fStore.collection("Users").document(user.getEmail());
                        DocumentReference da = fStore.collection("Admin").document(user.getEmail());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Username", Username.getText().toString());
                        userInfo.put("Email", Email.getText().toString());
                        userInfo.put("Phone", Phone.getText().toString());


                        if(RegisterAdmin.isChecked()){

                            userInfo.put("isAdmin", "1");
                            da.set(userInfo);

                            startActivity(new Intent(getApplicationContext(), login.class));
                            finish();
                        }

                        if(RegisterUser.isChecked()){

                            userInfo.put("isUser", "1");
                            df.set(userInfo);

                            startActivity(new Intent(getApplicationContext(), login.class));
                            finish();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    });
        Login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Register.this, login.class));
        }
    });





}



    public void checkField(EditText textField){
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
    }

}