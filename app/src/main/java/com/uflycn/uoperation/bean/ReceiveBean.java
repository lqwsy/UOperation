package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2018/2/24.
 */
public class ReceiveBean {
    private String ReceiverId;
    private String Receiver;
    private String ReceiveTime;

    public ReceiveBean(String receiverId, String receiver, String receiveTime) {
        ReceiverId = receiverId;
        Receiver = receiver;
        ReceiveTime = receiveTime;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }

    public String getReceiverId() {

        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }
}
