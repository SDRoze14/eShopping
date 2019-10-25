package dev.ecommerce.eshopping.ViewHoder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ecommerce.eshopping.Interface.ItemClickListner;
import dev.ecommerce.eshopping.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_category;
    public ItemClickListner itemClickListner;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_category = itemView.findViewById(R.id.txt_category);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
}
