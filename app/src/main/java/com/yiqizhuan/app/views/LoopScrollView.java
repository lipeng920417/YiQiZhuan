package com.yiqizhuan.app.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LoopScrollView extends ScrollView {

    private static final int MSG_SCROLL = 1;

    private int scrollSpeed = 1; // 滚动速度

    private boolean isScrolling = false;

    private LinearLayout contentLayout;

    private int contentHeight;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_SCROLL) {
                int scrollY = getScrollY();
                if (scrollY + getHeight() < contentHeight) {
                    smoothScrollBy(0, scrollSpeed);
                } else {
                    smoothScrollTo(0, 0);
                }
                if (isScrolling) {
                    handler.sendEmptyMessageDelayed(MSG_SCROLL, 10);
                }
            }
            return true;
        }
    });

    public LoopScrollView(Context context) {
        super(context);
        init(context);
    }

    public LoopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoopScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        contentLayout = new LinearLayout(context);
        addView(contentLayout);
    }

    public void addContentView(View view) {
        contentLayout.addView(view);
        measureContentHeight();
    }

    private void measureContentHeight() {
        contentLayout.measure(View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        contentHeight = contentLayout.getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrolling = false;
                handler.removeMessages(MSG_SCROLL);
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = true;
                handler.sendEmptyMessage(MSG_SCROLL);
                break;
        }
        return super.onTouchEvent(ev);
    }
}