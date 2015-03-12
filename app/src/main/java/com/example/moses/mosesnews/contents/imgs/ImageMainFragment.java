package com.example.moses.mosesnews.contents.imgs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.utils.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Moses on 2015
 */
public class ImageMainFragment extends Fragment {
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    ViewPager mViewPager;
    ImageButton mAddImgCatoBtn;
    ArrayList<String> mImageType = new ArrayList<>();
    MyImgFragmentAdapter myImgFragmentAdapter;

    public static ImageMainFragment newInstance() {
        return new ImageMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_main, container, false);
        initView(view);
        return view;

    }

    public void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.img_main_view_pager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.img_main_tab_strip);
        mAddImgCatoBtn = (ImageButton) view.findViewById(R.id.img_add_cato_btn);

        mPagerSlidingTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
        mPagerSlidingTabStrip.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        mViewPager.setAdapter(myImgFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);

        myImgFragmentAdapter.setData(mImageType);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Collections.addAll(mImageType, "精选图", "趣味图", "美图", "故事图");
        myImgFragmentAdapter = new MyImgFragmentAdapter(getFragmentManager());
    }

    class MyImgFragmentAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> mData = new ArrayList<>();

        public MyImgFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(ArrayList<String> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return ImgContentFragment.newInstance(mData.get(position));
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
