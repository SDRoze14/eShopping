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
import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import dev.ecommerce.eshopping.ViewHoder.CartViewHolder;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;

public class PaymentActivity extends AppCompatActivity {

    private Button btn;
    private ImageView close;
    private TextView id_order, total_price, money_bill;
    private String order_id, tprice, p, pprice, num, pcart, id, name, d, t, current_date,orderID;
    private Float money, balance, price;
    private float ttprice = 0;
    private int cd, ce ;

    private float ttp = 0;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference orderRef;
    private DatabaseReference listref;
//    private float ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        recyclerView = findViewById(R.id.recycle_order);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn = findViewById(R.id.b1);
        close = findViewById(R.id.close);
        id_order = findViewById(R.id.id_order);
        total_price = findViewById(R.id.total_price);
        money_bill = findViewById(R.id.money_bill);

        order_id = getIntent().getStringExtra("Order ID");
        tprice = getIntent().getStringExtra("Total Price");
        pcart =getIntent().getStringExtra("pcart");

        id_order.setText("Order ID: \n"+order_id);
//        total_price.setText(tprice);
        price = Float.valueOf(tprice);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    money = Float.valueOf(dataSnapshot.child("money").getValue().toString());
                    money_bill.setText(String.valueOf(money));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        listref = FirebaseDatabase.getInstance().getReference().child("Cart").child(pcart);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, ScanPaymentActivity.class);
                intent.putExtra("Order ID", orderID);
                intent.putExtra("Total Price", String.valueOf(ttp));
                intent.putExtra("Money", String.valueOf(money));
                startActivity(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");

        d = date.format(calendar.getTime());
        t = time.format(calendar.getTime());
        current_date = date.format(calendar.getTime());
        cd = Integer.parseInt(current_date);

        orderID = Prevalent.currentOnlineUser.getPhone()+pcart+d+t;

    }

    private void updateMoney() {


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(listref, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, OrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final OrdersViewHolder orderViewHolder, int i, final Cart cart) {

                orderViewHolder.id_product.setText(cart.getProduct_id());
                id = cart.getProduct_id();

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Product").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String pid = dataSnapshot.child("id").getValue().toString();
                            name = dataSnapshot.child("name").getValue().toString();
                            price = Float.valueOf(dataSnapshot.child("price").getValue().toString());
                            p = Float.toString(price);

                            orderViewHolder.name_product.setText(name);
                            orderViewHolder.price_product.setText(p);

                            DatabaseReference proRef = FirebaseDatabase.getInstance().getReference().child("Promotion").child(pid);
                            proRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Float discount = Float.valueOf(dataSnapshot.child("discount").getValue().toString());
                                        Float dis = discount/100;
                                        Float price_dis = price*dis;
                                        String date_end = dataSnapshot.child("date_end").getValue().toString();
                                        ce = Integer.parseInt(date_end);

                                        p = String.valueOf(price_dis);

                                        if (cd < ce) {

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID);

                                            HashMap<String, Object> orderMap = new HashMap<>();

                                            orderMap.put("price", price_dis);
                                            ref.child(cart.getUID()).updateChildren(orderMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            ttp += price;
                            total_price.setText(String.valueOf(ttp));

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
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_view, parent, false);
                OrdersViewHolder holder = new OrdersViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
