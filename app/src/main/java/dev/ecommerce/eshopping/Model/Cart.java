package dev.ecommerce.eshopping.Model;

public class Cart {
    private String cart_id, product_id, product_price, product_name;

    public Cart() {

    }

    public Cart(String cart_id, String product_id, String product_price, String product_name) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
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

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


}
