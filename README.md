# Android 反馈SDK接⼊⽂档

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
1. 可获取常⻅的热⻔问题及解答

2. 可查询⾃定义问题解答

### 反馈列表：
1. 可查看已提交反馈的进度状态
2. 可在详情页针对该反馈继续与客服问答

## 系统版本要求
Android 8.0及以上

## JDK版本
JDK11


## 导入
SLFeedback 通过aar方式导入

在app的build.gradle文件中添加如下配置

```ruby
在dependencies里添加：

android {
      .........
}

dependencies {
    .........
    implementation project(':SLFFeedback')
	注：这里目前是以aar方式接入，后期会以maven仓库方式接入，如下方式：
	例如： implementation 'com.xxx.xxxx.sandlglass:1.0.0'
}

end

```

## 工程配置

### 权限配置
所需权限均在sdk里已配置，如下权限：
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission
        android:name="android.permission.READ_PRIVILEGED_PHONE_STATE"
        tools:ignore="ProtectedPermissions" />
<uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="29" />
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<!-- 照相的权限 -->
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
```
## SDK使用说明

### 初始化SDK
在APP的Application中初始化SDK
```
在App的Application的onCreate方法里添加：

public class MyApplication extends Application {

    /**
     * params:this 上下文环境
     * params:true isDebug(是否开启debug模式，true为开启，false为关闭，若为false则控制台不显示插件log)
     */
    @Override
    public void onCreate() {
        super.onCreate();
        SLFApi.getInstance(this).init(true);
    }
}
```
### 自定义SDK字体样式
SDK提供了修改粗,中,细三种字体样式的接口,接入方可通过配置对应的字体名称来修改对应字体的样式. 注:字体样式为全局样式,只需要配置一次即可
```
//细
    public static String SLF_RegularFont = "fonts/Rany.otf";
    //中
    public static String SLF_MediumFont = "fonts/Rany-Medium.otf";
    //粗
    public static String SLF_BoldFont = "fonts/Rany-Bold.otf";
	
	例如：
	SLFFontSet.SLF_MediumFont = "自定义字体库路径"
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
	/**
     * 
     * @param appLogUrl  APPlog上传路径
     * @param firmwareLogUrl  固件上传路径
     */
public class MainActivity extends AppCompatActivity implements SLFUploadAppLogCallback {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		//在onCreate方法里设置当前回调
        SLFApi.getInstance(MainActivity.this,token).setAppLogCallBack(this);
        }
	}
					......//do something
@Override
    public void getUploadAppLogUrl(String appLogUrl, String firmwareLogUrl) {
	
          //TODO 根据拿到的appLogUrl和firmwareLogUrl上传log
             .............
			 
			//上传成功回调如下方法： 
		  	/**
                 * @param isComplete  true上传成功，false上传失败
                 * @param appFileName  压缩上传的applog的文件名称，一般是.zip
                 * @param firmwarFilName 压缩上传的固件log的文件名称，一般是.zip
                 */
				 SLFApi.getInstance(MainActivity.this).getUploadLogCompleteCallBack().isUploadComplete(true,"appLog.zip","firmwareLog.zip");
        
    }
}
```
### 进入SDK路由
```
点击进入插件，调用此方法：
/**
   * @param context 上下文环境
   * @param context 上下文环境
   * @param String    token
   */
 textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				//进入插件，将token传进来
               SLFApi.getInstance(MainActivity.this).gotoHelpAndFeedback(MainActivity.this,token);


            }
        });
```

## 演示工程
[演示工程](https://github.com/WLYWS/Sandglass_Project_android)

## Author


emphasized text
“yangjie”, yfijwan431@163.com



## License



SLFeedback is available under the MIT license. See the LICENSE file for more info.strong text
