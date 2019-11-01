package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Button scan_barcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav_bt_View = findViewById(R.id.bt_nav_bar);
        CircleImageView imageProfile = findViewById(R.id.user_image_profile);
        scan_barcode = findViewById(R.id.scan_barcode);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        final int id = item.getItemId();

        if (id==R.id.bt_home) {

        }else if (id==R.id.bt_money) {

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
    



}