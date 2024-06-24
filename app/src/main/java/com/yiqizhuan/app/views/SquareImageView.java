package com.yiqizhuan.app.views;


import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * @author LiPeng
 * @create 2024-06-24 1:23 PM
 */
public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width); // 设置宽高相等
    }
}
