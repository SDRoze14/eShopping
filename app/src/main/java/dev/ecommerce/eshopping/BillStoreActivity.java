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

import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;

public class BillStoreActivity extends AppCompatActivity {

    private ImageView close_bill_store;
    private TextView id_order_bill_store, date_bill, time_bill;
    private Button new_scan_bill;

    private String order_id, pprice, date, time;
    private Float tprice;

    private DatabaseReference orderRef;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_store);

        recyclerView = findViewById(R.id.recycle_bil_store);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        close_bill_store = findViewById(R.id.close_bill_store);
        id_order_bill_store = findViewById(R.id.id_order_bill_store);
        date_bill = findViewById(R.id.date_bill);
        time_bill = findViewById(R.id.time_bill);
        new_scan_bill = findViewById(R.id.new_scan_bill);

        order_id = getIntent().getStringExtra("order_id");

        id_order_bill_store.setText(order_id);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bill").child(order_id);
        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    date = dataSnapshot.child("date").getValue().toString();
                    time = dataSnapshot.child("time").getValue().toString();
                    date_bill.setText(date);
                    time_bill.setText(time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        close_bill_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillStoreActivity.this, StoreActivity.class));
            }
        });

        new_scan_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillStoreActivity.this, ScanBillActivity.class));
            }
        });
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
//
                        orderViewHolder.name_product.setText(orders.getName_product());
                        pprice = String.valueOf(orders.getPrice());
                        orderViewHolder.price_product.setText(pprice);
                        orderViewHolder.number.setText(String.valueOf(i+1));

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
