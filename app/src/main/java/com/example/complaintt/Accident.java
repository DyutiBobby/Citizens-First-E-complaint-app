package com.example.complaintt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Accident extends AppCompatActivity {

    EditText street,village,pincode,model,details,accused;
    private TextView Accident ,DateTime;
    public  Spinner s1;
    Button bt;
    int position2;
    String pAbd="9370394253",pDhu="9322670429",pNas="9307067287",pPne= "8080245350",pMum="9420676951";
    boolean valid= true;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String [] district_array={"/-district-/","Aurangabad,Maharashtra","Dhule,Maharashtra","Nashik,Maharashtra","Pune,Maharashtra","Mumbai,Mharashtra"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        s1= (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter adapter= new ArrayAdapter(Accident.this, android.R.layout.simple_spinner_item,district_array);
        s1.setAdapter(adapter);



        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        street = findViewById(R.id.editTextStreetAdd);
        village = findViewById(R.id.editTextCityorVillage);
        pincode = findViewById(R.id.editTextPincode);
        model = findViewById(R.id.editTextModelNo_);
        details = findViewById(R.id.editTextDetails);
        accused = findViewById(R.id.editTextTextPersonName);
        Accident = findViewById(R.id.accident);
        DateTime = findViewById(R.id.editTextDateandTime);
        bt = findViewById(R.id.button2);


        Bundle extras = getIntent().getExtras();
        DateTime.setText(getIntent().getExtras().getString("datetime_key"));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position2 = s1.getSelectedItemPosition();

                Intent intent = new Intent(com.example.complaintt.Accident.this,Notification.class);

                if(position2==1){
                    intent.putExtra("phone", pAbd);
                    startActivity(intent);
                }
                else if(position2==2){
                    intent.putExtra("phone", pDhu);
                    startActivity(intent);
                }
                else if(position2==3){
                    intent.putExtra("phone", pNas);
                    startActivity(intent);
                }
                else if(position2==4){
                    intent.putExtra("phone", pPne);
                    startActivity(intent);
                }
                else if(position2==5){
                    intent.putExtra("phone", pMum);
                    startActivity(intent);
                }
                else{
                        Toast.makeText(Accident.this, "Select District", Toast.LENGTH_SHORT).show();
                }

                checkField(street);
                checkField(village);
                checkField(pincode);
                checkField(model);
                checkField(details);


                FirebaseUser user = fAuth.getCurrentUser();
                DocumentReference df = fStore.collection("Users").document(user.getEmail());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Category","Accident");
                userInfo.put("Date and Time of Incident",DateTime.getText().toString());
                userInfo.put("Street address",street.getText().toString());
                userInfo.put("Village or city",village.getText().toString());
                userInfo.put("District ",s1.getSelectedItem().toString());
                userInfo.put("Pincode",pincode.getText().toString());
                userInfo.put("Model name and vehicle no of involved Vehicle",model.getText().toString());
                userInfo.put("Describe the incident",details.getText().toString());
                userInfo.put("Name of accused person",accused.getText().toString());;
                userInfo.put("Item Stolen", FieldValue.delete());
                userInfo.put("Details of Theft",FieldValue.delete());
                userInfo.put("Worth of stolen Item",FieldValue.delete());
                df.update(userInfo);

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