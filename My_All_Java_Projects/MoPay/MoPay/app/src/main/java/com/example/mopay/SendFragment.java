package com.example.mopay;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mopay.notification.Client;
import com.example.mopay.notification.Data;
import com.example.mopay.notification.MyResponse;
import com.example.mopay.notification.Sender;
import com.example.mopay.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mopay.Notification1.CHANNEL_1_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment implements View.OnClickListener{


    EditText username;
    EditText amoutOfMoney;
    Button send;
    Button backSend;
    FirebaseUser fuser;

    String userName2;
    FirebaseDatabase database;
    DatabaseReference referenceUser,referenceCreditNumber,referenceWallet;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference3;


    Query userNameQuery;
    boolean first=false;

    DatabaseReference mWallet,mWallet2;
    String aLongWallet;
    private int walletBalance;

    String aLongWallet2;
    private int walletBalance2;

    private Map<String, Object> mapValue;
    Context context1;
    APIService apiService;

    public SendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context1=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_send, container, false);
        username=v.findViewById(R.id.username_send_fragment);
        amoutOfMoney=v.findViewById(R.id.amount_of_money_fragment);
        send=v.findViewById(R.id.send_fragment);
        backSend=v.findViewById(R.id.backSend);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        database=FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        referenceWallet=database.getReference("Wallet");

        DatabaseReference userWallRed=referenceWallet.child(OnlyPassword.usersStatic.getUserNameString());

        mWallet=userWallRed.child("amountWallet");
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

        send.setOnClickListener(this);
        ///////////////////////////////////
        backSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        /////////////////////////////////
        return v;
    }

    @Override
    public void onClick(View v) {

        final String amountMoney=amoutOfMoney.getText().toString();
        userName2=username.getText().toString();

        if (!userName2.isEmpty() && !amountMoney.isEmpty()) {
            if (!userName2.equals(OnlyPassword.usersStatic.getUserNameString())) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wallet").child(userName2);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            first = true;
//                        Toast.makeText(context1,first+"",Toast.LENGTH_LONG).show();
                            String valueUser2 = dataSnapshot.child("amountWallet").getValue(String.class);
//                        Toast.makeText(context1,valueUser2,Toast.LENGTH_LONG).show();

                            walletBalance2 = Integer.parseInt(valueUser2);
                            final int wantMoneyToSend = Integer.parseInt(amountMoney);
                            walletBalance = walletBalance - wantMoneyToSend;
                            walletBalance2 = walletBalance2 + wantMoneyToSend;
                            mWallet.setValue(walletBalance + "");
                            DatabaseReference reference2 = reference.child("amountWallet");
                            reference2.setValue(walletBalance2 + "");


                            sendNotification1( wantMoneyToSend);


                            reference3=reference.child("id");
                            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String userid=dataSnapshot.getValue(String.class);

                                    String msg = "your wallet increased "+walletBalance2;
                                    if (!msg.equals("")){
//                            Toast.makeText(getApplicationContext(),userid,Toast.LENGTH_LONG).show();

                                        sendNotifiaction(userid,"youssef", msg,wantMoneyToSend);
                                    } else {
                                        Toast.makeText(context1, "You can't send empty message", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            first = false;
                            Toast.makeText(context1, first + "", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    private void sendNotifiaction(String receiver, final String username, final String message, final int wantMoney){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Toast.makeText(getApplicationContext(),receiver,Toast.LENGTH_LONG).show();

        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Token token = snapshot.getValue(Token.class);

                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userid=dataSnapshot.getValue(String.class);
                           // Toast.makeText(context1,userid,Toast.LENGTH_LONG).show();

                            Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username +" send "+wantMoney+" "+message, "Send Money",
                                    userid);

                           // Toast.makeText(context1,token.getToken(),Toast.LENGTH_LONG).show();

                            Sender sender = new Sender(data, token.getToken());
                            apiService.sendNotification(sender)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.code() == 200){
                                                if (response.body().success != 1){
                                                    Toast.makeText(context1, "Failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {

                                        }
                                    });

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
    public void sendNotification1( int amount ){

        NotificationManager nm =(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap wd = BitmapFactory.decodeResource(SendFragment.this.getResources(),R.drawable.send_money_icon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SendFragment.this.getActivity(),CHANNEL_1_ID)

                .setContentTitle("Send Money")
                .setContentText("you send " + amount +" to "+ userName2 )
                .setSmallIcon(R.drawable.send_money_icon)
                .setLargeIcon(wd)
                .setNumber(1);

                      /*  builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                        builder.setVibrate(new long[]{500,1000,500,1000,500});
                        builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.soundnotification));*/

        nm.notify(1,builder.build());
        Intent resultIntent = new Intent(SendFragment.this.getActivity(), IconTabsActivity.class);
        builder.setAutoCancel(true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(SendFragment.this.getActivity());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(SendFragment.this.getActivity());
        notificationManager.notify(1, builder.build());
    }
}


