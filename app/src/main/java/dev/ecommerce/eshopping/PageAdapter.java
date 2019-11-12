package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import dev.ecommerce.eshopping.Page1;
import dev.ecommerce.eshopping.Page2;

public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getItem(int position) {
        if(position == 0)
            return new Page1();
        else if(position == 1)
            return new Page2();

        return null;
    }
}
