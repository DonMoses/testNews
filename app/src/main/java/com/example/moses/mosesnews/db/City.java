package com.example.moses.mosesnews.db;

/**
 * Created by Moses on 2015
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;
    private String cityPyName;

    public String getCityPyName() {
        return cityPyName;
    }

    public void setCityPyName(String cityPyName) {
        this.cityPyName = cityPyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", provinceId=" + provinceId +
                ", cityPyName='" + cityPyName + '\'' +
                '}';
    }
}
