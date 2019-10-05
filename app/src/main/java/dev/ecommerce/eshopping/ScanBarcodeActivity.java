package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import dmax.dialog.SpotsDialog;

public class ScanBarcodeActivity extends AppCompatActivity {

    private CameraView camera_scan_barcode;
    private Button btn_detect_barcode;
    private AlertDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();
        camera_scan_barcode.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera_scan_barcode.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        camera_scan_barcode = findViewById(R.id.camera_view);
        btn_detect_barcode = findViewById(R.id.btn_detect_barcode);
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait")
                .setCancelable(false)
                .build();

        btn_detect_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_scan_barcode.start();
                camera_scan_barcode.captureImage();
            }
        });

        camera_scan_barcode.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                dialog.show();
                Bitmap bitmap =cameraKitImage.getBitmap();
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }
}
