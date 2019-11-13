package dev.ecommerce.eshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import dev.ecommerce.eshopping.Model.Promotion;

public class PageAdapter extends PagerAdapter {

    Context context;
    List<Promotion> promotionList;
    LayoutInflater inflater;

    public PageAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return promotionList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.pager_item, container, false);

        ImageView image_promotion = view.findViewById(R.id.image_promotion);

        Picasso.get().load(promotionList.get(position).getImage_promotion()).into(image_promotion);

        container.addView(view);
        return view;
    }
}
