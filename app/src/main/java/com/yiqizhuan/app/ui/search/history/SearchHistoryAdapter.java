package com.yiqizhuan.app.ui.search.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;

import com.yiqizhuan.app.R;

public class SearchHistoryAdapter extends FlowAdapter<String> {
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(ViewGroup parent, String item, int position) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, null);
    }

    @Override
    public void initView(View view, String item, int position) {
        AppCompatTextView textView = view.findViewById(R.id.item_tv);
        textView.setText(item);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onPositiveClick(item);
                }
            }
        });
    }

    public interface OnClickListener {
        void onPositiveClick(String s);
    }
}