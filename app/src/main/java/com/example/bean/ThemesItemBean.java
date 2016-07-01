package com.example.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ThemesItemBean implements Parcelable{
    private int limite;
    private List<String> subscribed;
    private List<ThemesItemOutputBean> others;


    protected ThemesItemBean(Parcel in) {
        limite = in.readInt();
        subscribed = in.createStringArrayList();
        others = in.createTypedArrayList(ThemesItemOutputBean.CREATOR);
    }

    public static final Creator<ThemesItemBean> CREATOR = new Creator<ThemesItemBean>() {
        @Override
        public ThemesItemBean createFromParcel(Parcel in) {
            return new ThemesItemBean(in);
        }

        @Override
        public ThemesItemBean[] newArray(int size) {
            return new ThemesItemBean[size];
        }
    };

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public List<String> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<String> subscribed) {
        this.subscribed = subscribed;
    }

    public List<ThemesItemOutputBean> getOthers() {
        return others;
    }

    public void setOthers(List<ThemesItemOutputBean> others) {
        this.others = others;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(limite);
        parcel.writeStringList(subscribed);
        parcel.writeTypedList(others);
    }
}
