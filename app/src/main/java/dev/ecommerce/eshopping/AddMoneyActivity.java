package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class AddMoneyActivity extends AppCompatActivity {

    private EditText edit_add_money;
    private Button btn_confirm_add_money;
    private ImageView back;
    private Float money, M;
    private String money_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        edit_add_money = findViewById(R.id.edit_add_money);
        btn_confirm_add_money = findViewById(R.id.btn_confirm_add_money);
        back = findViewById(R.id.back_money);

        money_add = edit_add_money.getText().toString();


        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    M = Float.valueOf(dataSnapshot.child("money").getValue().toString());
//                    money = money+M;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_confirm_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoney();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMoneyActivity.this, MoneyActivity.class));
            }
        });

    }

    private void AddMoney() {
        money = Float.valueOf(edit_add_money.getText().toString());

        if (money == null) {
            Toast.makeText(this, "กรุณาใส่จำนวนเงิน", Toast.LENGTH_SHORT).show();
        }else {
            money = money+M;
            updateMoney(money);
        }
    }

    private void updateMoney(Float money) {
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("money", money);
        refUser.updateChildren(userMap);
        startActivity(new Intent(AddMoneyActivity.this, MoneyActivity.class));
        Toast.makeText(this, "เติมเงินสำเร็จ", Toast.LENGTH_SHORT).show();
    }
}
