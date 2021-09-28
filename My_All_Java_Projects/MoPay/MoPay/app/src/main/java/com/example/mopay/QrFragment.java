package com.example.mopay;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class QrFragment extends Fragment implements View.OnClickListener {

    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    public final static int WIDTH = 500;
    private boolean isFirstEntered = true;



    Button creatQr;
    Button backQR;

    EditText editText;
    Bitmap bitmap ;
    String  EditTextValue;
    public final static int QRcodeWidth = 70;
    ImageView imageView;
    FirebaseUser user;

    String ownUID;
    DatabaseReference referenceWallet;
    DatabaseReference mWallet;
    DatabaseReference mUser;
    FirebaseAuth mAuth;
    private String userName2;

    String amountOfWallet;

    Context context1;
    public QrFragment() {
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
        View view= inflater.inflate(R.layout.fragment_qr, container, false);

        creatQr=view.findViewById(R.id.create_qr);
        editText=view.findViewById(R.id.amount_of_money);
        imageView=view.findViewById(R.id.qr_image);
        backQR=view.findViewById(R.id.backQR);

        user= FirebaseAuth.getInstance().getCurrentUser();

        referenceWallet=FirebaseDatabase.getInstance().getReference("Wallet");
        mUser=FirebaseDatabase.getInstance().getReference("Users");

        DatabaseReference mCurrentUser = referenceWallet.child(OnlyPassword.usersStatic.getUserNameString());
        DatabaseReference mBalance = mCurrentUser.child("amountWallet");

        mBalance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isFirstEntered) {
                    isFirstEntered = false;
                }
                else if(!isFirstEntered){
                    AlertDialog.Builder resultBuilder = new AlertDialog.Builder(context1);
                    resultBuilder
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent goBackTransaction = new Intent(getActivity().getApplicationContext(), IconTabsActivity.class);
                                    startActivity(goBackTransaction);
                                }
                            })
                            .setCancelable(true)
                            .setTitle("Transaction Complete")
                            .setMessage("Your balance will be updated");
                    AlertDialog resultDialog = resultBuilder.create();
                    resultDialog.show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Transaction.this, "Cannot find your balance",Toast.LENGTH_SHORT).show();
            }
        });


        creatQr.setOnClickListener(this);
        backQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if(!editText.getText().toString().isEmpty()){
            EditTextValue = editText.getText().toString();

            String tranMessage = OnlyPassword.usersStatic.getUserNameString() + " " + EditTextValue;
            Toast.makeText(getActivity().getApplicationContext(),tranMessage,Toast.LENGTH_LONG).show();
            Log.i("The string is ", tranMessage);
            try {
                Bitmap bitmap = encodeAsBitmap(tranMessage);
                imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
//
//            try {
//                bitmap = TextToImageEncode(EditTextValue);
//
//                imageView.setImageBitmap(bitmap);
//
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }

        }
        else{
            editText.requestFocus();
            Toast.makeText(getActivity().getApplicationContext(), "Please Enter Your Scanned Test" , Toast.LENGTH_LONG).show();
        }
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {

//        BitMatrix bitMatrix;
//        try {
//            bitMatrix = new MultiFormatWriter().encode(
//                    Value,
//                    BarcodeFormat.DATA_MATRIX.QR_CODE,
//                    QRcodeWidth, QRcodeWidth, null
//            );
//
//        } catch (IllegalArgumentException Illegalargumentexception) {
//
//            return null;
//        }
//        int bitMatrixWidth = bitMatrix.getWidth();
//
//        int bitMatrixHeight = bitMatrix.getHeight();
//
//        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
//
//        for (int y = 0; y < bitMatrixHeight; y++) {
//            int offset = y * bitMatrixWidth;
//
//            for (int x = 0; x < bitMatrixWidth; x++) {
//
//                pixels[offset + x] = bitMatrix.get(x, y) ?
//                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
//
//        bitmap.setPixels(pixels, 0, 70, 0, 0, bitMatrixWidth, bitMatrixHeight);
//        return bitmap;

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }




//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null) {
//                Log.e("Scan*******", "Cancelled scan");
//
//            } else {
//                Log.e("Scan", "Scanned");
//
////                tv_qr_readTxt.setText(result.getContents());
////                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            // This is important, otherwise the result will not be passed to the fragment
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}
