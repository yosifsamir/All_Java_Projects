package com.example.mopay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    TextView aboutMe , Card , Security ;
    ViewPager viewPager ;
    PagerViewAdapter pagerViewAdapter;
    static int PReq=1;
    static int REQUEST=1;
    static Uri pickedImgUri;

    FirebaseUser user;
    Button uploadImage;
    ImageView imgProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.profile_fragment, container, false);


        aboutMe = (TextView)v.findViewById(R.id.about_me);
        Card = (TextView)v.findViewById(R.id.card);
        Security = (TextView)v.findViewById(R.id.security);
        imgProfile=v.findViewById(R.id.image_profile);

        user= FirebaseAuth.getInstance().getCurrentUser();
//        Uri uriProfile=user.getPhotoUrl();
//
//        imgProfile.setImageURI(uriProfile);
//        if (ProfileFragment.pickedImgUri==null)


        if(user.getPhotoUrl()!=null){
            Glide.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(imgProfile);
        }
//        else
//            Glide.with(getActivity().getApplicationContext()).load(ProfileFragment.pickedImgUri).into(imgProfile);


        viewPager =(ViewPager)v.findViewById(R.id.fragment_container);

        uploadImage=v.findViewById(R.id.btn_upload);
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Black.ttf");
        uploadImage.setTypeface(typeface);

        uploadImage.setOnClickListener(this);

        aboutMe.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                viewPager.setCurrentItem(0);
            }
        });

        Card.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                viewPager.setCurrentItem(1);
            }
        });

        Security.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                viewPager.setCurrentItem(2);
            }
        });

        pagerViewAdapter = new PagerViewAdapter(getFragmentManager());


        viewPager.setAdapter(pagerViewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetpixels) {

            }

            @Override
            public void onPageSelected(int position) {

                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v ;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {
        if (position == 0) {
            aboutMe.setTextSize(15);
            aboutMe.setTextColor(getResources().getColor(R.color.bright_color));

            Card.setTextSize(10);
            Card.setTextColor(getResources().getColor(R.color.light_color));

            Security.setTextSize(10);
            Security.setTextColor(getResources().getColor(R.color.light_color));
        }
        if (position == 1) {
            aboutMe.setTextSize(10);
            aboutMe.setTextColor(getResources().getColor(R.color.light_color));

            Card.setTextSize(15);
            Card.setTextColor(getResources().getColor(R.color.bright_color));

            Security.setTextSize(10);
            Security.setTextColor(getResources().getColor(R.color.light_color));
        }
        if (position == 2) {
            aboutMe.setTextSize(10);
            aboutMe.setTextColor(getResources().getColor(R.color.light_color));

            Card.setTextSize(10);
            Card.setTextColor(getResources().getColor(R.color.light_color));

            Security.setTextSize(15);
            Security.setTextColor(getResources().getColor(R.color.bright_color));


        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity().getApplicationContext(),"you clicked on Upload image",Toast.LENGTH_SHORT).show();

            if(Build.VERSION.SDK_INT >= 22){
                checkAndRequestPermission();
            }
            else{
                openGallar();
            }



    }

    private void checkAndRequestPermission() {

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity().getApplicationContext(),"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},PReq);
            }

        }
        else{
            openGallar();
        }


    }

    private void openGallar() {
        Intent gallary=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        gallary.setType("image/*");
        startActivityForResult(gallary,REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQUEST && data!=null){
            pickedImgUri=data.getData();
            imgProfile.setImageURI(pickedImgUri);

        }

    }
}