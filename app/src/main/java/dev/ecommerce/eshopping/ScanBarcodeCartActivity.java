package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import dev.ecommerce.eshopping.Model.Order;
import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class ScanBarcodeCartActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView text_code,re_scan;
    private Button btn_confirm_cart;
    private BarcodeDetector barcodeDetector;

    private String id_order;
    private String phone;
    private String cart,dateid,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode_cart);

        surfaceView = findViewById(R.id.camera_preview);
        text_code = findViewById(R.id.text_code);
        btn_confirm_cart = findViewById(R.id.confirm_cart);
        re_scan = findViewById(R.id.re_scan);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateid = dateFormat1.format(calendar.getTime());
        date = dateFormat2.format(calendar.getTime());

        re_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_code.setText(null);

            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(
                        Barcode.QR_CODE
                ).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480).setAutoFocusEnabled(true).build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                }catch (IOException e){
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
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();

                if (qrcode.size() != 0) {
                    text_code.post(new Runnable() {
                        @Override
                        public void run() {
                            text_code.setText(qrcode.valueAt(0).displayValue);
                            cart = qrcode.valueAt(0).displayValue;

                            id_order = Prevalent.currentOnlineUser.getPhone()+qrcode.valueAt(0).displayValue+dateid;
                        }
                    });
                }
            }
        });

        phone = Prevalent.currentOnlineUser.getPhone();


        btn_confirm_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_code == null) {
                    Toast.makeText(ScanBarcodeCartActivity.this, "Scan QR code on cart.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(ScanBarcodeCartActivity.this, ListActivity.class);
                    intent.putExtra("cart", cart);
                    startActivity(intent);
                    AddOrders(phone, cart, id_order,date);
                }
            }
        });

//
    }

    private void AddOrders(final String phone, final String cart, final String id, final String date) {
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Orders").child(id).exists())) {
                    HashMap<String, Object> orderMap = new HashMap<>();
                    orderMap.put("id_order", id);
                    orderMap.put("phone", phone);
                    orderMap.put("cart_id", cart);
                    orderMap.put("date", date);

                    ref.child("Orders").child(id).updateChildren(orderMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ScanBarcodeCartActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ScanBarcodeCartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
