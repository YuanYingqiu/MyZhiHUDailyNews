package com.example.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ThemesItemOutputBean implements Parcelable{
    private int color;
    private String thumbnail;
    private String description;
    private int id;
    private String name;


    public ThemesItemOutputBean(){}

    protected ThemesItemOutputBean(Parcel in) {
        color = in.readInt();
        thumbnail = in.readString();
        description = in.readString();
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ThemesItemOutputBean> CREATOR = new Creator<ThemesItemOutputBean>() {
        @Override
        public ThemesItemOutputBean createFromParcel(Parcel in) {
            return new ThemesItemOutputBean(in);
        }

        @Override
        public ThemesItemOutputBean[] newArray(int size) {
            return new ThemesItemOutputBean[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(color);
        parcel.writeString(thumbnail);
        parcel.writeString(description);
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
