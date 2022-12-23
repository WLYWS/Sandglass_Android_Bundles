package com.wyze.sandglasslibrary.base;
/**
 * created by yangjie
 * describe:文件数据
 * time: 2022/12/21
 * @author yangjie
 */
public class SLFFileData {
    private String file_path;
    private String signed_url;
    private String type;         // 文件分类：1 app_log 2 device_log 3 image 4 video
    private String content_type; //image/png; image/jpeg; video/mpeg4
    private String file_name;
    private long size;           //文件大小
    private long duration;       // 视频时长

    @Override
    public String toString() {
        return "FileData{" +
                "file_path='" + file_path + '\'' +
                ", signed_url='" + signed_url + '\'' +
                ", type='" + type + '\'' +
                ", content_type='" + content_type + '\'' +
                ", file_name='" + file_name + '\'' +
                '}';
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getSigned_url() {
        return signed_url;
    }

    public void setSigned_url(String signed_url) {
        this.signed_url = signed_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
