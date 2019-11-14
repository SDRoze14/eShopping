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

import java.util.HashMap;

import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;

public class PaymentActivity extends AppCompatActivity {

    private Button btn;
    private ImageView close;
    private TextView id_order, total_price, money_bill;
    private String order_id, tprice, txt_money, pprice, num;
    private Float money, balance, price;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference orderRef;
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

        id_order.setText("Order ID: \n"+order_id);
        total_price.setText(tprice);
        price = Float.valueOf(tprice);



        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, BillActivity.class);
                intent.putExtra("Order ID", order_id);
                intent.putExtra("Total Price", tprice);
                startActivity(intent);

                updateMoney();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    money = Float.valueOf(dataSnapshot.child("money").getValue().toString());
                    txt_money = String.valueOf(money);
                    money_bill.setText(txt_money);
                    balance = money - price;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateMoney() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> moneyMap = new HashMap<>();
        moneyMap.put("money", balance);
        reference.updateChildren(moneyMap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef, Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersViewHolder orderViewHolder, int i, @NonNull Orders orders) {
                        orderViewHolder.id_product.setText(orders.getProduct_id());
                        num = String.valueOf(i+1);
                        orderViewHolder.number.setText(num);
                        orderViewHolder.name_product.setText(orders.getName_product());
                        pprice = String.valueOf(orders.getPrice());
                        orderViewHolder.price_product.setText(pprice);

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
