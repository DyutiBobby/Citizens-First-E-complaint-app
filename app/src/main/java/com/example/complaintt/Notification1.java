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

public class Notification1 extends AppCompatActivity {
    EditText Txt_pNumber;
    TextView Txt__message,Copy,Display;
    Button Send,logout;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String message;
    private static int MAX_SMS_MESSAGE_LENGTH = 160;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification1);
        Txt_pNumber = findViewById(R.id.Txt_phone_number);
        Txt__message = findViewById(R.id.Txt_message);
        Send = findViewById(R.id.Btn_send);
        Copy = findViewById(R.id.Copy);
        Display = findViewById(R.id.Phone_display);

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
                    String Street = documentSnapshot.getString("Street address");
                    String Village = documentSnapshot.getString("Village or city");
                    String district2 = documentSnapshot.getString("District ");
                    String Pincode = documentSnapshot.getString("Pincode");
                    String Details = documentSnapshot.getString("Describe the incident");
                    String Accused = documentSnapshot.getString("Name of accused person");

                    message = "New Harassment complaint registered."+"\n\nName: " + name + " \nPhone: " + phone + "\nEmail: " + email + ",\nDate and Time of incident: " + datetime + "\nStreet address: " + Street + "\nVillage or city: " + Village + "\nDistrict: " + district2 + "\nPincode: " + Pincode + "\nDescribe the incident: " + Details + "\nName of accused person: " + Accused;
                    Toast.makeText(Notification1.this, "" + message, Toast.LENGTH_LONG).show();
                    Txt__message.setText(message);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        Display.setText(getIntent().getExtras().getString("phone"));

        logout=findViewById(R.id.Logoutbutton);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Notification1.this,login.class));
                finish();
            }
        });
    }

    public void Send(View view) {

        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {

            MyMessage();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    private void MyMessage() {
        String display_phone = Display.getText().toString();
        String p_number = Txt_pNumber.getText().toString().trim();

        if(p_number.compareTo(display_phone)!= 0 ){
            Toast.makeText(this, "Number entered is incorrect. Please check number.", Toast.LENGTH_LONG).show();

        }

        else if (!p_number.equals("") || !message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            int length = message.length();
            if(length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messagelist = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(p_number, null, messagelist, null, null);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
            else{
                smsManager.sendTextMessage(p_number, null, message, null, null);

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
