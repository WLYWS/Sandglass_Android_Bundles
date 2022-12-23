package com.wyze.sandglasslibrary.utils.logutil;

import java.io.Serializable;

/**
 * Greated by yangjie
 * describe:log数据
 * time:2022/12/10
 */
public class SLFDebugData implements Serializable {
    public static final int LEVEL_PLUGIN = 1;   //第一层:插件
    public static final int LEVEL_CATEGORY = 2; //第二层:分类
    public static final int LEVEL_FILE = 3;     //第三层:文件

    private int level;//当前目录或文件的层级
    private String name;//用于显示的目录或文件名称
    private String path;//当前目录或文件的绝对路径
    private boolean isFile;//是否文件
    private boolean isDirectory;//是否目录
    private String pluginId;//传入的插件id
    private String pluginName;//传入的插件名称, 没有则是插件id
    private String category;//传入的自定义类型

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "WpkDebugData{" +
                "level=" + level +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isFile=" + isFile +
                ", isDirectory=" + isDirectory +
                ", pluginId='" + pluginId + '\'' +
                ", pluginName='" + pluginName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
