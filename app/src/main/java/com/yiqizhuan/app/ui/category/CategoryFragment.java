package com.yiqizhuan.app.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.databinding.FragmentCategoryBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.category.item.JinGangQuCategoryFlexibleItem;
import com.yiqizhuan.app.ui.home.item.JinGangQuFlexibleItem;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-07-09 7:33 PM
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener {
    FragmentCategoryBinding binding;
    private FlexibleAdapter<IFlexible> jinGangWeiFlexibleAdapter;
    private FlexibleAdapter<IFlexible> jinGangWeiPopFlexibleAdapter;
    private List<CategoryDefaultBean> categoryDefaultBeanList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder1);
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder2);
        initView();
        getCategoryDefault();
        return root;
    }

    private void initView() {
        binding.llyZhanKai.setOnClickListener(this);
        binding.rlyShouQi.setOnClickListener(this);
        jinGangWeiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWei.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rcJinGangWei.setAdapter(jinGangWeiFlexibleAdapter);
        binding.rcJinGangWei.setItemAnimator(new DefaultItemAnimator());

        jinGangWeiPopFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWeiPop.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
        binding.rcJinGangWeiPop.setAdapter(jinGangWeiPopFlexibleAdapter);
        binding.rcJinGangWeiPop.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyZhanKai:
                binding.rlyPop.setVisibility(View.VISIBLE);
                break;
            case R.id.rlyShouQi:
                binding.rlyPop.setVisibility(View.GONE);
                break;
        }
    }

    private void initCategoryDefaultData() {
        if (categoryDefaultBeanList == null) {
            categoryDefaultBeanList = new ArrayList<>();
        }
        for (CategoryDefaultBean categoryDefaultBean : categoryDefaultBeanList) {
            jinGangWeiFlexibleAdapter.addItem(new JinGangQuCategoryFlexibleItem(getActivity(), categoryDefaultBean, null));
            jinGangWeiPopFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), categoryDefaultBean, null));
        }
    }

    /**
     * 获取 类目列表
     */
    private void getCategoryDefault() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CATEGORY_DEFAULT, paramsMap, new BaseCallBack<BaseResult<List<CategoryDefaultBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<CategoryDefaultBean>> result) {
                if (result != null && result.getData() != null) {
                    categoryDefaultBeanList = result.getData();
                    initCategoryDefaultData();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

}
