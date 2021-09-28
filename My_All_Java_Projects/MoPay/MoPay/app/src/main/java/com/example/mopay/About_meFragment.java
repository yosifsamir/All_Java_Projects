package com.example.mopay;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

public class About_meFragment extends Fragment implements View.OnClickListener{
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText email;
    private EditText phone;
    private LinearLayout linearLayout;

    private ImageView firstNameImg;
    private ImageView lastNameImg;
    private ImageView ageImg;
    private ImageView emailImg;
    private ImageView  phoneImg;

    private TextView ageText;
    private TextView emailText;
    private TextView phoneText;



    private Button saveAboutMe;

    FirebaseDatabase database;
    DatabaseReference referenceUser;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    StorageReference mStorage;
    Context context1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1=context;
    }

    public About_meFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.about_me_fragment, container, false);

        firstName=view.findViewById(R.id.first_name_edit);
        lastName=view.findViewById(R.id.last_name_edit);
        age=view.findViewById(R.id.age_edit);
        email=view.findViewById(R.id.email_edit);
        phone=view.findViewById(R.id.phone_edit);
        linearLayout=view.findViewById(R.id.linear_about_me);
        saveAboutMe=view.findViewById(R.id.save_about_me);

        ageText=view.findViewById(R.id.age_text);
        emailText=view.findViewById(R.id.email_text);
        phoneText=view.findViewById(R.id.phone_text);

        setFonts();

        firstNameImg=view.findViewById(R.id.first_name_change);
        lastNameImg=view.findViewById(R.id.last_name_change);
        ageImg=view.findViewById(R.id.change_age);
        emailImg=view.findViewById(R.id.change_email);
        phoneImg=view.findViewById(R.id.change_phone);


        database= FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference().child("users_photo");


        referenceUser.child(user.getUid()).child("firsNameString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstNameString=dataSnapshot.getValue(String.class);
                firstName.setText(firstNameString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceUser.child(user.getUid()).child("lastNameString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastNameString=dataSnapshot.getValue(String.class);
                lastName.setText(lastNameString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceUser.child(user.getUid()).child("ageString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ageString=dataSnapshot.getValue(String.class);
                age.setText(ageString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        email.setText(user.getEmail());

        referenceUser.child(user.getUid()).child("phoneString").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneString=dataSnapshot.getValue(String.class);
                phone.setText(phoneString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        firstNameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(true);
                lastName.setEnabled(false);
                age.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);
            }
        });


        lastNameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(false);
                lastName.setEnabled(true);
                age.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);

            }
        });

        ageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                age.setEnabled(true);
                email.setEnabled(false);
                phone.setEnabled(false);

            }
        });

        emailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                age.setEnabled(false);
                email.setEnabled(true);
                phone.setEnabled(false);

            }
        });

        phoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                age.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(true);

            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                age.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);
            }
        });

        saveAboutMe.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {
        String firstNameString1 = firstName.getText().toString();
        String lastNameString1 = lastName.getText().toString();
        String ageString1 = age.getText().toString();
        String emailString1 = email.getText().toString();
        String phoneString1 = phone.getText().toString();

        firstName.setEnabled(false);
        lastName.setEnabled(false);
        age.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);



        if (firstNameString1.isEmpty() || lastNameString1.isEmpty() || ageString1.isEmpty() || emailString1.isEmpty() ||  phoneString1.isEmpty()) {
            if (firstNameString1.isEmpty()) {
                firstName.setError("first name is Empty");
                firstName.requestFocus();
                return;
            } else if (lastNameString1.isEmpty()) {
                lastName.setError("last name is Empty");
                lastName.requestFocus();
                return;
            } else if (ageString1.isEmpty()) {
                age.setError("age is Empty");
                age.requestFocus();
                return;
            } else if (emailString1.isEmpty()) {
                email.setError("Email is Empty");
                email.requestFocus();
                return;
            }
            else if (phoneString1.isEmpty()) {
                phone.setError("phone is Empty");
                phone.requestFocus();
                return;
            }

        }

        else {

            final ProgressDialog dialog = new ProgressDialog(context1);
            dialog.setCancelable(false);
            dialog.show();


            referenceUser.child(user.getUid()).child("firsNameString").setValue(firstNameString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            referenceUser.child(user.getUid()).child("lastNameString").setValue(lastNameString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            referenceUser.child(user.getUid()).child("ageString").setValue(ageString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            referenceUser.child(user.getUid()).child("phoneString").setValue(phoneString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            user.updateEmail(emailString1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });


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
                                            Toast.makeText(context1, "profile image is updated", Toast.LENGTH_LONG).show();                                        }
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
        ageText.setTypeface(typeface);
        emailText.setTypeface(typeface);
        phoneText.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Bold.ttf");
        firstName.setTypeface(typeface2);
        lastName.setTypeface(typeface2);
        age.setTypeface(typeface2);
        email.setTypeface(typeface2);
        phone.setTypeface(typeface2);
        saveAboutMe.setTypeface(typeface2);
    }
}
