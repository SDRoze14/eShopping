package dev.ecommerce.eshopping.ViewHoder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ecommerce.eshopping.Interface.ItemClickListner;
import dev.ecommerce.eshopping.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_pid, txt_pname, txt_price, txt_pamount;
    public ImageView img_product;
    public ItemClickListner itemClickListner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = itemView.findViewById(R.id.pic_product_list);
        txt_pid = itemView.findViewById(R.id.id_product_list);
        txt_pname = itemView.findViewById(R.id.name_product_list);
        txt_price = itemView.findViewById(R.id.price_product_list);
        txt_pamount = itemView.findViewById(R.id.amount_product_list);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);

    }
}
