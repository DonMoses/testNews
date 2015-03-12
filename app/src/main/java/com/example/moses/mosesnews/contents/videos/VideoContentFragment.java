package com.example.moses.mosesnews.contents.videos;

import android.content.Context;
import android.content.Intent;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.josnbeams.videos.VideoBeam;
import com.example.moses.mosesnews.utils.MosesHTTPUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Moses on 2015
 */
public class VideoContentFragment extends Fragment {
    //"热点视频", "娱乐视频", "搞笑视频", "精品视频"
    String videoType;
    PullToRefreshListView mPullToRefreshListView;
    final int pageSize = 10;
    int pageNo = 0;
    String video_type_id;
    final String reqMethod = "GET";
    final String reqType = "NEWS_VIDEO_GET";
    final String baseUtl = "http://c.3g.163.com/nc/video/list/";
    String httpUrl;
    MosesHTTPUtil mosesHTTPUtil;
    ArrayList<VideoBeam> mVideoList = new ArrayList<>();
    VideoContentAdapter mVideoContentAdapter;
    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<VideoContentFragment> mVideoReference;

        public MyHandler(VideoContentFragment videoContentFragment) {
            this.mVideoReference = new WeakReference<>(videoContentFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoContentFragment videoContentFragment = mVideoReference.get();
            if (videoContentFragment != null) {
                switch (msg.what) {
                    case 0:
                        String jsonStr = msg.obj.toString();
                        videoContentFragment.updateVideoList(jsonStr);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateVideoList(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String videoListJsonStr = jsonObject.getString(video_type_id);
        JSONArray jsonArray = JSON.parseArray(videoListJsonStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            VideoBeam videoBeam = JSONObject.parseObject(object.toJSONString(), VideoBeam.class);
            mVideoList.add(videoBeam);
            mVideoContentAdapter.setData(mVideoList);
        }
    }

    public static VideoContentFragment newInstance(String vType) {
        VideoContentFragment videoContentFragment = new VideoContentFragment();
        Bundle args = new Bundle();
        args.putString("video_type", vType);
        videoContentFragment.setArguments(args);
        return videoContentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_content, container, false);
        initView(view);
        return view;

    }

    public void initView(View view) {

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.video_content_pull_to_refresh_list_view);
        mVideoContentAdapter = new VideoContentAdapter(getActivity());
        mPullToRefreshListView.setAdapter(mVideoContentAdapter);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mVideoList.clear();
                pageNo = 0;
                getVideoJson();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                getVideoJson();
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                if (position >= 1 && mVideoList.get(position - 1).getMp4Hd_url() != null) {
                    intent.putExtra("mp4_url", mVideoList.get(position - 1).getMp4Hd_url());
                    startActivity(intent);
                } else {
                    intent.putExtra("mp4_url", mVideoList.get(position - 1).getMp4_url());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoType = getArguments().getString("video_type");
        setContentType(videoType);
        getVideoJson();

    }

    //    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    //    int pageSize = 10; //页大小，显示每页多少条数据
    //    String video_type_id = "00850FRB";  //视频类型标识, 此处表示精品视频
    //    请地址: "http://c.3g.163.com/nc/video/list/"+ video_type_id + "/n/"
    //                                          +pageNo*pageSize+ "-"  +pageSize+ ".html"
    public void setContentType(String videoType) {
        switch (videoType) {
            case "热点视频":
                video_type_id = "V9LG4B3A0";
                break;
            case "娱乐视频":
                video_type_id = "V9LG4CHOR";
                break;
            case "搞笑视频":
                video_type_id = "V9LG4E6VR";
                break;
            case "精品视频":
                video_type_id = "00850FRB";
                break;
            default:
                break;
        }
    }

    public void getVideoJson() {
        httpUrl = baseUtl + video_type_id + "/n/" + pageNo * pageSize + "-" + pageSize + ".html";
        mosesHTTPUtil = new MosesHTTPUtil(httpUrl, reqMethod, reqType);
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

    class VideoContentAdapter extends BaseAdapter {
        ArrayList<VideoBeam> mData = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        public VideoContentAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<VideoBeam> mData) {
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
                convertView = inflater.inflate(R.layout.layout_video_content_item, null);
                viewHolder = new ViewHolder();
                viewHolder.contentCoverView = (ImageView) convertView.findViewById(R.id.video_cover_view);
                viewHolder.videoTitleTxt = (TextView) convertView.findViewById(R.id.video_title_txt);
                viewHolder.videoLengthTxt = (TextView) convertView.findViewById(R.id.video_length_txt);
                viewHolder.videoReplyTxt = (TextView) convertView.findViewById(R.id.video_reply_txt);
                viewHolder.videoIsHdTxt = (TextView) convertView.findViewById(R.id.is_hd_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.videoTitleTxt.setText(mData.get(position).getTitle());
            int length = mData.get(position).getLength();
            String lengthStr;
            if (length < 3600) {
                String min;
                String sec;
                if ((length / 60) < 10) {
                    min = "0" + String.valueOf(length / 60);
                } else {
                    min = String.valueOf(length / 60);
                }

                if ((length % 60) < 10) {
                    sec = "0" + String.valueOf(length % 60);
                } else {
                    sec = String.valueOf(length % 60);
                }

                lengthStr = min + ":" + sec;

            } else {
                String hou = String.valueOf(length / 3600);
                String min;
                String sec;
                if (((length % 3600) / 60) < 10) {
                    min = "0" + String.valueOf((length % 3600) / 60);
                } else {
                    min = String.valueOf((length % 3600) / 60);
                }

                if (((length % 3600) % 60) < 10) {
                    sec = "0" + String.valueOf((length % 3600) % 60);
                } else {
                    sec = String.valueOf((length % 3600) % 60);
                }

                lengthStr = hou + ":" + min + ":" + sec;
            }
            viewHolder.videoLengthTxt.setText(lengthStr);
            viewHolder.videoReplyTxt.setText(String.valueOf(mData.get(position).getReplyCount()) + "贴");
            if (mData.get(position).getMp4Hd_url() != null) {
                viewHolder.videoReplyTxt.setText("高清");
            }
            Picasso.with(getActivity()).load(mData.get(position).getCover())
                    .into(viewHolder.contentCoverView);

            return convertView;
        }

        class ViewHolder {
            ImageView contentCoverView;
            TextView videoTitleTxt;
            TextView videoLengthTxt;
            TextView videoReplyTxt;
            TextView videoIsHdTxt;

        }
    }

}
