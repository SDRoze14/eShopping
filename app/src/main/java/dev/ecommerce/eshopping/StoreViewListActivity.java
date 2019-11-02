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
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import dev.ecommerce.eshopping.Model.Product;
import dev.ecommerce.eshopping.ViewHoder.ProductViewHolder;

public class StoreViewListActivity extends AppCompatActivity {

    private DatabaseReference productref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String pcategory;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_view_list);

        pcategory = getIntent().getStringExtra("pcategory");


        productref = FirebaseDatabase.getInstance().getReference().child("Product").child(pcategory);

        recyclerView = findViewById(R.id.recycleView_store);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        back = findViewById(R.id.product_list_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreViewListActivity.this, StoreViewCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
            new FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(productref, Product.class)
            .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
            new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Product product) {
                    productViewHolder.txt_pid.setText(product.getId());
                    productViewHolder.txt_pname.setText(product.getName());
                    productViewHolder.txt_price.setText("ราคา : "+product.getPrice()+" บาท");
                    productViewHolder.txt_pamount.setText("X"+product.getAmount());
                    Picasso.get().load(product.getImage()).into(productViewHolder.img_product);

                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_cart, parent, false);
                    ProductViewHolder holder = new ProductViewHolder(view);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Test--------------------------------------------------------------
                        }
                    });
                    return holder;
                }
            };
        recyclerView.setAdapter(adapter);
        adapter.startListening();   }
}
