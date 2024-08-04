package com.yiqizhuan.app.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.bean.ProductSearchDefaultBean;
import com.yiqizhuan.app.databinding.ActivitySearchBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.home.item.BottomFlexibleItem;
import com.yiqizhuan.app.ui.home.item.NoDataFlexibleItem;
import com.yiqizhuan.app.ui.search.history.ExpansionFoldLayout;
import com.yiqizhuan.app.ui.search.history.SearchHistoryAdapter;
import com.yiqizhuan.app.ui.search.item.RecommendFlexibleItem;
import com.yiqizhuan.app.ui.search.item.SearchFlexibleItem;
import com.yiqizhuan.app.util.ClickUtil;
import com.yiqizhuan.app.util.KeyboardUtils;
import com.yiqizhuan.app.util.ToastUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-07-07 4:35 PM
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    ActivitySearchBinding binding;
    //历史热词
    private boolean flag = true;
    private int MIN_VALUE = 2;
    //    private List<String> list = Utils.getHistoryList();
    private SearchHistoryAdapter mAdapter;

    private static final int MAX_SIZE = 20;
    private ArrayList<String> listHistory = new ArrayList<>(MAX_SIZE);
    //搜索结果
    private int page = 1;
    private int size = 20;
    private FlexibleAdapter<IFlexible> searchFlexibleAdapter;
    private String inputTextCurrent;
    private String firstCategoryId;
    private String type;
    private FlexibleAdapter<IFlexible> recommendFlexibleAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null) {
            firstCategoryId = getIntent().getStringExtra("firstCategoryId");
            type = getIntent().getStringExtra("type");
        }
        initView();
        productSearchDefault();
    }

    private void initView() {
        binding.tvSearch.setOnClickListener(this);
        binding.ivSearchBack.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    binding.llyHistoryRecommend.setVisibility(View.VISIBLE);
                    binding.rcSearch.setVisibility(View.GONE);
                    binding.smartRefreshLayout.setEnableLoadMore(false);
                    binding.smartRefreshLayout.setNoMoreData(true);
                }
            }
        });
        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    inputText(binding.edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
        getListHistory();
        mAdapter = new SearchHistoryAdapter();
        mAdapter.setOnClickListener(new SearchHistoryAdapter.OnClickListener() {
            @Override
            public void onPositiveClick(String s) {
                binding.edtSearch.setText(s);
                binding.edtSearch.setSelection(binding.edtSearch.getText().length()); // 设置光标位置
                inputText(s);
            }
        });
        initHistory();
        binding.smartRefreshLayout.setEnableRefresh(false);
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                productList(inputTextCurrent, false);
            }
        });
        //设置 Footer 为 球脉冲 样式
        binding.smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        searchFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.rcSearch.setAdapter(searchFlexibleAdapter);
        binding.rcSearch.setItemAnimator(new DefaultItemAnimator());
        binding.smartRefreshLayout.setEnableLoadMore(false);
        binding.smartRefreshLayout.setNoMoreData(true);
        //推荐
        recommendFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcRecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rcRecommend.setAdapter(recommendFlexibleAdapter);
        binding.rcRecommend.setItemAnimator(new DefaultItemAnimator());
    }

    private void initHistory() {
        if (listHistory != null && listHistory.size() > 0) {
            binding.llySearch.setVisibility(View.VISIBLE);
            binding.rlyHistory.removeAllViews();
            ExpansionFoldLayout flowListView = new ExpansionFoldLayout(this);
            flowListView.setEqually(false);
            flowListView.setFoldLines(MIN_VALUE);
            if (flag) {
                flowListView.setFold(true);
            } else {
                flowListView.setFold(false);
            }
            mAdapter.setNewData(listHistory);
            //如果列表返回不为空，那么给流式布局加上列表的最大position
            if (listHistory != null && listHistory.size() > 0) {
                flowListView.maxSize = listHistory.size() - 1;
            }
            flowListView.setAdapter(mAdapter);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            binding.rlyHistory.addView(flowListView, params);
        } else {
            binding.llySearch.setVisibility(View.GONE);
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSearchBack:
                finish();
                break;
            case R.id.tvSearch:
                if (ClickUtil.isRealClick()) {
                    inputText(binding.edtSearch.getText().toString());
                }
                break;
            case R.id.ivDelete:
                listHistory.clear();
                MMKVHelper.putString("listHistory", "");
                initHistory();
                break;
        }
    }


    public void addElement(String element) {
        if (listHistory != null && listHistory.size() > 0) {
            Iterator<String> iterator = listHistory.iterator();
            while (iterator.hasNext()) {
                String current = iterator.next();
                if (TextUtils.equals(element, current)) {
                    iterator.remove();
                }
            }
        }
        // 将新元素添加到第一个位置
        listHistory.add(0, element);
        // 如果列表超过了最大尺寸，移除最后一个元素
        if (listHistory.size() > MAX_SIZE) {
            listHistory.remove(listHistory.size() - 1);
        }
        initHistory();
        String jsonString = new Gson().toJson(listHistory);
        MMKVHelper.putString("listHistory", jsonString);
    }

    private void getListHistory() {
        String jsonString = MMKVHelper.getString("listHistory", "");
        if (!TextUtils.isEmpty(jsonString)) {
            // 使用 Gson 库将 JSON 字符串转换为 List<String>
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(jsonString, listType);
            listHistory.addAll(stringList);
        }
    }


    private void inputText(String inputText) {
        if (!TextUtils.isEmpty(inputText) && !TextUtils.isEmpty(inputText.trim())) {
            inputTextCurrent = inputText.trim();
            addElement(inputTextCurrent);
            binding.llyHistoryRecommend.setVisibility(View.GONE);
            binding.rcSearch.setVisibility(View.VISIBLE);
            KeyboardUtils.hideKeyboard(this);
            binding.smartRefreshLayout.setEnableLoadMore(true);
            binding.smartRefreshLayout.setNoMoreData(false);
            page = 1;
            productList(inputTextCurrent, true);
        } else {
            ToastUtils.showToast("请输入商品名称");
        }
    }

    private void updateDataUI(List<ProductListBean.Detail> result) {
        for (ProductListBean.Detail detail : result) {
            searchFlexibleAdapter.addItem(new SearchFlexibleItem(this, detail));
        }
    }

    /**
     * 商品搜索
     * "firstCategoryId": // 九大类目中搜索时，需要传递类目id
     * "type" : // 四大板块中搜索时，需要加上type，目前不涉及这个的搜索，值同四大板块
     */
    private void productList(String term, boolean loading) {
        if (loading) {
            showLoading();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page + "");
        paramsMap.put("size", size + "");
        paramsMap.put("term", term);
        if (!TextUtils.isEmpty(firstCategoryId)) {
            paramsMap.put("firstCategoryId", firstCategoryId);
        }
        if (!TextUtils.isEmpty(type)) {
            paramsMap.put("type", type);
        }
        OkHttpManager.getInstance().postRequest(Api.PRODUCT_SEARCH, paramsMap, new BaseCallBack<ProductListBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (loading) {
                    cancelLoading();
                }
            }

            @Override
            public void onSuccess(Call call, Response response, ProductListBean result) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (page == 1) {
                    searchFlexibleAdapter.clear();
                }
                if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
                    page++;
                    updateDataUI(result.getData().getDetails());
                } else {
                    if (searchFlexibleAdapter.getItemCount() < 1) {
                        searchFlexibleAdapter.addItem(new NoDataFlexibleItem(SearchActivity.this));
                    } else {
                        searchFlexibleAdapter.addItem(new BottomFlexibleItem(SearchActivity.this));
                    }
                    binding.smartRefreshLayout.setEnableLoadMore(false);
                    binding.smartRefreshLayout.setNoMoreData(true);
                }
                if (loading) {
                    cancelLoading();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (loading) {
                    cancelLoading();
                }
            }
        });
    }

    private void updateDataRecommend(List<ProductSearchDefaultBean> result) {
        for (ProductSearchDefaultBean productSearchDefaultBean : result) {
            recommendFlexibleAdapter.addItem(new RecommendFlexibleItem(this, productSearchDefaultBean));
        }
    }

    private void productSearchDefault() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_SEARCH_DEFAULT, paramsMap, new BaseCallBack<BaseResult<List<ProductSearchDefaultBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductSearchDefaultBean>> result) {
                if (result != null && result.getData() != null) {
                    updateDataRecommend(result.getData());
                }
                cancelLoading();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
