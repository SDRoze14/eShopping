package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import dev.ecommerce.eshopping.Model.Product;
import dev.ecommerce.eshopping.ViewHoder.ProductViewHolder;

public class StoreViewListActivity extends AppCompatActivity {

    private DatabaseReference productref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String id;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_view_list);

        productref = FirebaseDatabase.getInstance().getReference().child("Product");

        recyclerView = findViewById(R.id.recycleView_store);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        back = findViewById(R.id.product_list_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreViewListActivity.this, StoreActivity.class);
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
                        id = product.getId();
                        productViewHolder.txt_pname.setText(product.getName());
                        productViewHolder.txt_price.setText("ราคา : "+product.getPrice()+" บาท");
                        productViewHolder.txt_pamount.setText("Edit");
                        productViewHolder.txt_pamount.setTextColor(Color.BLUE);
                        Picasso.get().load(product.getImage()).into(productViewHolder.img_product);

                        productViewHolder.txt_pamount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence sequence[] = new CharSequence[]
                                        {
                                            "Edit",
                                            "Remove"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(StoreViewListActivity.this);

                                builder.setItems(sequence, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Intent intent = new Intent(StoreViewListActivity.this, EditProductActivity.class);
                                            intent.putExtra("product_id",product.getId());
                                            startActivity(intent);
                                        }
                                        if (which == 1) {
                                            productref.child(product.getId()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(StoreViewListActivity.this, "ลบรายการสำเร็จ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_cart, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        }



}
