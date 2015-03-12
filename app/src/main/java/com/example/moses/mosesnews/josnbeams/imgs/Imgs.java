package com.example.moses.mosesnews.josnbeams.imgs;

import java.util.ArrayList;

/**
 * Created by Moses on 2015
 */
public class Imgs {
    private ArrayList<TheImg> list;
    private String total;
    private String picTemplate;

    public void setList(ArrayList<TheImg> list) {
        this.list = list;
    }

    public ArrayList<TheImg> getList() {
        return list;
    }

    public void setPicTemplate(String picTemplate) {
        this.picTemplate = picTemplate;
    }

    public String getPicTemplate() {
        return picTemplate;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

}
