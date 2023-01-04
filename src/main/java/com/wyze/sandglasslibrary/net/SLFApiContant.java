package com.wyze.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFApiContant {
    public static final String CATEGORY_URL="/feedback/categories";//获取问题分类
    public static final String UPLOAD_FILE_URL="/feedback/upload/url";//获取文件上传链接
    public static final String CREATE_FEEDBACK_URL="/feedback";//创建反馈
    public static final String FEEDBACK_LIST_URL="/feedback/list";//反馈列表
    public static final String FEEDBACK_HISTORY_LIST_URL="/feedback/1/list";//获取反馈留言历史
    public static final String POST_FEEDBACK_URL ="/feedback/1/history";// 提交留言
    public static final String FEEDBACK_LOG_URL ="/feedback/{id}/log";//更新 sendLog，再次上传log
    public static final String FEEDBACK_FAQ_CATEGORIES = "/feedback/faq/categories";//获取首页FAQ分类
    public static final String FEEDBACK_FAQ_HOT = "/feedback/faq/hot";//获取欢迎语+热门问题
    public static final String FEEDBACK_FAQ_SEARCH = "/feedback/faq/search";//搜索FAQ（用户提问问题）
    public static final String FEEDBACK_FAQ_MARK = "/feedback/faq/mark";//标记FAQ是否解决
}
