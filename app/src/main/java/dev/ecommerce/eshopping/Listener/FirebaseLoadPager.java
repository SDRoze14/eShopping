package dev.ecommerce.eshopping.Listener;

import java.util.List;

import dev.ecommerce.eshopping.Model.Promotion;

public interface FirebaseLoadPager {
    void  onFirebaseLoadSuccess(List<Promotion> promotionList);
    void  onFirebaseLoadFailed(String message);

}
