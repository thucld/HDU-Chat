package vn.hdu.go2jp.hduchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;


public class ScannerActivity extends Activity {//implements ZXingScannerView.ResultHandler
    //    private ZXingScannerView mScannerView;
    private CompoundBarcodeView barcodeView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        IntentIntegrator integrator = new IntentIntegrator(this);
        barcodeView.initializeFromIntent(integrator.createScanIntent());
        findViewById(R.id.btnMyQrCode).setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), MyQrActivity.class);
            startActivity(intent);
        });
        barcodeView.setStatusText("");
//        mScannerView = new ZXingScannerView(this);    // Programmatically initialize the scanner view
//
//        RelativeLayout rl = findViewById(R.id.rlScanner);
//        rl.addView(mScannerView);
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//        mScannerView.setSoundEffectsEnabled(true);
//        mScannerView.setAutoFocus(true);              // Set the scanner view as the content view

    }


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                FireBaseUtil.getInstance().addContact(result.getText(), roomId -> {
                    Intent intent = new Intent(getApplication(), ChatBoxActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConst.KEY_ROOM_ID, roomId);
                    bundle.putString(AppConst.KEY_ROOM_TITLE,"");
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            //Do something with code result
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        mScannerView.startCamera();          // Start camera on resume
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mScannerView.stopCamera();           // Stop camera on pause
        barcodeView.pause();
        super.onPause();
    }

//    @Override
//    public void handleResult(Result result) {
//        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(
//                qrInputText,
//                null,
//                Contents.Type.TEXT,
//                BarcodeFormat.QR_CODE.toString(),
//                smallerDimension
//        );
//        try {
//            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//            ImageView myImage = (ImageView) findViewById(R.id.ivImage);
//            myImage.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }
}
