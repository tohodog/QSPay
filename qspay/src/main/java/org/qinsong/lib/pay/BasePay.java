package org.qinsong.lib.pay;

import android.content.Intent;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 描述
 */

public abstract class BasePay implements IPay {

    protected PayAPI.Callback callback;

    public PayAPI.Callback getCallback() {
        return callback;
    }

    public BasePay(PayAPI.Callback callback) {
        this.callback = callback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void release() {

    }
}
