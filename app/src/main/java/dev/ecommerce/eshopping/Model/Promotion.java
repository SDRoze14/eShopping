package dev.ecommerce.eshopping.Model;

public class Promotion {
    private String name_promotion, product_id, date_start, date_end, image_promotion;
    private int discount;

    public Promotion() {

    }

    public Promotion(String name_promotion, String product_id, String date_start, String date_end, String image_promotion, int discount) {
        this.name_promotion = name_promotion;
        this.product_id = product_id;
        this.date_start = date_start;
        this.date_end = date_end;
        this.image_promotion = image_promotion;
        this.discount = discount;
    }

    public String getName_promotion() {
        return name_promotion;
    }

    public void setName_promotion(String name_promotion) {
        this.name_promotion = name_promotion;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getImage_promotion() {
        return image_promotion;
    }

    public void setImage_promotion(String image_promotion) {
        this.image_promotion = image_promotion;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
