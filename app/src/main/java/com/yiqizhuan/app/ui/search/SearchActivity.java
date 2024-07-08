package com.yiqizhuan.app.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivitySearchBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.search.history.ExpansionFoldLayout;
import com.yiqizhuan.app.ui.search.history.SearchHistoryAdapter;
import com.yiqizhuan.app.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiPeng
 * @create 2024-07-07 4:35 PM
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private boolean flag = true;
    private int MIN_VALUE = 2;
    ActivitySearchBinding binding;
    //    private List<String> list = Utils.getHistoryList();
    private SearchHistoryAdapter mAdapter;

    private static final int MAX_SIZE = 20;
    private ArrayList<String> listHistory = new ArrayList<>(MAX_SIZE);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
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
                }
            }
        });
        getListHistory();
        mAdapter = new SearchHistoryAdapter();
        mAdapter.setOnClickListener(new SearchHistoryAdapter.OnClickListener() {
            @Override
            public void onPositiveClick(String s) {
                binding.edtSearch.setText(s);
            }
        });
        initHistory();
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
                String inputText = binding.edtSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(inputText)) {
                    addElement(inputText);
                    binding.llyHistoryRecommend.setVisibility(View.GONE);
                    closeKeyboard();
                } else {
                    ToastUtils.showToast("请输入商品名称");
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
