package com.yiqizhuan.app.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yiqizhuan.app.YQZApp;

/**
 * @author LiPeng
 * @create 2024-04-24 2:15 PM
 */
public class GlideUtil {
    public static void loadImage(String url, ImageView imageView) {
        Glide.with(YQZApp.getContext())
                .load(url)
                .into(imageView);
    }

    public static void loadImage(String url, ImageView imageView, int cornerRadius) {
        Glide.with(YQZApp.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(SizeUtils.dp2px(cornerRadius))))
                .into(imageView);
    }

    public static void loadImageCrossFade(String url, ImageView imageView, int cornerRadius) {
        Glide.with(YQZApp.getContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(3000))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(SizeUtils.dp2px(cornerRadius))))
                .into(imageView);
    }
}
