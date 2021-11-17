package com.qinsong.qspay.uppay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.qinsong.qspay.core.BasePay;
import org.qinsong.qspay.core.PayAPI;
import org.qinsong.qspay.core.PayInfo;

public class UPPAY extends BasePay {

    //private static WeakReference<WXPay> wxPayWeakReference;//使用弱指针需要调用者一直持有，不然会释放
    private static UPPAY upPay;//使用静态变量，不释放会内存泄漏

    //给微信的activity回调使用
    public static UPPAY get() {
        return upPay;
    }


    public UPPAY(PayAPI.Callback callback) {
        super(callback);
        upPay = this;
    }

    @Override
    public void pay(Activity activity, PayInfo orderInfo) throws Exception {
        String tn = orderInfo.payParam();
        String mode = orderInfo.testMode ? "01" : "00";

//        UPPayAssistEx.startPay(activity, null, null, tn, UPPayInfo.MODE);

        Intent intent = new Intent(activity, UPPayEntryActivity.class);
        intent.putExtra("tn", tn);
        intent.putExtra("mode", mode);
        activity.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("UPPay", "onActivityResult:" + requestCode + "/" + resultCode + "/" + data);
        if (data == null || data.getExtras() == null) {
            callback.fail("Intent data is null");
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str == null) {
            callback.fail("pay_result is null");
            return;
        }
        if (str.equalsIgnoreCase("success")) {
            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
//            if (data.hasExtra("result_data")) {
//                String result = data.getExtras().getString("result_data");
//                try {
//                    JSONObject resultJson = new JSONObject(result);
//                    String sign = resultJson.getString("sign");
//                    String dataOrg = resultJson.getString("data");
//                    // 此处的verify建议送去商户后台做验签
//                    // 如要放在手机端验，则代码必须支持更新证书
//                    //这里是验签。就是检测支付结果是否真的成功了。---xiong
////                    boolean ret = verify(dataOrg, sign, mMode);
////                    if (ret) {
////                        // 验签成功，显示支付结果
////                        msg = "支付成功！";
////                    } else {
////                        // 验签失败
////                        msg = "支付失败！";
////                    }
//                } catch (JSONException e) {
//                }
//            }
            String result = data.getExtras().getString("result_data");
            Log.d("UPPay", "支付成功:" + result);
            // 结果result_data为成功时，去商户后台轮询一下再展示成功
            callback.complete(result);
        } else if (str.equalsIgnoreCase("fail")) {
            callback.fail("支付失败");
            Log.d("UPPay", "支付失败");
        } else if (str.equalsIgnoreCase("cancel")) {
            Log.d("UPPay", "支付取消");
            callback.cancel();
        } else {
            Log.d("UPPay", "支付未知");
            callback.cancel();
        }
    }

    public void release() {
        upPay = null;
    }

}
