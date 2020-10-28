package com.example.dlpbgj;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 *  https://www.youtube.com/watch?v=MegowI4T_L8
 */

public class barcode_scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final String varText = "com.example.dlpbgj.MESSAGE2";
    private ZXingScannerView scanB;
    private TextView seeResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);

        // Initialising the scanner and text view on the scanner activity
        scanB = (ZXingScannerView)findViewById(R.id.zxscan);
        seeResult = (TextView)findViewById(R.id.txt_result);


        //Asking permission for opening the camera
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scanB.setResultHandler(barcode_scanner.this);
                        scanB.startCamera();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(barcode_scanner.this,"You must accept the permission",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    /**
     * Allows camera activity to shut down
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanB.stopCamera();
    }

    /**
     * gets and sets the ISBN
     * shows the ISBN on HomePage
     * @param isbnCode
     */
    @Override
    public void handleResult(Result isbnCode) {
        seeResult.setText(isbnCode.getText());
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        intent.putExtra(varText, isbnCode.getText());
        startActivity(intent);
        // scannerView.startCamera();
    }


}
