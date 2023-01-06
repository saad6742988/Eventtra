package com.example.eventtra;

public class subEventsModel {
    int pic; //As local pictures are stored in binary, for online pictures, change it to string
    String text; //name
    String desc; //description
    String price;   //price

    //constructor


    public subEventsModel(int pic, String text, String desc, String price) {
        this.pic = pic;
        this.text = text;
        this.desc = desc;
        this.price = price;
    }

    //getters
    public int getPic() {
        return pic;
    }

    public String getText() {
        return text;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }

    //setters
    public void setPic(int pic) {
        this.pic = pic;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
