package dev.ecommerce.eshopping.ViewHoder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ecommerce.eshopping.Interface.ItemClickListner;
import dev.ecommerce.eshopping.R;

public class PromotionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView pic_promotion_list;
    public TextView name_promotion_list, id_promotion_list, price_promotion_list, date_start_promotion, date_end_promotion;
    public ItemClickListner itemClickListner;


    public PromotionViewHolder(@NonNull View itemView) {
        super(itemView);
        pic_promotion_list = itemView.findViewById(R.id.pic_promotion_list);
        name_promotion_list = itemView.findViewById(R.id.name_promotion_list);
        id_promotion_list = itemView.findViewById(R.id.id_promotion_list);
        price_promotion_list = itemView.findViewById(R.id.price_promotion_list);
        date_start_promotion = itemView.findViewById(R.id.date_start_promotion);
        date_end_promotion = itemView.findViewById(R.id.date_end_promotion);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
}
