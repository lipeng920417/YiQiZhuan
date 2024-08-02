package com.yiqizhuan.app.ui.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.ui.order.OrderTabFragment;

import java.util.List;

/**
 * author : lipeng
 * date   : 2019-12-02 19:17
 * desc   : tab适配器
 */
public class TabOrderFragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> title;
    private Context context;
    private List<OrderTabFragment> fragments;

    public TabOrderFragmentAdapter(FragmentManager fm, List<OrderTabFragment> fragments, List<String> title, Context context) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_tab, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView iv = view.findViewById(R.id.iv);
        RelativeLayout rly = view.findViewById(R.id.rly);
        tvTitle.setText(title.get(position));
        tvTitle.setTextSize(14f);
        tvTitle.setTextColor(context.getResources().getColor(R.color.color_333333));
        iv.setVisibility(View.GONE);
        return view;
    }
}
