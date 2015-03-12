package com.example.moses.mosesnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moses.mosesnews.R;
import com.example.moses.mosesnews.josnbeams.news.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Moses on 2015.1.26
 */
public class NewsContentAdapter extends BaseAdapter {
    ArrayList<News> mList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    final int TYPE_SIMPLE = 0;
    final int TYPE_IMG_EXTRA = 1;
    final int TYPE_EDITOR = 2;

    public NewsContentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;

    }

    public void setList(ArrayList<News> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getEditor() != null && mList.get(position).getEditor().size() > 0) {
            return TYPE_EDITOR;
        } else {
            if (mList.get(position).getImgextra() != null && mList.get(position).getImgextra().size() > 0) {
                return TYPE_IMG_EXTRA;
            } else return TYPE_SIMPLE;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder simpleViewHolder = null;
        ImgExtraViewHolder imgExtraViewHolder = null;
        EditorViewHolder editorViewHolder = null;
        int tType = getItemViewType(position);
        if (convertView == null) {
            switch (tType) {
                case TYPE_SIMPLE:
                    convertView = inflater.inflate(R.layout.layout_simple_news_item, parent, false);
                    simpleViewHolder = new SimpleViewHolder();
                    simpleViewHolder.simpleTitle = (TextView) convertView.findViewById(R.id.simple_title);
                    simpleViewHolder.simpleDigest = (TextView) convertView.findViewById(R.id.simple_digest);
                    simpleViewHolder.simpleReplyCount = (TextView) convertView.findViewById(R.id.simple_reply);
                    simpleViewHolder.simpleImgView = (ImageView) convertView.findViewById(R.id.simple_image_view);
                    convertView.setTag(simpleViewHolder);
                    break;
                case TYPE_IMG_EXTRA:
                    convertView = inflater.inflate(R.layout.layout_imgextra_news_item, parent, false);
                    imgExtraViewHolder = new ImgExtraViewHolder();
                    imgExtraViewHolder.imgExtraTitle = (TextView) convertView.findViewById(R.id.img_extra_title);
                    imgExtraViewHolder.imgExtraReply = (TextView) convertView.findViewById(R.id.img_extra_reply);
                    imgExtraViewHolder.imgExtraImg1 = (ImageView) convertView.findViewById(R.id.img_extra_one);
                    imgExtraViewHolder.imgExtraImg2 = (ImageView) convertView.findViewById(R.id.img_extra_two);
                    imgExtraViewHolder.imgExtraImg3 = (ImageView) convertView.findViewById(R.id.img_extra_three);
                    convertView.setTag(imgExtraViewHolder);
                    break;
                case TYPE_EDITOR:
                    convertView = inflater.inflate(R.layout.layout_editor_news_item, parent, false);
                    editorViewHolder = new EditorViewHolder();
                    editorViewHolder.editorBg = (ImageView) convertView.findViewById(R.id.editor_bg_img);
                    editorViewHolder.editorIcon = (ImageView) convertView.findViewById(R.id.editor_icon_img);
                    editorViewHolder.editorNewsTitle = (TextView) convertView.findViewById(R.id.editor_news_title);
                    editorViewHolder.editorNewsDigest = (TextView) convertView.findViewById(R.id.editor_news_digest);
                    editorViewHolder.editorNewsEditorName = (TextView) convertView.findViewById(R.id.editor_name);
                    editorViewHolder.editorNewsReply = (TextView) convertView.findViewById(R.id.editor_news_reply);
                    editorViewHolder.editorNewsTag = (TextView) convertView.findViewById(R.id.editor_news_tag);
                    convertView.setTag(editorViewHolder);
            }
        } else {
            switch (tType) {
                case TYPE_SIMPLE:
                    simpleViewHolder = (SimpleViewHolder) convertView.getTag();
                    break;
                case TYPE_IMG_EXTRA:
                    imgExtraViewHolder = (ImgExtraViewHolder) convertView.getTag();
                    break;
                case TYPE_EDITOR:
                    editorViewHolder = (EditorViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (tType) {

            case TYPE_SIMPLE:
                simpleViewHolder.simpleTitle.setText(mList.get(position).getTitle());
                simpleViewHolder.simpleDigest.setText(mList.get(position).getDigest());
                simpleViewHolder.simpleReplyCount.setText(String.valueOf(mList.get(position).getReplyCount() + "跟帖"));
                Picasso.with(context).load(mList.get(position).getImgsrc()).
                        into(simpleViewHolder.simpleImgView);
                break;
            case TYPE_IMG_EXTRA:
                imgExtraViewHolder.imgExtraTitle.setText(mList.get(position).getTitle());
                imgExtraViewHolder.imgExtraReply.setText(String.valueOf(mList.get(position).getReplyCount() + "跟帖"));
                Picasso.with(context).load(mList.get(position).getImgsrc()).
                        into(imgExtraViewHolder.imgExtraImg1);
                Picasso.with(context).load(mList.get(position).getImgextra().get(0).getImgsrc()).
                        into(imgExtraViewHolder.imgExtraImg2);
                Picasso.with(context).load(mList.get(position).getImgextra().get(1).getImgsrc()).
                        into(imgExtraViewHolder.imgExtraImg3);
                break;
            case TYPE_EDITOR:
                editorViewHolder.editorNewsTitle.setText(mList.get(position).getTitle());
                editorViewHolder.editorNewsDigest.setText(mList.get(position).getDigest());
                editorViewHolder.editorNewsEditorName.setText(mList.get(position).getEditor().get(0).getEditorName());
                if (mList.get(position).getTAG() != null && !mList.get(position).getTAG().equals("")) {
                    editorViewHolder.editorNewsReply.setText(String.valueOf(mList.get(position).getReplyCount() + "跟帖"));
                } else {
                    editorViewHolder.editorNewsReply.setVisibility(View.GONE);
                }
                editorViewHolder.editorNewsTag.setText(mList.get(position).getTAG());
                Picasso.with(context).load(mList.get(position).getImgsrc()).
                        into(editorViewHolder.editorBg);
                Picasso.with(context).load(mList.get(position).getEditor().get(0).getEditorImg()).
                        into(editorViewHolder.editorIcon);
        }

        return convertView;
    }

    class SimpleViewHolder {
        ImageView simpleImgView;
        TextView simpleTitle;
        TextView simpleDigest;
        TextView simpleReplyCount;

    }

    class ImgExtraViewHolder {
        TextView imgExtraTitle;
        TextView imgExtraReply;
        ImageView imgExtraImg1;
        ImageView imgExtraImg2;
        ImageView imgExtraImg3;

    }

    class EditorViewHolder {
        TextView editorNewsTitle;
        TextView editorNewsDigest;
        TextView editorNewsEditorName;
        TextView editorNewsReply;
        TextView editorNewsTag;
        ImageView editorBg;
        ImageView editorIcon;

    }

}
