package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Greated by yangjie
 * describe:首页FAQ分类bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQBean implements Parcelable {
    /**id*/
    private long id;
    /**设备名称*/
    private String name;
    /**图标地址**/
    private String iconUrl;
    /**二级菜单列表*/
    private List<SLFFirstPageFAQProblemBean> sub;
    /**是否被选中*/
    private boolean isChecked;

    public SLFFirstPageFAQBean(long id, String name,String iconUrl, List<SLFFirstPageFAQProblemBean> sub,boolean isChecked){
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.sub = sub;
        this.isChecked = isChecked;
    }

    protected SLFFirstPageFAQBean (Parcel in) {
        id = in.readLong();
        name = in.readString();
        iconUrl = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator <SLFFirstPageFAQBean> CREATOR = new Creator <SLFFirstPageFAQBean>() {
        @Override
        public SLFFirstPageFAQBean createFromParcel (Parcel in) {
            return new SLFFirstPageFAQBean(in);
        }

        @Override
        public SLFFirstPageFAQBean[] newArray (int size) {
            return new SLFFirstPageFAQBean[size];
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

    public List<SLFFirstPageFAQProblemBean> getSub() {
        return sub;
    }

    public void setSub(List<SLFFirstPageFAQProblemBean> sub) {
        this.sub = sub;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", sub='" + sub + '\'' +
                ", isChecked='" + isChecked + '\'' +
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
        parcel.writeString(iconUrl);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
