/*
Copyright 2017 zhpanvip The BannerViewPager Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.yiqizhuan.app.views;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class ScaleInTransformerCustom1 implements ViewPager2.PageTransformer {

    private static final float DEFAULT_CENTER = 0.5f;
    private static final float DEFAULT_CENTER2 = 0.25f;
    public static final float DEFAULT_MIN_SCALE = 0.85f;
    private final float mMinScale;

    private final boolean mIsRtl;

    public ScaleInTransformerCustom1(float minScale, boolean isRtl) {
        mMinScale = minScale;
        mIsRtl = isRtl;
    }

    @Override
    public void transformPage(View view, float position) {
        if (mIsRtl) {
            position = -position;
        }
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setPivotY(pageHeight / 2f);
        view.setPivotX(pageWidth / 2f);
        if (position < -1) {
            // This page is way off-screen to the left.
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
            view.setPivotX(pageWidth);
        } else if (position <= 1) {
            // Modify the default slide transition to shrink the page as well
            float scaleFactor;
            if (position < 0) {
                scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
            } else {
                scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }else if(position <= 3){
            float positionCur = position - 2;
            // Modify the default slide transition to shrink the page as well
            float scaleFactor;
            if (positionCur < 0) {
                scaleFactor = (1 + positionCur) * (1 - mMinScale) + mMinScale;
            } else {
                scaleFactor = (1 - positionCur) * (1 - mMinScale) + mMinScale;
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
        }
    }
}
