# QSPay
[![](https://jitpack.io/v/tohodog/QSPay.svg)](https://jitpack.io/#tohodog/QSPay)
![zfb][zfbsvg]
![wx][wxsvg]
![union][unionsvg]
[![License][licensesvg]][license]

[zfbsvg]: https://img.shields.io/badge/支付宝-1.5.7.7-00A9EE.svg
[wxsvg]:  https://img.shields.io/badge/微信-6.4.4-41B035.svg
[unionsvg]:  https://img.shields.io/badge/银联-3.4.9-00908C.svg

[licensesvg]: https://img.shields.io/badge/License-Apache--2.0-red.svg
[license]: https://github.com/tohodog/QSPay/blob/master/LICENSE

[apisvg]: https://img.shields.io/badge/API-9+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=9
😀安卓一行代码☕搞定微信、支付宝、银联支付

### Gradle
```
//build.gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
}
//app.gradle
dependencies {
    implementation 'com.github.tohodog:QSPay:1.0.0'
}
```

### 启动支付API
```
    public void paySdk(PayInfo payInfo, PAY_TYPE pay_type) {
        PayAPI.get(this, pay_type).pay(payInfo, new PayCallback() {
            @Override
            public void onComplete(PAY_TYPE payType, String result) {
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(PAY_TYPE payType, String msg) {
                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(PAY_TYPE payType) {
                Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_LONG).show();
            }
        });
    }

```
### 微信支付
```
        //参数由后台接口生成
        WXPayInfo payInfo = new WXPayInfo();
        payInfo.appId = "wx41acb06b6a756b80";
        payInfo.prepayId = "wx0909150675574360653f9b341120102000";
        payInfo.partnerId = "1523993851";
        payInfo.nonceStr = "a01427863e3940699c9ac29442e0770b";
        payInfo.timeStamp = "1594257306";
        payInfo.packageValue = "Sign=WXPay";
        payInfo.sign = "8418D3E4C36B1E3A956763D71BBBF6DC";
        paySdk(payInfo, PAY_TYPE.WEIXIN);
        
        //微信需要在自己项目包名下新建wxapi文件夹,然后新建一个Activity继承QSPayEntryActivity
        public class WXPayEntryActivity extends QSPayEntryActivity {
        }
        //AndroidManifest.xml配置
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:exported="true"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

```
### 支付宝支付
```
        //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//沙箱模式
        //参数由后台接口生成
        AliPayInfo payInfo = new AliPayInfo();
        payInfo.payParam = "alipay_sdk=alipay-easysdk-java-2.0.0&app_id=2019091767145019&biz_content=%7B%22out_trade_no%22%3A%22102020070909062278810001%22%2C%22total_amount%22%3A%220%22%2C%22subject%22%3A%22test%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.reol.top%2FpayNotify%2FaliPay&sign=KRB3zZQQ7JeEeoHOJWwCSQaJ6ehv1I7WHSHtzJ4Y9pjq2HzBVR%2B5Mq9Z9%2BInAK%2Fcr%2Bc4pOiiJjPqdp61sUkrcWomPGjwFrObMC3xj29PeOBv%2FFCvR9UvRbIUr1tQ7El7YP8sSCRsI7BsBvHNhaxEz%2Ft6CvEAfchei28eC658cJvijw3FnYYVdRA2jBhU4YqTWM%2Ft9HEwBq5KGy8c9cOyyLvS9Hg0pAORVGkpSc9%2B8Rv0kwtmWsojoTTQZvHePYoWlXoR07WJxgOWcxLbMJ%2FASqDs5P1fBaoQLXJfbkQk1c%2Fx3sBnHWWuW%2FL%2BhtbikFX%2FkN9dJcLwuXauNyU9ls1Oog%3D%3D&sign_type=RSA2&timestamp=2020-07-09+09%3A06%3A22&version=1.0";
        paySdk(payInfo, PAY_TYPE.ALIPAY);

```
### 银联支付
```
        UPPayInfo.MODE = "01";//00正式 01测试
        UPPayInfo upPayInfo = new UPPayInfo();
        upPayInfo.tn = "989239916971662107220";
        paySdk(upPayInfo, PAY_TYPE.UPPAY);
```

## Log
### v1.0.0(2020-07-09)
  * 支持微信,支付宝,银联支付

## Other
  * 有问题请Add [issues](https://github.com/tohodog/QSPay/issues)
  * 如果项目对你有帮助的话欢迎[![star][starsvg]][star]
  
[starsvg]: https://img.shields.io/github/stars/tohodog/QSPay.svg?style=social&label=Stars
[star]: https://github.com/tohodog/QSPay
