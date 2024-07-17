package com.yiqizhuan.app.ui.integral;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityIntegralAuthorizationBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.ui.base.BaseActivity;

import java.util.Calendar;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 兑换协议
 */
public class IntegralAuthorizationActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralAuthorizationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntegralAuthorizationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("个人授权书");
        actionbarBack.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        binding.tvContent.setText("为了保障您的合法权益，您应当完整阅读、充分理解本授权书条款内容，并严格遵守本授权书条款。请您注意，当您通过点击或勾选方式打开阅读本授权书时，即视为您已阅读并且完全正确理解和接受本授权书的所有条款，并自愿受到本授权书条款的约束。" +
                "\n若您不同意或不接受本授权书有关条款，请您退出并终止进行下一步操作。" +
                "\n\n起点众惠(北京)科技发展有限公司(以下简称“被授权人”或本人(或称授权人)充分理解并同意，贵司(或称被授权人)为本人提供商城服务，为客观、准确地评估本人的数据，并为本人提供优质的服务，就使用本人的相关必要信息及第三方数据查询事宜，本人同意并授权贵司在本人兑换积分申请阶段，或积分使用期间对信息基础数据库进行，查询、采集、保留、加工并使用本人如下信息:" +
                "\n一、本人同意被授权人使用本人以下授权信息。"+
                "\n1.基本信息:姓名、用户名、证件号码、证件类型、性别、出生日期、积分兑换信息、兑付资产信息、联系地址、等信息;" +
                "\n2.身份认证信息:公安部公民身份认证信息、银行卡真实性校验信息等;" +
                "\n3.手机及通信运营商信息:手机在网信息、设备信息、号码归属地、操作日志等;" +
                "\n4.积分兑换及兑付资产信息等。" +
                "\n二、本人同意贵司基于要提供给本人的业务，可自行或受合作机构的委托以贵司的名义将本人的基本信息、兑付信息、积分信息及其他相关信息报送至自有平台信息基础数据库、等机构，用于本授权书第三条约定的目的。本人同意将本人信息基础数据库进行展示。本人认可传输共享此类信息是为本人提供相关服务的必要前提。" +
                "\n三、本人同意授权贵司将上述信息用于以下用途" +
                "\n1.确认本人身份真实性;" +
                "\n2.审核本人兑付额度、积分兑换额度、积分使用情况等信息;" +
                "\n3.依法或经有权部门要求。" +
                "\n四、若因本授权书发生任何纠纷或争议，首先应友好协商解决。协商不成的，本人同意将纠纷或争议提交被授权人所在地有管辖权的人民法院管辖。本授权书的成立:生效、履行、解释及纠纷解决，适用中华人民共和国大陆地区法律。" +
                "\n本人同意被授权人留存本授权书、已查询到的本人信息、兑付信息、积分兑换信息、积分使用信息及被授权人合法收集到的其他信息，保存期限为法律法规要求的最短时间。本人已知悉本授权书所有内容(特别是加粗字体内容)的意义以及由此产生的法律效力，自愿作出上述授权。" +
                "\n特此授权。" +
                "\n\n授权人: "+ MMKVHelper.getString("userName","") +
                "\n证件号码:"+MMKVHelper.getString("idNumber","") +
                "\n授权日期: " + year + "年" + month + "月" + day + "日");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
        }
    }
}
