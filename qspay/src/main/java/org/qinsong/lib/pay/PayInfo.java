package org.qinsong.lib.pay;

import java.io.Serializable;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 订单实体类
 */

public abstract class PayInfo implements Serializable {

    /**
     * 全局参数-测试模式
     */
    public static boolean TestMode = false;

    public abstract String payParam();

    public boolean testMode = TestMode;
}
