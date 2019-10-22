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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class StoreAddProductActivity extends AppCompatActivity {

    private EditText product_uid, product_name, product_price, product_description, product_category;
    public static TextView editText_barcode;
    private TextView product_date, product_time, close, done;
    private ImageView product_image;
    private Button btn_scan_barcode;

    private ProgressDialog loadingBar;

    private String pcode,pname,pdescription,pcategory,price,pdate,ptime,puid,pimg;

    private Uri uri;
    private StorageReference pImgref;
    private DatabaseReference pref;

    private static final int GalleryPick = 1;

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
        product_category = findViewById(R.id.edit_pcateegory);
        product_date = findViewById(R.id.txt_pdate);
        product_time = findViewById(R.id.txt_ptime);
        close = findViewById(R.id.close_add_product);
        done = findViewById(R.id.done_add_product);
        product_image = findViewById(R.id.img_product);

        loadingBar = new ProgressDialog(this);

        pcode = getIntent().getStringExtra("pcode");
        pImgref = FirebaseStorage.getInstance().getReference().child("product image");
        pref = FirebaseDatabase.getInstance().getReference().child("Product");

        editText_barcode.setText(pcode);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        product_date.setText(date.format(calendar.getTime()));
        product_time.setText(time.format(calendar.getTime()));


        btn_scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanAddProductActivity.class));
            }
        });

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
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

    private void AddNewProduct() {
        pcode = editText_barcode.getText().toString();
        puid = product_uid.getText().toString();
        pname = product_name.getText().toString();
        price = product_price.getText().toString();
        pdescription = product_description.getText().toString();
        pcategory = product_category.getText().toString();

        if (uri == null) {
            Toast.makeText(this, "Product image is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pcode)) {
            Toast.makeText(this, "Product code is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname)) {
            Toast.makeText(this, "Product name is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(puid)) {
            Toast.makeText(this, "Product UID is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Product price is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pdescription)) {
            Toast.makeText(this, "Product description is Empty.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pcategory)) {
            Toast.makeText(this, "Product category is Empty.", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        pdate = product_date.getText().toString();
        ptime = product_time.getText().toString();

        final StorageReference filePath = pImgref.child(uri.getLastPathSegment() + pcode + ".jpg");

        final UploadTask uploadTask = filePath.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(StoreAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(StoreAddProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        pimg = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            pimg = task.getResult().toString();
                            Toast.makeText(StoreAddProductActivity.this, "got the Product image Url Successfully...", Toast
                                .LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> pMap = new HashMap<>();
        pMap.put("id", pcode);
        pMap.put("UID", puid);
        pMap.put("name", pname);
        pMap.put("price", price);
        pMap.put("description", pdescription);
        pMap.put("category", pcategory);
        pMap.put("date", pdate);
        pMap.put("time", ptime);
        pMap.put("image", pimg);

        pref.child(pcode).updateChildren(pMap)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(StoreAddProductActivity.this, StoreActivity.class);
                        startActivity(intent);

                        Toast.makeText(StoreAddProductActivity.this, "Add new Product success..", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else {
                        loadingBar.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(StoreAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode==RESULT_OK && data != null) {
            uri = data.getData();
            product_image.setImageURI(uri);
        }
    }
}
