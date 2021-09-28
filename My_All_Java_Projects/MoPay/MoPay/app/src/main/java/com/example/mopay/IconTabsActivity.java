package com.example.mopay;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mopay.notification.Client;
import com.example.mopay.notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.example.mopay.Notification1.CHANNEL_1_ID;



public class IconTabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ButtonsFragment.Communicator {

    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    boolean aBoolean=true;
    ImageView imageView2;

    private IntentIntegrator qrScan;


    FirebaseUser fuser;
    FirebaseDatabase database;
    DatabaseReference referenceUser,referenceCreditNumber,referenceWallet;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    private String ownUserName, transactionAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        database=FirebaseDatabase.getInstance();
        referenceUser=database.getReference("Users");
        referenceCreditNumber=database.getReference("Credit_Number");
        referenceWallet=database.getReference("Wallet");
//        referenceCard=database.getReference("Card");
        firebaseAuth=FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        updateToken(FirebaseInstanceId.getInstance().getToken());


        referenceUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() ) {
                    OnlyPassword.usersStatic = dataSnapshot.child(firebaseAuth.getUid()).getValue(Users.class);
                    Toast.makeText(getApplicationContext(), "Read User data is done", Toast.LENGTH_LONG).show();
                    referenceUser.removeEventListener(this);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceCreditNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OnlyPassword.accountBalanceStatic=dataSnapshot.child(OnlyPassword.usersStatic.getCreditCardString()).getValue(AccountBalance.class);
                referenceCreditNumber.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        SharedPreferences mPrefrences;
        SharedPreferences.Editor mEditor;

        mPrefrences = getApplicationContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        mEditor = mPrefrences.edit();

        switch (item.getItemId()) {

            case R.id.nav_contact:
                Intent a = new Intent(getApplicationContext(),ContactActivity.class);
                startActivity(a);
                break;

            case R.id.nav_about:
                Intent b = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(b);
                break;

            case R.id.nav_Logout:

              //  FirebaseAuth.getInstance().signOut();


                mEditor.putString(getString(R.string.Checkbox), "false");
                mEditor.commit();

                mEditor.putString(getString(R.string.Email), null);
                mEditor.commit();

                mEditor.putString(getString(R.string.Password), null);
                mEditor.commit();

//                mEditor.remove("Checkbox");
//                mEditor.remove("Email");
//                mEditor.remove("Password");
//                mEditor.commit();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            if(imageView2!=null)
            imageView2.setVisibility(View.INVISIBLE);
        }


    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.home_icon_1,
                R.drawable.profile,
                R.drawable.notification_icon_1


        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#4FBA6F"), PorterDuff.Mode.SRC_IN);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#4FBA6F"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#959DAD"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "ONE");
        adapter.addFrag(new ProfileFragment(), "TWO");
        adapter.addFrag(new NotificationFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void comm(ImageView imageView) {
        imageView2=imageView;
        if(imageView!=null)
        if (aBoolean==false){

        }

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    public void deposit (View view){
        Intent intent=new Intent(getApplicationContext(),DepositActivity.class);
        startActivity(intent);

    }
    public void withdraw (View view){
        Intent intent=new Intent(getApplicationContext(),WithdrawActivity.class);
        startActivity(intent);

    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Transaction Incomplete", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    String[] separatedMessage = result.getContents().split(" ");
                    final String userName2 = separatedMessage[0];
                    final String tranAmount = separatedMessage[1];
                    transactionAmount = tranAmount;
                    //Log.i("The sender is", senderPhone);
                    //Log.i("The amount is", tranAmount);

//                    if(tranAmount.contains(".")){
//                        String[] separatedAmount = tranAmount.split("\\.");
//                        String inte = separatedAmount[0];
//                        String deci = separatedAmount[1];
//                        //Log.i("The amount is", inte);
//                        //Log.i("The amount is", deci);
//                        int integ = Integer.parseInt(inte);
//                        int decimal = Integer.parseInt(deci);
//                        if(deci.length() == 1){decimal = decimal * 10;}
//                        int total = integ*100 + decimal;
//                        Log.i("The amount is", total+"");
//                        transactBalance(ownPhone, senderPhone, total);
//                    }


                        int total = Integer.parseInt(tranAmount) /** 100*/;
                        transactBalance(OnlyPassword.usersStatic.getUserNameString(), userName2, total);

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Transfer Amount  fromPhone -> toPhone
     * @param phoneToPay
     * @param phoneToReceive
     * @param Amount
     */
    public void transactBalance(String phoneToPay, String phoneToReceive, final int Amount){
        //get the database ref from  fromPhone
        DatabaseReference mFromUser = referenceWallet.child(phoneToPay);
        final DatabaseReference mFromBalance = mFromUser.child("amountWallet");
        //get the database ref  from toPhone
        DatabaseReference mToUser = referenceWallet.child(phoneToReceive);
        final DatabaseReference mToBalance = mToUser.child("amountWallet");
        final String sender = phoneToPay;
        final String receiver = phoneToReceive;

        mFromBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retrieve the balance for fromPhone from database
                int fromBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                if(sender.equals(receiver)){Toast.makeText(getApplicationContext(), "If you do it again, I'll call 999",Toast.LENGTH_SHORT).show(); return;}
                if(fromBalance >= Amount) {   // if enough money, deduct the value
                    fromBalance = fromBalance - Amount;
                }else{
                    Toast.makeText(getApplicationContext(), "Not enough money",Toast.LENGTH_SHORT).show();
                    return;
                }
                final int fromBalanceCopy = fromBalance;
                //retrieve the balance for toPhone from database
                mToBalance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int toBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                        toBalance = toBalance + Amount;   //add value
                        mFromBalance.setValue(fromBalanceCopy+"");   //update the balance for both account to database
                        mToBalance.setValue(toBalance + "");


                        sendNotification(Amount,receiver);

                        //inform user transaction has been done
//                        AlertDialog.Builder resultBuilder = new AlertDialog.Builder(getApplicationContext());
//                        resultBuilder
//                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                })
//                                .setCancelable(true)
//                                .setTitle("Transaction Complete")
//                                .setMessage("You paid " + transactionAmount + " dollar(s)");
//                        AlertDialog resultDialog = resultBuilder.create();
//                        resultDialog.show();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    public void sendNotification(int Amount,String receiver){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap q = BitmapFactory.decodeResource(IconTabsActivity.this.getResources(), R.drawable.qr_icon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(IconTabsActivity.this, CHANNEL_1_ID)

                .setContentTitle("scan QR")
                .setContentText("you send (" + Amount + ") to " + receiver)
                .setSmallIcon(R.drawable.qr_icon)
                .setLargeIcon(q)
                .setNumber(1);


        nm.notify(1, builder.build());

        Intent resultIntent = new Intent(IconTabsActivity.this, IconTabsActivity.class);

        builder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(IconTabsActivity.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(IconTabsActivity.this);
        notificationManager.notify(1, builder.build());

    }


}
