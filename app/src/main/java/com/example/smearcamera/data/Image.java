package com.example.smearcamera.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    private long id;
    private String imagePath;
    private Uri mediaThumbnail;
    private boolean checked;
    private int selectionNum;
    private long size;
    private int width;
    private int height;
    private String displayName;

    protected Image(Parcel in) {
        imagePath = in.readString();
        mediaThumbnail = in.readParcelable(Uri.class.getClassLoader());
        checked = in.readByte() != 0;
        selectionNum = in.readInt();
        size = in.readLong();
        width = in.readInt();
        height = in.readInt();
        displayName = in.readString();
    }

    public Image(long id,
            String imagePath,
            Uri mediaThumbnail,
            boolean checked,
            int selectionNum,
            long size,
            int width,
            int height,
            String displayName){
        this.id = id;
        this.imagePath = imagePath;
        this.mediaThumbnail = mediaThumbnail;
        this.checked = checked;
        this.selectionNum  = selectionNum;
        this.size = size;
        this.width = width;
        this.height = height;
        this.displayName = displayName;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Uri getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(Uri mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getSelectionNum() {
        return selectionNum;
    }

    public void setSelectionNum(int selectionNum) {
        this.selectionNum = selectionNum;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imagePath='" + imagePath + '\'' +
                ", mediaThumbnail=" + mediaThumbnail +
                ", checked=" + checked +
                ", selectionNum=" + selectionNum +
                ", size=" + size +
                ", width=" + width +
                ", height=" + height +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
        dest.writeParcelable(mediaThumbnail, flags);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeInt(selectionNum);
        dest.writeLong(size);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(displayName);
    }
}
