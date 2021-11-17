package com.qinsong.qspay.uppay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unionpay.UPPayAssistEx;

public class UPPayEntryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UPPayAssistEx.startPay(this, null, null,
                getIntent().getStringExtra("tn"), getIntent().getStringExtra("mode"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        Log.e("UPPayEntryActivity", "onActivityResult:" + data.getExtras());
        UPPAY upPay = UPPAY.get();
        if (upPay == null)
            return;
        upPay.release();
        upPay.onActivityResult(requestCode, resultCode, data);
    }
}