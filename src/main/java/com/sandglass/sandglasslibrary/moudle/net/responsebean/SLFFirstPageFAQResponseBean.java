package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by yangjie on 2023/2/20
 */
public class SLFFirstPageFAQResponseBean implements Parcelable {
    public List <SLFFirstPageFAQBean> data;
    public int code;
    public String message;

    public SLFFirstPageFAQResponseBean(){}
    protected SLFFirstPageFAQResponseBean (Parcel in) {
        data = in.createTypedArrayList(SLFFirstPageFAQBean.CREATOR);
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator <SLFFirstPageFAQResponseBean> CREATOR = new Creator <SLFFirstPageFAQResponseBean>() {
        @Override
        public SLFFirstPageFAQResponseBean createFromParcel (Parcel in) {
            return new SLFFirstPageFAQResponseBean(in);
        }

        @Override
        public SLFFirstPageFAQResponseBean[] newArray (int size) {
            return new SLFFirstPageFAQResponseBean[size];
        }
    };

    @Override
    public int describeContents ( ) {
        return 0;
    }

    @Override
    public void writeToParcel (@NonNull Parcel parcel, int i) {
        parcel.writeTypedList(data);
        parcel.writeInt(code);
        parcel.writeString(message);
    }
}
