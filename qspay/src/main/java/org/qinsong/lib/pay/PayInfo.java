package org.qinsong.lib.pay;

import java.io.Serializable;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2018/3/29
 * 订单实体类
 */

public abstract class PayInfo implements Serializable {

    public abstract String payParam();

}
