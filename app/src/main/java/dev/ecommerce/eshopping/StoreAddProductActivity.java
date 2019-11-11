package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import dev.ecommerce.eshopping.Model.Product;
import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class StoreAddProductActivity extends AppCompatActivity {

    private EditText product_uid, product_name, product_price, product_description, product_amount;
    public static TextView editText_barcode = null;
    private TextView product_date, product_time, close, done;
    private ImageView product_image;
    private Button btn_scan_barcode;
    private Spinner spinner_pcategory;

    private ProgressDialog loadingBar;

    private String pcode,pname,pdescription,pcategory,pdate,ptime,puid,pimg,pamount;
    private Float price;

    private Uri uri;
    private String myUri;
    private StorageReference pImgref;

    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_product);

        btn_scan_barcode =findViewById(R.id.btn_scan_barcode);
        editText_barcode = findViewById(R.id.txt_barcode);
        product_uid = findViewById(R.id.edit_puid);
        product_name = findViewById(R.id.edit_pname);
        product_price = findViewById(R.id.edit_price);
        product_description = findViewById(R.id.edit_pdescription);
        product_date = findViewById(R.id.txt_pdate);
        product_time = findViewById(R.id.txt_ptime);
        close = findViewById(R.id.close_add_product);
        done = findViewById(R.id.done_add_product);
        product_image = findViewById(R.id.img_product);
        product_amount = findViewById(R.id.edit_pamount);
        spinner_pcategory = findViewById(R.id.spinner_pcategory);
        //set spinner-------------------------------------------------------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StoreAddProductActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.spinner_category));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pcategory.setAdapter(adapter);

        spinner_pcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spinner_pcategory.getSelectedItem().toString();
                if (category.equals("กรุณาเลือกประเภทสินค้า")) {

                }else {
                    Toast.makeText(StoreAddProductActivity.this, category, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------

        loadingBar = new ProgressDialog(this);

        pImgref = FirebaseStorage.getInstance().getReference().child("product image");

        pcode = getIntent().getStringExtra("pcode");
        editText_barcode.setText(pcode);

        //date and time-------------------------------------------------------------------------------
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        product_date.setText(date.format(calendar.getTime()));
        product_time.setText(time.format(calendar.getTime()));
        //---------------------------------------------------------------------------------------------

        //btn scan go to scan barcode activity--------------------------------------------------------
        btn_scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanAddProductActivity.class));

            }
        });
        //-------------------------------------------------------------------------------------------

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .start(StoreAddProductActivity.this);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewProduct();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreAddProductActivity.this, StoreActivity.class);
                startActivity(intent);
            }
        });

    }

    //set edit text to string ----------------------------------------------------------------------------
    private void AddNewProduct() {
        pcode = editText_barcode.getText().toString();
        puid = product_uid.getText().toString();
        pname = product_name.getText().toString();
        price = Float.valueOf(product_price.getText().toString());
        pdescription = product_description.getText().toString();
        pcategory = spinner_pcategory.getSelectedItem().toString();
        pamount = product_amount.getText().toString();

        if (uri == null) {
            Toast.makeText(this, "กรุณาใส่รูปสินค้า", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pcode)) {
            Toast.makeText(this, "กรุณาสแกนรหัสสินค้า", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname)) {
            Toast.makeText(this, "กรุณาใส่ชื่อสินค้า", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(puid)) {
            Toast.makeText(this, "กรุณากรอก UID", Toast.LENGTH_SHORT).show();
        }
        else if (price == null) {
            Toast.makeText(this, "กรุณากรอกราคาสินค้า", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pdescription)) {
            Toast.makeText(this, "กรุณากรอกรายละเอียดสินค้า", Toast.LENGTH_SHORT).show();
        }
        else if (pcategory == "กรุณาเลือกประเภทสินค้า") {
            Toast.makeText(this, "กรุณาเลือกประเภทสินค้า", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pamount)) {
            Toast.makeText(this, "กรุณาใส่จำนวนสินค้า", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation(pcode, puid, pname, price, pdescription, pcategory, pamount);
        }
    }

    // add category to category database------------------------------------------------------------------

    // add data to product database;
    private void StoreProductInformation(final String pcode, final String puid, final String pname, final Float price, final String pdescription, final String pcategory, final String pamount) {
        loadingBar.setTitle("เพิ่มสินค้าใหม่");
        loadingBar.setMessage("กรุณารอสักครู่กำลังเพิ่มสินค้าใหม่");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        pdate = product_date.getText().toString();
        ptime = product_time.getText().toString();

        if (uri != null) {
            final StorageReference filePath = pImgref
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

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Product");

                                HashMap<String, Object> productMap = new HashMap<>();
                                productMap.put("id", pcode);
                                productMap.put("name", pname);
                                productMap.put("uid", puid);
                                productMap.put("price", price);
                                productMap.put("category", pcategory);
                                productMap.put("description", pdescription);
                                productMap.put("amount", pamount);
                                productMap.put("time", ptime);
                                productMap.put("date", pdate);
                                productMap.put("image", myUri);
                                ref.child(pcode).updateChildren(productMap);

//                                Firestore --------------------------------------------------------------------------
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Product").document(pcode)
                                        .set(productMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                loadingBar.dismiss();


                                Intent intent = new Intent(StoreAddProductActivity.this, StoreViewListActivity.class);
                                intent.putExtra("pcategory", pcategory);
                                startActivity(intent);
                                Toast.makeText(StoreAddProductActivity.this, "เพิ่มสินค้าใหม่สำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(StoreAddProductActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "รูปสินค้าไม่ได้เลือก", Toast.LENGTH_SHORT).show();
        }

    }

    //crop image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode==RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            uri = result.getUri();
            product_image.setImageURI(uri);
        }else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StoreAddProductActivity.this, StoreAddProductActivity.class));
            finish();
        }
    }
}
