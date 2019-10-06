package dev.ecommerce.eshopping.Model;

public class Product {

    private String product_name, product_uid, product_img, product_id;
    private Float product_price;

    public Product(){

    }

    public Product (String product_name, String product_uid, Float product_price, String product_img, String product_id) {
        this.product_name = product_name;
        this.product_uid = product_uid;
        this.product_price = product_price;
        this.product_img = product_img;
        this.product_id =product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_uid() {
        return product_uid;
    }

    public void setProduct_uid(String product_uid) {
        this.product_uid = product_uid;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
    }
}
