package com.wyze.sandglasslibrary.moudle;

import java.io.Serializable;
import java.util.List;
/**
 * Greated by yangjie
 * Desction:图片文件夹
 * 2022/12/6
 */
public class SLFPhotoFolderInfo implements Serializable {
    private String folderId;
    private String folderName;
    private SLFMediaData coverPhoto;
    private List<SLFMediaData> photoList;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public SLFMediaData getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(SLFMediaData coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<SLFMediaData> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<SLFMediaData> photoList) {
        this.photoList = photoList;
    }
}
