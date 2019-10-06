package dev.ecommerce.eshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import dev.ecommerce.eshopping.Model.Product;

public class CustomAdepter extends BaseAdapter {

    Context c;
    ArrayList<Product> products;
    LayoutInflater inflater;

    public CustomAdepter(Context c, ArrayList<Product> products) {
        this.c = c;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_cart, parent, false);
        }


        return null;
    }
}
