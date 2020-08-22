package com.youedata.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class AESUtils {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "utf-8";
    /**
     * 建议为16位或32位
     */
    private static final String KEY = "youedata12345678";
    /**
     * 必须16位
     * 初始化向量IV不可以为32位，否则异常java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 16 bytes long
     */
    private static final String IV = "youedata12345678";

    /**
     * 加密
     */
    public static String encrypt(String context) {
        try {
            byte[] decode = context.getBytes(CHARSET);
            byte[] bytes = createKeyAndIv(decode, Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    public static String decrypt(String context) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(context);
            byte[] bytes = createKeyAndIv(decode, Cipher.DECRYPT_MODE);
            return new String(bytes, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取key & iv
     */
    private static byte[] createKeyAndIv(byte[] context, int opmode) throws Exception {
        byte[] key = KEY.getBytes(CHARSET);
        byte[] iv = IV.getBytes(CHARSET);
        return cipherFilter(context, opmode, key, iv);
    }

    /**
     * 执行操作
     */
    private static byte[] cipherFilter(byte[] context, int opmode, byte[] key, byte[] iv) throws Exception {
        Key secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(opmode, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(context);
    }

    /**
     * 主方法测试
     */
    public static void main(String[] args) throws Exception{
        String context = "account";
        System.out.println("元数据" + context);
        String encrypt = encrypt(context);
        System.out.println("加密之后：" + encrypt);
//        String decrypt = decrypt(URLDecoder.decode("5Dnkd5vCD6EWgbXCreIgUQ==", "GBK"));
        String decrypt = decrypt("jsLbUdbJZ6vbXmMO6aCUuQ==");
        System.out.println("解密之后：" + decrypt);
        System.out.println(Encrypt.sha("7c4a8d09ca3762af61e59520943dc26494f8941b"));

        System.out.println(URLDecoder.decode("5Dnkd5vCD6EWgbXCreIgUQ==", "GBK"));

    }

}

