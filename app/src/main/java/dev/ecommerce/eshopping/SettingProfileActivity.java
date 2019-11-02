package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class SettingProfileActivity extends AppCompatActivity {

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfileImageRef;
    private String checker = "";

    private CircleImageView imageProfileSetting;
    private TextView close,done,changeImageTextBtn;
    private EditText nameSetting,phoneSetting,addressSetting,emailSetting;
    private RadioButton radioMale,radioFemail;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        Paper.init(this);

        close = findViewById(R.id.close_setting);
        done = findViewById(R.id.done_setting);

        imageProfileSetting = findViewById(R.id.image_setting);
        changeImageTextBtn = findViewById(R.id.text_btn_change_image);

        nameSetting = findViewById(R.id.edit_name_setting);
        phoneSetting = findViewById(R.id.edit_phone_setting);
        addressSetting = findViewById(R.id.edit_address_setting);
        emailSetting = findViewById(R.id.edit_email_setting);

        radioMale = findViewById(R.id.radio_male);
        radioFemail = findViewById(R.id.radio_female);

        if (radioMale.isChecked()) {
            gender = "Male";
        }
        if (radioFemail.isChecked()) {
            gender = "Female";
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameSetting.setText(Prevalent.currentOnlineUser.getName());
        phoneSetting.setText(Prevalent.currentOnlineUser.getPhone());
        addressSetting.setText(Prevalent.currentOnlineUser.getAddress());
        emailSetting.setText(Prevalent.currentOnlineUser.getEmail());


        userInfoDisplay(imageProfileSetting,nameSetting,phoneSetting,addressSetting,emailSetting,gender);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")){
                    userInfoSave();
                }else  {
                    updateOnlyUserInfo();
                }
            }
        });

        changeImageTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingProfileActivity.this);
            }
        });

    }

    private void updateOnlyUserInfo() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("แก้ไขบัญชี");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", nameSetting.getText().toString());
        userMap.put("phone", phoneSetting.getText().toString());
        userMap.put("address", addressSetting.getText().toString());
        userMap.put("email", emailSetting.getText().toString());
//        userMap.put("gender", gender.getBytes().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        progressDialog.dismiss();

        startActivity(new Intent(SettingProfileActivity.this, MainActivity.class));
        Toast.makeText(SettingProfileActivity.this, "แก้ไขบัญชีส่วนตัวสำเร็จ", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageProfileSetting.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Error, ลองอีกครั้ง", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingProfileActivity.this, SettingProfileActivity.class));
            finish();
        }

    }

    private void userInfoSave() {
        if (TextUtils.isEmpty(nameSetting.getText().toString())){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phoneSetting.getText().toString())){
            Toast.makeText(this, "Phone is mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(addressSetting.getText().toString())){
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(emailSetting.getText().toString())){
            Toast.makeText(this, "Email is mandatory", Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("แก้ไขบัญชี");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfileImageRef
                    .child(imageUri.getLastPathSegment()+ ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                myUrl = downloadUri.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", nameSetting.getText().toString());
                                userMap.put("phone", phoneSetting.getText().toString());
                                userMap.put("address", addressSetting.getText().toString());
                                userMap.put("email", emailSetting.getText().toString());
//                        userMap.put("gender", gender.getBytes().toString());
                                userMap.put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingProfileActivity.this, SettingProfileActivity.class));
                                Toast.makeText(SettingProfileActivity.this, "แก้ไขบัญชีส่วนตัวสำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "รูปโปรไฟล์ไม่ได้เลือก", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView imageProfileSetting, final EditText nameSetting, final EditText phoneSetting,
                                 final EditText addressSetting, final EditText emailSetting, final String gender) {
        DatabaseReference Userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        Userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
//                        String gender = dataSnapshot.child("gender").getValue().toString();

                        Picasso.get().load(image).into(imageProfileSetting);
                        nameSetting.setText(name);
                        phoneSetting.setText(phone);
                        addressSetting.setText(address);
                        emailSetting.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}