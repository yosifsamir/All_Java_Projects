package com.example.mopay;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CardFragment extends Fragment implements View.OnClickListener{
    private char slash = '/';
    private static final char space = ' ';

    private EditText cardNumber;
    private EditText date;
    private EditText cvv;
    private EditText cardHolderName;
    private LinearLayout linearLayout;

    private TextView cardNumberText;
    private TextView dateText;
    private TextView cvvText;
    private TextView cardHolderText;

    private ImageView cardNumberImg;
    private ImageView dateImg;
    private ImageView cvvImg;
    private ImageView cardHolderImg;


    private Button saveCardInfo;


    FirebaseDatabase database;
    DatabaseReference referenceUser;
    DatabaseReference creditRefernce;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    StorageReference mStorage;

    Context context1;


    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.card_fragment, container, false);

        cardNumber=view.findViewById(R.id.credit_card_number_edit);
        date=view.findViewById(R.id.date_edit);
        cvv=view.findViewById(R.id.cvv_edit);
        cardHolderName=view.findViewById(R.id.card_holder_edit);
        linearLayout=view.findViewById(R.id.linear_card_info);
        saveCardInfo=view.findViewById(R.id.save_card);

        cardNumberText=view.findViewById(R.id.credit_card_number_text);
        dateText=view.findViewById(R.id.date_text);
        cvvText=view.findViewById(R.id.cvv_text);
        cardHolderText=view.findViewById(R.id.card_holder_text);

        setFonts();

        cardNumberImg=view.findViewById(R.id.change_credit_card_number);
        dateImg=view.findViewById(R.id.change_date);
        cvvImg=view.findViewById(R.id.change_cvv);
        cardHolderImg=view.findViewById(R.id.change_card_holder);


        firebaseAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        creditRefernce=database.getReference("Credit_Number").child(firebaseAuth.getCurrentUser().getUid());
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference().child("users_photo");


        referenceUser.child(user.getUid()).child("creditCardString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cardNumberString=dataSnapshot.getValue(String.class);
                cardNumber.setText(cardNumberString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceUser.child(user.getUid()).child("dateString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dateString=dataSnapshot.getValue(String.class);
                date.setText(dateString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceUser.child(user.getUid()).child("cvvString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cvvString=dataSnapshot.getValue(String.class);
                cvv.setText(cvvString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        referenceUser.child(user.getUid()).child("cardHolderString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cardHolderString1=dataSnapshot.getValue(String.class);
                cardHolderName.setText(cardHolderString1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        cardNumberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber.setEnabled(true);
                date.setEnabled(false);
                cvv.setEnabled(false);
                cardHolderName.setEnabled(false);
            }
        });


        dateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber.setEnabled(false);
                date.setEnabled(true);
                cvv.setEnabled(false);
                cardHolderName.setEnabled(false);

            }
        });

        cvvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber.setEnabled(false);
                date.setEnabled(false);
                cvv.setEnabled(true);
                cardHolderName.setEnabled(false);
            }
        });

        cardHolderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber.setEnabled(false);
                date.setEnabled(false);
                cvv.setEnabled(false);
                cardHolderName.setEnabled(true);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber.setEnabled(false);
                date.setEnabled(false);
                cvv.setEnabled(false);
                cardHolderName.setEnabled(false);
            }
        });

        saveCardInfo.setOnClickListener(this);

        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Remove spacing char
                if (editable.length() > 0 && (editable.length() % 5) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if (space == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                // Insert char where needed.
                if (editable.length() > 0 && (editable.length() % 5) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf(space)).length <= 3) {
                        editable.insert(editable.length() - 1, String.valueOf(space));

                    }
                }

                if (editable.length() >= 16) {

                }
            }
        });


        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                switch (editable.length()) {
                    case 1:
                        if (Integer.parseInt(editable.toString()) > 1) {
                            editable.clear();
                        }
                        break;

                    case 2:
                        if (((int) editable.charAt(0)) > 0) {
                            if (((int) editable.charAt(1)) > 2) {
                                editable.delete(1, 1);
                            }
                        }
                }

                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);

                    if (Character.isDigit(c)) {
                        editable.insert(editable.length() - 1, String.valueOf(slash));

                    }
                }

            }
        });

        cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                }
            }
        });



        return view;
    }

    @Override
    public void onClick(View v) {

        final String cardNumberString1 = cardNumber.getText().toString();
        String dateString1 = date.getText().toString();
        String cvvString1 = cvv.getText().toString();
        String cardHolderString1 = cardHolderName.getText().toString();

        cardNumber.setEnabled(false);
        date.setEnabled(false);
        cvv.setEnabled(false);
        cardHolderName.setEnabled(false);

        if (cardNumberString1.isEmpty() || dateString1.isEmpty() || cvvString1.isEmpty() || cardHolderString1.isEmpty()){
            if (cardNumberString1.isEmpty()){
                cardNumber.setError("card number is Empty");
                cardNumber.requestFocus();
                return;
            }
            else if(dateString1.isEmpty()){
                date.setError("date is Empty");
                date.requestFocus();
                return;
            }
            else if (cvvString1.isEmpty()){
                cvv.setError("first name is Empty");
                cvv.requestFocus();
                return;
            }
            else if(cardHolderString1.isEmpty()){
                cardHolderName.setError("first name is Empty");
                cardHolderName.requestFocus();
                return;
            }
        }

        else if (cardNumber.getText().toString().length()<19){
            cardNumber.setError("Credit Card is less than 16");
            cardNumber.requestFocus();
            return;
        }

        else if (dateString1.length()>5){
            date.setError("correct the Date");
            date.requestFocus();
            return;
        }

        else {

            final ProgressDialog dialog = new ProgressDialog(context1);
            dialog.setCancelable(false);
            dialog.show();


            referenceUser.child(user.getUid()).child("creditCardString").setValue(cardNumberString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(context1, "credit card number is change", Toast.LENGTH_SHORT).show();
                        creditRefernce.child("creditNumber").setValue(cardNumberString1);
//                  dialog.dismiss();
                    } else {
                        Toast.makeText(context1, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        dialog.show();

            referenceUser.child(user.getUid()).child("dateString").setValue(dateString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(context1, "date is change", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    } else {
                        Toast.makeText(context1, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

//        dialog.show();
            referenceUser.child(user.getUid()).child("cvvString").setValue(cvvString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(context1, "cvv is change", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    } else {
                        Toast.makeText(context1, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

//        dialog.show();

            referenceUser.child(user.getUid()).child("cardHolderString").setValue(cardHolderString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(context1, "cardholder is change", Toast.LENGTH_SHORT).show();
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } else {
                        Toast.makeText(context1, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


//        dialog.show();

            if (ProfileFragment.pickedImgUri != null) {
                if (ProfileFragment.pickedImgUri != user.getPhotoUrl()) {
                    final ProgressDialog dialog2 = new ProgressDialog(context1);
                    dialog2.setCancelable(false);
                    dialog2.show();

                    final StorageReference imageFilePath = mStorage.child(ProfileFragment.pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(ProfileFragment.pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest
                                            .Builder()
                                            .setPhotoUri(uri)
                                            .build();

                                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (dialog2.isShowing())
                                                dialog2.dismiss();
                                            Toast.makeText(context1, "profile image is updated", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            }
        }
    }

    public void setFonts(){
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Black.ttf");
        cardNumberText.setTypeface(typeface);
        dateText.setTypeface(typeface);
        cvvText.setTypeface(typeface);
        cardHolderText.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Bold.ttf");
        cardNumber.setTypeface(typeface2);
        date.setTypeface(typeface2);
        cvv.setTypeface(typeface2);
        cardHolderName.setTypeface(typeface2);
        saveCardInfo.setTypeface(typeface2);

    }
}
