package com.example.mopay;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.mopay.Notification1.CHANNEL_1_ID;


public class WithdrawActivity extends AppCompatActivity {

    private EditText accountNumber;
    private EditText amountOfMoney;
    private Button withdraw;
    private Button back;
    private TextView largeWithdraw;

    FirebaseDatabase database;
    DatabaseReference referenceUser,referenceCreditNumber,referenceWallet;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    DatabaseReference mBalance;
    DatabaseReference mWallet;
    DatabaseReference creditNumberPhone;


    String accountNumberString;


    String balance;
    String aLongWallet;

    private int actualBalance;
    private int walletBalance;
    private int wantBalanceToAdd;
    final int SEND_SMS_PERMISSION_REQUEST_CODE=1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        accountNumber=findViewById(R.id.account_number_withdraw);
        amountOfMoney=findViewById(R.id.amount_of_money_withdraw);
        withdraw=findViewById(R.id.withdraw);
        back=findViewById(R.id.backWithdraw);
        largeWithdraw=findViewById(R.id.largeWithdraw);
        setFonts();
        firebaseAuth=FirebaseAuth.getInstance();

        if(checkPermission(Manifest.permission.SEND_SMS)){

        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        database=FirebaseDatabase.getInstance();
        referenceCreditNumber=database.getReference("Credit_Number");
        referenceUser=database.getReference("Users");
        referenceWallet=database.getReference("Wallet");

        DatabaseReference userCredRef=referenceCreditNumber.child(firebaseAuth.getCurrentUser().getUid());
        DatabaseReference userWallRed=referenceWallet.child(OnlyPassword.usersStatic.getUserNameString());

        mBalance=userCredRef.child("balance");
        creditNumberPhone=userCredRef.child("creditPhoneNumber");
        mWallet=userWallRed.child("amountWallet");


        firebaseAuth=FirebaseAuth.getInstance();

        accountNumber.setText(OnlyPassword.usersStatic.getCreditCardString());
        accountNumberString=OnlyPassword.usersStatic.getCreditCardString();


        mBalance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balance=dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(),balance.toString(),Toast.LENGTH_LONG).show();
                actualBalance=Integer.parseInt(balance);
//                mBalance.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mWallet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aLongWallet=dataSnapshot.getValue(String.class);
                walletBalance=Integer.parseInt(aLongWallet);
//                Toast.makeText(getApplicationContext(),aLongWallet.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String amountMoney=amountOfMoney.getText().toString();
                if(!amountMoney.equals("")){
                    wantBalanceToAdd=Integer.parseInt(amountMoney);
                    if (actualBalance<wantBalanceToAdd){
                        Toast.makeText(getApplicationContext(),"the balance in your credit card is less than the amount of money that you want to add to your wallet ",Toast.LENGTH_LONG).show();
                    }
                    else {
                        actualBalance = actualBalance - wantBalanceToAdd;
                        walletBalance=walletBalance+wantBalanceToAdd;
                        mBalance.setValue(actualBalance+"");
                        mWallet.setValue(walletBalance+"");

                        sendNotification();

                        creditNumberPhone.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String creditPhoneString=dataSnapshot.getValue(String.class);
                                if (creditPhoneString==null || creditPhoneString.length()==0){
                                    return;
                                }
                                if(checkPermission(Manifest.permission.SEND_SMS)){
                                    SmsManager smsManager=SmsManager.getDefault();
                                    smsManager.sendTextMessage(creditPhoneString,null,"has been discount "+amountMoney+" and now your account is "+mBalance,null,null);
                                    Toast.makeText(getApplicationContext(),"message sent",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"permission denited",Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"EditText is Empty or there is some error",Toast.LENGTH_LONG).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithdrawActivity.super.onBackPressed();
            }
        });

    }

    boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(getApplicationContext(),permission);
        return check== PackageManager.PERMISSION_GRANTED;
    }

    public void setFonts(){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Bold.ttf");
        amountOfMoney.setTypeface(typeface);
        accountNumber.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Black.ttf");
        withdraw.setTypeface(typeface2);
        back.setTypeface(typeface2);
        largeWithdraw.setTypeface(typeface2);
    }

    public void sendNotification(){

        NotificationManager nm =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap wd = BitmapFactory.decodeResource(WithdrawActivity.this.getResources(),R.drawable.with_draw_icon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(WithdrawActivity.this,CHANNEL_1_ID)

                .setContentTitle("Withdraw")
                .setContentText("your money in wallet increase " + wantBalanceToAdd  )
                .setSmallIcon(R.drawable.with_draw_icon)
                .setLargeIcon(wd)
                .setNumber(1);

        nm.notify(1,builder.build());


        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setVibrate(new long[]{500,1000,500,1000,500});
        builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.soundnotification));

        Intent resultIntent = new Intent(WithdrawActivity.this, IconTabsActivity.class);

        builder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(WithdrawActivity.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(WithdrawActivity.this);
        notificationManager.notify(1, builder.build());


    }
}
