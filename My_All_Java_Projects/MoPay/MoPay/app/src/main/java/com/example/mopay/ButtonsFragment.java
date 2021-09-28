package com.example.mopay;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonsFragment extends Fragment implements View.OnClickListener{

    Button sendButton;
    Button qrButton;
    Button reqButton;

    Communicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator=(Communicator)context;
    }

    public ButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_buttons, container, false);

        sendButton=view.findViewById(R.id.send_money);
        qrButton=view.findViewById(R.id.scan_qr);
        reqButton=view.findViewById(R.id.request_money);
        sendButton.setOnClickListener(this);
        qrButton.setOnClickListener(this);
        reqButton.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.send_money) {

            SendFragment sendFragment;
            sendFragment = new SendFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.inside_relative, sendFragment, "send_fragment")
                    .addToBackStack("send_fragment")
                    .commit();

            FrameLayout frameLayout = getActivity().findViewById(R.id.frame_layout);

            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setImageResource(R.drawable.send_money_icon);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
            imageView.setLayoutParams(layoutParams);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
//            imageView.setId();
            frameLayout.addView(imageView);
            frameLayout.setForegroundGravity(Gravity.CENTER);


            communicator.comm(imageView);
        }

        else if(v.getId()==R.id.scan_qr){

            /*QrFragment qrFragment;
            qrFragment = new QrFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.inside_relative, qrFragment, "qr_fragment")
                    .addToBackStack("qr_fragment")
                    .commit();

            FrameLayout frameLayout = getActivity().findViewById(R.id.frame_layout);

            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setImageResource(R.drawable.qr_icon);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
            imageView.setLayoutParams(layoutParams);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            frameLayout.addView(imageView);
            frameLayout.setForegroundGravity(Gravity.CENTER);

            communicator.comm(imageView);
*/
            IntentIntegrator integrator = new IntentIntegrator(ButtonsFragment.this.getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();


        }

        else if(v.getId()==R.id.request_money){

            QrFragment qrFragment;
            qrFragment = new QrFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.inside_relative, qrFragment, "qr_fragment")
                    .addToBackStack("qr_fragment")
                    .commit();

            FrameLayout frameLayout = getActivity().findViewById(R.id.frame_layout);

            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setImageResource(R.drawable.qr_icon);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
            imageView.setLayoutParams(layoutParams);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            frameLayout.addView(imageView);
            frameLayout.setForegroundGravity(Gravity.CENTER);

            communicator.comm(imageView);



        }


    }
    interface Communicator{
        public void comm(ImageView imageView);

    }
}
