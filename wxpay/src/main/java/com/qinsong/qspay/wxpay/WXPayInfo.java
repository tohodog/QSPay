package com.qinsong.qspay.wxpay;

import org.json.JSONException;
import org.json.JSONObject;
import org.qinsong.qspay.core.PayInfo;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/7/31
 * https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Access_Guide/Android.html#jump2
 */

public class WXPayInfo extends PayInfo {

    @Override
    public String payParam() {
        JSONObject json = new JSONObject();
        try {
            //必填
            json.put("appId", appId);
            json.put("partnerId", partnerId);
            json.put("prepayId", prepayId);
            json.put("nonceStr", nonceStr);
            json.put("timeStamp", timeStamp);
            json.put("packageValue", packageValue);
            json.put("sign", sign);
            //非必填
            json.putOpt("extData", extData);
            json.putOpt("signType", signType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;
    public String extData;
    public String signType;

}
