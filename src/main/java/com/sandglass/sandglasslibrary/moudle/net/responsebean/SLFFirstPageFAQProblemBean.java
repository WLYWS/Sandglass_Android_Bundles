package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Greated by yangjie
 * describe:首页FAQ分类二级菜单数据bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQProblemBean implements Parcelable {
    /**id*/
    private long id;
    /**设备名称*/
    private String name;
    /**三级菜单列表*/
    private List<SLFFirstPageFAQFapListBean> faqList;
    /**是否展开*/
    private boolean isExtend;

    public SLFFirstPageFAQProblemBean(long id, String name, List<SLFFirstPageFAQFapListBean> faqList,boolean isExtend){
        this.id = id;
        this.name = name;
        this.faqList = faqList;
        this.isExtend = isExtend;
    }

    protected SLFFirstPageFAQProblemBean (Parcel in) {
        id = in.readLong();
        name = in.readString();
        isExtend = in.readByte() != 0;
    }

    public static final Creator <SLFFirstPageFAQProblemBean> CREATOR = new Creator <SLFFirstPageFAQProblemBean>() {
        @Override
        public SLFFirstPageFAQProblemBean createFromParcel (Parcel in) {
            return new SLFFirstPageFAQProblemBean(in);
        }

        @Override
        public SLFFirstPageFAQProblemBean[] newArray (int size) {
            return new SLFFirstPageFAQProblemBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SLFFirstPageFAQFapListBean> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<SLFFirstPageFAQFapListBean> faqList) {
        this.faqList = faqList;
    }

    public boolean isExtend() {
        return isExtend;
    }

    public void setExtend(boolean extend) {
        isExtend = extend;
    }


    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQProblemBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faqList='" + faqList + '\'' +
                '}';
    }

    @Override
    public int describeContents ( ) {
        return 0;
    }

    @Override
    public void writeToParcel (@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isExtend ? 1 : 0));
    }
}
