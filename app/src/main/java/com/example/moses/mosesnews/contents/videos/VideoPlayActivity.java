package com.example.moses.mosesnews.contents.videos;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.moses.mosesnews.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Moses on 2015.2.6
 */
public class VideoPlayActivity extends Activity implements View.OnClickListener {
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private int position;
    private SeekBar mSeekBar;
    private String videoUrl;
    boolean setTrue = true;

    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<VideoPlayActivity> mVideoPlayReference;

        public MyHandler(VideoPlayActivity videoPlayActivity) {
            this.mVideoPlayReference = new WeakReference<>(videoPlayActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoPlayActivity playActivity = mVideoPlayReference.get();
            if (playActivity != null) {
                switch (msg.what) {
                    case 0:
                        int currentPosition = msg.arg1;
                        playActivity.updateSeekBarSeek(currentPosition);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateSeekBarSeek(int currentPosition) {
        mSeekBar.setProgress(currentPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoUrl = getIntent().getStringExtra("mp4_url");
        initView();
    }

    public void initView() {
        mMediaPlayer = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(R.id.video_surface);
        mSeekBar = (SeekBar) findViewById(R.id.surface_seek_bar);
        Button mStartBtn = (Button) findViewById(R.id.start_btn);
        Button mPreBtn = (Button) findViewById(R.id.pre_btn);
        Button mStopBtn = (Button) findViewById(R.id.stop_btn);

        mSurfaceView.getHolder().setKeepScreenOn(true);
        mSurfaceView.getHolder().addCallback(new MySurfaceCallBackListener());

        mStartBtn.setOnClickListener(this);
        mPreBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                try {
                    startPlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pre_btn:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
                break;
            case R.id.stop_btn:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                break;
        }
    }

    public void startPlay() throws IOException {
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(videoUrl);
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        int videoDuration = mMediaPlayer.getDuration();
        mSeekBar.setMax(videoDuration);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                mMediaPlayer.start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (setTrue) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            int currentPosition = mMediaPlayer.getCurrentPosition();
                            Message msg = Message.obtain();
                            msg.what = 0;
                            msg.arg1 = currentPosition;
                            mHandler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        Log.e("TAG", ">>>>>>Exception>>>e>");
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mMediaPlayer.isPlaying()) {
            position = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.stop();
        }
        super.onPause();
    }

    class MySurfaceCallBackListener implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (position == 0) {
                try {
                    startPlay();
                    mMediaPlayer.seekTo(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

}
