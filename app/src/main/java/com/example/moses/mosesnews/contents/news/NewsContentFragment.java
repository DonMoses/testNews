package com.example.moses.mosesnews.contents.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.moses.mosesnews.MainActivity;
import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.adapters.NewsContentAdapter;
import com.example.moses.mosesnews.adapters.TopImgAdapter;
import com.example.moses.mosesnews.josnbeams.news.News;
import com.example.moses.mosesnews.utils.HTTPUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moses on 2015.1.26
 */
public class NewsContentFragment extends Fragment {
    PullToRefreshListView mRefreshListView;
    ViewPager headerViewPager;
    ArrayList<News> mNews = new ArrayList<>();
    ArrayList<News> mTempImgList = new ArrayList<>();
    NewsContentAdapter adapter;
    HTTPUtil mHttpUtil;
    //地址: "http://c.m.163.com/nc/article/headline/"+ news_type_id +pageNo*pageSize+ "-"  +pageSize+ ".html"
    String urlStr = "http://c.m.163.com/nc/article/headline/";  //
    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    int pageSize = 20; //页大小，显示每页多少条数据
    String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻
    String requestMode = HTTPUtil.METHOD_GET;
    HashMap<String, String> parameters = new HashMap<>();
    LayoutInflater inflater;
    TopImgAdapter headerPagerAdapter;
    boolean pagerLock = true;
    boolean alwaysOn = true;

    MainActivity activity;

    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<NewsContentFragment> mNewsContentReference;

        public MyHandler(NewsContentFragment newsContentFragment) {
            this.mNewsContentReference = new WeakReference<>(newsContentFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            NewsContentFragment newsContentFragment = mNewsContentReference.get();
            if (newsContentFragment != null) {
                switch (msg.what) {
                    case 1:
                        String str = msg.obj.toString();
//                    Log.e("TAG", "s>>>>>>>>>>>" + str);
                        newsContentFragment.updateNewsList(str);
                        break;
                    case 2:
                        int i = msg.arg1;
                        newsContentFragment.updateHeaderViewPager(i);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateNewsList(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String str1 = jsonObject.get(news_type_id).toString();
            JSONArray jsonArray = new JSONArray(str1);
//                        Log.e("TAG", "jsonArray>>>>>>>>>>>" + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                News news = JSON.parseObject(jsonArray.getString(i), News.class);   // archive News Object by json string.
                mNews.add(news);
                adapter.setList(mNews);

                // when refresh completed
                mRefreshListView.onRefreshComplete();

                //set source for TopImgAdapter
                if (news.getImgextra() != null) {
                    // if there's imgExtra with the News obj, add this obj to a new ArrayList<News>
                    // the create the data source for the headerViewPager.
                    if (mTempImgList.size() < 5) {
                        mTempImgList.add(news);
                        headerPagerAdapter.setList(mTempImgList);    // notifyDataChanged() by headerPagerAdapter;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateHeaderViewPager(int currentPage) {
        headerViewPager.setCurrentItem(currentPage);
    }

    /**
     * get instance
     */
    public static NewsContentFragment newInstance(String news_type_id_in) {
        NewsContentFragment newsContentFragment = new NewsContentFragment();
        Bundle args = new Bundle();
        args.putString("news_type_id", news_type_id_in);
        newsContentFragment.setArguments(args);

        return newsContentFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        news_type_id = bundle.getString("news_type_id");
        inflater = LayoutInflater.from(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_content, container, false);
        initView(view);
        return view;

    }

    /**
     * init Views and other stuff
     */
    public void initView(View v) {
        getJsonNew();
        mRefreshListView = (PullToRefreshListView) v.findViewById(R.id.content_pull_to_refresh_list_view);
        adapter = new NewsContentAdapter(getActivity());
        mRefreshListView.setAdapter(adapter);

        View view = inflater.inflate(R.layout.layout_header_view_pager, null);
        ListView refreshableListView = mRefreshListView.getRefreshableView();
        refreshableListView.addHeaderView(view);

        headerViewPager = (ViewPager) view.findViewById(R.id.header_pager);
        headerPagerAdapter = new TopImgAdapter(getActivity(), getFragmentManager());
        headerViewPager.setAdapter(headerPagerAdapter);
        headerViewPager.setOffscreenPageLimit(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // this is a sub thread always running for automatically play next img in headerViewPager.
                int i = headerViewPager.getCurrentItem();
                while (alwaysOn) {
                    if (i < headerPagerAdapter.getCount()) {    // if the index is not the end yet;
                        try {
                            Thread.sleep(5000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        Log.e("TAG", ">>>i>>>>>>>" + i);
                        Message msg = Message.obtain();
                        msg.what = 2;
                        msg.arg1 = i;
                        mHandler.sendMessage(msg);     // send the index as int i, headerViewPager will setCurrentItem by it;
                        i++;        //  after sent the index, turn the index to next.
                    } else {
                        i = 0;      // else if index is the end, turn the index to 0 to begin a new cycle.   ^_^
                    }
                }
            }
        }).start();

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alwaysOn) {
                        if (mTempImgList.size() > 0) {
                            headerViewPager.setVisibility(View.VISIBLE); //if there's data for headerViewpager, set it VISIBLE.
                        } else {
                            headerViewPager.setVisibility(View.GONE);   // if there's no data for headerViewpager, set it GONE.
                        }
                    }
                }
            }).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        headerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // if the user scrolled the headerViewPager, turn the lock boolean pagerLock to false,
                // as the sub thread that used to set the current item of the headerViewPager won't send a int
                // param automatically that is needed for it's working until the user's scrolling action done.
                pagerLock = false;

            }

            @Override
            public void onPageSelected(int position) {
                pagerLock = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                pagerLock = false;
            }
        });

        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                if (mNews.get(position).getUrl_3w() != null && !mNews.get(position).getUrl_3w().equals("")) {
                    // if the News obj has a url_3w or a url address, then replace the current layout with a NewsDetailFragment instance
                    //  as to load the detail info for url_3w or url( As this always be done by a WebView).
                    intent.putExtra("url", mNews.get(position - 2).getUrl_3w());
                    startActivity(intent);

                } else if (mNews.get(position).getUrl() != null && !mNews.get(position).getUrl().equals("")) {
                    intent.putExtra("url", mNews.get(position - 2).getUrl());
                    startActivity(intent);

                }
            }

        });

        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // reload the top page when scroll down at the top index.
                mNews.clear();
                pageNo = 0;
                getJsonNew();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // load next page when scroll up at the bottom index.
                pageNo++;
                getJsonNew();
            }
        });
    }

    /**
     * get hTTP JSON info with params.
     */
    public void getJsonNew() {
        parameters.clear();
        parameters.put("news_type_id", news_type_id);
        parameters.put("pageNo", String.valueOf(pageNo));
        parameters.put("pageSize", String.valueOf(pageSize));
        mHttpUtil = new HTTPUtil(urlStr, parameters, requestMode);
        mHttpUtil.sendRequest(new HTTPUtil.HttpCallBackInterface() {
            @Override
            public void onFinish(String s) {
                Message msg = Message.obtain();
                msg.obj = s;
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });
    }
}
