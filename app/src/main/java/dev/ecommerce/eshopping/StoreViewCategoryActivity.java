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

import dev.ecommerce.eshopping.Model.Category;
import dev.ecommerce.eshopping.ViewHoder.CategoryViewHolder;

public class StoreViewCategoryActivity extends AppCompatActivity {

    private DatabaseReference categoryref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String pcategory;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_view_category);

        categoryref = FirebaseDatabase.getInstance().getReference().child("Category");

        recyclerView = findViewById(R.id.list_category);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        back = findViewById(R.id.back_list_category);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreViewCategoryActivity.this, StoreActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categoryref, Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, int i, @NonNull Category category) {
                        categoryViewHolder.txt_category.setText(category.getCategory());

                        categoryViewHolder.txt_category.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pcategory = categoryViewHolder.txt_category.getText().toString();

                                Intent intent = new Intent(StoreViewCategoryActivity.this, StoreViewListActivity.class);
                                intent.putExtra("pcategory", pcategory);
                                startActivity(intent);
                                Toast.makeText(StoreViewCategoryActivity.this, pcategory, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
                        CategoryViewHolder holder = new CategoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
