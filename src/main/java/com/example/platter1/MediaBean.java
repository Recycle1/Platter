package com.example.platter1;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class MediaBean{
        private String path ;
        private String mediaName ;
        private Bitmap thumbImg ;
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
        public String getMediaName() {
            return mediaName;
        }
        public void setMediaName(String mediaName) {
            this.mediaName = mediaName;
        }
        public Bitmap getThumbImg() {
            return path != null ? getVideoThumbNail(path) : null;
        }
        public void setThumbImg(Bitmap thumbImg) {
            this.thumbImg = thumbImg;
        }
        @Override
        public String toString() {
            return "MediaBean [path=" + path + ", mediaName=" + mediaName + ", thumbImg=" + thumbImg + "]";
        }
        public Bitmap getVideoThumbNail(String filePath) {
            Bitmap bitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(filePath);
                bitmap = retriever.getFrameAtTime();
            }
            catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    retriever.release();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
}
