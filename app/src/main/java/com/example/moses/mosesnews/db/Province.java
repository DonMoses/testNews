package com.example.moses.mosesnews.db;

/**
 * Created by Moses on 2015
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;
    private String provincePyName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvincePyName() {
        return provincePyName;
    }

    public void setProvincePyName(String provincePyName) {
        this.provincePyName = provincePyName;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", provinceName='" + provinceName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", provincePyName='" + provincePyName + '\'' +
                '}';
    }
}
