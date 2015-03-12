package com.example.moses.mosesnews.contents.videos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.utils.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Moses on 2015
 */
public class VideoMainFragment extends Fragment {
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    ViewPager mViewPager;
    MyVideoFragmentAdapter myVideoFragmentAdapter;
    ArrayList<String> mTitles;

    public static VideoMainFragment newInstance() {
        return new VideoMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_main, container, false);
        initView(view);

        return view;

    }

    public void initView(View v) {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.video_main_tab_strip);
        mViewPager = (ViewPager) v.findViewById(R.id.video_main_view_pager);

        mTitles = new ArrayList<>();
        Collections.addAll(mTitles, "热点视频", "娱乐视频", "搞笑视频", "精品视频");
        myVideoFragmentAdapter = new MyVideoFragmentAdapter(getFragmentManager());
        mViewPager.setAdapter(myVideoFragmentAdapter);

        myVideoFragmentAdapter.setData(mTitles);        ////////////////
        mPagerSlidingTabStrip.setViewPager(mViewPager);     ////成对出现
    }

    class MyVideoFragmentAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> mData = new ArrayList<>();

        public MyVideoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(ArrayList<String> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return VideoContentFragment.newInstance(mData.get(position));
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mData.get(position);
        }
    }

}
