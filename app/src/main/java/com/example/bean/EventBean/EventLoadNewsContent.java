package com.example.bean.EventBean;

import com.example.bean.NewsContentBean;

public class EventLoadNewsContent extends BaseEventBean {
    private NewsContentBean bean;

    public EventLoadNewsContent() {
    }

    public EventLoadNewsContent(int eventCode, String eventMsg, NewsContentBean bean) {
        super(eventCode, eventMsg);
        this.bean = bean;
    }

    public NewsContentBean getBean() {
        return bean;
    }

    public void setBean(NewsContentBean bean) {
        this.bean = bean;
    }
}
