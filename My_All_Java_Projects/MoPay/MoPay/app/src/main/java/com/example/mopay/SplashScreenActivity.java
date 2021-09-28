package com.example.mopay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        final String sharedEmail=  preferences.getString(getString(R.string.Email), null);
        final String sharedPassword=  preferences.getString(getString(R.string.Password), null);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {


                if(sharedEmail==null || sharedPassword==null || sharedEmail.equals("") || sharedPassword.equals("")){
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
                else{
                    mAuth.signInWithEmailAndPassword(sharedEmail, sharedPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               // Toast.makeText(getApplicationContext(), "log in complete", Toast.LENGTH_LONG).show();
                                OnlyPassword.passwordStatic = sharedPassword;
                                Intent intent = new Intent(getApplicationContext(), PinActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }, secondsDelayed * 3000);



    }
}
