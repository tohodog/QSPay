package org.qinsong.lib.pay.upmp;

import org.qinsong.lib.pay.PayInfo;

public class UPPayInfo extends PayInfo {

    public String tn;//后台返回的订单号
    public String ext;

    @Override
    public String payParam() {
        return tn;
    }

}