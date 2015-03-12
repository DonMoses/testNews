package com.example.moses.mosesnews.contents.imgs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.josnbeams.imgs.ImgData;
import com.example.moses.mosesnews.josnbeams.imgs.ImgJson;
import com.example.moses.mosesnews.josnbeams.imgs.TheImg;
import com.example.moses.mosesnews.utils.MosesHTTPUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moses on 2015.2.5
 */
public class ImgContentFragment extends Fragment {
    PullToRefreshListView mPullToRefreshListView;
    ImgContentAdapter mImgContentAdapter;
    ArrayList<ImgData> mImgs = new ArrayList<>();
    ArrayList<TheImg> mTheImags = new ArrayList<>();
    String imgType;
    MosesHTTPUtil mosesHTTPUtil;
    String channel;
    String adid;
    String wm;
    String from;
    String chwm;
    String oldchwm;
    String imei;
    String uid;
    int p = 0;

    private static class MyHandler extends Handler {
        private final WeakReference<ImgContentFragment> mImgContentFragment;

        public MyHandler(ImgContentFragment imgContentFragment) {
            mImgContentFragment = new WeakReference<>(imgContentFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            ImgContentFragment imgContentFragment = mImgContentFragment.get();
            if (imgContentFragment != null) {
                switch (msg.what) {
                    case 0:
                        String imgJsonStr = msg.obj.toString();
                        ImgJson imgJson = JSONObject.parseObject(imgJsonStr, ImgJson.class);
                        ArrayList<ImgData> imgList = imgJson.getData().getList();
                        for (ImgData imgData : imgList) {
                            imgContentFragment.updateHandlerImg(imgData);
                        }
                    default:
                        break;
                }
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);

    public static ImgContentFragment newInstance(String img_type) {
        ImgContentFragment imgContentFragment = new ImgContentFragment();
        Bundle args = new Bundle();
        args.putString("img_type", img_type);
//        Log.e("TAG", ">>>>>>>>img_type>>>" + img_type);
        imgContentFragment.setArguments(args);
        return imgContentFragment;
    }

    public void updateHandlerImg(ImgData imgData) {
        mImgs.add(imgData);
        mImgContentAdapter.setData(mImgs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imgType = bundle.getString("img_type");
//        Log.e("TAG", ">>>>>>>>create>>> imgType>>>" + imgType);
        mImgContentAdapter = new ImgContentAdapter(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_content, container, false);
        initViewData(imgType);
        initView(view);
        return view;

    }

    public void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.img_content_pull_to_refresh_list_view);
        mPullToRefreshListView.setAdapter(mImgContentAdapter);
        mImgContentAdapter.setData(mImgs);

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTheImags = mImgs.get(position).getPics().getList();


            }
        });
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mImgs.clear();
                p = 0;
                getImgJson(imgType);
//                Log.e("TAG", ">>>>pageNo>>>>>>>>>>>>>>>" + p);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                getImgJson(imgType);
//                Log.e("TAG", ">>>>pageNo>>>>>>>>>>>>>>>" + p);
            }
        });
    }

    public void initViewData(String type) {
        getImgJson(type);
    }

    public HashMap<String, String> initHashMapParam(String type) {
        setImgContentType(type);
        HashMap<String, String> mParameters = new HashMap<>();

        mParameters.put("channel", channel);
        mParameters.put("adid", adid);
        mParameters.put("wm", wm);
        mParameters.put("from", from);
        mParameters.put("chwm", chwm);
        mParameters.put("oldchwm", oldchwm);
        mParameters.put("imei", imei);
        mParameters.put("uid", uid);
        mParameters.put("pageNo", String.valueOf(p));

        return mParameters;
    }

    public void setImgContentType(String type) {
        switch (type) {
            case "精选图":
                channel = "hdpic_toutiao";
                adid = "4ad30dabe134695c3b7c3a65977d7e72";
                wm = "b207";
                from = "6042095012";
                chwm = "12050_0001";
                oldchwm = "";
                imei = "867064013906290";
                uid = "802909da86d9f5fc";
                break;
            case "趣味图":
                channel = "hdpic_funny";
                adid = "4ad30dabe134695c3b7c3a65977d7e72";
                wm = "b207";
                from = "6042095012";
                chwm = "12050_0001";
                oldchwm = "12050_0001";
                imei = "867064013906290";
                uid = "802909da86d9f5fc";
                break;
            case "美图":
                channel = "hdpic_pretty";
                adid = "4ad30dabe134695c3b7c3a65977d7e72";
                wm = "b207";
                from = "6042095012";
                chwm = "12050_0001";
                oldchwm = "12050_0001";
                imei = "867064013906290";
                uid = "802909da86d9f5fc";
                break;
            case "故事图":
                channel = "hdpic_story";
                adid = "4ad30dabe134695c3b7c3a65977d7e72";
                wm = "b207";
                from = "6042095012";
                chwm = "12050_0001";
                oldchwm = "12050_0001";
                imei = "867064013906290";
                uid = "802909da86d9f5fc";
                break;
        }
    }

    public void getImgJson(String type) {

        String utlStr = "http://api.sina.cn/sinago/list.json";
        HashMap<String, String> mParameters = initHashMapParam(type);
        String reqMethod = "GET";
        String reqType = "NEWS_IMG_GET";
        mosesHTTPUtil = new MosesHTTPUtil(utlStr, mParameters, reqMethod, reqType);
        mosesHTTPUtil.sendRequest(new MosesHTTPUtil.HttpCallBackInterface() {
            @Override
            public void onFinish(String s) {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = s;
                mHandler.sendMessage(msg);
            }
        });

    }

    class ImgContentAdapter extends BaseAdapter {
        ArrayList<ImgData> mData = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        public ImgContentAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<ImgData> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_img_content_item, null);
                viewHolder = new ViewHolder();
                viewHolder.contentImgView = (ImageView) convertView.findViewById(R.id.img_content_view);
                viewHolder.imgInfoTxt = (TextView) convertView.findViewById(R.id.img_info_txt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imgInfoTxt.setText(mData.get(position).getTitle());
            Picasso.with(getActivity()).load(mData.get(position).getPic())
                    .into(viewHolder.contentImgView);

            return convertView;
        }

        class ViewHolder {
            ImageView contentImgView;
            TextView imgInfoTxt;

        }
    }
}
