package com.youedata.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 常用加密算法工具类
 */
@SuppressWarnings("restriction")
public class Encrypt {

    /**
     * 用MD5算法进行加密
     *
     * @param str
     *            需要加密的字符串
     * @return MD5加密后的结果
     */
    public static String md5(String str) {
        return encode(str, "MD5");
    }

    /**
     * 用SHA算法进行加密
     *
     * @param str
     *            需要加密的字符串
     * @return SHA加密后的结果
     */
    public static String sha(String str) {
        return encode(str, "SHA");
    }

    public static String md5_sha(String str){
        return md5(sha(str));
    }

    /**
     * 用base64算法进行加密
     *
     * @param str
     *            需要加密的字符串
     * @return base64加密后的结果
     */
    public static String base64(String str) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(str.getBytes());
    }

    /**
     * 用base64算法进行解密
     *
     * @param str
     *            需要解密的字符串
     * @return base64解密后的结果
     * @throws IOException
     */
    public static String decodeBase64(String str) throws IOException {
        BASE64Decoder encoder = new BASE64Decoder();
        return new String(encoder.decodeBuffer(str));
    }

    private static String encode(String str, String method) {
        MessageDigest md = null;
        String dstr = null;
        try {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dstr;
    }

}

