package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.paperdb.Paper;

public class MoneyActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Button btn_add_bank;
    private RelativeLayout btn_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        btn_add_bank =findViewById(R.id.btn_add_bank);
        btn_bank = findViewById(R.id.btn_bank);
        BottomNavigationView nav_bt_View = findViewById(R.id.bt_nav_bar);

        btn_add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, AddBankActivity.class));
            }
        });

        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, AddMoneyActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        final int id = item.getItemId();

        if (id==R.id.bt_home) {

        }else if (id==R.id.bt_money) {
            Intent intent = new Intent(MoneyActivity.this, HomeActivity.class);
            startActivity(intent);

        }else if (id==R.id.bt_menu) {

            PopupMenu popupMenu = new PopupMenu(MoneyActivity.this, findViewById(R.id.bt_menu));
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id1 = menuItem.getItemId();
                    if (id1 == R.id.bt_setting) {
                        Intent intent = new Intent(MoneyActivity.this, SettingProfileActivity.class);
                        startActivity(intent);
                    }
                    else if (id1 == R.id.bt_logout) {
                        Paper.book().destroy();
                        Intent intent = new Intent(MoneyActivity.this, MainActivity.class);
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
