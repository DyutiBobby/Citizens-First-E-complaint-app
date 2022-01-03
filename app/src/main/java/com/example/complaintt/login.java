package com.example.complaintt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button CreateAccount;
    private Button Login1;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        CreateAccount = findViewById(R.id.CreateAccount);
        Login1 = findViewById(R.id.Loginbtn);
        Login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(Email);
                checkField(Password);
                Log.d("TAG","onClick" + Email.getText().toString());

                if (valid) {
                    fAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getEmail());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,Register.class));
            }
        });

    }

    private void checkUserAccessLevel(String Email) {
        DocumentReference df = fStore.collection("Users").document(Email);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess:" + documentSnapshot.getData());


                if(documentSnapshot.getString("isUser") != null){
                    startActivity( new Intent(login.this, Profile.class));
                    finish();
                }

                else{
                    startActivity( new Intent(login.this, AdminActivity.class));
                    finish();
                }
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
