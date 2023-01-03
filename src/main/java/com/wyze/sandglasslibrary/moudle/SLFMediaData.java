package com.wyze.sandglasslibrary.moudle;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.File;
/**
 * created by yangjie
 * describe:Media type moudle
 * time: 2022/12/5
 */
public class SLFMediaData implements Parcelable {


    public static final Creator<SLFMediaData> CREATOR = new Creator<SLFMediaData>() {
        @Override
        public SLFMediaData createFromParcel(Parcel in) {
            return new SLFMediaData(in);
        }

        @Override
        public SLFMediaData[] newArray(int size) {
            return new SLFMediaData[size];
        }
    };
    /**图片ID*/
    private long id;
    private String title;
    /**图片，视频源地址*/
    private String originalPath;
    /**图片、视频创建时间*/
    private long createDate;
    /**图片、视频最后修改时间*/
    private long modifiedDate;
    /**媒体类型*/
    private String mimeType;
    /**宽*/
    private int width;
    /**高*/
    private int height;
    /**纬度*/
    private double latitude;
    /**经度*/
    private double longitude;
    /**图片方向*/
    private int orientation;
    /**文件大小*/
    private long length;
    /**视频的时长*/
    private long duration;
    /**文件夹相关*/
    private String bucketId;
    private String bucketDisplayName;
    /**大缩略图*/
    private String thumbnailBigPath;
    /**小缩略图*/
    private String thumbnailSmallPath;
    /**上传图片的path*/
    private String uploadPath;
    /**上传缩略图的path*/
    private String uploadThumPath;
    /**上传压缩图地址*/
    private String uploadUrl;
    /**上传缩略图地址*/
    private String uploadThumurl;
    /**是否空闲*/
    private String uploadStatus;
    /**文件名*/
    private String fileName;

    public SLFMediaData() {
    }

    SLFMediaData(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalPath = in.readString();
        createDate = in.readLong();
        modifiedDate = in.readLong();
        mimeType = in.readString();
        bucketId = in.readString();
        bucketDisplayName = in.readString();
        thumbnailBigPath = in.readString();
        thumbnailSmallPath = in.readString();
        width = in.readInt();
        height = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        orientation = in.readInt();
        length = in.readLong();
        duration = in.readLong();
        uploadPath = in.readString();
        uploadThumPath = in.readString();
        uploadUrl = in.readString();
        uploadThumurl = in.readString();
        uploadStatus = in.readString();
        fileName = in.readString();
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getUploadThumurl() {
        return uploadThumurl;
    }

    public void setUploadThumurl(String uploadThumurl) {
        this.uploadThumurl = uploadThumurl;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getUploadThumPath() {
        return uploadThumPath;
    }

    public void setUploadThumPath(String uploadThumPath) {
        this.uploadThumPath = uploadThumPath;
    }

    public String getUploadStatus() {
        if(uploadStatus==null){
            uploadStatus = "";
        }
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalPath);
        dest.writeLong(createDate);
        dest.writeLong(modifiedDate);
        dest.writeString(mimeType);
        dest.writeString(bucketId);
        dest.writeString(bucketDisplayName);
        dest.writeString(thumbnailBigPath);
        dest.writeString(thumbnailSmallPath);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(orientation);
        dest.writeLong(length);
        dest.writeLong(duration);
        dest.writeString(uploadPath);
        dest.writeString(uploadThumPath);
        dest.writeString(uploadUrl);
        dest.writeString(uploadThumurl);
        dest.writeString(uploadStatus);
        dest.writeString(fileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getThumbnailBigPath() {
        if(!TextUtils.isEmpty(thumbnailBigPath)){
            if (new File(thumbnailBigPath).exists()) {
                return thumbnailBigPath;
            }
        }

        return "";
    }

    public void setThumbnailBigPath(String thumbnailBigPath) {
        this.thumbnailBigPath = thumbnailBigPath;
    }

    public String getThumbnailSmallPath() {
        if(!TextUtils.isEmpty(thumbnailSmallPath)){
            if (new File(thumbnailSmallPath).exists()) {
                return thumbnailSmallPath;
            }
        }

        return "";
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SLFMediaData)) {
            return false;
        }

        SLFMediaData bean = (SLFMediaData) obj;
        return bean.getId() == getId();

    }

    @Override
    public String toString() {
        return "SLFMediaData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalPath='" + originalPath + '\'' +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", mimeType='" + mimeType + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", orientation=" + orientation +
                ", length=" + length +
                ", bucketId='" + bucketId + '\'' +
                ", bucketDisplayName='" + bucketDisplayName + '\'' +
                ", thumbnailBigPath='" + thumbnailBigPath + '\'' +
                ", thumbnailSmallPath='" + thumbnailSmallPath + '\'' +
                '}';
    }

}
