
package com.dragon.android.spirit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ImageSenderInfo implements Parcelable {

    @SerializedName("sender_name")
    private String sender;
    @SerializedName("sender_age")
    private int age;

    public ImageSenderInfo() {
    }

    public ImageSenderInfo(String sender, int age) {
        this.sender = sender;
        this.age = age;
    }

    public final static Creator<ImageSenderInfo> CREATOR = new Creator<ImageSenderInfo>() {

        @SuppressWarnings({
                "unchecked"
        })
        @Override
        public ImageSenderInfo createFromParcel(Parcel in) {
            ImageSenderInfo instance = new ImageSenderInfo();
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.age = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        @Override
        public ImageSenderInfo[] newArray(int size) {
            return (new ImageSenderInfo[size]);
        }

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sender);
        dest.writeValue(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
