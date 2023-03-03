# Sandglass_Android
客服反馈组件Android端
# SLFeedback

## 功能介绍

### 收集/上传APP Log和Crash 日志

1. SDK为接入方提供写入log的功能，在提交反馈后log文件会自动上传至反馈平台以供查看

2. SDK为接入方提供自动收集Crash日志的功能，在提交反馈后Crash日志会自动上传至反馈平台以供查看

### 提交反馈：
1. 可选择反馈对应的问题类型

2. 可填写反馈文字描述

3. 可上传反馈图片/视频描述

4. 可上传APP Log和Device Log

### 智能助手：
1. 可获取常见的热门问题及解答

2. 可查询自定义问题解答

3. 可查询相关问题及解答

### 反馈列表：
1. 可查看已提交反馈的进度状态
2. 可在详情页针对该反馈继续与客服问答

## 系统版本要求
Android 26及以上

## 语言版本
Java11


## 导入
SLFeedback 通过aar方式导入

在app的build.gradle文件中添加如下配置

```ruby
在dependencies里添加：

implementation project(':SLFFeedback')

后期maven仓库建立，这里将直接引入maven仓库地址，目前以aar方式添加

end

```

## 工程配置

### 权限配置
所需权限均在sdk里已配置

## SDK使用说明

### 初始化SDK
在APP的Application中初始化SDK
```
在App的Application的onCreate方法里添加：

SLFApi.getInstance(this).init(true);

参数说明
：
1.this是context上下文

2.true 是指isDebug模式，false为关闭，关闭情况下不显示插件log，true会显示插件log
```
### 写入Log
SDK提供了写入Log的方法
```
//param1：TAG
//param2：Log内容
SLFLogUtil.d(TAG,"slf_oncreate");
SLFLogUtil.e(TAG,"slf_oncreate");
SLFLogUtil.i(TAG,"slf_oncreate");
SLFLogUtil.w(TAG,"slf_oncreate");
SLFLogUtil.json(String TAG,String json);//打印json
SLFLogUtil.xml(String TAG,String xml);//打印xml
```
### 上传APP Log 和 Device Log
SDK将上传APP Log 和 Device log的权限开放给接入方，接入方可根据自身情况选择使用SDK提供的Log功能或自有的Log功能。可以在所在的Activity implements SLFUploadAppLogCallback：
实现：getUploadAppLogUrl方法。
```
例如：
public class MainActivity extends AppCompatActivity implements SLFUploadAppLogCallback {
......
@Override
    public void getUploadAppLogUrl(String appLogUrl, String firmwareLogUrl) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SLFApi.getInstance(MainActivity.this).getUploadLogCompleteCallBack().isUploadComplete(true,"appLog.zip","firmwareLog.zip");
            }
        }, 5000);  //延迟5s// 秒执行
    }
}

//getUploadAppLogUrl(String appLogUrl, String firmwareLogUrl)
参数说明：
1.appLogUrl    appLog上传url获取
2.firmwareLogUrl   固件log上传url获取

调用此回调通知插件上传完成：
SLFApi.getInstance(MainActivity.this).getUploadLogCompleteCallBack().isUploadComplete(true,"appLog.zip","firmwareLog.zip");

```
### 进入SDK路由
```
SLFApi.getInstance(MainActivity.this).gotoHelpAndFeedback(MainActivity.this);
```

## 演示工程
[演示工程](https://github.com/WLYWS/Sandglass_Project_android)

## Author


emphasized text
“yangjie”, yfijwan431@163.com



## License



SLFeedback is available under the MIT license. See the LICENSE file for more info.strong text
