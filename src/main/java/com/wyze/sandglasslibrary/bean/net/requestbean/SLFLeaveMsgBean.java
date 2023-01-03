package com.wyze.sandglasslibrary.bean.net.requestbean;

/**
 * Created by wangjian on 2022/12/21
 */
public class SLFLeaveMsgBean {
    public String path;
    public String fileName;
    public String contentType;
    public String thumbnailPath;
    public String thumbnailContentType;

    public SLFLeaveMsgBean (String path, String fileName, String contentType, String thumbnailPath, String thumbnailContentType) {
        this.path = path;
        this.fileName = fileName;
        this.contentType = contentType;
        this.thumbnailPath = thumbnailPath;
        this.thumbnailContentType = thumbnailContentType;
    }

    public SLFLeaveMsgBean(){}

    @Override
    public String toString ( ) {
        return "HistiryBean{" +
                "path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", thumbnailContentType='" + thumbnailContentType + '\'' +
                '}';
    }

    public void setPath (String path) {
        this.path = path;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public void setContentType (String contentType) {
        this.contentType = contentType;
    }

    public void setThumbnailPath (String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setThumbnailContentType (String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getPath ( ) {
        return path;
    }

    public String getFileName ( ) {
        return fileName;
    }

    public String getContentType ( ) {
        return contentType;
    }

    public String getThumbnailPath ( ) {
        return thumbnailPath;
    }

    public String getThumbnailContentType ( ) {
        return thumbnailContentType;
    }
}
