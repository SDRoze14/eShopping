package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class PaymentActivity extends AppCompatActivity {

    private Button btn;
    private ImageView close;
    private TextView id_order, total_price, money_bill;
    private String order_id, tprice, txt_money;
    private Float money;
//    private float ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btn = findViewById(R.id.b1);
        close = findViewById(R.id.close);
        id_order = findViewById(R.id.id_order);
        total_price = findViewById(R.id.total_price);
        money_bill = findViewById(R.id.money_bill);

        order_id = getIntent().getStringExtra("Order ID");
        tprice = getIntent().getStringExtra("Total Price");

        id_order.setText("Order ID: "+order_id);
        total_price.setText(tprice);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, BillActivity.class));
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    money = Float.valueOf(dataSnapshot.child("money").getValue().toString());
                    txt_money = String.valueOf(money);
                    money_bill.setText(txt_money);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
