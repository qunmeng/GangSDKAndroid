package com.qm.gangsdk.ui.event;

import com.qm.gangsdk.core.outer.receiver.base.BaseGangReceiverEvent;

/**
 * Created by Administrator on 2017/10/16.
 *
 * 突发任务完成事件
 */

public class XLAccidentTaskSuccessEvent extends BaseGangReceiverEvent<String> {
    public XLAccidentTaskSuccessEvent(String data) {
        super(data);
    }

    @Override
    public String getMessageData() {
        return super.getMessageData();
    }
}
