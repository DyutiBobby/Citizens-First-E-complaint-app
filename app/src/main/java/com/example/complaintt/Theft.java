package com.example.complaintt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.complaintt.R.id.harassment;

public class Theft extends AppCompatActivity {

    EditText Street1, Village1, Pincode1,Item,Worth, Details1, Accused1;
    private TextView Theft,date_time;
    Spinner s4 ;
    Button bt4;
    int position4;
    String pAbd="9370394253",pDhu="9322670429",pNas="9307067287",pPne= "8080245350",pMum="9420676951";
    boolean valid= true;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String [] district_array3={"/-district-/","Aurangabad,Maharashtra","Dhule,Maharashtra","Nashik,Maharashtra","Pune,Maharashtra","Mumbai,Mharashtra"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft);

        s4 = (Spinner) findViewById(R.id.spinnerTheft);

        ArrayAdapter adapter= new ArrayAdapter(Theft.this, android.R.layout.simple_spinner_item,district_array3);
        s4.setAdapter(adapter);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        Street1 = findViewById(R.id.editTextTheftStreet);
        Village1 = findViewById(R.id.editTextTheftVillage);
        Pincode1 = findViewById(R.id.editTextTheftPincode);
        Accused1 = findViewById(R.id.editTextTheftPersonName);
        Details1 = findViewById(R.id.editTextTheftDetails);
        Theft = findViewById(R.id.textViewTheft);
        date_time = findViewById(R.id.editTextTheftDate_Time);
        Item = findViewById(R.id.editTextTheftItems);
        Worth = findViewById(R.id.editTextWorth);
        bt4=findViewById(R.id.button4);

        Bundle extras = getIntent().getExtras();
        date_time.setText(getIntent().getExtras().getString("datetime_key"));


        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position4 = s4.getSelectedItemPosition();

                Intent intent = new Intent(com.example.complaintt.Theft.this,Notification2.class);

                if (position4 == 1) {
                    intent.putExtra("phone", pAbd);
                    startActivity(intent);
                }
                else if (position4 == 2) {
                    intent.putExtra("phone", pDhu);
                    startActivity(intent);
                } else if (position4 == 3) {
                    intent.putExtra("phone", pNas);
                    startActivity(intent);
                } else if (position4 == 4) {
                    intent.putExtra("phone", pPne);
                    startActivity(intent);
                } else if (position4 == 5) {
                    intent.putExtra("phone", pMum);
                    startActivity(intent);
                } else {
                    Toast.makeText(Theft.this, "Select District", Toast.LENGTH_SHORT).show();
                }

                checkField(Street1);
                checkField(Village1);
                checkField(Pincode1);
                checkField(Details1);
                checkField(Item);
                checkField(Worth);

                FirebaseUser user = fAuth.getCurrentUser();
                DocumentReference df = fStore.collection("Users").document(user.getEmail());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Category","Theft");
                userInfo.put("Date and Time of Incident", date_time.getText().toString());
                userInfo.put("Street address", Street1.getText().toString());
                userInfo.put("Village or city", Village1.getText().toString());
                userInfo.put("District ", s4.getSelectedItem().toString());
                userInfo.put("Pincode", Pincode1.getText().toString());
                userInfo.put("Item Stolen", Item.getText().toString());
                userInfo.put("Worth of stolen Item", Worth.getText().toString());
                userInfo.put("Details of Theft", Details1.getText().toString());
                userInfo.put("Name of accused person", Accused1.getText().toString());
                userInfo.put("Model name and vehicle no of involved Vehicle", FieldValue.delete());
                userInfo.put("Describe the incident", FieldValue.delete());
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