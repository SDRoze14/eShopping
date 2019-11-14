package dev.ecommerce.eshopping.Model;

public class Orders {

    private String id_order,date,time,product_id,name_product, uid, amount;
    private Float price;
    private int quality;

    public Orders(){

    }

    public Orders(String id_order, String date, String time, String product_id, String name_product, Float price, int quality, String uid, String amount) {
        this.id_order = id_order;
        this.date = date;
        this.time = time;
        this.product_id = product_id;
        this.name_product = name_product;
        this.price = price;
        this.quality = quality;
        this.uid = uid;
        this.amount = amount;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


}
