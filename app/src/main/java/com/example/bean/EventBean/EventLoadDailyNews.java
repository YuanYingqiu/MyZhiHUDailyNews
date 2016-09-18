package com.example.bean.EventBean;

public class EventLoadDailyNews extends BaseEventBean {
    public EventLoadDailyNews() {
    }
    public EventLoadDailyNews(int code,String msg){
        super(code,msg);
    }

    public EventLoadDailyNews(int code , String msg , boolean isNeedRemoveAll){
        super(code,msg);
        this.isNeedRemoveAll = isNeedRemoveAll;
    }

    private boolean isNeedRemoveAll;

    public boolean isNeedRemoveAll() {
        return isNeedRemoveAll;
    }

    public void setNeedRemoveAll(boolean needRemoveAll) {
        isNeedRemoveAll = needRemoveAll;
    }
}
