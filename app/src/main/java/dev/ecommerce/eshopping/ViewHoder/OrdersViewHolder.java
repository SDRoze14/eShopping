package dev.ecommerce.eshopping.ViewHoder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ecommerce.eshopping.Interface.ItemClickListner;
import dev.ecommerce.eshopping.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView id_product, name_product, price_product, number;
    private ItemClickListner itemClickListner;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.number);
        id_product = itemView.findViewById(R.id.id_product_order);
        name_product = itemView.findViewById(R.id.name_product_order);
        price_product = itemView.findViewById(R.id.price_product_order);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
