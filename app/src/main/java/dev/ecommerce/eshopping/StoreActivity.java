package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dev.ecommerce.eshopping.Model.User;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class StoreActivity extends AppCompatActivity {

    private LinearLayout  btn_add_new_product, btn_view_product, btn_scan_bill, btn_promotion, counter ,promotion_list;
    private TextView name_use_store;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btn_logout = findViewById(R.id.btn_logout);
        btn_add_new_product = findViewById(R.id.btn_add_new_product);
        btn_view_product = findViewById(R.id.btn_view_product);
        btn_scan_bill = findViewById(R.id.btn_scan_bill);
        name_use_store = findViewById(R.id.name_use_store);
        btn_promotion = findViewById(R.id.promotion);
        counter = findViewById(R.id.counter);
        promotion_list = findViewById(R.id.promotion_list);

        name_use_store.setText(Prevalent.currentOnlineUser.getName());

        btn_add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StoreActivity.this, StoreAddProductActivity.class);
                startActivity(intent1);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent2 = new Intent(StoreActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });

        btn_view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(StoreActivity.this, StoreViewListActivity.class);
                startActivity(intent3);
            }
        });

        btn_scan_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreActivity.this, "Scan bill", Toast.LENGTH_SHORT).show();
            }
        });

        btn_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, PromotionActivity.class);
                startActivity(intent);
            }
        });

        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, ConfirmPaymentActivity.class));
            }
        });

        promotion_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, PromotionListActivity.class));
            }
        });
    }
}
