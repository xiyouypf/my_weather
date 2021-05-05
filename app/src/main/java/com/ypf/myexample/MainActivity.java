package com.ypf.myexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * 打地鼠
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private TextView textView;
    private ImageView imageView;
    private Button button;
    public int[][] mPosition = new int[][]{
            {0, 0}, {0, 200},
            {100, 0}, {100, 200},
            {200, 0}, {200, 200},
            {300, 0}, {300, 200}};
    private int mTotalCount;
    private int mSuccessCount;
    public static final int MAX_COUNT = 10;
    public static final int CODE = 123;
    public static final int RANDOM_NUMBER = 500;
    private GophersHandler gophersHandler = new GophersHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setTitle("打地鼠");
    }

    private void initView() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);
        imageView.setOnTouchListener(this::onTouch);
    }

    @Override
    public void onClick(View v) {
        start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.setVisibility(View.GONE);
        mSuccessCount++;
        textView.setText("打中了 " + mSuccessCount + " 只，共 " + MAX_COUNT + " 只");
        return false;
    }

    private void start() {
        //发送消息
        textView.setText("开始了");
        button.setText("游戏中...");
        button.setEnabled(false);//不能再点击
        next(0);
    }

    private void next(int delayTime) {
        int position = new Random().nextInt(mPosition.length);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 = position;
        gophersHandler.sendMessageDelayed(message, delayTime);
        mTotalCount++;
    }


    public static class GophersHandler extends Handler {
        public WeakReference<MainActivity> mWeakReference;

        public GophersHandler(MainActivity activity) {
            this.mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mWeakReference.get();
            if (msg.what == CODE) {
                if (activity.mTotalCount > MAX_COUNT) {
                    activity.clear();
                    Toast.makeText(activity, "地鼠打完了", Toast.LENGTH_SHORT).show();
                    return;
                }
                int position = msg.arg1;
                activity.imageView.setX(activity.mPosition[position][0]);
                activity.imageView.setY(activity.mPosition[position][1]);
                activity.imageView.setVisibility(View.VISIBLE);
                int randomTime = new Random().nextInt(RANDOM_NUMBER) + RANDOM_NUMBER;
                activity.next(randomTime);
            }
        }
    }

    //清理数据
    private void clear() {
        mTotalCount = 0;
        mSuccessCount = 0;
        imageView.setVisibility(View.GONE);
        button.setText("点击开始");
        button.setEnabled(true);
    }
}