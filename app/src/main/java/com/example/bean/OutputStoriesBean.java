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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutputStoriesBean bean = (OutputStoriesBean) o;

        if (multipic != bean.multipic) return false;
        if (type != bean.type) return false;
        if (title != null ? !title.equals(bean.title) : bean.title != null) return false;
        if (id != null ? !id.equals(bean.id) : bean.id != null) return false;
        if (image != null ? !image.equals(bean.image) : bean.image != null) return false;
        return date != null ? date.equals(bean.date) : bean.date == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (multipic ? 1 : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }
}
