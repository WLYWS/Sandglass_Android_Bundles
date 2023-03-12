package com.sandglass.sandglasslibrary.bean;

/**
 * Greated by yangjie
 * describe:http code
 * time:2023/3/12
 */
public class SLFHttpStatusCode {
    /**成功*/
    public static final String SUCCESS_CODE = "0";
    /**openai敏感词校验失败*/
    public static final String OPAI_FAIL_CODE = "1001";
    /**需要重新登录、或授权*/
    public static final String TOKEN_FAILED = "401";
    /**加解密异常*/
    public static final String DECODE_FAILED = "403";
    /**服务端有代码bug**/
    public static final String SERVICE_FAILED = "500";
    /**无网发送失败**/
    public static final String NO_NETWORK_FAILED = "1002";
    /**发送失败*/
    public static final String REQUEST_FAILED = "1003";
}
