package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.HashMap;

import dev.ecommerce.eshopping.Prevalent.Prevalent;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanPaymentActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private String order_id, tprice ,m;

    private Float money, balance, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        order_id = getIntent().getStringExtra("Order ID");
        tprice = getIntent().getStringExtra("Total Price");
        m = getIntent().getStringExtra("Money");

        price = Float.valueOf(tprice);
        money = Float.valueOf(m);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
            }else {
                requestPermissions();
            }
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScanPaymentActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                dispalyAlertMessage("You need to allow access for both permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        scannerView.stopCamera();
    }

    public void dispalyAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ScanPaymentActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    public void handleResult(Result result) {
        final String scan_result = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment");
        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = Prevalent.currentOnlineUser.getPhone();
                if (!scan_result.equals( number)) {

                    Toast.makeText(ScanPaymentActivity.this, "หมายเลขไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ScanPaymentActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else {

                    Intent intent = new Intent(ScanPaymentActivity.this, BillActivity.class);
                    intent.putExtra("order_id",order_id);
                    intent.putExtra("total_price", String.valueOf(tprice));
                    startActivity(intent);

                    balance = money - price;


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Prevalent.currentOnlineUser.getPhone());

                    HashMap<String, Object> moneyMap = new HashMap<>();
                    moneyMap.put("money", balance);
                    reference.updateChildren(moneyMap);

                }
            }
        });
        builder.setNeutralButton("สแกนอีกครั้ง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ScanPaymentActivity.this);
            }
        });
        builder.setMessage(String.valueOf(scan_result));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
