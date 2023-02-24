package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie
 * describe:FAQ详情页数据模型
 * time:2023/2/23
 */
public class SLFFaqDetailBean {
    /**faq id*/
    private long id;
    /**title*/
    private String title;
    /**content 富文本*/
    private String content;

    public SLFFaqDetailBean(){
    }

    public SLFFaqDetailBean(long id,String title,String content){
        this.id = id;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString ( ) {
        return "SLFFaqDetailBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
