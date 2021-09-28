package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mychat.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class MainActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private EditText usernameEdt,emailEdt,passwordEdt,confirmPasswordEdt;
    private String username,email,password,confirmPassword;
    private Uri uri;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private CircleProgressDialog circleProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        iniFirebase();

//        if(firebaseAuth.getCurrentUser()!=null){
//            startActivity(new Intent(MainActivity.this,StartActivity.class));
//            finish();
//        }
    }

    private void iniFirebase() {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    private void initViews() {
        circleImageView=findViewById(R.id.circleLogo);
        usernameEdt=findViewById(R.id.username);
        emailEdt=findViewById(R.id.email);
        passwordEdt=findViewById(R.id.password);
        confirmPasswordEdt=findViewById(R.id.confirm_password);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(MainActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                circleImageView.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void login(View view) {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    public void register(View view) {
        username=usernameEdt.getText().toString();
        email=emailEdt.getText().toString();
        password=passwordEdt.getText().toString();
        confirmPassword=confirmPasswordEdt.getText().toString();

        if (TextUtils.isEmpty(username)){
            usernameEdt.requestFocus();
            usernameEdt.setError("Enter your name");
            return;
        }
        if (TextUtils.isEmpty(email)){
            emailEdt.requestFocus();
            emailEdt.setError("Enter your Email");
            return;
        }
        if (TextUtils.isEmpty(password)){
            passwordEdt.requestFocus();
            passwordEdt.setError("Enter your Passowrd");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEdt.requestFocus();
            confirmPasswordEdt.setError("Enter your Confirm Password");
            return;
        }
        if (password.length()<6){
            passwordEdt.requestFocus();
            passwordEdt.setError("password too short");
            return;
        }
        if (!confirmPassword.equals(password)){
            confirmPasswordEdt.requestFocus();
            confirmPasswordEdt.setError("Confirm Password not equal password");
            return;
        }
        if (uri==null){
            new GuideView.Builder(this)
                .setTitle("Guide Title Text")
                .setContentText("select an image here")
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(circleImageView)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }
        circleProgressDialog=new CircleProgressDialog(MainActivity.this);
        circleProgressDialog.setProgressColor(Color.BLACK);
        circleProgressDialog.setProgressWidth(5);
        circleProgressDialog.showDialog();

        createUser(username,email,password);
    }

    private void createUser(final String username, final String email, String password) {
        if (uri==null){

            Toast.makeText(this, "select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id=task.getResult().getUser().getUid();
                    uploadPicture(username,email,uri,id);
                }else{
                    circleProgressDialog.dismiss();
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        emailEdt.requestFocus();
                        emailEdt.setError("Your Email is wrong");
                        return;
                    }
//                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void uploadPicture(final String username, final String email, final Uri uri, final String id) {
        UploadTask uploadTask;
        Toast.makeText(this, "image/"+uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        String fileName = FirebaseAuth.getInstance().getUid()+".jpg";

        final StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("images/"+fileName);

        uploadTask = (UploadTask) refStorage.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return refStorage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri imagePath=task.getResult();
                Toast.makeText(MainActivity.this, imagePath.toString(), Toast.LENGTH_SHORT).show();
                saveToDB(username,email,imagePath.toString(),id);
            }
        });

    }
    private void saveToDB(String username, String email, String imagePath, String id) {
        User user=new User(username,email,imagePath);
        databaseReference.child("Users").child(id).setValue(user);
        circleProgressDialog.dismiss();
        startActivity(new Intent(MainActivity.this,StartActivity.class));
        finish();
    }
}
