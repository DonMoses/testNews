package com.example.moses.mosesnews.contents.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.josnbeams.weather.Weather;
import com.example.moses.mosesnews.josnbeams.weather.operweather.OpenWeather;
import com.example.moses.mosesnews.utils.MosesHTTPUtil;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Moses on 2015.2.7
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    private TextView cityName;
    private TextView updateTimeTxt, dateTxt, infoTxt, degreeTxt;
    ImageButton refreshBtn, viewZoneBtn;

    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<WeatherActivity> mWeatherActivity;

        public MyHandler(WeatherActivity weatherActivity) {
            this.mWeatherActivity = new WeakReference<>(weatherActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            WeatherActivity weatherActivity = mWeatherActivity.get();
            if (weatherActivity != null) {
                switch (msg.what) {
                    case 0:
                        String weatherJsonStr = msg.obj.toString();
                        JSONObject jsonObject = JSON.parseObject(weatherJsonStr);
                        int status = jsonObject.getIntValue("status");
                        if (status == 200) {
                            Weather weather = JSONObject.parseObject(jsonObject.getString("data"), Weather.class);
                            weatherActivity.updateWeather(weather);
                        } else if (status == 300) {
                            weatherActivity.weatherErrorInfo();
                        }
                        break;
                    case 1:
                        OpenWeather openWeather = JSONObject.parseObject(msg.obj.toString(), OpenWeather.class);
                        weatherActivity.updateOpenWeather(openWeather);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateWeather(Weather weather) {
        updateTimeTxt.setText("今天" + weather.getRefreshTime() + "发布");
        dateTxt.setText(weather.getDateTime());
        infoTxt.setText(weather.getWindDirection());
        degreeTxt.setText(weather.getMinTemp() + " ~ " + weather.getMaxTemp());
        cityName.setText(weather.getCity());
    }

    public void updateOpenWeather(OpenWeather openWeather) {
        //定义日期的中文输出格式,并输出日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒 E", Locale.CHINA);
        Date date = new Date(openWeather.getDt());
        updateTimeTxt.setText("今天" + sdf.format(date) + "发布");
        infoTxt.setText(openWeather.getWeather().get(0).getMain());
        double minTemp = (openWeather.getMain().getTemp_min() - 273.15) * 0.8;
        double maxTemp = (openWeather.getMain().getTemp_max() - 273.15) * 0.8;
        double curTemp = (openWeather.getMain().getTemp() - 273.15) * 0.8;
        DecimalFormat df = new DecimalFormat("#.0");
        degreeTxt.setText(df.format(minTemp) + "℃ ~ " + df.format(maxTemp) + "℃");
        infoTxt.setText(df.format(curTemp) + "℃");
    }

    public void weatherErrorInfo() {
        infoTxt.setText("非商业用户访问次数过于频繁（20次/小时），请您购买服务！");
        updateTimeTxt.setVisibility(View.INVISIBLE);
        dateTxt.setVisibility(View.INVISIBLE);
        degreeTxt.setVisibility(View.INVISIBLE);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        getWeatherJson();
    }

    //http://api.36wu.com/Weather/GetWeather?district=成都
    public void initView() {
        cityName = (TextView) findViewById(R.id.weather_city_name);
        updateTimeTxt = (TextView) findViewById(R.id.weather_update_time);
        dateTxt = (TextView) findViewById(R.id.weather_date);
        infoTxt = (TextView) findViewById(R.id.weather_info);
        degreeTxt = (TextView) findViewById(R.id.weather_degree);

        refreshBtn = (ImageButton) findViewById(R.id.weather_refresh_btn);
        viewZoneBtn = (ImageButton) findViewById(R.id.weather_drawer_btn);
        refreshBtn.setOnClickListener(this);
        viewZoneBtn.setOnClickListener(this);

    }

    public void getWeatherJson() {
        String city = cityName.getText().toString();
        String districtStr = "district";
        String weatherApiUrl = "http://api.36wu.com/Weather/GetWeather";
        String url = weatherApiUrl + "?" + districtStr + "=" + city;
        Log.e("TAG", ">>>>>>>weather url>>>>>>>" + url);
        String requestType = MosesHTTPUtil.NORMAL_SIMPLE_TYPE;
        String requestMethod = MosesHTTPUtil.METHOD_GET;
        MosesHTTPUtil mMosesHTTPUtil = new MosesHTTPUtil(url, requestMethod, requestType);
        mMosesHTTPUtil.sendRequest(new MosesHTTPUtil.HttpCallBackInterface() {
            @Override
            public void onFinish(String s) {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = s;
                mHandler.sendMessage(msg);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_refresh_btn:
                getWeatherJson();
                break;
            case R.id.weather_drawer_btn:
                Intent intent = new Intent(this, ZoneActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            String weatherXMLUrl = data.getStringExtra("weather_xml_url");
            String resultCityName = data.getStringExtra("result_city_name");
            cityName.setText(resultCityName);
            Log.e("TAG", ">>>>>>weatherXMLUrl>>>" + weatherXMLUrl);
            MosesHTTPUtil mosesHTTPUtil = new MosesHTTPUtil(weatherXMLUrl, MosesHTTPUtil.METHOD_GET,
                    MosesHTTPUtil.NORMAL_SIMPLE_TYPE);
            mosesHTTPUtil.sendRequest(new MosesHTTPUtil.HttpCallBackInterface() {
                @Override
                public void onFinish(String s) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = s;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }


}
