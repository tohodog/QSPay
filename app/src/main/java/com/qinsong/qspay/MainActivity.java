package com.qinsong.qspay;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qinsong.qspay.alipay.AliPayInfo;
import com.qinsong.qspay.uppay.UPPayInfo;
import com.qinsong.qspay.wxpay.WXPayInfo;

import org.qinsong.qspay.core.PAY_TYPE;
import org.qinsong.qspay.core.PayAPI;
import org.qinsong.qspay.core.PayCallback;
import org.qinsong.qspay.core.PayInfo;

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
        paySdk(payInfo, PAY_TYPE.WXPAY);
    }

    public void alipay(View view) {
        //参数由后台接口生成
        AliPayInfo payInfo = new AliPayInfo();
        //payInfo.testMode = true;//沙箱模式
        payInfo.payParam = "alipay_sdk=alipay-easysdk-java-2.0.0&app_id=2018053060296290&biz_content=%7B%22out_trade_no%22%3A%22102020080312330440410005%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%22xxxx%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.reol.top%2FpayNotify%2FaliPay&sign=afuOqIWPOHTXiebZbwSmXdhqXuuZGgEDvAQfJ3nPV1v214aB6bfXIPZX7EVvvnZvRzqm0H4zmn7gBCFutvT8JK7MzmOamjpYWYso%2BGKl73E8jEU8N4237bAAm1AfaFYv9VEmAzeOsECOhN3eNaw78whDdL6Z%2FNz5XlKKg1GBNo0xgGqPMALmO21FFtM%2FszagaEGN7iz7%2FyMOKp8ag9msJBrs%2Fxe%2FZZm3Q55ccLWYYGt7UgQYkJQi%2BVb%2F5t8PqPawzVH9wZkAbdlr6i%2Fz%2BjK3uzdSUYSDD9RQYk2oIxVZdZnjxRWP0T48TADT4VyUd8cV12LqU0Cl4nsKmUe5TtXWLQ%3D%3D&sign_type=RSA2&timestamp=2020-08-03+12%3A33%3A04&version=1.0";
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
