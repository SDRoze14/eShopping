package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private ImageView qr_payment;
    private EditText input_phone_payment;
    private Button confirm_payment;
    private String input_phone;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        qr_payment = findViewById(R.id.qr_payment);
        input_phone_payment = findViewById(R.id.input_phone_payment);
        confirm_payment = findViewById(R.id.confirm_payment);

        confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone = input_phone_payment.getText().toString().trim();
                if (input_phone.length() > 0){
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            input_phone, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qr_payment.setImageBitmap(bitmap);
                        input_phone_payment.setText(null);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(ConfirmPaymentActivity.this, "กรุณากรอกหมายเลขโทรศัพท์", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
