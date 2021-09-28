package com.example.mopay;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private static final char space = ' ';
    private char slash = '/';
    EditText firstName,lastName,userName,age,email,password,confirmPassword,pin,phone,creditCard,cvv,date,cardHolder;

    String   firsNameString,lastNameString,userNameString,ageString,emailString,passwordString,confirmPasswordString,pinString,phoneString,creditCardString,cvvString,dateString,cardHolderString;

    FirebaseDatabase database;
    DatabaseReference referenceUser,referenceWallet,referenceCreditNumber;/*,referenceCard*/;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseAuth auth;

    Query userNameQuery;
    Query cardNumberQuery;

    boolean first=false;
    boolean second=false;




    Button button1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firstName=findViewById(R.id.first_name);
        lastName=findViewById(R.id.last_name);
        userName=findViewById(R.id.username);
        age=findViewById(R.id.age);
        email=findViewById(R.id.emailSignUp);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        pin=findViewById(R.id.pin);
        phone=findViewById(R.id.phone);
        creditCard=findViewById(R.id.credit_card);
        cvv=findViewById(R.id.cvv);
        date=findViewById(R.id.date);
        cardHolder=findViewById(R.id.card_holder);
        button1 = (Button) findViewById(R.id.register);

        setFonts();

        database=FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        referenceWallet=database.getReference("Wallet");
        referenceCreditNumber=database.getReference("Credit_Number");
//        referenceCard=database.getReference("Card");
        firebaseAuth=FirebaseAuth.getInstance();





        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                initializeStringVariable();
                makeSignUp();



            }
        });

        creditCard.addTextChangedListener(new TextWatcher() {
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


        cardHolder.addTextChangedListener(new TextWatcher() {
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
    }

    private void makeSignUp() {
        if (firsNameString.isEmpty() || lastNameString.isEmpty() || userNameString.isEmpty() || ageString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || confirmPasswordString.isEmpty() || pinString.isEmpty() || phoneString.isEmpty() || creditCardString.isEmpty() || cvvString.isEmpty() || dateString.isEmpty() || cardHolderString.isEmpty() || !(passwordString.equals(confirmPasswordString))) {

            if (firsNameString.isEmpty()) {
                firstName.setError("first name is Empty");
                firstName.requestFocus();
                return;
            } else if (lastNameString.isEmpty()) {
                lastName.setError("last name is Empty");
                lastName.requestFocus();
                return;
            } else if (userNameString.isEmpty()) {
                userName.setError("Username is Empty");
                userName.requestFocus();
                return;
            } else if (ageString.isEmpty()) {
                age.setError("Age is Empty");
                age.requestFocus();
                return;
            } else if (emailString.isEmpty()) {
                email.setError("Email is Empty");
                email.requestFocus();
                return;
            } else if (passwordString.isEmpty()) {
                password.setError("password is Empty");
                password.requestFocus();
                return;
            } else if (confirmPasswordString.isEmpty()) {
                confirmPassword.setError("Confirm Password is Empty");
                confirmPassword.requestFocus();
                return;
            } else if (!passwordString.equals(confirmPasswordString)) {
                confirmPassword.setError("Confirm Password is not equal password");
                confirmPassword.requestFocus();
                return;
            } else if (pinString.isEmpty()) {
                pin.setError("Pin is Empty");
                pin.requestFocus();
                return;
            } else if (phoneString.isEmpty()) {
                phone.setError("phone is Empty");
                phone.requestFocus();
                return;
            } else if (creditCardString.isEmpty()) {
                creditCard.setError("Credit Card is Empty");
                creditCard.requestFocus();
                return;
            } else if (cvvString.isEmpty()) {
                cvv.setError("cvv is Empty");
                cvv.requestFocus();
                return;
            } else if (dateString.isEmpty()) {
                date.setError("Date is Empty");
                date.requestFocus();
                return;
            } else if (cardHolderString.isEmpty()) {
                cardHolder.setError("Card Holder is Empty");
                cardHolder.requestFocus();
                return;
            }
        }
        else if (creditCardString.length()<19){
            creditCard.setError("Credit Card is less than 16");
            creditCard.requestFocus();
            return;
        }
        else if (pinString.length()<4){
            pin.setError("Pin should less than 4");
            pin.requestFocus();
            return;
        }
        else if (dateString.length()<5){
            date.setError("correct the Date");
            date.requestFocus();
            return;
        }

        else {
            userNameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("userNameString").equalTo(userNameString);
            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Toast.makeText(getApplicationContext(), "you inside in onDataChange", Toast.LENGTH_LONG).show();

                    if (dataSnapshot.getChildrenCount() > 0) {
                        first = false;
                        Toast.makeText(getApplicationContext(), "username is already exist", Toast.LENGTH_LONG).show();
                        userName.setError("Username is already exist");
                        userName.requestFocus();

                        return;
                    } else {
                        first = true;
                        cardNumberQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("creditCardString").equalTo(creditCardString);
                        cardNumberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    second = false;
                                    creditCard.setError("credit is already exist");
                                    creditCard.requestFocus();
                                    return;
                                } else {
                                    second = true;

                                    Toast.makeText(getApplicationContext(),Boolean.toString(first)+" "+Boolean.toString(second),Toast.LENGTH_LONG).show();

                                    if (first == true && second == true) {
                                        Toast.makeText(getApplicationContext(), "yes you entered the else", Toast.LENGTH_LONG).show();

                                        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    user = firebaseAuth.getCurrentUser();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Users user = new Users(firsNameString, lastNameString, userNameString, ageString, emailString, passwordString, confirmPasswordString, pinString, phoneString, creditCardString, cvvString, dateString, cardHolderString);
                                                                referenceUser.child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            Toast.makeText(getApplicationContext(), "register completed", Toast.LENGTH_LONG).show();
                                                                            OnlyPassword.passwordStatic = passwordString;
                                                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                            startActivity(intent);
                                                                            Toast.makeText(getApplicationContext(), "register completed", Toast.LENGTH_LONG).show();
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                                String userid = firebaseUser.getUid();

                                                                Wallet wallet = new Wallet("0",userid);

                                                                //////////////////////////////////////////////////////////////////////////////////////////
                                                                referenceWallet.child(userNameString).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            AccountBalance accountBalance = new AccountBalance(creditCardString,"0", "0");
                                                                            referenceCreditNumber.child(firebaseAuth.getCurrentUser().getUid()).setValue(accountBalance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                }
                                                                            });

                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            else{
                                                                Toast.makeText(getApplicationContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }



    private void initializeStringVariable() {

        firsNameString=firstName.getText().toString();
        lastNameString=lastName.getText().toString();
        userNameString=userName.getText().toString();
        ageString=age.getText().toString();
        emailString=email.getText().toString();
        passwordString=password.getText().toString();
        confirmPasswordString=confirmPassword.getText().toString();
        pinString=pin.getText().toString();
        phoneString=phone.getText().toString();
        creditCardString=creditCard.getText().toString();
        cvvString=cvv.getText().toString();
        dateString=date.getText().toString();
        cardHolderString=cardHolder.getText().toString();



    }

    private void setFonts(){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Bold.ttf");
        firstName.setTypeface(typeface);
        lastName.setTypeface(typeface);
        userName.setTypeface(typeface);
        age.setTypeface(typeface);
        email.setTypeface(typeface);
        password.setTypeface(typeface);
        confirmPassword.setTypeface(typeface);
        pin.setTypeface(typeface);
        creditCard.setTypeface(typeface);
        date.setTypeface(typeface);
        cvv.setTypeface(typeface);
        cardHolder.setTypeface(typeface);
        phone.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Regular.ttf");
        button1.setTypeface(typeface2);


    }

}
