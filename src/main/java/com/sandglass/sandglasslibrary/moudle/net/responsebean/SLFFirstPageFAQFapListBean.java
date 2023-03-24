package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Greated by yangjie
 * describe:首页FAQ分类三级菜单数据bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQFapListBean implements Parcelable {
    /**id*/
    private long id;
    /**title*/
    private String title;


    public SLFFirstPageFAQFapListBean(long id,String title){
        this.id = id;
        this.title = title;
    }

    protected SLFFirstPageFAQFapListBean (Parcel in) {
        id = in.readLong();
        title = in.readString();
    }

    public static final Creator <SLFFirstPageFAQFapListBean> CREATOR = new Creator <SLFFirstPageFAQFapListBean>() {
        @Override
        public SLFFirstPageFAQFapListBean createFromParcel (Parcel in) {
            return new SLFFirstPageFAQFapListBean(in);
        }

        @Override
        public SLFFirstPageFAQFapListBean[] newArray (int size) {
            return new SLFFirstPageFAQFapListBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQFapListBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents ( ) {
        return 0;
    }

    @Override
    public void writeToParcel (@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
    }
}
