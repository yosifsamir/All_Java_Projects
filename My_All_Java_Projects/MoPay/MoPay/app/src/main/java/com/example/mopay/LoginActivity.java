package com.example.mopay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public SharedPreferences mPrefrences;
    public SharedPreferences.Editor mEditor;

    Button login ;
    Button signUp ;
    EditText email,password;
    TextView forgot;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    public CheckBox mcheckbox;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email_login);
        password=findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.Login);
        signUp = (Button) findViewById(R.id.signUp);
        forgot=findViewById(R.id.forget);
        mcheckbox = findViewById(R.id.checkBoxRememberMe);


        mPrefrences = getApplicationContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        mEditor = mPrefrences.edit();

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Bold.ttf");
        login.setTypeface(typeface);
        signUp.setTypeface(typeface);
        email.setTypeface(typeface);
        password.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Regular.ttf");
        forgot.setTypeface(typeface2);
        mcheckbox.setTypeface(typeface2);


        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        reference=database.getReference("Users");


        checkSharedPereferences();

        if(mcheckbox.isChecked())
        {
            mEditor.putString(getString(R.string.Checkbox), "True");
            mEditor.commit();

            String email2 = email.getText().toString();
            mEditor.putString(getString(R.string.Email), email2);
            mEditor.commit();

            final String password2 = password.getText().toString();
            mEditor.putString(getString(R.string.Password), password2);
            mEditor.commit();

            mAuth.signInWithEmailAndPassword(email2, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "log in complete", Toast.LENGTH_LONG).show();
                        OnlyPassword.passwordStatic = password2;
                        Intent intent = new Intent(getApplicationContext(), PinActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String email2=email.getText().toString();
                final String password2=password.getText().toString();
                if(email2.isEmpty() || password2.isEmpty()) {

                    if (email2.isEmpty()) {
                        email.setError("the email is Empty");
                        email.requestFocus();
                    } else if (password2.isEmpty()) {
                        password.setError("password is Empty");
                        password.requestFocus();
                    }
                }
                else{
                    if(mcheckbox.isChecked()) {

                        mEditor.putString(getString(R.string.Checkbox), "True");
                        mEditor.commit();

                        mEditor.putString(getString(R.string.Email), email2);
                        mEditor.commit();

                        mEditor.putString(getString(R.string.Password), password2);
                        mEditor.commit();

                    }

                    mAuth.signInWithEmailAndPassword(email2,password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user=mAuth.getCurrentUser();
                                if (user.isEmailVerified()){
                                    Toast.makeText(getApplicationContext(),"log in complete",Toast.LENGTH_LONG).show();
                                    OnlyPassword.passwordStatic=password2;
                                    Intent intent=new Intent(getApplicationContext(),IconTabsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Please verified your account",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });
    }

    public void forget (View view)
    {
        Intent i = new Intent(LoginActivity.this,ForgetActivity.class);
        startActivity(i);
    }


    private void checkSharedPereferences() {

        String Checkbox = mPrefrences.getString(getString(R.string.Checkbox), "false");
        String Email = mPrefrences.getString(getString(R.string.Email), "");
        String Password = mPrefrences.getString(getString(R.string.Password), "");


        email.setText(Email);
        password.setText(Password);

        if (Checkbox.equals("True")) {
            mcheckbox.setChecked(true);
        } else {
            mcheckbox.setChecked(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

      /*  user=mAuth.getCurrentUser();
        if (user!=null){
            Intent intent=new Intent(getApplicationContext(),PinActivity.class);
            startActivity(intent);
            finish();
        }
        */
    }


}
