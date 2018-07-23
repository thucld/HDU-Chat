package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import vn.hdu.go2jp.hduchat.R;

public class MyQrActivity extends AppCompatActivity {
    private ImageView imMyQrCode;
    private Button btnQrScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        imMyQrCode = findViewById(R.id.imMyQrCode);
        btnQrScanner = findViewById(R.id.btnQrScanner);
        btnQrScanner.setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), ScannerActivity.class);
            startActivity(intent);
        });
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(FirebaseAuth.getInstance().getUid(), BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = findViewById(R.id.imMyQrCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (Exception e) {

        }
    }
}
