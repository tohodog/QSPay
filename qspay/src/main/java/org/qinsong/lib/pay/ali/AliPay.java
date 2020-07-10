package org.qinsong.lib.pay.ali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import org.qinsong.lib.pay.BasePay;
import org.qinsong.lib.pay.PayAPI;
import org.qinsong.lib.pay.PayInfo;

import java.util.Map;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/26
 * 支付宝支付
 */

public class AliPay extends BasePay {


    private final int SDK_PAY_FLAG = 1;

    public AliPay(PayAPI.Callback callback) {
        super(callback);
    }

    @Override
    public void pay(final Activity activity, PayInfo payInfo) {

        EnvUtils.setEnv(payInfo.testMode ? EnvUtils.EnvEnum.SANDBOX : EnvUtils.EnvEnum.ONLINE);

        final String finalPayParam = payInfo.payParam();
        if (finalPayParam.startsWith("http")) {
            //支付宝h5支付
            final PayTask task = new PayTask(activity);
            boolean isIntercepted = task.payInterceptorWithUrl(finalPayParam, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    Log.i("onPayResult", result.getResultCode());

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 支付结果返回
                            if (TextUtils.equals(result.getResultCode(), "9000")) {
                                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                callback.complete(result.getReturnUrl());

                            } else {
                                if (TextUtils.equals(result.getResultCode(), "6001"))
                                    callback.cancel();
                                else
                                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                    callback.fail(result.getReturnUrl());
                            }
                        }
                    });
                }
            });
            if (!isIntercepted) {
                throw new IllegalArgumentException("Can't Intercepted URL");
            }
            return;
        }

        //SDK支付
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(finalPayParam, true);
                Log.i("onPayResult", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);

            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                callback.complete(resultInfo);

            } else {
                if (TextUtils.equals(resultStatus, "6001"))
                    callback.cancel();
                else
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    callback.fail(resultStatus + "|" + payResult.getMemo());
            }
        }
    };
}
//        9000 	订单支付成功
//        8000 	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//        4000 	订单支付失败
//        5000 	重复请求
//        6001 	用户中途取消
//        6002 	网络连接出错
//        6004 	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态