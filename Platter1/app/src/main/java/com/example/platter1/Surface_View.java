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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

}
