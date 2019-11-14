package dev.ecommerce.eshopping.ViewHoder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import dev.ecommerce.eshopping.Interface.ItemClickListner;
import dev.ecommerce.eshopping.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_product_id, txt_product_name, txt_product_price,num;
    public ImageView imageView;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_product_id = itemView.findViewById(R.id.id_product_list);
        txt_product_name = itemView.findViewById(R.id.name_product_list);
        txt_product_price = itemView.findViewById(R.id.price_product_list);
        num = itemView.findViewById(R.id.amount_product_list);
        imageView = itemView.findViewById(R.id.pic_product_list);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
