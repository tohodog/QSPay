package com.qinsong.qspay.alipay;

import org.json.JSONObject;
import org.qinsong.qspay.core.PayInfo;
import org.qinsong.qspay.core.QSPayConstants;

import java.util.Map;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 阿里订单的信息
 * https://docs.open.alipay.com/204/105465/
 */

public class AliPayInfo extends PayInfo {

    public String payParam;//后台构建好的参数,直接用

    //必填
    public String out_trade_no;//商户网站唯一订单号
    public String subject;//标题
    public String total_amount;//金额
    public final String product_code = "QUICK_MSECURITY_PA";//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PA
    public String notify_url = QSPayConstants.ALI_NOTICE_URL;

    //选填
    public String timeout_express;//订单付款时间
    public String body;//订单详细描述
    public String goods_type;//商品主类型：0—虚拟类商品，1—实物类商品 注：虚拟类商品不支持使用花呗渠道
    public String enable_pay_channels;//可用渠道，用户只能在指定渠道范围内支付当有多个渠道时用“,”分隔
    public String disable_pay_channels;//禁用渠道，可用禁用二选一
    public String passback_params;//如果请求时传递了该参数，则返回给商户时会回传该参数。


    public String extend_params;// 业务扩展参数说明 花呗

//    sys_service_provider_id 	 系统商编号，该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID 	2088511833207846
//    needBuyerRealnamed  是否发起实名校验 T：发起 F：不发起
//    TRANS_MEMO  账务备注
//    注：该字段显示在离线账单的账务备注中 	促销
//    hb_fq_num  花呗分期数（目前仅支持3、6、12）
//    注：使用该参数需要仔细阅读“花呗分期接入文档” 	3
//    hb_fq_seller_percent  卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持
//    注：使用该参数需要仔细阅读“花呗分期接入文档”


    public String buildParam() {
        try {
            JSONObject biz_content = new JSONObject();
            biz_content.put("out_trade_no", out_trade_no);
            biz_content.put("product_code", product_code);
            biz_content.put("body", body);
            biz_content.put("subject", subject);
            biz_content.put("total_amount", total_amount);
            biz_content.put("timeout_express", timeout_express);

            boolean rsa2 = (QSPayConstants.RSA2_PRIVATE.length() > 0);
            Map<String, String> params = OrderInfoUtil.buildOrderParamMap(biz_content.toString(), notify_url);
            String orderParam = OrderInfoUtil.buildOrderParam(params);
            //这里需要去服务器签名,但是一般参数都是服务器构建好回来,不用客户端构建,减少风险
            String privateKey = rsa2 ? QSPayConstants.RSA2_PRIVATE : QSPayConstants.RSA_PRIVATE;
            String sign = OrderInfoUtil.getSign(params, privateKey, rsa2);
            payParam = orderParam + "&" + sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payParam;
    }

    @Override
    public String payParam() {
        if (payParam == null) payParam = buildParam();
        return payParam;
    }

}
