package com.example.moses.mosesnews.josnbeams.weather.operweather;

import java.util.ArrayList;

/**
 * Created by Moses on 2015
 */
public class OpenWeather {
    private CityLocation coord;
    private SunRiseMsg sys;
    private ArrayList<CloudMsg> weather;
    private MainWeatherMsg main;
    private WindMsg wind;
    private CloudMsgAll clouds;
    private long dt;
    private int id;
    private String name;
    private int cod;

    public CityLocation getCoord() {
        return coord;
    }

    public void setCoord(CityLocation coord) {
        this.coord = coord;
    }

    public SunRiseMsg getSys() {
        return sys;
    }

    public void setSys(SunRiseMsg sys) {
        this.sys = sys;
    }

    public ArrayList<CloudMsg> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<CloudMsg> weather) {
        this.weather = weather;
    }

    public MainWeatherMsg getMain() {
        return main;
    }

    public void setMain(MainWeatherMsg main) {
        this.main = main;
    }

    public WindMsg getWind() {
        return wind;
    }

    public void setWind(WindMsg wind) {
        this.wind = wind;
    }

    public CloudMsgAll getClouds() {
        return clouds;
    }

    public void setClouds(CloudMsgAll clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
