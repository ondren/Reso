package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class ShowQrActivity extends Activity {
    ImageView iv_qr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_qr_activity);
        System.out.println("ShowQR"+"Activity " + " is working");
        iv_qr = findViewById(R.id.iv_qr);
        Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(view -> {
            finish();
        });

        String link = getIntent().getStringExtra("link");
        System.out.println("I got extras: " + link);
        generateQR(link);

    }

    private void generateQR(String link) {
        System.out.println("Creating QR using..." + link);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(link, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            iv_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
