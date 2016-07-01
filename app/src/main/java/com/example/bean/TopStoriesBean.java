package com.example.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TopStoriesBean implements Parcelable {
    private String title;
    private String id;
    private boolean multipic;
    private String image;
    private String date;
    private int type;

    public TopStoriesBean() {
    }

    protected TopStoriesBean(Parcel in) {
        title = in.readString();
        id = in.readString();
        multipic = in.readByte() != 0;
        image = in.readString();
        date = in.readString();
        type = in.readInt();
    }

    public static final Creator<TopStoriesBean> CREATOR = new Creator<TopStoriesBean>() {
        @Override
        public TopStoriesBean createFromParcel(Parcel in) {
            return new TopStoriesBean(in);
        }

        @Override
        public TopStoriesBean[] newArray(int size) {
            return new TopStoriesBean[size];
        }
    };


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopStoriesBean that = (TopStoriesBean) o;

        if (multipic != that.multipic) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (multipic ? 1 : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

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
