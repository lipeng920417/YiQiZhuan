package com.yiqizhuan.app.views;


import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * @author LiPeng
 * @create 2024-06-24 1:06 PM
 */
public class FullWidthImageView extends AppCompatImageView {

    public FullWidthImageView(Context context) {
        super(context);
    }

    public FullWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullWidthImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (getDrawable() != null) {
            int drawableWidth = getDrawable().getIntrinsicWidth();
            int drawableHeight = getDrawable().getIntrinsicHeight();
            if (drawableWidth > 0 && drawableHeight > 0) {
                float aspectRatio = (float) drawableHeight / (float) drawableWidth;
                height = (int) (width * aspectRatio);
            }
        }
        setMeasuredDimension(width, height);
    }
}
