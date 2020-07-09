package org.qinsong.lib.pay;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.qinsong.lib.pay.ali.AliPay;
import org.qinsong.lib.pay.upmp.UPPay;
import org.qinsong.lib.pay.wx.WXPay;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 支付操作类
 */

public class PayAPI {


    public static PayAPI get(Activity activity, PAY_TYPE authShareType) {
        return new PayAPI(activity, authShareType);
    }

    private Activity activity;
    private IPay iPay;
    private PAY_TYPE payType;
    private PayCallback payCallback;

    private PayAPI(Activity activity, PAY_TYPE payType) {
        this.activity = activity;
        this.payType = payType;
        switch (payType) {
            case ALIPAY:
                iPay = new AliPay(new MyCallback());
                break;
            case WEIXIN:
                iPay = new WXPay(new MyCallback());
                break;
            case UPPAY:
                iPay = new UPPay(new MyCallback());
                break;
        }
    }


    public PayAPI pay(PayInfo payInfo, PayCallback payCallback) {
        this.payCallback = payCallback;
        try {
            iPay.pay(activity, payInfo);
        } catch (Exception e) {
            e.printStackTrace();
            payCallback.onFail(payType, e.getMessage());
        }
        return this;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        iPay.onActivityResult(requestCode, resultCode, data);
    }

    public void release() {
        iPay.release();
        activity = null;
    }

    private class MyCallback implements Callback {

        @Override
        public void complete(String result) {
            payCallback.onComplete(payType, result);
        }

        @Override
        public void fail(String msg) {
            payCallback.onFail(payType, msg);
        }

        @Override
        public void cancel() {
            payCallback.onCancel(payType);
        }
    }


    public interface Callback {
        void complete(String result);

        void fail(String msg);

        void cancel();
    }


    public static void initWX(Application context, String app_id, String key) {
        SDKConstants.context = context;
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(app_id);
        SDKConstants.WX_APPID = app_id;
        SDKConstants.WX_KEY = key;
    }

}
