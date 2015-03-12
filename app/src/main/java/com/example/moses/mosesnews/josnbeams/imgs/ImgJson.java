package com.example.moses.mosesnews.josnbeams.imgs;

/**
 * Created by Moses on 2015
 */
public class ImgJson {
    private int status;
    private ImgsAll data;

    public void setData(ImgsAll data) {
        this.data = data;
    }

    public ImgsAll getData() {
        return data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
