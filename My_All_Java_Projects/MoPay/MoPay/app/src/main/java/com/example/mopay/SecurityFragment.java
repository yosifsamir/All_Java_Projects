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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SecurityFragment extends Fragment implements View.OnClickListener {

    private EditText password;
    private EditText pin;
    private EditText username;

    private TextView passwordText;
    private TextView pinText;
    private TextView userNameText;

    private LinearLayout linearLayout;

    private ImageView passwordImg;
    private ImageView pinImg;

    FirebaseDatabase database;
    DatabaseReference referenceUser;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    StorageReference mStorage;


    Button saveSecurity;

    String passwordString;
    String pinString;
    String userNameString;

    boolean aBoolean=false;
    Context context1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1=context;
    }

    public SecurityFragment() {
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
        View view= inflater.inflate(R.layout.security_fragment, container, false);

        password=view.findViewById(R.id.password_edit);
        pin=view.findViewById(R.id.pin_edit);
        username=view.findViewById(R.id.username_edit);
        linearLayout=view.findViewById(R.id.linear2);
        saveSecurity = view.findViewById(R.id.save_security);

        passwordText=view.findViewById(R.id.password_text);
        pinText=view.findViewById(R.id.pin_text1);
        userNameText=view.findViewById(R.id.username_security_text);

        setFonts();

        passwordImg=view.findViewById(R.id.change_password);
        pinImg=view.findViewById(R.id.change_pin);


        database=FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
//        referenceCard=database.getReference("Card");
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference().child("users_photo");

        if (OnlyPassword.getPasswordStatic() == null ||
                OnlyPassword.usersStatic.getPinString()==null ||
                OnlyPassword.usersStatic.getUserNameString()==null){

        }
        else {

            password.setText(OnlyPassword.getPasswordStatic());
            pin.setText(OnlyPassword.usersStatic.getPinString());
            username.setText(OnlyPassword.usersStatic.getUserNameString());



            passwordImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password.setEnabled(true);
                    pin.setEnabled(false);

                }
            });


            pinImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password.setEnabled(false);
                    pin.setEnabled(true);

                }
            });


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password.setEnabled(false);
                    pin.setEnabled(false);
                }
            });

            saveSecurity.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        passwordString = password.getText().toString();
        pinString = pin.getText().toString();
        userNameString = username.getText().toString();

        password.setEnabled(false);
        pin.setEnabled(false);


        if (passwordString.isEmpty() || pinString.isEmpty() ) {
            if (passwordString.isEmpty()) {
                password.setError("Password is Empty");
                password.requestFocus();
                return;
            } else if (pinString.isEmpty()) {
                pin.setError("Pin is Empty");
                pin.requestFocus();
                return;
            }

        }
        else if (pinString.length()>4){
            pin.setError("Pin should less than 4");
            pin.requestFocus();
            return;
        }
        else{
            final ProgressDialog dialog = new ProgressDialog(context1);
            dialog.setCancelable(false);
            dialog.show();

            if (passwordString.equals(OnlyPassword.passwordStatic)) {
//        Toast.makeText(getActivity().getApplicationContext(),"the password does not change",Toast.LENGTH_LONG).show();
            } else {
                user.updatePassword(passwordString).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(), "password is changed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


            referenceUser.child(user.getUid()).child("pinString").setValue(pinString).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity().getApplicationContext(),"pin is change",Toast.LENGTH_SHORT).show();

                        aBoolean = true;
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
//                        Toast.makeText(getActivity().getApplicationContext(), "pin changed", Toast.LENGTH_SHORT).show();
                    } else {
                        aBoolean = false;
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            if (aBoolean == true) {
                Toast.makeText(getActivity().getApplicationContext(), "the data are updated", Toast.LENGTH_LONG).show();
            }


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
        passwordText.setTypeface(typeface);
        pinText.setTypeface(typeface);
        userNameText.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Bold.ttf");
        password.setTypeface(typeface2);
        pin.setTypeface(typeface2);
        username.setTypeface(typeface2);
        saveSecurity.setTypeface(typeface2);

    }
}
