package com.example.moses.mosesnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moses.mosesnews.contents.SettingActivity;
import com.example.moses.mosesnews.contents.imgs.ImageMainFragment;
import com.example.moses.mosesnews.contents.news.NewsMainPagerFragment;
import com.example.moses.mosesnews.contents.videos.VideoMainFragment;
import com.example.moses.mosesnews.contents.weather.WeatherActivity;

/**
 * Created by Moses on 2015.1.26
 */
public class SlidingMenuLeftFragment extends Fragment implements View.OnClickListener {
    Button newsButton, videosButton, picturesButton, weatherButton, settingButton;
    MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_menu_left, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化menu 中Button控件（item）
     *
     * @param view :侧滑菜单视图
     */
    public void initView(View view) {
        newsButton = (Button) view.findViewById(R.id.item_news);
        videosButton = (Button) view.findViewById(R.id.item_videos);
        picturesButton = (Button) view.findViewById(R.id.item_pictures);
        weatherButton = (Button) view.findViewById(R.id.item_weather);
        settingButton = (Button) view.findViewById(R.id.item_setting);

        newsButton.setOnClickListener(this);
        videosButton.setOnClickListener(this);
        picturesButton.setOnClickListener(this);
        weatherButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
    }

    /**
     * 侧滑菜单的点击事
     *
     * @param v : item in sliding menu
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_news:
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().
                        replace(R.id.main_container, NewsMainPagerFragment.newInstance())
                        .commit();
                activity.toggle();
                break;
            case R.id.item_videos:
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().
                        replace(R.id.main_container, VideoMainFragment.newInstance())
                        .commit();
                activity.toggle();
                break;
            case R.id.item_pictures:
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().
                        replace(R.id.main_container, ImageMainFragment.newInstance()).
                        commit();
                activity.toggle();
                break;
            case R.id.item_weather:
                startActivity(new Intent(getActivity(), WeatherActivity.class));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.toggle();
                break;
            case R.id.item_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.toggle();
                break;
            default:
                break;
        }
    }

}
