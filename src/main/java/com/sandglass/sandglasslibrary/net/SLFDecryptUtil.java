package com.sandglass.sandglasslibrary.net;

import android.util.Base64;

import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wangjian on 2022/12/21
 */
public class SLFDecryptUtil {


    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private static final String CIP_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String DecryAes(String base64Data,String secret){
        try {
            byte[] data = Base64.decode(base64Data,Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CIP_ALGORITHM);
            //设置为解密模式
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //执行解密操作
            byte[] result = cipher.doFinal(data);
            SLFLogUtil.e("SLFDecryptUtil","解密 成功 ="+new String(result, CHARSET_UTF8));
            return new String(result, CHARSET_UTF8);
        } catch (Exception e) {
            SLFLogUtil.e("SLFDecryptUtil","解密 e ="+e.getMessage());
        }
        return null;
    }
}
