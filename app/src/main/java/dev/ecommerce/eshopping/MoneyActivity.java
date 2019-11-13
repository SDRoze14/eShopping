package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class MoneyActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Button btn_add_bank;
    private RelativeLayout btn_bank;
    private TextView money, name_bank, atm_no;
    private String name_b,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        btn_add_bank =findViewById(R.id.btn_add_bank);
        btn_bank = findViewById(R.id.btn_bank);
        money = findViewById(R.id.money);
        name_bank = findViewById(R.id.name_bank);
        atm_no = findViewById(R.id.atm_no);

        name_bank.setText("กรุณาเชื่อมบัญชีธนาคาร");
        atm_no.setText(null);
        name_b = String.valueOf(name_bank);

        status = getIntent().getStringExtra("status");

        BottomNavigationView nav_bt_View = findViewById(R.id.bt_nav_bar);
        nav_bt_View.setOnNavigationItemSelectedListener(this);

        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        DatabaseReference refBank = FirebaseDatabase.getInstance().getReference().child("Bank").child(Prevalent.currentOnlineUser.getPhone());

        refBank.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String atm = dataSnapshot.child("atm_no").getValue().toString();
                    String name = dataSnapshot.child("bank").getValue().toString();
                    name_bank.setText(name);
                    atm_no.setText(atm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Float M = Float.valueOf(dataSnapshot.child("money").getValue().toString());

                    money.setText(Float.toString(M) + " บาท");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, AddBankActivity.class));
            }
        });

        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (status == "added") {
                    startActivity(new Intent(MoneyActivity.this, AddMoneyActivity.class));

//                }else {
//                    Toast.makeText(MoneyActivity.this, "ยังไม่ได้เชื่อมบัญชีธนาคาร", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        final int id = item.getItemId();

        if (id==R.id.bt_home) {
            startActivity(new Intent(MoneyActivity.this, HomeActivity.class ));

        }else if (id==R.id.bt_money) {

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
