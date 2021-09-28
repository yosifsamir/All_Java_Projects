package com.example.mopay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

public class HomeFragment extends Fragment /*implements View.OnClickListener*/{

    View v ;
    Button btngenerte;
    Button btnscan;
    Button btnsend;
    TextView profileName;

    TextView moneyWallet;
    Wallet wallet;
    ImageView imgProfileHome;
    FirebaseUser user;

    DatabaseReference referenceWallet;
    DatabaseReference mWallet;
    DatabaseReference mUser;
    DatabaseReference userNameProfile;
    FirebaseAuth mAuth;
    FirebaseUser userInfo;
    private String userName2;
    Context context1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1=context;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inside_relative,new ButtonsFragment(),"button_fragment")
//                    .addToBackStack("button_fragment")
                    .commit();
        }

        else {

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inside_relative,new ButtonsFragment(),"button_fragment")
//                    .addToBackStack("button_fragment")
                    .commit();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
/////////////////////////////////////////////////////////////////////////////
        profileName=v.findViewById(R.id.profile_name);
        user= FirebaseAuth.getInstance().getCurrentUser();
        imgProfileHome=v.findViewById(R.id.profile_image_home);
        moneyWallet=v.findViewById(R.id.money_wallet);

        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Black.ttf");
        moneyWallet.setTypeface(typeface);
        profileName.setTypeface(typeface);


        referenceWallet=FirebaseDatabase.getInstance().getReference("Wallet");
        mUser=FirebaseDatabase.getInstance().getReference("Users");
        userNameProfile=mUser.child(user.getUid()).child("firsNameString");
        final String profileNameString=profileName.getText().toString();

        userInfo=FirebaseAuth.getInstance().getCurrentUser();
        userNameProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userNameProfileString=dataSnapshot.getValue(String.class);
                String userNameProfileString2=profileNameString+userNameProfileString;
                profileName.setText(userNameProfileString2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference userNameDatabase=mUser.child(user.getUid()).child("userNameString");

        userNameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userName2=dataSnapshot.getValue(String.class);
                Toast.makeText(context1,userName2,Toast.LENGTH_LONG).show();

                DatabaseReference mWallet2=referenceWallet.child(userName2).child("amountWallet");
                mWallet2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String amountOfWallet=dataSnapshot.getValue(String.class);
                        moneyWallet.setText(amountOfWallet);
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

        if(user.getPhotoUrl()!=null){
            Glide.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(imgProfileHome);

        }
        return v ;
    }

    @Override
    public void onStart() {
        super.onStart();
//                    Toast.makeText(getActivity().getApplicationContext(), "onStart is called", Toast.LENGTH_LONG).show();

//        if (ProfileFragment.pickedImgUri==null ){
//            if(user.getPhotoUrl()!=null){
//                Glide.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(imgProfileHome);
////
//            }






//            mWallet.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String amountOfWallet=dataSnapshot.getValue(String.class);
//                    moneyWallet.setText(amountOfWallet);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


//        referenceWallet.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
////                    Toast.makeText(getActivity().getApplicationContext(),OnlyPassword.usersStatic.getUserNameString(),Toast.LENGTH_LONG).show();
//                    wallet = dataSnapshot.child(OnlyPassword.usersStatic.getUserNameString()).getValue(Wallet.class);
//                    if (wallet==null){
//                        Toast.makeText(getActivity().getApplicationContext(),"wallet is null",Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        moneyWallet.setText(wallet.getAmountWallet() + "");
//                    }
//                }
//                else {
//                    Toast.makeText(getActivity().getApplicationContext(),"data does not exist",Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//

//        }
//        else {
//
//            Toast.makeText(getActivity().getApplicationContext(), "onStart is called", Toast.LENGTH_LONG).show();
//            imgProfileHome.setImageURI(ProfileFragment.pickedImgUri);
//        }
    }


    /* @Override
    public void onClick(View v) {

        if (v == btnscan){

        }

    else if( v == btngenerte){

         Intent i = new Intent(HomeFragment.this.getActivity(),GenerateQRActivity.class);
         startActivity(i);
     }

     else if(v == btnsend){

         Intent i = new Intent(HomeFragment.this.getActivity(),SendActivity.class);
         startActivity(i);
     }

    }*/
}
