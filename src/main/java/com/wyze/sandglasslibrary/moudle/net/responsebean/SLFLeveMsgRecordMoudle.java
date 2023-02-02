package com.wyze.sandglasslibrary.moudle.net.responsebean;
/**
 * Greated by yangjie
 * describe:反馈留言历史多媒体moudle
 * time:2023/1/31
 */
public class SLFLeveMsgRecordMoudle {
    /**文件名 waa.png*/
    private String fileName;
    /**文件类型 image/png*/
    private String contentType;
    /**文件路径*/
    private String url;
    /**缩略图文件路径*/
    private String thumbnailUrl;
    /**缩略图文件类型*/
    private String thumbnailContentType;

    public SLFLeveMsgRecordMoudle(String fileName,String contentType,String url,String thumbnailUrl,String thumbnailContentType){
        this.fileName = fileName;
        this.contentType = contentType;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    @Override
    public String toString ( ) {
        return "SLFLeveMsgRecordMoudle{" +
                "fileName=" + fileName +
                ", contentType='" + contentType + '\'' +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thumbnailContentType='" + thumbnailContentType + '\'' +
                '}';
    }
}
