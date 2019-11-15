package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;

public class BillActivity extends AppCompatActivity {

    private String order_id, tprice, txt_money, pprice, num, id, uid;
    private Float money, balance, price;
    private TextView id_order_bill, all_price, date_bill, time_bill, all_amount;
    private ImageView close, qr_bill;

    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        recyclerView = findViewById(R.id.recycle_bill);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        id_order_bill = findViewById(R.id.id_order_bill);
        close = findViewById(R.id.close);
        all_price = findViewById(R.id.all_price);
        all_amount = findViewById(R.id.all_amount);
        date_bill = findViewById(R.id.date_bill);
        time_bill = findViewById(R.id.time_bill);
        qr_bill = findViewById(R.id.qr_bill);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        date_bill.setText(date.format(calendar.getTime()));
        time_bill.setText(time.format(calendar.getTime()));

        order_id = getIntent().getStringExtra("order_id");
        tprice = getIntent().getStringExtra("total_price");

        id_order_bill.setText(order_id);
        all_price.setText(tprice);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, HomeActivity.class));
            }
        });

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                order_id, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qr_bill.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
                        all_amount.setText(String.valueOf(i+1));


                        uid = orders.getUid();

                        id = orders.getProduct_id();


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
