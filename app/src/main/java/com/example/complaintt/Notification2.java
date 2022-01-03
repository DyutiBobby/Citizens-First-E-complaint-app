package com.example.complaintt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Notification2 extends AppCompatActivity {

    EditText txt_pNumber1;
    TextView txt__message1,copy1,display1;
    Button send1,Logout1;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String Message1;
    private static int MAX_SMS_MESSAGE_LENGTH = 160;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);
        txt_pNumber1 = findViewById(R.id.txt_phone_number1);
        txt__message1 = findViewById(R.id.txt_message1);
        send1 = findViewById(R.id.btn_send1);
        copy1 = findViewById(R.id.copy1);
        display1 = findViewById(R.id.phone_display1);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        DocumentReference df = fStore.collection("Users").document(user.getEmail());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("Username");
                    String phone = documentSnapshot.getString("Phone");
                    String email = documentSnapshot.getString("Email");
                    String datetime = documentSnapshot.getString("Date and Time of Incident");
                    String Street1 = documentSnapshot.getString("Street address");
                    String Village1 = documentSnapshot.getString("Village or city");
                    String district3 = documentSnapshot.getString("District ");
                    String Pincode1 = documentSnapshot.getString("Pincode");
                    String Item = documentSnapshot.getString("Item Stolen");
                    String Worth = documentSnapshot.getString("Worth of stolen Item");
                    String Details1 = documentSnapshot.getString("Details of Theft");
                    String Accused1 = documentSnapshot.getString("Name of accused person");

                    Message1 = "New Theft complaint registered" + "\n\nName: " + name + " \nPhone: " + phone + "\nEmail: " + email + ",\nDate and Time of Theft: " + datetime + "\nStreet address: " + Street1 + "\nVillage or city: " + Village1 + "\nDistrict: " + district3 + "\nPincode: " + Pincode1 + "\nItem Stolen: " + Item + "\nWorth of stolen Item: " + Worth + "\nDetails of Theft: " + Details1 + "\nName of accused person: " + Accused1;
                    Toast.makeText(Notification2.this, "" + Message1, Toast.LENGTH_LONG).show();
                    txt__message1.setText(Message1);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        display1.setText(getIntent().getExtras().getString("phone"));

        Logout1=findViewById(R.id.logoutbutton1);

        Logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Notification2.this,login.class));
                finish();
            }
        });
    }

    public void send1(View view) {

        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {

            MyMessage();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    private void MyMessage() {
        String display_phone = display1.getText().toString();
        String p_number = txt_pNumber1.getText().toString().trim();

        if(p_number.compareTo(display_phone)!= 0 ){
            Toast.makeText(this, "Number entered is incorrect. Please check number.", Toast.LENGTH_LONG).show();

        }

        else if (!p_number.equals("") || !Message1.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            int length = Message1.length();
            if(length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messagelist = smsManager.divideMessage(Message1);
                smsManager.sendMultipartTextMessage(p_number, null, messagelist, null, null);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
            else{
                smsManager.sendTextMessage(p_number, null, Message1, null, null);

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 0:

                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    MyMessage();

                } else {

                    Toast.makeText(this, "You don't have Required Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}