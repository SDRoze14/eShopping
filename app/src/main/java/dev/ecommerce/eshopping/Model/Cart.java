package dev.ecommerce.eshopping.Model;

public class Cart {
    String cart_id,product_id;

    public Cart() {

    }

    public Cart(String cart_id, String product_id) {
        this.cart_id = cart_id;
        this.product_id = product_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String toString() {
        return this.product_id;
    }
}
