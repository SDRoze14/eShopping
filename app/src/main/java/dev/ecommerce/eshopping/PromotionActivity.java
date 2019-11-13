package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;

public class PromotionActivity extends AppCompatActivity{

    private TextView date_start, time_start, date_end, time_end;
    private Button scan_pcode, promotion_done;
    private String pcode ,product_id, name_promo, date_s, date_e;
    private int discount;
    private EditText promo_code;
    private ImageView back, img_promotion;
    private EditText name_promotion, edit_discount;

    private RelativeLayout rela_img_promotion;

    private LinearLayout sdate, stime, edate, etime;
    private int day, mouth, syear, house, sminute;

    private Uri uri;
    private String myUri;
    private StorageReference promoImgref;

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
        img_promotion = findViewById(R.id.img_promotion);
        rela_img_promotion = findViewById(R.id.rela_img_promotion);
        name_promotion = findViewById(R.id.name_promotion);
        edit_discount = findViewById(R.id.discount);

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
        rela_img_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .start(PromotionActivity.this);
            }
        });

        promotion_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promotionSave();
            }
        });

        promoImgref = FirebaseStorage.getInstance().getReference().child("Promotion image");


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

    private void promotionSave() {
        product_id = promo_code.getText().toString();
        name_promo = name_promotion.getText().toString();
        discount = Integer.valueOf(edit_discount.getText().toString());
        date_s = date_start.getText().toString();
        date_e = date_end.getText().toString();

        if (TextUtils.isEmpty(product_id)) {
            Toast.makeText(this, "กรุณากรอกหรือสแกนรหัสสินค้า", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name_promo)) {
            Toast.makeText(this, "กรุณากรอกชื่อโปรโมชั่น", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(String.valueOf(discount))) {
            Toast.makeText(this, "กรุณากรอกชส่วนลด", Toast.LENGTH_SHORT).show();
        }else if (uri == null) {
            Toast.makeText(this, "กรุณาเลือกรูปโปรโมชั่น", Toast.LENGTH_SHORT).show();
        }else {
            uploadPromotion(product_id, name_promo, discount, date_s, date_e);
        }

    }

    private void uploadPromotion(String product_id, String name_promo, int discount, String date_s, String date_e) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("เพิ่มโปรโมชั่น");
        progressDialog.setMessage("กรุณารอสักครู่กำลังอัพโหลดโปรโมชั่น");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (uri != null) {
            final StorageReference filePath = promoImgref
                    .child(uri.getLastPathSegment() + this.pcode + ".jpg");

            final UploadTask uploadTask = filePath.putFile(uri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri =  task.getResult();
                                myUri = downloadUri.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Promotion");

                                HashMap<String, Object> promoMap = new HashMap<>();
                                promoMap.put("name_promotion", name_promo);
                                promoMap.put("product_id", product_id);
                                promoMap.put("discount", discount);
                                promoMap.put("date_start", date_s);
                                promoMap.put("date_end", date_e);
                                promoMap.put("image_promotion", myUri);
                                ref.child(String.valueOf(product_id)).updateChildren(promoMap);


                                progressDialog.dismiss();

                                Intent intent = new Intent(PromotionActivity.this, PromotionActivity.class);
                                startActivity(intent);
                                Toast.makeText(PromotionActivity.this, "เพิ่มสินค้าใหม่สำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(PromotionActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "รูปสินค้าไม่ได้เลือก", Toast.LENGTH_SHORT).show();
        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            uri = result.getUri();
            img_promotion.setImageURI(uri);

        }else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PromotionActivity.this, PromotionActivity.class));
            finish();
        }
    }
}
