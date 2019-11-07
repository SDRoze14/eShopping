package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class PromotionActivity extends AppCompatActivity{

    private Spinner spinner;
    private TextView date_start, time_start, date_end, time_end;
    private Button scan_pcode, promotion_done;
    private EditText pcode;
    private ImageView back;

    private LinearLayout sdate, stime, edate, etime;

    private int day, mouth, syear, house, sminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        date_start = findViewById(R.id.date_start);
        time_start = findViewById(R.id.time_start);
        date_end = findViewById(R.id.date_end);
        time_end = findViewById(R.id.time_end);
        back = findViewById(R.id.back_promotion);
        scan_pcode = findViewById(R.id.btn_scan_pcode_promo);
        promotion_done = findViewById(R.id.promotion_done);
        pcode = findViewById(R.id.pcode_promo);

        sdate = findViewById(R.id.date_start_promo);
        stime = findViewById(R.id.time_start_promo);
        edate = findViewById(R.id.date_end_promo);
        etime = findViewById(R.id.time_end_promotion);

//        Spinner////////////////////////////////////////////////////////////////////////////////////
        spinner = findViewById(R.id.type_promotion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PromotionActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.type_promotion));

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

        house = calendar.get(Calendar.HOUR);
        sminute = calendar.get(Calendar.MINUTE);
//        time start promotion
        time_start.setText(house+":"+sminute);
        stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(PromotionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time_start.setText(hourOfDay+":"+minute);
                    }
                },house, sminute, false);
                timePickerDialog.show();
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

//        time end promotion
        time_end.setText(house+":"+sminute);
        etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(PromotionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time_end.setText(hourOfDay+":"+minute);
                    }
                },house, sminute, false);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_promotion, fragment)
                .commit();

    }



}
