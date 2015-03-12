package com.example.moses.mosesnews.contents.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.moses.mosesnews.R;

import java.util.ArrayList;

/**
 * Created by Moses on 2015.2.4
 */
public class NewsCategoryActivity extends Activity {
    ArrayList<String> mMAddedTitles = new ArrayList<>();
    ArrayList<String> mMAddedYetPTitles = new ArrayList<>();
    GridView addCatoGroup, addedYetCatoGroup;
    ArrayAdapter<String> addedAdapter, addedYetAdapter;

    public void initCatoGroup() {
        addCatoGroup = (GridView) findViewById(R.id.grid_view_add);
        addedYetCatoGroup = (GridView) findViewById(R.id.grid_view_add_yet);

        addedAdapter = new ArrayAdapter<>(this, R.layout.view_cato_item, mMAddedTitles);
        addedYetAdapter = new ArrayAdapter<>(this, R.layout.view_cato_item, mMAddedYetPTitles);

        addCatoGroup.setAdapter(addedAdapter);
        addedYetCatoGroup.setAdapter(addedYetAdapter);

        addCatoGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentItem = mMAddedTitles.get(position);
                mMAddedTitles.remove(position);
                addedAdapter.notifyDataSetChanged();

                mMAddedYetPTitles.add(currentItem);
                addedYetAdapter.notifyDataSetChanged();

            }
        });

        addedYetCatoGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentItem = mMAddedYetPTitles.get(position);
                mMAddedYetPTitles.remove(position);
                addedYetAdapter.notifyDataSetChanged();

                mMAddedTitles.add(currentItem);
                addedAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_category);

        Intent intent = getIntent();
        mMAddedTitles = intent.getStringArrayListExtra("titles");
        mMAddedYetPTitles = intent.getStringArrayListExtra("pTitles");

        initCatoGroup();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("titles", mMAddedTitles);
        intent.putStringArrayListExtra("pTitles", mMAddedYetPTitles);
        this.setResult(0, intent);
        Log.e("TAG", ">>>>>mMAddedTitles>>>>response>>>>>" + mMAddedTitles);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cato_fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        View view = findViewById(R.id.cato_layout);
        view.startAnimation(animation);
    }
}
