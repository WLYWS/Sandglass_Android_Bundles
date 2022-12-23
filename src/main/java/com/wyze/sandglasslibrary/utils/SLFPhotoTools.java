package com.wyze.sandglasslibrary.utils;

import android.content.Context;

import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.moudle.SLFPhotoFolderInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SLFPhotoTools {

    /**
     * 获取所有图片
     * @param context
     * @return
     */
    public static List<SLFPhotoFolderInfo> getAllPhotoFolder(Context context) {

        final ArrayList<SLFPhotoFolderInfo> allPhotoFolderList = new ArrayList<SLFPhotoFolderInfo>();

        //所有图片
        SLFPhotoFolderInfo allPhotoFolderInfo = new SLFPhotoFolderInfo();
        SLFPhotoFolderInfo allVideoFolderInfo = new SLFPhotoFolderInfo();

        if(SLFPhotoSelectorUtils.selectType!=null){
            switch (SLFPhotoSelectorUtils.selectType){
                case All:
                    allPhotoFolderInfo.setFolderId("0");
                    allPhotoFolderInfo.setFolderName("all pictures");
                    allPhotoFolderInfo.setPhotoList(new ArrayList<SLFMediaData>());
                    allPhotoFolderList.add(0, allPhotoFolderInfo);

                    allPhotoFolderInfo.setFolderId("1");
                    allVideoFolderInfo.setFolderName("all videos");
                    allVideoFolderInfo.setPhotoList(new ArrayList<SLFMediaData>());
                    allPhotoFolderList.add(1, allVideoFolderInfo);

                    getPhoto(context, allPhotoFolderList, allPhotoFolderInfo);

                    allVideoFolderInfo.setPhotoList(SLFMediaUtil.getMediaWithVideoList(context));
                    if(allVideoFolderInfo.getPhotoList().size()>0){
                        allVideoFolderInfo.setCoverPhoto(allVideoFolderInfo.getPhotoList().get(0));
                    }

                    allPhotoFolderInfo.getPhotoList().addAll(allVideoFolderInfo.getPhotoList());

                    Collections.sort(allPhotoFolderInfo.getPhotoList(), new Comparator<SLFMediaData>() {
                        @Override
                        public int compare(SLFMediaData temp1, SLFMediaData temp2) {
                            return (temp2.getModifiedDate()+"").compareTo(temp1.getModifiedDate()+"");
                        }
                    });

                    break;
                case Image:
                    allPhotoFolderInfo.setFolderId("0");
                    allPhotoFolderInfo.setFolderName("all pictures");
                    allPhotoFolderInfo.setPhotoList(new ArrayList<SLFMediaData>());
                    allPhotoFolderList.add(0, allPhotoFolderInfo);

                    getPhoto(context, allPhotoFolderList, allPhotoFolderInfo);
                    break;
                case Video:
                    allPhotoFolderInfo.setFolderId("1");
                    allVideoFolderInfo.setFolderName("all videos");
                    allVideoFolderInfo.setPhotoList(new ArrayList<SLFMediaData>());
                    allPhotoFolderList.add(0, allVideoFolderInfo);

                    allVideoFolderInfo.setPhotoList(SLFMediaUtil.getMediaWithVideoList(context));
                    if(allVideoFolderInfo.getPhotoList().size()>0){
                        allVideoFolderInfo.setCoverPhoto(allVideoFolderInfo.getPhotoList().get(0));
                    }

                    break;
            }
        }


        return allPhotoFolderList;
    }

    private static void getPhoto(Context context, ArrayList<SLFPhotoFolderInfo> allPhotoFolderList, SLFPhotoFolderInfo allPhotoFolderInfo) {

        HashMap<String, SLFPhotoFolderInfo> bucketMap = new HashMap<String, SLFPhotoFolderInfo>();

        allPhotoFolderInfo.setPhotoList(SLFMediaUtil.getMediaWithImageList(context));
        if(allPhotoFolderInfo.getPhotoList()!=null && allPhotoFolderInfo.getPhotoList().size()>0){
            allPhotoFolderInfo.setCoverPhoto(allPhotoFolderInfo.getPhotoList().get(0));

            //通过bucketId获取文件夹
            for (int i = 0; i < allPhotoFolderInfo.getPhotoList().size(); i++) {
                SLFMediaData mediaData = allPhotoFolderInfo.getPhotoList().get(i);
                if(mediaData == null){
                    continue;
                }

                String bucketId = mediaData.getBucketId();
                String bucketName = mediaData.getBucketDisplayName();
                SLFPhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);
                if (photoFolderInfo == null) {
                    photoFolderInfo = new SLFPhotoFolderInfo();
                    photoFolderInfo.setPhotoList(new ArrayList<SLFMediaData>());
                    photoFolderInfo.setFolderId(bucketId);
                    photoFolderInfo.setFolderName(bucketName);
                    photoFolderInfo.setCoverPhoto(mediaData);
                    bucketMap.put(bucketId, photoFolderInfo);
                    allPhotoFolderList.add(photoFolderInfo);
                }
                photoFolderInfo.getPhotoList().add(mediaData);
            }
        }
    }
}
