package org.qinsong.qspay.core;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 描述
 */

public interface PayCallback {


    void onComplete(PAY_TYPE payType, String result);

    void onFail(PAY_TYPE payType, String msg);

    void onCancel(PAY_TYPE payType);
}
