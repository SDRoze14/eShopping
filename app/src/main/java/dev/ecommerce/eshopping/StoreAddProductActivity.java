package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StoreAddProductActivity extends AppCompatActivity {

    private EditText editText_barcode;
    private Button btn_scan_barcode;

    private String pcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_product);

        btn_scan_barcode =findViewById(R.id.btn_scan_barcode);
        editText_barcode = (EditText) findViewById(R.id.txt_barcode);

        pcode = getIntent().getStringExtra("code");

        editText_barcode.setText(pcode);


        btn_scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreAddProductActivity.this, ScanAddProductActivity.class);
                startActivity(intent);
            }
        });


    }
}
