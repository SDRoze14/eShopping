package dev.ecommerce.eshopping.Model;

public class Category {

    private String id;
    private String[] category = {"ผักและผลไม้", "เนื้อสัตว์", "ปลาและอาหารทะเล", "นม เนย ไข่", "อาหารแช่แช็ง", "อาหารสำเร็จรูป", "เครื่องดื่มและขนมขบเคี้ยว", "อุปกรณืและของใช้ในครัวเรือน", "เครื่องเขียนอละอุปกรณ์ในสำนักงาน"
            , "เครื่องใช้ไฟฟ้า", "สุขภาพและความงาม", "แม่และเด็ก", "ผลิตภัณฑ์สำหรับสัตว์เลี้ยง"};

    public Category(){

    }

    public Category(String id, String[] category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }
}
