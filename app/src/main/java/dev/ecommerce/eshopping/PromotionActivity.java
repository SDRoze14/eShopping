package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PromotionActivity extends AppCompatActivity {

    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        spinner = findViewById(R.id.type_promotion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PromotionActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type_promotion));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final FragmentBuy1Free1 buy1free1 = new FragmentBuy1Free1();
        final FragmentDiscount discount = new FragmentDiscount();
        final FragmentBuy2 buy2 = new FragmentBuy2();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String promotion = spinner.getSelectedItem().toString();
                if (promotion.equals("กรุณาเลือกโปรโมชั่น")) {

                }else if (promotion.equals("ซื้อ 1 แถม 1")) {
                    replaceFragment(buy1free1);

                }else if (promotion.equals("ลดราคา")) {
                    replaceFragment(discount);

                }else if (promotion.equals("ซื้อคู่กัน")) {
                    replaceFragment(buy2);


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_promotion, fragment)
                .commit();

    }

}
