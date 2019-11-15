package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import dev.ecommerce.eshopping.Model.Orders;
import dev.ecommerce.eshopping.Model.Promotion;
import dev.ecommerce.eshopping.ViewHoder.OrdersViewHolder;
import dev.ecommerce.eshopping.ViewHoder.PromotionViewHolder;

public class PromotionListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference promotionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);

        recyclerView = findViewById(R.id.recycle_promotion);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        promotionRef = FirebaseDatabase.getInstance().getReference().child("Promotion");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Promotion> options =
                new FirebaseRecyclerOptions.Builder<Promotion>()
                        .setQuery(promotionRef, Promotion.class)
                        .build();

        FirebaseRecyclerAdapter<Promotion, PromotionViewHolder> adapter =
                new FirebaseRecyclerAdapter<Promotion, PromotionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PromotionViewHolder promotionViewHolder, int i, @NonNull Promotion promotion) {
                        Picasso.get().load(promotion.getImage_promotion()).into(promotionViewHolder.pic_promotion_list);
                        promotionViewHolder.date_end_promotion.setText(promotion.getDate_end());
                        promotionViewHolder.date_start_promotion.setText(promotion.getDate_start());
                        promotionViewHolder.id_promotion_list.setText(promotion.getProduct_id());
                        promotionViewHolder.name_promotion_list.setText(promotion.getName_promotion());
                        String discount = String.valueOf(promotion.getDiscount());
                        promotionViewHolder.price_promotion_list.setText(discount);


                    }

                    @NonNull
                    @Override
                    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_list, parent, false);
                        PromotionViewHolder holder = new PromotionViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
