package com.example.moses.mosesnews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses on 2015
 */
public class MosesNewsWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "moses_news_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static MosesNewsWeatherDB mosesNewsWeatherDB;    //单例

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化。 【 单例】
     */
    private MosesNewsWeatherDB(Context context) {
        MosesNewsWeatherOpenHelper dbHelper = new MosesNewsWeatherOpenHelper(
                context, DB_NAME, null, VERSION);

        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取MosesNewsWeatherDB 的实例
     */
    public synchronized static MosesNewsWeatherDB getInstance(Context context) {
        if (mosesNewsWeatherDB == null) {
            mosesNewsWeatherDB = new MosesNewsWeatherDB(context);
        }
        return mosesNewsWeatherDB;
    }

    /**
     * 将Province实例储存到数据库
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            values.put("province_py_name", province.getProvincePyName());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvincePyName(cursor.getString(cursor.getColumnIndex("province_py_name")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 将City实例储存到数据库
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            values.put("city_py_name", city.getCityPyName());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityPyName(cursor.getString(cursor.getColumnIndex("city_py_name")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 将County实例储存到数据库
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            values.put("county_py_name", county.getCountyPyName());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                county.setCountyPyName(cursor.getString(cursor.getColumnIndex("county_py_name")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
