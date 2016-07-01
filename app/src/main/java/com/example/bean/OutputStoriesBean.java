package com.example.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OutputStoriesBean implements Parcelable{
    private String title;
    private String id;
    private boolean multipic;
    private String image;
    private String date;
    private int type;

    public OutputStoriesBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    protected OutputStoriesBean(Parcel in) {
        title = in.readString();
        id = in.readString();
        multipic = in.readByte() != 0;
        image = in.readString();
        date = in.readString();
        type = in.readInt();
    }

    public static final Creator<OutputStoriesBean> CREATOR = new Creator<OutputStoriesBean>() {
        @Override
        public OutputStoriesBean createFromParcel(Parcel in) {
            return new OutputStoriesBean(in);
        }

        @Override
        public OutputStoriesBean[] newArray(int size) {
            return new OutputStoriesBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(id);
        parcel.writeByte((byte) (multipic ? 1 : 0));
        parcel.writeString(image);
        parcel.writeString(date);
        parcel.writeInt(type);
    }
}
