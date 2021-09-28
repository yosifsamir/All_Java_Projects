package com.example.mopay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhoneActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText countryCode;
    private Button sendCode;

    private String phone;

    FirebaseDatabase database;
    DatabaseReference referenceUser,referenceCreditNumber;
    DatabaseReference referenceUser2;
    DatabaseReference userCredRef;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference mCreditPhone;
    String phoneDatabase;
    String creditCardNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        phoneNumber=findViewById(R.id.phoneNumberVerfication);
        countryCode=findViewById(R.id.countryCode);
        sendCode=findViewById(R.id.sendCodeVerification);

        database=FirebaseDatabase.getInstance();
        referenceCreditNumber=database.getReference("Credit_Number");
        referenceUser=database.getReference("Users").child(FirebaseAuth.getInstance().getUid());
        referenceUser2=referenceUser.child("creditCardString");

        referenceUser2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                creditCardNumber = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), creditCardNumber, Toast.LENGTH_LONG).show();

                userCredRef = referenceCreditNumber.child(creditCardNumber);
                mCreditPhone = userCredRef.child("creditPhoneNumber");
                mCreditPhone.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        phoneDatabase = dataSnapshot.getValue(String.class);
                        Toast.makeText(getApplicationContext(), phoneDatabase, Toast.LENGTH_LONG).show();

                        sendCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String number = phoneNumber.getText().toString().trim();
                                String countryCodeString = countryCode.getText().toString();

                                if (!number.equals(phoneDatabase) || number.isEmpty() || number.length() < 11) {
                                    phoneNumber.setError("Valid number is required");
                                    phoneNumber.requestFocus();
                                    return;
                                } else {
                                    phone = countryCodeString + number;


                                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                                    intent.putExtra("phonenumber", phone);
                                    startActivity(intent);

                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        DatabaseReference userCredRef=referenceCreditNumber.child(creditCardNumber);
//        mCreditPhone=userCredRef.child("creditPhoneNumber");
//        mCreditPhone.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                phoneDatabase=dataSnapshot.getValue(String.class);
//                Toast.makeText(getApplicationContext(),phoneDatabase,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//
//        sendCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String number=phoneNumber.getText().toString().trim();
//                String countryCodeString=countryCode.getText().toString();
//
//                if (!number.equals(phoneDatabase) || number.isEmpty() || number.length() < 11) {
//                    phoneNumber.setError("Valid number is required");
//                    phoneNumber.requestFocus();
//                    return;
//                }
//                else{
//                    phone=countryCodeString+number;
//
//
//                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
//                    intent.putExtra("phonenumber", phone);
//                    startActivity(intent);
//
//                }
//
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        referenceUser.removeValue();
        userCredRef.removeValue();

    }
}
