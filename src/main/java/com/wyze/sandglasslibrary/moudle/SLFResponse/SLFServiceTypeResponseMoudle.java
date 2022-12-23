package com.wyze.sandglasslibrary.moudle.SLFResponse;

import com.wyze.sandglasslibrary.moudle.SLFServiceTilteMoudle;
import com.wyze.sandglasslibrary.moudle.SLFServiceType;

import java.util.List;
/**
 * Greated by yangjie
 * describe:serviceType返回的json数据
 * time:2022/12/20
 * 返回json基础对象 当data返回是array时用这个
 */
public class SLFServiceTypeResponseMoudle extends SLFBaseResponseMoudle{

    public List<SLFServiceTilteMoudle> data;
}
