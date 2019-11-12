package dev.ecommerce.eshopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.Calendar;

public class PromotionActivity extends AppCompatActivity{

    private Spinner spinner;
    private TextView date_start, time_start, date_end, time_end;
    private Button scan_pcode, promotion_done;
    private String pcode;
    private EditText promo_code;
    private ImageView back, add_img_buy1free1, icon_img_add;
    private FrameLayout frameLayout;

    private LinearLayout sdate, stime, edate, etime;
    private int day, mouth, syear, house, sminute;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        date_start = findViewById(R.id.date_start);
        date_end = findViewById(R.id.date_end);
        back = findViewById(R.id.back_promotion);
        scan_pcode = findViewById(R.id.btn_scan_pcode_promo);
        promotion_done = findViewById(R.id.promotion_done);
        promo_code = findViewById(R.id.pcode_promo);
        add_img_buy1free1 = (ImageView) findViewById(R.id.img_promotion);
//        frameLayout = findViewById(R.id.frame_promotion);

        sdate = findViewById(R.id.date_start_promo);
        edate = findViewById(R.id.date_end_promo);

//        product code //////////////////////////////////////////////////////////////////////////////////
        pcode = getIntent().getStringExtra("pcode");
        promo_code.setText(pcode);

        scan_pcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodePromotionActivity.class));
            }
        });

//        Image //////////////////////////////////////////////
//          buy 1 free 1
        add_img_buy1free1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .start(PromotionActivity.this);
            }
        });

//        Spinner////////////////////////////////////////////////////////////////////////////////////
        spinner = findViewById(R.id.type_promotion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PromotionActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.type_promotion));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final FragmentDiscount discount = new FragmentDiscount();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String promotion = spinner.getSelectedItem().toString();
                if (promotion.equals("กรุณาเลือกโปรโมชั่น")) {
                    Toast.makeText(PromotionActivity.this, "กรุณาเลือกโปรโมชั่น", Toast.LENGTH_SHORT).show();
                }else if (promotion.equals("ลดราคา")) {
//                    replaceFragment(discount);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//  Date time ////////////////////////////////////////////////////////////////////////////////////

        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mouth = calendar.get(Calendar.MONTH);
        syear = calendar.get(Calendar.YEAR);

//        date start promotion

        date_start.setText(day+"/"+mouth+"/"+syear);
        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(PromotionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        date_start.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                },syear, mouth, day);
                datePickerDialog.show();
            }
        });

//        date end promotion
        date_end.setText(day+"/"+mouth+"/"+syear);
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PromotionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        date_end.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                },day, mouth, syear);
                datePickerDialog.show();
            }
        });
    }

   /* private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_promotion, fragment)
                .commit();

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            uri = result.getUri();
            add_img_buy1free1.setImageURI(uri);

        }else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PromotionActivity.this, PromotionActivity.class));
            finish();
        }
    }
}
