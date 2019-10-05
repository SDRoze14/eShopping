package dev.ecommerce.eshopping.Model;

public class Order {
    private String id_order,phone,cart,date;

    public Order() {

    }

    public Order(String id_cart, String phone, String cart, String date) {
        this.id_order = id_cart;
        this.phone = phone;
        this.cart = cart;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }
}
