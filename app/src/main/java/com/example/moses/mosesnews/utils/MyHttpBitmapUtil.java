package com.example.moses.mosesnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 丹 on 2015/1/14.
 */
public class MyHttpBitmapUtil {

    private LruCache<String, Bitmap> lruCache;
    private Executor executor = new ThreadPoolExecutor(10, 100, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    private HttpBitmapCallBackListener callBackListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            callBackListener.onFinish(bitmap);
        }
    };

    public MyHttpBitmapUtil() {
        Long memorySize = Runtime.getRuntime().maxMemory();
        int maxCacheSize = (int) (memorySize / 8);
        lruCache = new LruCache<String, Bitmap>(maxCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
    }

    public Bitmap getBitmap(String bitmapURL, HttpBitmapCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
        Bitmap mBitmap = lruCache.get(bitmapURL);
        if (mBitmap != null) {
//            Log.e("TAG", "mBitmap = lruCache.get(imgURL)>>>>>>>>>>>>>>" + mBitmap);
            return mBitmap;
        } else {
//            Log.e("TAG", "mBitmap>>>threadBitmap(imgURL)>>>>>>>>>>>" + bitmapURL);
            threadBitmap(bitmapURL);
        }
        //这里通过在handler中使用接口回调返回bitmap
        return null;
    }

    private void threadBitmap(final String bitmapURL) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                try {
                    URL rul = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) rul.openConnection();
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                Message msg = Message.obtain();
                msg.obj = bitmap;
                lruCache.put(bitmapURL, bitmap);
                mHandler.sendMessage(msg);
            }
        }.executeOnExecutor(executor, bitmapURL);
//        new Thread(){
//            @Override
//            public void run() {
//                Bitmap bitmap = null;
//                try {
//                    URL rul = new URL(bitmapURL);
//                    HttpURLConnection connection = (HttpURLConnection)rul.openConnection();
//                    InputStream is = connection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = Message.obtain();
//                msg.obj = bitmap;
//                lruCache.put(bitmapURL,bitmap);
//                mHandler.sendMessage(msg);
//            }
//        }.start();
    }

    public interface HttpBitmapCallBackListener {
        void onFinish(Bitmap bitmap);
    }

}
