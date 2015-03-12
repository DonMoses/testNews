package com.example.moses.mosesnews.db;

/**
 * Created by Moses on 2015
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;
    private String countyPyName;

    public String getCountyPyName() {
        return countyPyName;
    }

    public void setCountyPyName(String countyPyName) {
        this.countyPyName = countyPyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", countyName='" + countyName + '\'' +
                ", countyCode='" + countyCode + '\'' +
                ", cityId=" + cityId +
                ", countyPyName='" + countyPyName + '\'' +
                '}';
    }
}
