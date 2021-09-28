package com.example.mopay;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotificationFragment extends Fragment {

    FirebaseUser user;
    ImageView imgProfileNotification;
    TextView emailNotification;

    TextView emailAddress;
    TextView weWellLet;
    TextView makeApayment;
    TextView startApayment;
    TextView haveAproblem;
    TextView receiveApeyment;

    public NotificationFragment() {
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
        View v= inflater.inflate(R.layout.notification_fragment, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        imgProfileNotification=v.findViewById(R.id.circleImageNotification);
        emailNotification=v.findViewById(R.id.emailAddressNotification);
        emailNotification.setText(user.getEmail());

        emailAddress=v.findViewById(R.id.email_address);
        weWellLet=v.findViewById(R.id.we_well_let);
        makeApayment=v.findViewById(R.id.notification_make_payment);
        startApayment=v.findViewById(R.id.notification_start_a_payment);
        haveAproblem=v.findViewById(R.id.notification_i_have_a_problem);
        receiveApeyment=v.findViewById(R.id.receive_a_payment);

        setFonts();

        if(user.getPhotoUrl()!=null){
            Glide.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(imgProfileNotification);
        }

        return v;
    }

    public void setFonts(){
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Black.ttf");
        weWellLet.setTypeface(typeface);

        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro-Bold.ttf");
        makeApayment.setTypeface(typeface2);
        startApayment.setTypeface(typeface2);
        haveAproblem.setTypeface(typeface2);
        receiveApeyment.setTypeface(typeface2);
        emailNotification.setTypeface(typeface2);
        emailAddress.setTypeface(typeface2);
    }
}
