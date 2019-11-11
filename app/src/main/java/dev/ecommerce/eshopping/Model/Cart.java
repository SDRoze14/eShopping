package dev.ecommerce.eshopping.Model;

public class Cart {
    private String ID, product_id, UID;
    private Float product_price;
    private int i;

    public Cart() {

    }

    public Cart(String ID, String product_id, Float product_price, String UID, int i) {
        this.ID = ID;
        this.product_id = product_id;
        this.UID = UID;
        this.product_price = product_price;
        this.i = i;
}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProduct_id() {
        return product_id;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
