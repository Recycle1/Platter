package com.example.platter1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScannerAnsyTask extends AsyncTask<Void, Integer, List<MediaBean>>{
        private List<MediaBean> mMediaInfoList = new ArrayList<MediaBean>(); // 媒体列表类

        private Activity mActivity; // 依附于某个Activity，因为AsyncTask要在UI线程中执行
        private ProgressBar mProgressBar;
        private TextView mTextView;
        private Handler progressHandler ;

        public ScannerAnsyTask() {
            super();
        }

        public ScannerAnsyTask(Activity activity, ProgressBar progressBar,TextView textView) {
            super();
            this.mActivity = activity;
            this.mProgressBar = progressBar;
            this.mTextView=textView;
        }

        @Override
        protected List<MediaBean> doInBackground(Void... params) {
            int vis = 0;
            char Vision[] = null;
            Vision = Build.VERSION.RELEASE.toCharArray();
            for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
                vis = vis * j + ((int) Vision[i] - 48);
            }
            if(vis<9){
                File file=new File("sdcard/");
                mMediaInfoList = getVideoFile(mMediaInfoList,file);
            }
            else{
                mMediaInfoList = getVideoFile(mMediaInfoList,Environment.getExternalStorageDirectory());
            }
//		mMediaInfoList = filterVideo(mMediaInfoList); // 这里可以选择不过滤小文件
            //Log.e("CJT", "最后的大小" + "ScannerAnsyTask---第一条数据--");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return mMediaInfoList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<MediaBean> videoInfos) {
            super.onPostExecute(videoInfos);
            //Log.e("CJT", "最后的大小" + "ScannerAnsyTask---View.GONE--");
            mProgressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
        }

        /**
         * 获取视频文件
         *
         * @param list
         * @param file
         * @return
         */
        private List<MediaBean> getVideoFile(final List<MediaBean> list, File file) {

            file.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    String name = file.getName();

                    int i = name.indexOf('.');
                    if (i != -1) {
                        name = name.substring(i);
                        if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp") || name.equalsIgnoreCase(".wmv")
                                || name.equalsIgnoreCase(".ts") || name.equalsIgnoreCase(".rmvb")
                                || name.equalsIgnoreCase(".mov") || name.equalsIgnoreCase(".m4v")
                                || name.equalsIgnoreCase(".avi") || name.equalsIgnoreCase(".m3u8")
                                || name.equalsIgnoreCase(".3gpp") || name.equalsIgnoreCase(".3gpp2")
                                || name.equalsIgnoreCase(".mkv") || name.equalsIgnoreCase(".flv")
                                || name.equalsIgnoreCase(".divx") || name.equalsIgnoreCase(".f4v")
                                || name.equalsIgnoreCase(".rm") || name.equalsIgnoreCase(".asf")
                                || name.equalsIgnoreCase(".ram") || name.equalsIgnoreCase(".mpg")
                                || name.equalsIgnoreCase(".v8") || name.equalsIgnoreCase(".swf")
                                || name.equalsIgnoreCase(".m2v") || name.equalsIgnoreCase(".asx")
                                || name.equalsIgnoreCase(".ra") || name.equalsIgnoreCase(".ndivx")
                                || name.equalsIgnoreCase(".xvid")) {
                            MediaBean video = new MediaBean();
                            file.getUsableSpace();
                            video.setMediaName(file.getName());
                            try {
                                video.setPath(file.getCanonicalPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Log.e("CJT", "最后的大小" + "ScannerAnsyTask---视频名称--name--" + video.getPath());
                            list.add(video);
                            return true;
                        }
                        // 判断是不是目录
                    } else if (file.isDirectory()) {
                        getVideoFile(list, file);
                    }
                    return false;
                }
            });

            return list;
        }

        /**
         * 10M=10485760 b,小于10m的过滤掉 过滤视频文件
         *
         * @param videoInfos
         * @return
         */
        private List<MediaBean> filterVideo(List<MediaBean> videoInfos) {
            List<MediaBean> newVideos = new ArrayList<MediaBean>();
            for (MediaBean videoInfo : videoInfos) {
                File f = new File(videoInfo.getPath());
                if (f.exists() && f.isFile() && f.length() > 10485760) {
                    newVideos.add(videoInfo);
                    //Log.e("CJT", "ScannerAnsyTask---视频文件大小" + f.length());
                } else {
                    //Log.e("CJT", "ScannerAnsyTask---视频文件太小或者不存在");
                }
            }
            return newVideos;
        }
}
