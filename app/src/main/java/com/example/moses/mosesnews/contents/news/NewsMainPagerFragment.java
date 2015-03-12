package com.example.moses.mosesnews.contents.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.contents.EmptyFragment;
import com.example.moses.mosesnews.utils.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Moses on 2015.2.1
 */
public class NewsMainPagerFragment extends Fragment {
    ViewPager mViewPager;
    PagerSlidingTabStrip mTabStrip;
    ImageButton mAddCatoBtn;
    public MyPagerFragmentAdapter mFragmentAdapter;
    FragmentManager mFragmentManager;
    ArrayList<String> mAddedTitles;
    ArrayList<String> mAddedYetTitles;

    public static NewsMainPagerFragment newInstance() {
        return new NewsMainPagerFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mAddedTitles", mAddedTitles);
        outState.putStringArrayList("mAddedYetTitles", mAddedYetTitles);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", ">>>>>onCreateView>>>>>>>>mAddedTitles>>>>>>>" + mAddedTitles);
        View view = inflater.inflate(R.layout.fragment_news_main, container, false);
        initView(view);
        initAddCatoBtn(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddedTitles = new ArrayList<>();
        mAddedYetTitles = new ArrayList<>();

        initTitles(mAddedTitles, mAddedYetTitles);
        Log.e("TAG", ">>>>>onCreate>>>>>>>>mAddedTitles>>>>>>>" + mAddedTitles);

        mFragmentManager = getFragmentManager();
        mFragmentAdapter = new MyPagerFragmentAdapter(mFragmentManager);

    }

    public void initTitles(ArrayList<String> yTitles, ArrayList<String> nTitles) {
        Collections.addAll(yTitles, "头条", "娱乐", "体育", "科技", "财经", "图灵机器");
        Collections.addAll(nTitles, "栏目1", "栏目2", "栏目3", "栏目4",
                "栏目5", "栏目6", "栏目7", "栏目8", "栏目9", "栏目10", "栏目11", "栏目12", "栏目13", "栏目14",
                "栏目15", "栏目16", "栏目17", "栏目18", "栏目19", "栏目20", "栏目21", "栏目22", "栏目23",
                "栏目24", "栏目25", "栏目26");
    }

    public void initView(View v) {

        mViewPager = (ViewPager) v.findViewById(R.id.news_main_view_pager);
        mTabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.news_main_tab_strip);
        //mTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
        mTabStrip.setDividerColor(getResources().getColor(android.R.color.darker_gray));
        mTabStrip.setIndicatorColor(getResources().getColor(android.R.color.holo_red_dark));
        mTabStrip.setIndicatorHeight(8);

        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mFragmentAdapter.setData(mAddedTitles);    ///////////成对出现
        mTabStrip.setViewPager(mViewPager);         //////////////////

    }

    public void initAddCatoBtn(View view) {
        mAddCatoBtn = (ImageButton) view.findViewById(R.id.news_add_cato_btn);
        mAddCatoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsCategoryActivity.class);
                intent.putStringArrayListExtra("titles", mAddedTitles);
                intent.putStringArrayListExtra("pTitles", mAddedYetTitles);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            mAddedTitles.clear();
            mAddedYetTitles.clear();

            mAddedTitles = data.getStringArrayListExtra("titles");
            mAddedYetTitles = data.getStringArrayListExtra("pTitles");
            Log.e("TAG", ">>>>>mAddedTitles>>>>result>>>>>>>>>>>" + mAddedTitles);

            mFragmentAdapter.setData(mAddedTitles);  //////////////   成对出现
            mTabStrip.setViewPager(mViewPager);     //////////////////
        }
    }

    class MyPagerFragmentAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> mTitles = new ArrayList<>();

        public MyPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 动态设置\刷新数据源
         *
         * @param mTitles : title 数据源
         */
        public void setData(ArrayList<String> mTitles) {
            this.mTitles = mTitles;
            notifyDataSetChanged();

        }

        @Override
        public Fragment getItem(int position) {
            if (mTitles.get(position).equals("头条")) {
                return NewsContentFragment.newInstance("T1348647909107");
            }
            if (mTitles.get(position).equals("娱乐")) {
                return NewsContentFragment.newInstance("T1348648517839");
            }
            if (mTitles.get(position).equals("体育")) {
                return NewsContentFragment.newInstance("T1348649079062");
            }
            if (mTitles.get(position).equals("财经")) {
                return NewsContentFragment.newInstance("T1348648756099");
            }
            if (mTitles.get(position).equals("科技")) {
                return NewsContentFragment.newInstance("T1348649580692");
            }
            if (mTitles.get(position).equals("图灵机器")) {
                return TuLingChatFragment.newInstance();
            }

            return EmptyFragment.newInstance(mTitles.get(position));
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);

        }

    }

}
