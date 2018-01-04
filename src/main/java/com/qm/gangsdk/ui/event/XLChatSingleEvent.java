package com.qm.gangsdk.ui.event;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.receiver.base.BaseGangReceiverEvent;

/**
 * Created by lijiyuan on 2017/12/13.
 *
 * 发私聊事件
 */

public class XLChatSingleEvent extends BaseGangReceiverEvent<XLMessageBody> {

    public XLChatSingleEvent(XLMessageBody data) {
        super(data);
    }

    @Override
    public XLMessageBody getMessageData() {
        return super.getMessageData();
    }
}
