package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dev.ecommerce.eshopping.Model.Cart;
import dev.ecommerce.eshopping.Model.Product;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import dev.ecommerce.eshopping.ViewHoder.CartViewHolder;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button paymants;
    private TextView id_cart_top, total_ptice;

    private DatabaseReference listref;

    private String id,pcart, n;
    private String[] nid;

    private Float tprice = Float.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.list_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        paymants = findViewById(R.id.pay_list);
        id_cart_top = findViewById(R.id.id_cart_list);
        total_ptice = findViewById(R.id.list_total_price);

        pcart = getIntent().getStringExtra("cart_id");

        listref = FirebaseDatabase.getInstance().getReference().child("Cart").child(pcart);

    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Cart> options =
            new FirebaseRecyclerOptions.Builder<Cart>()
            .setQuery(listref, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
            = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final CartViewHolder cartViewHolder, int i, final Cart cart) {

                cartViewHolder.txt_product_id.setText(cart.getProduct_id());
                String num = String.valueOf(i);
                cartViewHolder.num.setText(cart.getUID());

                /*String pid = cart.getProduct_id();
                String uid = cart.getUID();
                int count = 0, m = 0;
                for (count=0;count<i;count++ ){
                    m++;
                }
                String c = String.valueOf(m);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Order").child(pcart).child(c);

                HashMap<String, Object> order = new HashMap<>();
                order.put("uid", uid);
                order.put("pid", pid);
                ref.updateChildren(order);

                if ((pic == pic) && (puid == puid)) {
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Cart").child(pcart);
                    dref.orderByChild("UID").equalTo(puid);

                    dref.removeValue();
                }
*/
                final String id = cartViewHolder.txt_product_id.getText().toString();

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Product").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            Float price = Float.valueOf(dataSnapshot.child("price").getValue().toString());
                            String p = Float.toString(price);

                            cartViewHolder.txt_product_name.setText(name);
                            cartViewHolder.txt_product_price.setText(p);

                            /*for (int n=0;n<i;n++){
                                tprice = tprice +  Float.valueOf(p);
                            }
                            String ttp = String.valueOf(tprice);*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_cart, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

            }
}
