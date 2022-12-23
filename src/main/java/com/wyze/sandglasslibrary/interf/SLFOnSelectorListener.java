package com.wyze.sandglasslibrary.interf;

import com.wyze.sandglasslibrary.moudle.SLFMediaData;

import java.util.List;

/**
 * created by yangjie
 * describe:选择相册监听接口
 * time: 2022/12/6
 * @author yangjie
 */
public interface SLFOnSelectorListener {
    void onSelect(List<SLFMediaData> selectMediaList);
}
