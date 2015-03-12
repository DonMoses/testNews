package com.example.moses.mosesnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * Created by Moses on 2015.2.7
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimIn();

    }

    public void splashAnimIn() {
        View view = findViewById(R.id.splash_layout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashAnimOut();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void splashAnimOut() {
        View view = findViewById(R.id.splash_layout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startSplashActivity();
                SplashActivity.this.onStop();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void startSplashActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
