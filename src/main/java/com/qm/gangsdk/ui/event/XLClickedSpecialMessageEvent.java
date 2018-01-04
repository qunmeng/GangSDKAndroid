package com.qm.gangsdk.ui.event;

import com.qm.gangsdk.core.outer.common.entity.XLMessageSpecialBean;
import com.qm.gangsdk.core.outer.receiver.base.BaseGangReceiverEvent;

/**
 * Created by Administrator on 2017/10/16.
 *
 * 特殊消息事件
 */

public class XLClickedSpecialMessageEvent extends BaseGangReceiverEvent<XLMessageSpecialBean> {
    public XLClickedSpecialMessageEvent(XLMessageSpecialBean data) {
        super(data);
    }

    @Override
    public XLMessageSpecialBean getMessageData() {
        return super.getMessageData();
    }
}
