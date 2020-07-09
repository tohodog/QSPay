package org.qinsong.lib.pay;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/26
 * 支付接口
 */

public interface IPay {

    void pay(Activity activity, PayInfo orderInfo) throws Exception;

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void release();

}
