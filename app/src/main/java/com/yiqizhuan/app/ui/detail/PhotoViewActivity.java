package com.yiqizhuan.app.ui.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityPhotoViewBinding;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.detail.adapter.ViewPagerAdapter;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-24 10:00 AM
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener {
    ActivityPhotoViewBinding binding;
    ViewPagerAdapter adapter;
    private List<String> imageUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        includeActionbar.setText("查看图片");
        actionbarBack.setOnClickListener(this);
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");

        adapter = new ViewPagerAdapter(this, imageUrls);
        binding.viewPager.setAdapter(adapter);

        // 设置初始角标
        updateIndicator(0);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicator(position);
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        binding.viewPager.setAdapter(adapter);

        // 设置初始角标
        updateIndicator(0);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicator(position);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
        }
    }

    private void updateIndicator(int position) {
        String text = (position + 1) + " / " + imageUrls.size();
        binding.indicator.setText(text);
    }

}
