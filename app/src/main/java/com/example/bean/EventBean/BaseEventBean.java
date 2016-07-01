package com.example.bean.EventBean;


public class BaseEventBean {
    protected int eventCode;
    protected String eventMsg;

    public BaseEventBean() {
    }

    public BaseEventBean(int eventCode, String eventMsg) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventMsg() {
        return eventMsg;
    }

    public void setEventMsg(String eventMsg) {
        this.eventMsg = eventMsg;
    }
}
