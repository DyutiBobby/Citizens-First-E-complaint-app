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

public class Harassment extends AppCompatActivity {

    EditText Street, Village, Pincode, Details, Accused;
    private TextView Harassment, Date_Time;
    Spinner spinner3;
    Button bt2;
    int position3;
    String pAbd="9370394253",pDhu="9322670429",pNas="9307067287",pPne= "8080245350",pMum="9420676951";
    boolean valid = true;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String[] district_array1 = {"/-district-/", "Aurangabad,Maharashtra", "Dhule, Maharashtra", "Nashik,Maharashtra", "Pune,Maharashtra", "Mumbai,Maharashtra"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harassment);
        spinner3 = findViewById(R.id.spinner3);

        ArrayAdapter adapter = new ArrayAdapter(Harassment.this, android.R.layout.simple_spinner_dropdown_item, district_array1);
        spinner3.setAdapter(adapter);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Street = findViewById(R.id.haddress);
        Village = findViewById(R.id.hcity);
        Pincode = findViewById(R.id.hpincode);
        Accused = findViewById(R.id.hcriminal);
        Details = findViewById(R.id.editTextDetails2);
        Harassment = findViewById(harassment);
        Date_Time = findViewById(R.id.editTextDate_time);
        bt2 = findViewById(R.id.hsubmit);

        Bundle extras = getIntent().getExtras();
        Date_Time.setText(getIntent().getExtras().getString("datetime_key"));

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position3 = spinner3.getSelectedItemPosition();


                Intent intent = new Intent(com.example.complaintt.Harassment.this, Notification1.class);

                if (position3 == 1) {
                    intent.putExtra("phone", pAbd);
                    startActivity(intent);
                }
                else if (position3 == 2) {
                    intent.putExtra("phone", pDhu);
                    startActivity(intent);
                } else if (position3 == 3) {
                    intent.putExtra("phone", pNas);
                    startActivity(intent);
                } else if (position3 == 4) {
                    intent.putExtra("phone", pPne);
                    startActivity(intent);
                } else if (position3 == 5) {
                    intent.putExtra("phone", pMum);
                    startActivity(intent);
                } else {
                    Toast.makeText(Harassment.this, "Select District", Toast.LENGTH_SHORT).show();
                }

                checkField(Street);
                checkField(Village);
                checkField(Pincode);
                checkField(Details);


                FirebaseUser user = fAuth.getCurrentUser();
                DocumentReference df = fStore.collection("Users").document(user.getEmail());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Category","Harassment");
                userInfo.put("Date and Time of Incident", Date_Time.getText().toString());
                userInfo.put("Street address", Street.getText().toString());
                userInfo.put("Village or city", Village.getText().toString());
                userInfo.put("District ", spinner3.getSelectedItem().toString());
                userInfo.put("Pincode", Pincode.getText().toString());
                userInfo.put("Describe the incident", Details.getText().toString());
                userInfo.put("Name of accused person", Accused.getText().toString());
                userInfo.put("Model name and vehicle no of involved Vehicle", FieldValue.delete());
                userInfo.put("Item Stolen", FieldValue.delete());
                userInfo.put("Details of Theft",FieldValue.delete());
                userInfo.put("Worth of stolen Item",FieldValue.delete());
                df.update(userInfo);


            }
        });
    }

    public void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
    }
}

