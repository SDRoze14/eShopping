package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
    private TextView id_cart_top, total_ptice;

    private DatabaseReference listref;

    private String id,pcart, name;
    private float tprice = 0, price;
    private int count;
    private String p;

    private Button btn_payment;
    private String d, t, orderID;
    private ImageView close_list;

    private Dialog dialog;
    private ImageView img_promotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.list_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        id_cart_top = findViewById(R.id.id_cart_list);
        total_ptice = findViewById(R.id.list_total_price);
        btn_payment = findViewById(R.id.pay_list);
        close_list = findViewById(R.id.close_list);

        pcart = getIntent().getStringExtra("cart_id");

        id_cart_top.setText("รหัสรถเข็น: "+pcart );

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
//                cartViewHolder.num.setText(cart.getUID());

                id = cartViewHolder.txt_product_id.getText().toString();

                final DatabaseReference promotionRef = FirebaseDatabase.getInstance().getReference()
                        .child("Promotion").child(id);
                promotionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dialog = new Dialog(ListActivity.this);
                            dialog.setContentView(R.layout.promotion_dialog);
                            img_promotion = dialog.findViewById(R.id.img_promotion);


                            String img_pro = dataSnapshot.child("image_promotion").getValue().toString();
                            Picasso.get().load(img_pro).into(img_promotion);

                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Product").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String pid = dataSnapshot.child("id").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();
                            name = dataSnapshot.child("name").getValue().toString();
                            price = Float.valueOf(dataSnapshot.child("price").getValue().toString());
                            p = Float.toString(price);

                            cartViewHolder.txt_product_name.setText(name);
                            cartViewHolder.txt_product_price.setText(p);
                            Picasso.get().load(image).into(cartViewHolder.imageView);

                            DatabaseReference proRef = FirebaseDatabase.getInstance().getReference().child("Promotion").child(pid);
                            proRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Float discount = Float.valueOf(dataSnapshot.child("discount").getValue().toString());
                                        Float dis = discount/100;
                                        Float price_dis = price*dis;

                                        p = String.valueOf(price_dis);
                                        cartViewHolder.txt_product_price.setText(p);

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID);

                                        HashMap<String, Object> orderMap = new HashMap<>();

                                        orderMap.put("price", price_dis);
                                        ref.child(cart.getUID()).updateChildren(orderMap);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            cartViewHolder.num.setText(null);

                            tprice += price;
                            total_ptice.setText("ราคารวม "+String.valueOf(tprice));

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID);

                            HashMap<String, Object> orderMap = new HashMap<>();
                            orderMap.put("product_id", pid);
                            orderMap.put("name_product", name);
                            orderMap.put("price", price);
                            orderMap.put("uid", cart.getUID());
                            orderMap.put("id_order", orderID);
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
