package com.example.moses.mosesnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

import com.example.moses.mosesnews.contents.news.NewsMainPagerFragment;
import com.warmtel.slidingmenu.lib.SlidingMenu;
import com.warmtel.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Moses on 2015.3.8
 */
public class MainActivity extends SlidingFragmentActivity {
    public NewsMainPagerFragment mMainPagerFragment;
    private SlidingMenu.CanvasTransformer mTransformer;
    private ImageButton mBackBtn;
    private Bitmap backBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSlidingMenu();

    }

    public void initSlidingMenu() {
        SlidingMenu slidingMenuLeft = getSlidingMenu();
        slidingMenuLeft.setMode(SlidingMenu.LEFT);
        setBehindContentView(R.layout.layout_left_menu);
        slidingMenuLeft.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenuLeft.setSlidingEnabled(true);

        SlidingMenuLeftFragment slidingMenuLeftFragment = new SlidingMenuLeftFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.left_menu_layout, slidingMenuLeftFragment).commit();
        slidingMenuLeft.setBehindOffsetRes(R.dimen.sliding_menu_left_above_offset);
        slidingMenuLeft.setBehindScrollScale(0);
        slidingMenuLeft.setFadeDegree(0.35f);

        initSlidUpCanvasTransformer();
        slidingMenuLeft.setBehindCanvasTransformer(mTransformer);

        slidingMenuLeft.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                Matrix matrix = new Matrix();
                matrix.setRotate(0);
                Bitmap bitmap = Bitmap.createBitmap(backBitmap, 0, 0,
                        backBitmap.getWidth(), backBitmap.getHeight(), matrix, true);
                mBackBtn.setImageBitmap(bitmap);
            }
        });
    }

    private static Interpolator interpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t + 1.0f;
        }
    };

    private void initSlidUpCanvasTransformer() {
        mTransformer = new SlidingMenu.CanvasTransformer() {

            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                canvas.translate(0, canvas.getHeight() * (1 - interpolator.getInterpolation(percentOpen)));
                Matrix matrix = new Matrix();
                matrix.setRotate(percentOpen * +180);
                Bitmap bitmap = Bitmap.createBitmap(backBitmap, 0, 0,
                        backBitmap.getWidth(), backBitmap.getHeight(), matrix, true);
                mBackBtn.setImageBitmap(bitmap);
            }
        };
    }

    public void initView() {
        mBackBtn = (ImageButton) findViewById(R.id.back_to_sliding_menu_btn);
        backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.btn_back_sliding_menu_pressed);
        mMainPagerFragment = NewsMainPagerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().
                add(R.id.main_container, mMainPagerFragment).commit();

    }

    public void onSlidingMenuToggle(View v) {
        switch (v.getId()) {
            case R.id.back_to_sliding_menu_btn:
                this.toggle();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
