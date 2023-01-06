package com.example.eventtra;

public class subEventsModel {
    int pic; //As local pictures are stored in binary, for online pictures, change it to string
    String text;

    public subEventsModel(int pic, String text) {
        this.pic = pic;
        this.text = text;
    }

    //getters
    public int getPic() {
        return pic;
    }

    public String getText() {
        return text;
    }

    //setters
    public void setPic(int pic) {
        this.pic = pic;
    }

    public void setText(String text) {
        this.text = text;
    }
}
