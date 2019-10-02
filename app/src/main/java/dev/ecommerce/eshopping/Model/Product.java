package dev.ecommerce.eshopping.Model;

public class Product {

    private String barcode, pname, pmodel, ptime, description, pimage;
    private Integer price, quantity;

    public Product() {

    }

    public Product(String barcode, String pname, String pmodel, String ptime, String description, String pimage, Integer price, Integer quantity) {
        this.barcode = barcode;
        this.pname = pname;
        this.pmodel = pmodel;
        this.ptime = ptime;
        this.description = description;
        this.pimage = pimage;
        this.price = price;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPmodel() {
        return pmodel;
    }

    public void setPmodel(String pmodel) {
        this.pmodel = pmodel;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
