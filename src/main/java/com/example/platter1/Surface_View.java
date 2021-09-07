package com.example.platter1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class Surface_View extends SurfaceView {

    public Surface_View(Context context) {
        super(context);
    }

    public Surface_View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Surface_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Surface_View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width,height;
        if(MainActivity.width==0){
            width = getDefaultSize(0, widthMeasureSpec);
            if(MainActivity.height==0)height = getDefaultSize(0, heightMeasureSpec);
            else height=MainActivity.height;
        }
        else if(MainActivity.height==0){
            height = getDefaultSize(0, heightMeasureSpec);
            width=MainActivity.width;
        }
        else{
            width=MainActivity.width;
            height=MainActivity.height;
        }
        super.onMeasure(width, height);
        setMeasuredDimension(width, height);
    }

}
