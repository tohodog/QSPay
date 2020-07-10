package com.qinsong.qspay;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.qinsong.lib.pay.PAY_TYPE;
import org.qinsong.lib.pay.PayAPI;
import org.qinsong.lib.pay.PayCallback;
import org.qinsong.lib.pay.PayInfo;
import org.qinsong.lib.pay.ali.AliPayInfo;
import org.qinsong.lib.pay.upmp.UPPayInfo;
import org.qinsong.lib.pay.wx.WXPayInfo;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        QSPermissionUtil.requestPermission(this, new QSPermissionUtil.PermissionListener() {
            @Override
            public void onPermissionSucceed(int requestCode, List<String> grantedList) {

            }

            @Override
            public void onPermissionFailed(int requestCode, List<String> deniedList) {

            }
        }, QSPermissionUtil.SDCARD);
    }

    public void wechat(View view) {
        //参数由后台接口生成
        WXPayInfo payInfo = new WXPayInfo();
        payInfo.appId = "wx41acb06b6a756b80";
        payInfo.prepayId = "wx0909150675574360653f9b341120102000";
        payInfo.partnerId = "1523993851";
        payInfo.nonceStr = "a01427863e3940699c9ac29442e0770b";
        payInfo.timeStamp = "1594257306";
        payInfo.packageValue = "Sign=WXPay";
        payInfo.sign = "8418D3E4C36B1E3A956763D71BBBF6DC";
        paySdk(payInfo, PAY_TYPE.WEIXIN);
    }

    public void alipay(View view) {
        //参数由后台接口生成
        AliPayInfo payInfo = new AliPayInfo();
        //payInfo.testMode = true;//沙箱模式
        payInfo.payParam = "alipay_sdk=alipay-easysdk-java-2.0.0&app_id=2019091767145019&biz_content=%7B%22out_trade_no%22%3A%22102020070909062278810001%22%2C%22total_amount%22%3A%220%22%2C%22subject%22%3A%22test%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.reol.top%2FpayNotify%2FaliPay&sign=KRB3zZQQ7JeEeoHOJWwCSQaJ6ehv1I7WHSHtzJ4Y9pjq2HzBVR%2B5Mq9Z9%2BInAK%2Fcr%2Bc4pOiiJjPqdp61sUkrcWomPGjwFrObMC3xj29PeOBv%2FFCvR9UvRbIUr1tQ7El7YP8sSCRsI7BsBvHNhaxEz%2Ft6CvEAfchei28eC658cJvijw3FnYYVdRA2jBhU4YqTWM%2Ft9HEwBq5KGy8c9cOyyLvS9Hg0pAORVGkpSc9%2B8Rv0kwtmWsojoTTQZvHePYoWlXoR07WJxgOWcxLbMJ%2FASqDs5P1fBaoQLXJfbkQk1c%2Fx3sBnHWWuW%2FL%2BhtbikFX%2FkN9dJcLwuXauNyU9ls1Oog%3D%3D&sign_type=RSA2&timestamp=2020-07-09+09%3A06%3A22&version=1.0";
        paySdk(payInfo, PAY_TYPE.ALIPAY);

    }

    public void union(View view) {
        UPPayInfo upPayInfo = new UPPayInfo();
        upPayInfo.tn = "989239916971662107220";
        upPayInfo.testMode = true;//测试模式
        paySdk(upPayInfo, PAY_TYPE.UPPAY);
    }


    //启动支付
    public void paySdk(PayInfo payInfo, PAY_TYPE pay_type) {
        PayAPI.get(this, pay_type).pay(payInfo, new PayCallback() {
            @Override
            public void onComplete(PAY_TYPE payType, String result) {
                //同步支付结果成功
                Log.e("onComplete", payType + "->" + result);
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                textView.setText(payType + "->" + result);
            }

            @Override
            public void onFail(PAY_TYPE payType, String msg) {
                Log.e("onFail", payType + "->" + msg);
                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_LONG).show();
                textView.setText(payType + "->" + msg);
            }

            @Override
            public void onCancel(PAY_TYPE payType) {
                Log.e("onCancel", payType + "->" + "Cancel");
                Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_LONG).show();
                textView.setText(payType + "->" + "Cancel");
            }
        });
    }
}
