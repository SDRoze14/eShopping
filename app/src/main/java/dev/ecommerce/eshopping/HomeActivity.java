package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.ecommerce.eshopping.Listener.FirebaseLoadPager;
import dev.ecommerce.eshopping.Model.Promotion;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import dev.ecommerce.eshopping.Transformer.DepthPageTransformer;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FirebaseLoadPager, ValueEventListener {

    private Button scan_barcode;
    private TextView all_money;
    private String money;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private DatabaseReference refPromotion;
    private FirebaseLoadPager firebaseLoadPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav_bt_View = findViewById(R.id.bt_nav_bar);
        CircleImageView imageProfile = findViewById(R.id.user_image_profile);
        scan_barcode = findViewById(R.id.scan_barcode);
        all_money = findViewById(R.id.money_view_home);

        refPromotion = FirebaseDatabase.getInstance().getReference("Promotion");

         firebaseLoadPager = this;
         
         loadPromotion();

         pager = findViewById(R.id.pager);
         pager.setPageTransformer(true, new DepthPageTransformer());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Float price = Float.valueOf(dataSnapshot.child("money").getValue().toString());

                    all_money.setText(Float.toString(price)+" บาท");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.user_profile).into(imageProfile);


        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScanBarcodeCartActivity.class);
                startActivity(intent);
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        nav_bt_View.setOnNavigationItemSelectedListener(this);
    }

    private void loadPromotion() {
        /*refPromotion.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Promotion>  promotionList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                    promotionList.add(snapshot.getValue(Promotion.class));
                firebaseLoadPager.onFirebaseLoadSuccess(promotionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadPager.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });*/
        refPromotion.addValueEventListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        final int id = item.getItemId();

        if (id==R.id.bt_home) {

        }else if (id==R.id.bt_money) {
            Intent intent = new Intent(HomeActivity.this, MoneyActivity.class);
            startActivity(intent);

        }else if (id==R.id.bt_menu) {

            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, findViewById(R.id.bt_menu));
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id1 = menuItem.getItemId();
                    if (id1 == R.id.bt_setting) {
                        Intent intent = new Intent(HomeActivity.this, SettingProfileActivity.class);
                        startActivity(intent);
                    }
                    else if (id1 == R.id.bt_logout) {
                        Paper.book().destroy();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
        return true;
    }


    @Override
    public void onFirebaseLoadSuccess(List<Promotion> promotionList) {
        pagerAdapter = new PageAdapter(this, promotionList);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Promotion>  promotionList = new ArrayList<>();
        for (DataSnapshot snapshot: dataSnapshot.getChildren())
            promotionList.add(snapshot.getValue(Promotion.class));
        firebaseLoadPager.onFirebaseLoadSuccess(promotionList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        firebaseLoadPager.onFirebaseLoadFailed(databaseError.getMessage());
    }

    @Override
    protected void onDestroy() {
        refPromotion.removeEventListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        refPromotion.addValueEventListener(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        refPromotion.removeEventListener(this);
        super.onStop();
    }
}