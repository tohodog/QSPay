package com.qinsong.qspay.wxpay;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.qinsong.qspay.core.QSPayConstants;

//TODO 新建一个类继承这个,放到自己项目报名的目录下, 并在xml注册,参考WXPayEntryActivity
public class QSPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Log.e(TAG, "onCreate");
        api = WXAPIFactory.createWXAPI(this, QSPayConstants.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.i("WXEntryActivity", "onReq:" + req);
    }

    @Override
    public void onResp(BaseResp resp) {
        finish();

        Log.i(TAG, "onResp:" + resp.errCode);
        WXPAY wxPay = WXPAY.get();
        if (wxPay == null)
            return;
        wxPay.release();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (resp instanceof PayResp) {
                    PayResp payResp = (PayResp) resp;
                    wxPay.getCallback().complete(payResp.returnKey);
                }
                return;
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL ||
                    resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                wxPay.getCallback().cancel();
                return;
            }

            wxPay.getCallback().fail("errStr:" + resp.errStr + ",errCode:" + resp.errCode);

        }
    }
}