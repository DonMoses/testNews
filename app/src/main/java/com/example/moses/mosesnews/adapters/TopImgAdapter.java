package com.example.moses.mosesnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.contents.news.NewsDetailActivity;
import com.example.moses.mosesnews.josnbeams.news.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopImgAdapter extends PagerAdapter {
    View view;
    ArrayList<News> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    FragmentManager fm;

    public TopImgAdapter(Context context, FragmentManager fm) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fm = fm;
    }

    public void setList(ArrayList<News> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        view = inflater.inflate(R.layout.layout_top_img, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.top_img_view);
        TextView topTitleTxt = (TextView) view.findViewById(R.id.top_title_txt);
        TextView topReplyTxt = (TextView) view.findViewById(R.id.top_reply_txt);

        topTitleTxt.setText(mList.get(position).getTitle());
        topReplyTxt.setText(String.valueOf(mList.get(position).getReplyCount()) + "跟帖");
        Picasso.with(context).load(mList.get(position).getImgsrc()).into(imageView);

        View view1 = view.findViewById(R.id.top_info);
        view1.getBackground().setAlpha(200);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                // if the item in the headerViewpager has a url_3w or a url address, then replace the current layout with a NewsDetailFragment instance
                //  as to load the detail info for url_3w or a url( As this always be done by a WebView).
                if (mList.get(position).getUrl_3w() != null && !mList.get(position).getUrl_3w().equals("")) {
                    intent.putExtra("url", mList.get(position).getUrl_3w());
                    context.startActivity(intent);
                } else if (mList.get(position).getUrl() != null && mList.get(position).getUrl().equals("")) {
                    intent.putExtra("url", mList.get(position).getUrl());
                    context.startActivity(intent);
                }

            }
        });

        container.addView(view);
//        Log.e("TAG", ">>obj>>>instantiateItem>>>>>>>>>>>>>>");

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(view);

    }
}