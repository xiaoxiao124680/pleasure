package com.yufusoft.payplatform.ccenter.mq.bo;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.util.bean.InteractionBO;

/**
 * 返回结果集
 * @param <T>
 * @author yzp
 * @date 2018.11.30
 */
public class MessageQueueResult<T> extends InteractionBO {

    private static final long serialVersionUID = 1L;

    private String resultCode;

    private String resultMsg;

    private T attachment;

    public MessageQueueResult(String resultCode, String resultMsg) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public MessageQueueResult(String resultCode, String resultMsg, T attachment) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.attachment = attachment;
    }

    public MessageQueueResult(T attachment) {
        super();
        this.resultCode = "0000000";
        this.attachment = attachment;
    }

    public MessageQueueResult(MessageQueueRespConst e) {
        super();
        this.resultCode = e.getCode();
        this.resultMsg = e.getDesc();
    }

    public MessageQueueResult(MessageQueueRespConst e, T attachment) {
        super();
        this.resultCode = e.getCode();
        this.resultMsg = e.getDesc();
        this.attachment = attachment;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public T getAttachment() {
        return attachment;
    }

    public boolean isSucc() {
        return "0000000".equals(this.resultCode);
    }
}

