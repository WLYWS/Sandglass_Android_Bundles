package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:首页FAQ分类三级菜单数据bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQFapListBean {
    /**id*/
    private long id;
    /**title*/
    private String title;


    public SLFFirstPageFAQFapListBean(long id,String title){
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQFapListBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
