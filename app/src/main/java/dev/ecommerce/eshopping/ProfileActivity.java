package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.ecommerce.eshopping.Prevalent.Prevalent;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Paper.init(this);

        ImageView backArrow = findViewById(R.id.back_profile);

        CircleImageView imageProfile = findViewById(R.id.image_profile);
        TextView nameProfile = findViewById(R.id.name_profile);
        TextView moneyProfile = findViewById(R.id.num_money_profile);
        TextView phoneProfile = findViewById(R.id.num_phone_profile);
        TextView addressProfile = findViewById(R.id.detail_address_profile);
        TextView emailProfile = findViewById(R.id.view_email_profile);

        nameProfile.setText(Prevalent.currentOnlineUser.getName());
        phoneProfile.setText(Prevalent.currentOnlineUser.getPhone());
        emailProfile.setText(Prevalent.currentOnlineUser.getEmail());
        addressProfile.setText(Prevalent.currentOnlineUser.getAddress());
        moneyProfile.setText(Float.toString(Prevalent.currentOnlineUser.getMoney()));
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.user_profile).into(imageProfile);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}