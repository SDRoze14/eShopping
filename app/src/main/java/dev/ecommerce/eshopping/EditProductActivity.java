package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditProductActivity extends AppCompatActivity {

    private TextView txt_barcode, txt_pdate, txt_ptime, close_edit_product, done_edit_product;
    private EditText edit_pname, edit_price, edit_pdescription;
    private ImageView img_product;

    private Spinner spinner_pcategory;

    private String category, id;
    private String des, pid, img, name;

    private String pcode,pname,pdescription,pcategory,pdate,ptime;
    private Float price;

    private Uri uri;
    private String myUri;
    private StorageReference pImgref;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        txt_barcode = findViewById(R.id.txt_barcode);
        txt_pdate = findViewById(R.id.txt_pdate);
        txt_ptime = findViewById(R.id.txt_ptime);
        close_edit_product = findViewById(R.id.close_edit_product);
        done_edit_product = findViewById(R.id.done_edit_product);
        edit_pname = findViewById(R.id.edit_pname);
        edit_price = findViewById(R.id.edit_price);
        edit_pdescription = findViewById(R.id.edit_pdescription);
        img_product = findViewById(R.id.img_product);
        spinner_pcategory = findViewById(R.id.spinner_pcategory);

        loadingBar = new ProgressDialog(this);

        id = getIntent().getStringExtra("product_id");


        txt_barcode.setText(id);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        txt_pdate.setText(date.format(calendar.getTime()));
        txt_ptime.setText(time.format(calendar.getTime()));

        pImgref = FirebaseStorage.getInstance().getReference().child("product image");

        //set spinner-------------------------------------------------------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProductActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.spinner_category));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pcategory.setAdapter(adapter);

        spinner_pcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner_pcategory.getSelectedItem().toString();
                if (category.equals("กรุณาเลือกประเภทสินค้า")) {

                }else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------

        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .start(EditProductActivity.this);
            }
        });

        close_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProductActivity.this, StoreViewListActivity.class));
            }
        });

        done_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcode = txt_barcode.getText().toString();
                pname = edit_pname.getText().toString();
                price = Float.valueOf(edit_price.getText().toString());
                pcategory = spinner_pcategory.getSelectedItem().toString();
                pdescription = edit_pdescription.getText().toString();

                updateProduct(pcode, pname, price, pcategory, pdescription);
            }
        });

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product").child(id);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    des = dataSnapshot.child("description").getValue().toString();
                    img = dataSnapshot.child("image").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    price = Float.valueOf(dataSnapshot.child("price").getValue().toString());

                    edit_pdescription.setText(des);
                    edit_pname.setText(name);
                    Picasso.get().load(img).into(img_product);
                    edit_price.setText(String.valueOf(price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProduct(final String pcode, final String pname, final Float price, final String pdescription, final String pcategory) {

        loadingBar.setTitle("แก้ไขข้อมูลสินค้า");
        loadingBar.setMessage("กรุณารอสักครู่กำลังแก้ไขข้อมูลสินค้าใหม่");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        String date, time;
        pdate = txt_pdate.getText().toString();
        ptime = txt_ptime.getText().toString();

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
                                productMap.put("price", price);
                                productMap.put("category", category);
                                productMap.put("description", pdescription);
                                productMap.put("time", ptime);
                                productMap.put("date", pdate);
                                productMap.put("image", myUri);
                                ref.child(id).updateChildren(productMap);


                                loadingBar.dismiss();


                                Intent intent = new Intent(EditProductActivity.this, StoreViewListActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditProductActivity.this, "เพิ่มสินค้าใหม่สำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(EditProductActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode==RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            uri = result.getUri();
            img_product.setImageURI(uri);
        }else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditProductActivity.this, StoreViewListActivity.class));
            finish();
        }
    }
}
