package com.yiqizhuan.app.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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

    public static void loadImageBig(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter();

        Glide.with(YQZApp.getContext())
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadImageBanner(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter(); // 确保图片不被裁剪

        Glide.with(YQZApp.getContext())
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }
}
