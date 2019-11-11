package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MoneyActivity extends AppCompatActivity {

    private Button btn_add_bank;
    private RelativeLayout btn_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        btn_add_bank =findViewById(R.id.btn_add_bank);
        btn_bank = findViewById(R.id.btn_bank);

        btn_add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, AddBankActivity.class));
            }
        });

        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, AddMoneyActivity.class));
            }
        });
    }
}
