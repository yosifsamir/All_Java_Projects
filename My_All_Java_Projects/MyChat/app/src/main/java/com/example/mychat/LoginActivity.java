package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEdt;
    private EditText passwordEdt;
    private Button loginBtn;
    private FirebaseAuth firebaseAuth;
    private CircleProgressDialog circleProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initFireBaseAuth();
        loginBtn.setOnClickListener(this);
    }

    private void initFireBaseAuth() {
        firebaseAuth=FirebaseAuth.getInstance();

    }

    private void initViews() {
        emailEdt=findViewById(R.id.email_login_edt);
        passwordEdt=findViewById(R.id.password_login_edt);
        loginBtn=findViewById(R.id.login_btn);
    }


    @Override
    public void onClick(View v) {

        String email=emailEdt.getText().toString();
        String password=passwordEdt.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailEdt.requestFocus();
            emailEdt.setError("Enter your Email");
            return;
        }
        if (TextUtils.isEmpty(password)){
            passwordEdt.requestFocus();
            passwordEdt.setError("Enter your Password");
            return;
        }
        circleProgressDialog=new CircleProgressDialog(LoginActivity.this);
        circleProgressDialog.setProgressColor(Color.BLACK);
        circleProgressDialog.setProgressWidth(5);
        circleProgressDialog.showDialog();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    circleProgressDialog.dismiss();
                    Intent intent=new Intent(LoginActivity.this,StartActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(LoginActivity.this, "Please check your email or password", Toast.LENGTH_SHORT).show();
                        circleProgressDialog.dismiss();
                        return;
                    }
                    else{
                        circleProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
