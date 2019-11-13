package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import dev.ecommerce.eshopping.Model.Cart;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import dev.ecommerce.eshopping.ViewHoder.CartViewHolder;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button paymants;
    private TextView id_cart_top, total_ptice;

    private DatabaseReference listref;

    private String id,pcart, name;
    private String[] nid;

    private float tprice = 0, price;

    private Button btn_payment;
    private int day, mouth, year, hours, min, sec;
    private String date ,d, t, y, hh, mm, ss, orderID;
    private ImageView close_list;

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
        btn_payment = findViewById(R.id.pay_list);
        close_list = findViewById(R.id.close_list);

        pcart = getIntent().getStringExtra("cart_id");

        listref = FirebaseDatabase.getInstance().getReference().child("Cart").child(pcart);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");

        d = date.format(calendar.getTime());
        t = time.format(calendar.getTime());

        orderID = Prevalent.currentOnlineUser.getPhone()+pcart+d+t;

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, PaymentActivity.class);
                intent.putExtra("Total Price", String.valueOf(tprice));
                intent.putExtra("Order ID", orderID);
                startActivity(intent);
                finish();
            }
        });

        close_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, HomeActivity.class));
            }
        });

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
                cartViewHolder.num.setText(cart.getUID());

                id = cartViewHolder.txt_product_id.getText().toString();

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Product").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            name = dataSnapshot.child("name").getValue().toString();
                            price = Float.valueOf(dataSnapshot.child("price").getValue().toString());
                            String p = Float.toString(price);

                            cartViewHolder.txt_product_name.setText(name);
                            cartViewHolder.txt_product_price.setText(p);
                            Picasso.get().load(image).into(cartViewHolder.imageView);

                            tprice += price;
                            total_ptice.setText("ราคารวม "+String.valueOf(tprice));

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Order").child(orderID);

                            HashMap<String, Object> orderMap = new HashMap<>();
                            orderMap.put("id", id);
                            orderMap.put("name", name);
                            orderMap.put("price", price);
                            orderMap.put("uid", cart.getUID());
                            ref.child(cart.getUID()).updateChildren(orderMap);
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
