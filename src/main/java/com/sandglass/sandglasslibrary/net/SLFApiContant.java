package com.sandglass.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFApiContant {
    public static final String CATEGORY_URL="/feedback/categories";//获取问题分类
    public static final String UPLOAD_FILE_URL="/feedback/upload/url";//获取文件上传链接
    public static final String CREATE_FEEDBACK_URL="/feedback";//创建反馈
    public static final String FEEDBACK_LIST_URL="/feedback/list";//反馈列表
    public static final String FEEDBACK_HISTORY_LIST_URL="/feedback/{id}/list";//获取反馈留言历史
    public static final String POST_FEEDBACK_URL ="/feedback/{id}/history";// 提交留言
    public static final String FEEDBACK_LOG_URL ="/feedback/{id}/log";//更新 sendLog，再次上传log
    public static final String FEEDBACK_FAQ_CATEGORIES = "/feedback/faq/categories";//获取首页FAQ分类
    public static final String FEEDBACK_FAQ_HOT = "/feedback/faq/hot";//获取欢迎语+热门问题
    public static final String FEEDBACK_FAQ_SEARCH = "/feedback/faq/search";//搜索FAQ（用户提问问题）
    public static final String FEEDBACK_FAQ_MARK = "/feedback/faq/mark";//标记FAQ是否解决
    public static final String FEEDBACK_UN_READ_COUNT = "/feedback/unread_count";//获取反馈未读数量
    public static final String FEEDBACK_FAQ_DETAIL = "/feedback/faq/";//获取FAQ详情
    public static final String FEEDBACK_OPENAI = "/feedback/openai";//用户提问问题openai
    public static final String FEEDBACK_ILLEGLA_WORD ="/feedback/openai/moderation";//敏感词汇校验(提交反馈)
    public static final String FIRST_PAGE_GET_USERINO = "/feedback/user/info";//deviceinfo email iconUrl
    public static final String USER_DEVICE_LIST = "/feedback/user/device/list";//用户的devicelist
    public static final String FEEDBACK_UPDATE_CACHE = "/feedback/cache";//获取数据更新时间
}
