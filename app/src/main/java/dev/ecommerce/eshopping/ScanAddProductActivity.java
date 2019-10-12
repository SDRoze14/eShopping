package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import dev.ecommerce.eshopping.Model.Product;

public class ScanAddProductActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView txt_product_code;
    private Button btn_confirm_pcode;
    private BarcodeDetector barcodeDetector;
    private String pcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_add_product);

        surfaceView = findViewById(R.id.camera_add_product);
        txt_product_code = findViewById(R.id.txt_product_code);
        btn_confirm_pcode = findViewById(R.id.confirm_code_product);

        barcodeDetector = new BarcodeDetector.Builder(this)
            .setBarcodeFormats(
                Barcode.CODE_128
            ).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(640,480).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();

                if (barcode.size() != 0) {
                    txt_product_code.post(new Runnable() {
                        @Override
                        public void run() {
                            txt_product_code.setText(barcode.valueAt(0).displayValue);
                            pcode = barcode.valueAt(0).displayValue;

                        }
                    });
                }

            }
        });

        btn_confirm_pcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanAddProductActivity.this, StoreAddProductActivity.class);
                intent.putExtra("code", pcode);
                startActivity(intent);
            }
        });
    }
}
