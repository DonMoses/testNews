package com.example.moses.mosesnews.contents.news;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.moses.mosesnews.R;

/**
 * Created by Moses on 2015.1.27
 */
public class NewsDetailActivity extends Activity {
    WebView mWebView;
    String urlStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        urlStr = getIntent().getStringExtra("url");
        intiView();
    }

    public void intiView() {
        mWebView = (WebView) findViewById(R.id.news_detail_web_view);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(urlStr);

    }

}
