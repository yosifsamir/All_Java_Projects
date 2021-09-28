package com.example.mopay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class PinActivity extends AppCompatActivity {

    SharedPreferences mPrefrences;
    SharedPreferences.Editor mEditor;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    private ImageView imageView;

    FirebaseDatabase database;
    DatabaseReference referenceUser;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
//    String pinString1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        mPinLockView=findViewById(R.id.pin_lock_view);
        mIndicatorDots=findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        imageView=findViewById(R.id.profile_image);

        database= FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        if(user.getPhotoUrl()!=null){
            Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(imageView);
        }



        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(final String pin) {

                referenceUser.child(user.getUid()).child("pinString").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pinString=dataSnapshot.getValue(String.class);
//                Toast.makeText(getApplicationContext(),pinString+"",Toast.LENGTH_LONG).show();
                        if(pin.equals(pinString)){
                            Intent intent=new Intent(getApplicationContext(),IconTabsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"failed code, try again",Toast.LENGTH_LONG).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });


//        referenceUser.child(user.getUid()).child("pinString").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String pinString=dataSnapshot.getValue(String.class);
////                Toast.makeText(getApplicationContext(),pinString+"",Toast.LENGTH_LONG).show();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }
    public void Forget (View view)
    {
        Intent i = new Intent(PinActivity.this,ForgetActivity.class);
        startActivity(i);
    }

    public void LoginAgain (View view)
    {

        SharedPreferences mPrefrences;
        SharedPreferences.Editor mEditor;

        mPrefrences = getApplicationContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        mEditor = mPrefrences.edit();

        mEditor.putString(getString(R.string.Checkbox), "false");
        mEditor.commit();

        mEditor.putString(getString(R.string.Email), "");
        mEditor.commit();

        mEditor.putString(getString(R.string.Password), "");
        mEditor.commit();

        Intent i = new Intent(PinActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
