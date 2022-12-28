package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import com.wyze.sandglasslibrary.moudle.SLFMediaData;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SLFMediaUtil {
    public static List<SLFMediaData> getMediaWithImageList(Context context) {
        return getMediaWithImageList(context, String.valueOf(Integer.MIN_VALUE));
    }

    /**
     * 从数据库中读取图片
     */
    public static List<SLFMediaData> getMediaWithImageList(Context context, String bucketId) {
        List<SLFMediaData> mediaDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        projection.add(MediaStore.Images.Thumbnails.DATA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.equals(bucketId, String.valueOf(Integer.MIN_VALUE))) {
            selection = MediaStore.Images.Media.BUCKET_ID + "=?";
            selectionArgs = new String[]{bucketId};
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), selection,
                selectionArgs, MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    SLFMediaData mediaData = parseImageCursorAndCreateThumImage(context, cursor);
                    if (mediaData != null) {
                        mediaDataList.add(mediaData);
                    }
                } while (cursor.moveToNext());
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaDataList;
    }

    public static List<SLFMediaData> getMediaWithVideoList(Context context) {
        return getMediaWithVideoList(context, String.valueOf(Integer.MIN_VALUE));
    }

    /**
     * 从数据库中读取视频
     */
    public static List<SLFMediaData> getMediaWithVideoList(Context context, String bucketId) {
        List<SLFMediaData> mediaDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Video.Media._ID);
        projection.add(MediaStore.Video.Media.TITLE);
        projection.add(MediaStore.Video.Media.DATA);
        projection.add(MediaStore.Video.Media.BUCKET_ID);
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Video.Media.MIME_TYPE);
        projection.add(MediaStore.Video.Media.DATE_ADDED);
        projection.add(MediaStore.Video.Media.DATE_MODIFIED);
        projection.add(MediaStore.Video.Media.LATITUDE);
        projection.add(MediaStore.Video.Media.LONGITUDE);
        projection.add(MediaStore.Video.Media.SIZE);
        projection.add(MediaStore.Video.Media.DURATION);
        projection.add(MediaStore.Video.Thumbnails.DATA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Video.Media.WIDTH);
            projection.add(MediaStore.Video.Media.HEIGHT);
        }
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.equals(bucketId, String.valueOf(Integer.MIN_VALUE))) {
            selection = MediaStore.Video.Media.BUCKET_ID + "=?";
            selectionArgs = new String[]{bucketId};
        }

        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), selection,
                selectionArgs, MediaStore.Video.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    SLFMediaData mediaData = parseVideoCursorAndCreateThumImage(context, cursor);
                    mediaDataList.add(mediaData);
                } while (cursor.moveToNext());
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaDataList;
    }

    /**
     * 根据原图获取图片相关信息
     */
    public static SLFMediaData getMediaBeanWithImage(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        projection.add(MediaStore.Images.Thumbnails.DATA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath}, null);
        SLFMediaData mediaData = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mediaData = parseImageCursorAndCreateThumImage(context, cursor);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaData;
    }

    /**
     * 根据地址获取视频相关信息
     */
    public static SLFMediaData getMediaBeanWithVideo(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Video.Media._ID);
        projection.add(MediaStore.Video.Media.TITLE);
        projection.add(MediaStore.Video.Media.DATA);
        projection.add(MediaStore.Video.Media.BUCKET_ID);
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Video.Media.MIME_TYPE);
        projection.add(MediaStore.Video.Media.DATE_ADDED);
        projection.add(MediaStore.Video.Media.DATE_MODIFIED);
        projection.add(MediaStore.Video.Media.LATITUDE);
        projection.add(MediaStore.Video.Media.LONGITUDE);
        projection.add(MediaStore.Video.Media.SIZE);
        projection.add(MediaStore.Video.Media.DURATION);
        projection.add(MediaStore.Video.Thumbnails.DATA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Video.Media.WIDTH);
            projection.add(MediaStore.Video.Media.HEIGHT);
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection.toArray(new String[projection.size()]),
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath}, null);
        SLFMediaData mediaData = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mediaData = parseVideoCursorAndCreateThumImage(context, cursor);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaData;
    }


    /**
     * 解析图片cursor并且创建缩略图
     * <p>
     * update 17.07.23 log
     * <p>
     * 判断图片 Size ，如果小于等于0则返回 Null，避免出现 No such file or directory
     */
    @SuppressLint("Range")
    @Nullable
    private static SLFMediaData parseImageCursorAndCreateThumImage(Context context, Cursor cursor) {
        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        if (TextUtils.isEmpty(originalPath) || size <= 0 || !new File(originalPath).exists()) {
            return null;
        }

        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        String thumbImg = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

        SLFMediaData mediaData = new SLFMediaData();
        mediaData.setId(id);
        mediaData.setTitle(title);
        mediaData.setOriginalPath(originalPath);
        mediaData.setBucketId(bucketId);
        mediaData.setBucketDisplayName(bucketDisplayName);
        mediaData.setMimeType(mimeType);
        mediaData.setCreateDate(createDate);
        mediaData.setModifiedDate(modifiedDate);
        mediaData.setThumbnailSmallPath(thumbImg);
        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
//                Logger.e(e);
            }
        }
        mediaData.setWidth(width);
        mediaData.setHeight(height);
        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        mediaData.setLatitude(latitude);
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        mediaData.setLongitude(longitude);
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
        mediaData.setOrientation(orientation);
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        mediaData.setLength(length);

        return mediaData;
    }

    /**
     * 解析视频cursor并且创建缩略图
     */
    @SuppressLint("Range")
    private static SLFMediaData parseVideoCursorAndCreateThumImage(Context context, Cursor cursor) {
        SLFMediaData mediaData = new SLFMediaData();
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
        mediaData.setId(id);
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        mediaData.setTitle(title);
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        mediaData.setOriginalPath(originalPath);
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
        mediaData.setBucketId(bucketId);
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
        mediaData.setBucketDisplayName(bucketDisplayName);
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
        mediaData.setMimeType(mimeType);
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
        mediaData.setCreateDate(createDate);
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
        mediaData.setModifiedDate(modifiedDate);
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        mediaData.setLength(length);
        long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
        mediaData.setDuration(duration);
        String thumPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
        mediaData.setThumbnailSmallPath(thumPath);

        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                Log.e("MediaUtil", e.getMessage());
            }
        }
        mediaData.setWidth(width);
        mediaData.setHeight(height);

        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE));
        mediaData.setLatitude(latitude);
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
        mediaData.setLongitude(longitude);
        return mediaData;
    }

    //根据路径得到视频缩略图
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }
    //获取视频总时长
    public static String getVideoDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); //
        return duration;

    }
}
