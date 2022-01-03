package com.example.complaintt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification extends AppCompatActivity {
    EditText txt_pNumber;
    TextView txt__message,copy,display;
    Button send,Logout;
    public Spinner s1;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String Message;
    private static int MAX_SMS_MESSAGE_LENGTH = 160;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        txt_pNumber = findViewById(R.id.txt_phone_number);
        txt__message = findViewById(R.id.txt_message);
        send = findViewById(R.id.btn_send);
        copy = findViewById(R.id.copy);
        display = findViewById(R.id.phone_display);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        DocumentReference df = fStore.collection("Users").document(user.getEmail());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("Username");
                    String phone = documentSnapshot.getString("Phone");
                    String email = documentSnapshot.getString("Email");
                    String datetime = documentSnapshot.getString("Date and Time of Incident");
                    String street = documentSnapshot.getString("Street address");
                    String village = documentSnapshot.getString("Village or city");
                    String district1 = documentSnapshot.getString("District ");
                    String pincode = documentSnapshot.getString("Pincode");
                    String vehicle = documentSnapshot.getString("Model name and vehicle no of involved Vehicle");
                    String details = documentSnapshot.getString("Describe the incident");
                    String accused =documentSnapshot.getString("Name of accused person");


                    Message="New Accident complaint registered"+"\n\nName: "+name+" \nPhone: "+phone+"\nEmail: "+email+",\nDate and Time of accident: "+datetime+"\nStreet address: "+street+"\nVillage or city: "+village+"\nDistrict: "+district1+"\nPincode: "+pincode+"\nModel name and vehicle no of involved Vehicle: "+vehicle+"\nDescribe the incident: "+details+"\nName of accused person: "+accused;
                    Toast.makeText(Notification.this,""+Message,Toast.LENGTH_LONG).show();
                    txt__message.setText(Message);
                }
            }
        });



        Bundle extras = getIntent().getExtras();
        display.setText(getIntent().getExtras().getString("phone"));

        Logout=findViewById(R.id.logoutbutton);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Notification.this,login.class));
                finish();
            }
        });
    }


    public void send(View view) {

        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {

            MyMessage();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    private void MyMessage() {
        String display_phone = display.getText().toString();
        String p_number = txt_pNumber.getText().toString().trim();

        if(p_number.compareTo(display_phone)!= 0 ){
            Toast.makeText(this, "Number entered is incorrect. Please check number.", Toast.LENGTH_LONG).show();
        }

         else if (!p_number.equals("") || !Message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
             int length = Message.length();
             if(length > MAX_SMS_MESSAGE_LENGTH) {
                 ArrayList<String> messagelist = smsManager.divideMessage(Message);
                 smsManager.sendMultipartTextMessage(p_number, null, messagelist, null, null);
                 Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
             }
            else{
                smsManager.sendTextMessage(p_number, null, Message, null, null);

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






