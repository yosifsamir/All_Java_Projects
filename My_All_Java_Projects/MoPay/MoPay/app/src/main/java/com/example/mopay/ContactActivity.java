package com.example.mopay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText name,email,phone,message;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        name=findViewById(R.id.contact_name);
        email=findViewById(R.id.contact_email);
        phone=findViewById(R.id.contact_phone);
        message=findViewById(R.id.contact_message);
        submit=findViewById(R.id.contact_submit);

        submit.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        String nameString=name.getText().toString();
        String emailString=email.getText().toString();
        String phoneString=phone.getText().toString();
        String messageString=message.getText().toString();



        if (TextUtils.isEmpty(nameString)){
            name.setError("Enter Your Name");
            name.requestFocus();
            return;
        }

        Boolean onError = false;
        if (!isValidEmail(emailString)) {
            onError = true;
            email.setError("Invalid Email");
            return;
        }

        if (TextUtils.isEmpty(phoneString)){
            phone.setError("Enter Your Subject");
            phone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(messageString)){
            message.setError("Enter Your Message");
            message.requestFocus();
            return;
        }

        Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        sendEmail.setType("plain/text");
        sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"projectgrad123@gmail.com"});
//        sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                "name:"+nameString+'\n'+"Email ID:"+emailString+'\n'+"phone:"+phoneString+'\n'+"Message:"+'\n'+messageString);

        /* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(sendEmail, "Send mail..."));


    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
