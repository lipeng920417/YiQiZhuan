package com.yiqizhuan.app.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yiqizhuan.app.R;


public class SRoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    float width, height;
    private int leftTopRadius;
    private int rightTopRadius;
    private int leftBottomRadius;
    private int rightBottomRadius;

    public SRoundImageView(Context context) {
        this(context, null);
    }

    public SRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private void init(Context context, AttributeSet attrs) {

        //读取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SRoundImageView);
        int defaultRadius = 0;
        int radius = typedArray.getDimensionPixelOffset(R.styleable.SRoundImageView_radius, defaultRadius);
        leftTopRadius = typedArray.getDimensionPixelOffset(R.styleable.SRoundImageView_left_top_radius, defaultRadius);
        rightTopRadius = typedArray.getDimensionPixelOffset(R.styleable.SRoundImageView_right_top_radius, defaultRadius);
        leftBottomRadius = typedArray.getDimensionPixelOffset(R.styleable.SRoundImageView_left_bottom_radius, defaultRadius);
        rightBottomRadius = typedArray.getDimensionPixelOffset(R.styleable.SRoundImageView_right_bottom_radius, defaultRadius);

        //如果四个角的值没有设置，就用通用的radius
        if (leftTopRadius == defaultRadius) {
            leftTopRadius = radius;
        }
        if (rightTopRadius == defaultRadius) {
            rightTopRadius = radius;
        }
        if (leftBottomRadius == defaultRadius) {
            leftBottomRadius = radius;
        }
        if (rightBottomRadius == defaultRadius) {
            rightBottomRadius = radius;
        }
        typedArray.recycle();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (width > 12 && height > 12) {
            @SuppressLint("DrawAllocation") Path path = new Path();
            /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
            float[] rids = {leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
            path.addRoundRect(new RectF(0, 0, width, height), rids, Path.Direction.CW);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }


    public void setImage(String url) {
        setImage(url, R.mipmap.s_icon_default);
    }

    public void setImage(String url, int placeHolder) {
        Glide.with(this).load(url).placeholder(placeHolder).into(this);
    }

    /**
     * 图片宽度填充满view ,高度等比缩放
     */
    public void setImageFitWidth(String url) {
        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                int intrinsicWidth = resource.getIntrinsicWidth();
                int height = resource.getIntrinsicHeight();
                if (intrinsicWidth > 0 && height > 0) {
                    getLayoutParams().width = getWidth();
                    getLayoutParams().height = height * getLayoutParams().width / intrinsicWidth;
                }
                return false;
            }
        }).error(R.mipmap.s_icon_default).placeholder(R.mipmap.s_icon_default).into(this);
    }

}
