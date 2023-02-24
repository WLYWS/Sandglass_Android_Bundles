package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:首页FAQ分类bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQBean {
    /**id*/
    private long id;
    /**设备名称*/
    private String name;
    /**图标地址**/
    private String iconUrl;
    /**二级菜单列表*/
    private List<SLFFirstPageFAQProblemBean> sub;
    /**是否被选中*/
    private boolean isChecked;

    public SLFFirstPageFAQBean(long id, String name,String iconUrl, List<SLFFirstPageFAQProblemBean> sub,boolean isChecked){
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.sub = sub;
        this.isChecked = isChecked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SLFFirstPageFAQProblemBean> getSub() {
        return sub;
    }

    public void setSub(List<SLFFirstPageFAQProblemBean> sub) {
        this.sub = sub;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", sub='" + sub + '\'' +
                ", isChecked='" + isChecked + '\'' +
                '}';
    }
}
