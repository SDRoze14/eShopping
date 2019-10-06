package dev.ecommerce.eshopping.Model;

public class Product {

    private String product_name, UID;
    private Float product_price;

    public Product(){

    }

    public Product (String product_name, String UID, Float product_price) {
        this.product_name = product_name;
        this.UID = UID;
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
    }
}
