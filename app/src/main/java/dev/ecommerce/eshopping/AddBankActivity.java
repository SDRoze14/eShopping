package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class AddBankActivity extends AppCompatActivity {

    private Spinner spinner;
    private int day, mouth, syear;
    private RelativeLayout date_of_birth;
    private TextView birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);

        spinner = findViewById(R.id.spinner_bank);
        date_of_birth = findViewById(R.id.date_of_birth);
        birth = findViewById(R.id.birthday);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddBankActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.bank));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mouth = calendar.get(Calendar.MONTH);
        syear = calendar.get(Calendar.YEAR);

        birth.setText(day+"/"+mouth+"/"+syear);
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddBankActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        birth.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                },syear, mouth, day);
                datePickerDialog.show();
            }
        });
    }
}
