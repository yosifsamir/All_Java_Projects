package com.example.mopay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText emailEd;
    private TextView submit,back;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        emailEd=findViewById(R.id.email_forgot_edit);
        submit=findViewById(R.id.forgot_button);
        back=findViewById(R.id.backToLoginBtn);

        submit.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();



        }

    @Override
    public void onClick(View v) {
        String emailString=emailEd.getText().toString();
        if (!emailString.isEmpty()){
            mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"password send to your email",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
