package com.ypf.myexample;

import android.os.AsyncTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadHelper {
    public static void download(String url, String filePath, OnDownloadListener listener) {
        DownloadAsyncTask task = new DownloadAsyncTask(url, filePath, listener);
        task.execute();
    }

    public interface OnDownloadListener {
        void onStart();
        void onSuccess(int code, File file);
        void onFailed(int code, File file, String message);
        void onProgress(int progress);
    }

    public static class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        String sUrl;
        String filePath;
        OnDownloadListener listener;

        public DownloadAsyncTask(String url, String filePath, OnDownloadListener listener) {
            this.sUrl = url;
            this.filePath = filePath;
            this.listener = listener;
        }

        @Override//异步任务之前，在主线程中
        protected void onPreExecute() {
            //可以操作UI
            if (listener != null) {
                listener.onStart();
            }
        }

        @Override//在另外一个事件中处理事件
        protected Boolean doInBackground(String... strings) {
            if (strings == null || strings.length < 1) {
                return false;
            }
            try {
                //构造URL
                URL url = new URL(sUrl);
                //构造connection
                URLConnection connection = url.openConnection();
                //获取输入流
                InputStream inputStream = connection.getInputStream();
                //获取下载内容的总长度
                int contentLength = connection.getContentLength();
                //已下载的大小
                int downloadSize = 0;
                //准备下载地址
                File file = new File(filePath);
                if (file.exists()) {
                    boolean b = file.delete();
                    if (!b) {//表示删除失败
                        if (listener != null) {
                            listener.onFailed(-1, file, "文件刪除失敗");
                        }
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
                if (listener != null) {
                    listener.onFailed(-2, new File(filePath), e.getMessage());
                }
                return false;
            }
            if (listener != null) {
                listener.onSuccess(0, new File(filePath));
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values != null && values.length > 0) {
                if (listener != null) {
                    listener.onProgress(values[0]);
                }
            }
        }

        @Override//异步任务之后，在主线程中 处理结果
        protected void onPostExecute(Boolean result) {
            //处理结果
            if (listener != null) {
                if (result) {
                    listener.onSuccess(0, new File(filePath));
                } else {
                    listener.onFailed(-1, new File(filePath), "下载失败");
                }
            }
        }
    }
}
