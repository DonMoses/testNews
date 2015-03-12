package com.example.moses.mosesnews.contents.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.db.City;
import com.example.moses.mosesnews.db.County;
import com.example.moses.mosesnews.db.MosesNewsWeatherDB;
import com.example.moses.mosesnews.db.Province;
import com.example.moses.mosesnews.utils.MosesHTTPUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses on 2015
 */
public class ZoneActivity extends Activity {

    private static final String TAG = "ZoneActivity";

    private MosesNewsWeatherDB mosesNewsWeatherDB;
    private List<Province> mProvinces;
    private List<City> mCities;
    private List<County> mCounties;
    private TextView provinceTxt, cityTxt, countyTxt;
    private String provincePy = null, cityPy = null, countyPy = null;
    private ArrayAdapter<String> provinceAdapter, cityAdapter, countyAdapter;
    private List<String> provincesStr;
    private List<String> citiesStr;
    private List<String> countiesStr;

    String xmlUrl = null;
    String resultCityName = null;

    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<ZoneActivity> mZoneActivity;

        public MyHandler(ZoneActivity zoneActivity) {
            this.mZoneActivity = new WeakReference<>(zoneActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ZoneActivity zoneActivity = mZoneActivity.get();
            if (zoneActivity != null) {
                switch (msg.what) {
                    case 0:
                        String provinceXmlStr = msg.obj.toString();
                        zoneActivity.parseProvinceXMLWithPull(provinceXmlStr);
//                    Log.e("TAG", "provinceXmlStr>>>>>>>>>>" + provinceXmlStr);
                        break;
                    case 1:
                        String cityXmlStr = msg.obj.toString();
                        zoneActivity.parseCityXMLWithPull(cityXmlStr, msg.arg1);
//                    Log.e("TAG", "provinceXmlStr>>>>>>>>>>" + cityXmlStr);
                        break;

                    case 2:
                        String countyXmlStr = msg.obj.toString();
                        zoneActivity.parseCountyXMLWithPull(countyXmlStr, msg.arg1);
//                    Log.e("TAG", "countyXmlStr>>>>>>>>>>" + countyXmlStr);
                        break;

                    default:
                        break;

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        initDB();
    }

    public void initDB() {
        mProvinces = new ArrayList<>();
        mCities = new ArrayList<>();
        mCounties = new ArrayList<>();

        provincesStr = new ArrayList<>();
        citiesStr = new ArrayList<>();
        countiesStr = new ArrayList<>();

        mosesNewsWeatherDB = MosesNewsWeatherDB.getInstance(this);

        provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, provincesStr);
        cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citiesStr);
        countyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countiesStr);

        mProvinces = mosesNewsWeatherDB.loadProvinces();
        Log.e("TAG", ">>>>mProvinces.size()>>>" + mProvinces.size());
        if (mProvinces.size() < 1) {
            requestForZoneXML("http://flash.weather.com.cn/wmaps/xml/china.xml", 0, 0);
        } else {
            for (Province province : mProvinces) {
                provincesStr.add(province.getProvinceName());
                provinceAdapter.notifyDataSetChanged();
            }
        }

        provinceTxt = (TextView) findViewById(R.id.province_text);
        cityTxt = (TextView) findViewById(R.id.city_text);
        countyTxt = (TextView) findViewById(R.id.county_text);

        ListView provinceListView = (ListView) findViewById(R.id.province_list_view);
        ListView cityListView = (ListView) findViewById(R.id.city_list_view);
        ListView countyListView = (ListView) findViewById(R.id.county_list_view);

        provinceListView.setAdapter(provinceAdapter);
        cityListView.setAdapter(cityAdapter);
        countyListView.setAdapter(countyAdapter);

        provinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provincePy = mProvinces.get(position).getProvincePyName();

                provinceTxt.setText(mProvinces.get(position).getProvinceName());
                cityTxt.setText("市");
                countyTxt.setText("县");

                mCities.clear();
                citiesStr.clear();
                cityAdapter.notifyDataSetChanged();

                mCounties.clear();
                countiesStr.clear();
                countyAdapter.notifyDataSetChanged();

                mCities = mosesNewsWeatherDB.loadCities(mProvinces.get(position).getId());
                if (mCities.size() < 1) {
                    requestForZoneXML("http://flash.weather.com.cn/wmaps/xml/" +
                                    mProvinces.get(position).getProvincePyName() + ".xml", 1,
                            mProvinces.get(position).getId());
                } else {
                    for (City city : mCities) {
                        citiesStr.add(city.getCityName());
                        cityAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityPy = mCities.get(position).getCityPyName();

                cityTxt.setText(mCities.get(position).getCityName());
                countyTxt.setText("县");

                mCounties.clear();
                countiesStr.clear();
                countyAdapter.notifyDataSetChanged();

                mCounties = mosesNewsWeatherDB.loadCounties(mCities.get(position).getId());
                if (mCounties.size() < 1) {
                    requestForZoneXML("http://flash.weather.com.cn/wmaps/xml/" +
                                    mCities.get(position).getCityPyName() + ".xml", 2,
                            mCities.get(position).getId());
                } else {
                    for (County county : mCounties) {
                        countiesStr.add(county.getCountyName());
                        countyAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        countyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countyPy = mCounties.get(position).getCountyPyName();

                countyTxt.setText(mCounties.get(position).getCountyName());
            }
        });
    }

    public void requestForZoneXML(String url, final int msgWhat, final int province_or_city_id) {
        MosesHTTPUtil mosesHTTPUtil = new MosesHTTPUtil(url,
                MosesHTTPUtil.METHOD_GET, MosesHTTPUtil.NORMAL_SIMPLE_TYPE);
        mosesHTTPUtil.sendRequest(new MosesHTTPUtil.HttpCallBackInterface() {
            @Override
            public void onFinish(String s) {
                Message msg = Message.obtain();
                msg.what = msgWhat;
                msg.obj = s;
                msg.arg1 = province_or_city_id;
                mHandler.sendMessage(msg);
            }
        });

    }

    public void parseProvinceXMLWithPull(String baseXMLStr) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(baseXMLStr));
            int eventType = xmlPullParser.getEventType();
            String quName;
            String provinceCode;
            String provincePyName;
            Province province;
            int i = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        mProvinces = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        province = new Province();
                        if ("city".equals(nodeName)) {
                            quName = xmlPullParser.getAttributeValue(null, "quName");
                            provincePyName = xmlPullParser.getAttributeValue(null, "pyName");
                            if (i / 2 < 10) {
                                provinceCode = "0" + String.valueOf(i / 2);
                            } else {
                                provinceCode = String.valueOf(i / 2);
                            }
                            province.setProvinceName(quName);
                            province.setProvincePyName(provincePyName);
                            province.setProvinceCode(provinceCode);
                            mProvinces.add(province);
                            provincesStr.add(province.getProvinceName());
                            provinceAdapter.notifyDataSetChanged();
                            mosesNewsWeatherDB.saveProvince(province);
                        }
                        break;
                }
                i++;
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }

    public void parseCityXMLWithPull(String provinceXMLStr, int provinceId) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(provinceXMLStr));
            int eventType = xmlPullParser.getEventType();
            String cityName;
            String cityCode;
            String cityPyName;
            City city;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        mCities = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        city = new City();
                        if ("city".equals(nodeName)) {
                            cityName = xmlPullParser.getAttributeValue(null, "cityname");
                            cityCode = xmlPullParser.getAttributeValue(null, "url");
                            cityPyName = xmlPullParser.getAttributeValue(null, "pyName");
                            city.setCityName(cityName);
                            city.setCityPyName(cityPyName);
                            city.setCityCode(cityCode);
                            city.setProvinceId(provinceId);
                            mCities.add(city);
                            citiesStr.add(city.getCityName());
                            cityAdapter.notifyDataSetChanged();
                            mosesNewsWeatherDB.saveCity(city);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }

    public void parseCountyXMLWithPull(String cityXMLStr, int cityId) {
        mCounties.clear();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(cityXMLStr));
            int eventType = xmlPullParser.getEventType();
            String countyName;
            String countyCode;
            String countyPyName;
            County county;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        mCounties = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        county = new County();
                        if ("city".equals(nodeName)) {
                            countyName = xmlPullParser.getAttributeValue(null, "cityname");
                            countyCode = xmlPullParser.getAttributeValue(null, "url");
                            countyPyName = xmlPullParser.getAttributeValue(null, "pyName");
                            county.setCountyName(countyName);
                            county.setCountyPyName(countyPyName);
                            county.setCountyCode(countyCode);
                            county.setCityId(cityId);
                            mCounties.add(county);
                            countiesStr.add(county.getCountyName());
                            countyAdapter.notifyDataSetChanged();
                            Log.e(TAG, county.toString());
                            mosesNewsWeatherDB.saveCounty(county);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearchWeatherClick(View view) {
        String pyStr = null;
        switch (view.getId()) {
            case R.id.search_weather_btn:
                if (provincePy == null && cityPy == null && countyPy == null) {
                    Toast.makeText(this, "请选择查询区域", Toast.LENGTH_SHORT).show();
                } else if (provincePy != null && cityPy == null && countyPy == null) {
                    pyStr = provincePy;
                    resultCityName = provinceTxt.getText().toString();
                } else if (provincePy != null && cityPy != null && countyPy == null) {
                    pyStr = cityPy;
                    resultCityName = cityTxt.getText().toString();
                } else if (provincePy != null && cityPy != null) {
                    pyStr = countyPy;
                    resultCityName = countyTxt.getText().toString();
                }

                if (pyStr == null) {
                    Toast.makeText(this, "地址有误，请重新选择！", Toast.LENGTH_SHORT).show();
                } else {
                    xmlUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + pyStr + ",cn";
                    setResultAction();
                }

                break;
        }

    }

    public void setResultAction() {
        Intent intent = new Intent();
        intent.putExtra("weather_xml_url", xmlUrl);
        intent.putExtra("result_city_name", resultCityName);
        this.setResult(0, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        this.setResult(1, new Intent());
        super.onBackPressed();
    }
}
