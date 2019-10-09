package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.ecommerce.eshopping.Model.Cart;

public class ListActivity extends AppCompatActivity {

    private DatabaseReference ref;

    private TextView listView;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    private ValueEventListener valueEventListener;

    private FirebaseListAdapter firebaseListAdapter;
    private TextView pID, pname, price, id_cart;
    private ImageView pimg;
    private String puid,cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_view_cart);
        id_cart = findViewById(R.id.id_cart_list);
        pID = findViewById(R.id.id_product_list);

        cart_id = getIntent().getStringExtra("cart");
        if (cart_id == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        id_cart.setText("ID Caer : "+cart_id);

        ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(cart_id).child("product_id");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                String p_id = cart.getProduct_id()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

    }
}
