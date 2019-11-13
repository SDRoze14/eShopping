package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.Money;

import java.util.Calendar;
import java.util.HashMap;

import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class AddBankActivity extends AppCompatActivity {

    private Spinner spinner;
    private int day, mouth, syear;
    private RelativeLayout date_of_birth;
    private TextView birth;
    private EditText atm_card_no, atm_card_pass, id_card_no;
    private Button btn_add_money;
    private ImageView back_bank;

    private String bank, ATMCardNo = null, ATMCardPass, IDCardNo, BirthDay;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);

        spinner = findViewById(R.id.spinner_bank);
        date_of_birth = findViewById(R.id.date_of_birth);
        birth = findViewById(R.id.birthday);
        atm_card_no = findViewById(R.id.atm_card_no);
        atm_card_pass = findViewById(R.id.atm_card_pass);
        id_card_no = findViewById(R.id.id_card_no);
        btn_add_money = findViewById(R.id.btn_add_money);
        back_bank = findViewById(R.id.back_bank);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddBankActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.bank));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank = spinner.getSelectedItem().toString();
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
                        BirthDay = String.valueOf(birth);
                    }
                },syear, mouth, day);
                datePickerDialog.show();
            }
        });

        btn_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBank();
            }
        });

        back_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBankActivity.this, MoneyActivity.class));
            }
        });
    }

    private void AddBank() {
        ATMCardNo = atm_card_no.getText().toString();
        ATMCardPass = atm_card_pass.getText().toString();
        IDCardNo = id_card_no.getText().toString();
        BirthDay = birth.getText().toString();

        if (ATMCardNo.length() < 16 || TextUtils.isEmpty(ATMCardNo)) {
            Toast.makeText(this, "เลขบัตร ATM ไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(ATMCardPass)) {
            Toast.makeText(this, "กรุณากรอกรหัสบัตร ATM", Toast.LENGTH_SHORT).show();
        }else if (IDCardNo.length() < 13 || TextUtils.isEmpty(IDCardNo)) {
            Toast.makeText(this, "เลขบัตรประชาชนไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
        } else if (bank == "กรุณาเลือกธนาคาร"){
            Toast.makeText(this, "กรุณาเลือกธนาคาร", Toast.LENGTH_SHORT).show();
        }else {
            UploadBank(ATMCardNo, ATMCardPass, IDCardNo, BirthDay, bank);
        }

    }

    private void UploadBank(String AtmCardNo, String AtmCardPass, String IdCardNo, String birthDay, String Bank) {

        DatabaseReference refBank = FirebaseDatabase.getInstance().getReference().child("Bank");

        HashMap<String, Object> bankMap = new HashMap<>();
        bankMap.put("atm_no", AtmCardNo);
        bankMap.put("atm_pass", AtmCardPass);
        bankMap.put("id_no", IdCardNo);
        bankMap.put("bank", Bank);
        bankMap.put("birth_day", birthDay);
        refBank.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(bankMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddBankActivity.this, "เชื่อมบัญชีธนาคารสำเร็จ", Toast.LENGTH_SHORT).show();
                        String status = "added";
                        Intent intent = new Intent(AddBankActivity.this, MoneyActivity.class);
                        intent.putExtra("status", status);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddBankActivity.this, "เชื่อมบัญชีธนาคารสำเร็จ กรุณาตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
