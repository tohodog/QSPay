package org.qinsong.lib.pay.upmp;

import org.qinsong.lib.pay.PayInfo;

public class UPPayInfo extends PayInfo {

    public static String MODE = "00";//mMode参数解释： "00"正式环境  "01"测试环境

    public String tn;//后台返回的订单号

    @Override
    public String payParam() {
        return tn;
    }

}