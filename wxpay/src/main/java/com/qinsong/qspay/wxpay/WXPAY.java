package com.qinsong.qspay.wxpay;

import android.app.Activity;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.qinsong.qspay.core.BasePay;
import org.qinsong.qspay.core.PayAPI;
import org.qinsong.qspay.core.PayInfo;
import org.qinsong.qspay.core.QSPayConstants;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/26
 * 微信支付
 */

public class WXPAY extends BasePay {


    //private static WeakReference<WXPay> wxPayWeakReference;//使用弱指针需要调用者一直持有，不然会释放
    private static WXPAY wxPay;//使用静态变量，不释放会内存泄漏

    //给微信的activity回调使用
    public static WXPAY get() {
        return wxPay;
    }

    public WXPAY(PayAPI.Callback callback) {
        super(callback);
        //wxPayWeakReference = new WeakReference<WXPay>(this);
        wxPay = this;
    }

    @Override
    public void pay(Activity activity, PayInfo payInfo) throws JSONException {
        //签名参数全部小写
        JSONObject json = new JSONObject(payInfo.payParam());
        String appid = QSPayConstants.WX_APPID;
        appid = json.getString("appId");
        IWXAPI api = WXAPIFactory.createWXAPI(activity.getApplication(), appid);

        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId = json.getString("appId");
        req.partnerId = json.getString("partnerId");
        req.prepayId = json.getString("prepayId");
        req.nonceStr = json.getString("nonceStr");
        req.timeStamp = json.getString("timeStamp");
        req.packageValue = json.getString("packageValue");
        req.sign = json.getString("sign");

        req.extData = json.optString("extData", null); // optional

        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);

    }

    public void release() {
        wxPay = null;
    }
}
