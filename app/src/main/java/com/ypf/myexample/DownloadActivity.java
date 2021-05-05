package com.ypf.myexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.AsynchronousChannelGroup;

/**
 * 下载文件
 * 1. 网络上请求数据：申请网络权限，读写存储权限
 * 2. 布局我们的Layout
 * 3. 下载前 UI
 * 4. 下载中 数据，更新进度
 * 5. 下载后 UI
 */
public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int INIT_PROGRESS = 0;
    public static final String APK_URL = "https://download.jetbrains.com.cn/idea/ideaIU-2021.1.exe";
    private ProgressBar progressBar;
    private Button button;
    private TextView textView;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        //初始化视图，点击监听
        initView();

        //初始化UI数据
        setData();
    }

    private void initView() {
        progressBar = findViewById(R.id.download_progress_bar);
        button = findViewById(R.id.download_button);
        textView = findViewById(R.id.download_text_view);
        button.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.download_button) {
            // TODO: 2021/5/1 下载任务
            DownloadAsyncTask task = new DownloadAsyncTask();
            task.execute(APK_URL);
        }
    }

    private void setData() {
        progressBar.setProgress(INIT_PROGRESS);
        button.setText(R.string.click_download);
        textView.setText(R.string.download_text);
    }

    public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override//异步任务之前，在主线程中
        protected void onPreExecute() {
            //可以操作UI
            button.setText(R.string.downloading);
            textView.setText(R.string.downloading);
            progressBar.setProgress(INIT_PROGRESS);
        }

        @Override//在另外一个事件中处理事件
        protected Boolean doInBackground(String... strings) {
            if (strings == null || strings.length < 1) {
                return false;
            }
            try {
                //构造URL
                URL url = new URL(strings[0]);
                //构造connection
                URLConnection connection = url.openConnection();
                //获取输入流
                InputStream inputStream = connection.getInputStream();
                //获取下载内容的总长度
                int contentLength = connection.getContentLength();
                //已下载的大小
                int downloadSize = 0;
                //准备下载地址
                String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
                filePath = Environment.getExternalStorageDirectory() + File.separator + fileName;
                File file = new File(filePath);
                if (file.exists()) {
                    boolean b = file.delete();
                    if (!b) {//表示删除失败
                        return false;
                    }
                }
                byte[] bytes = new byte[1024];
                int len;
                //创建一个输出管道
                OutputStream outputStream = new FileOutputStream(file);
                while ((len = inputStream.read(bytes)) != -1) {
                    //将下载的文件放到文件管道里
                    outputStream.write(bytes, 0, len);
                    //累加大小
                    downloadSize += len;
                    //发送进度
                    publishProgress(downloadSize * 100 / contentLength);
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
            if (values != null && values.length > 0) {
                progressBar.setProgress(values[0]);
            }
        }

        @Override//异步任务之后，在主线程中 处理结果
        protected void onPostExecute(Boolean result) {
            //处理结果
            button.setText(result ? getString(R.string.download_finish) : getString(R.string.download_failed));
            textView.setText(result ? getString(R.string.download_finish) + filePath : getString(R.string.download_failed));
        }
    }
}