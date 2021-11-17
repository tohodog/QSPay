package com.qinsong.qspay.uppay;

import org.qinsong.qspay.core.PayInfo;

//https://open.unionpay.com/tjweb/acproduct/list?apiSvcId=450&index=5
public class UPPayInfo extends PayInfo {

    public String tn;//后台返回的订单号
    public String ext;

    @Override
    public String payParam() {
        return tn;
    }

}