/*
 *   ©2016 ALL Rights Reserved DHX
 *  　　   ┏┓   ┏┓
 *  　　 ┏━┛┻━━━┛┻━┓
 *   　　┃         ┃
 *   　　┃    ━    ┃
 *   　　┃  ┳┛ ┗┳  ┃
 *   　　┃         ┃
 *   　　┃    ┻    ┃
 *   　　┗━┓     ┏━┛
 *         ┃    ┃  Code is far away from bug with the animal protecting
 *         ┃    ┃    神兽保佑,代码无bug
 *         ┃    ┗━━━━━┓
 *         ┃          ┣┓
 *         ┃          ┏┛
 *         ┗┓┓┏━━━━┓┓┏┛
 *          ┃┫┫    ┃┫┫
 *          ┗┻┛    ┗┻┛
 *   ━━━━━━感觉萌萌哒━━━━━━
 *
 */

package com.gl.gl_wechat.common;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

/**
 * AES加解密工具
 * ClassName: AESUtil <br/>
 * Author: zc  <br/>
 * Date: 2020/4/6 21:44 <br/>
 * Version: 1.0 <br/>
 */

public class AESUtil {

    /**
     * 秘钥
     */
    private static final String AESKEY = "funnymudpee";

    /**
     * <p>
     * AES加密
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 11:26
     *
     * @param bytes 待加密字符串
     * @return 加密后字符串
     * @throws Exception
     */
    public static String encrypt(String bytes) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(AESKEY.getBytes("UTF-8"));

        keyGenerator.init(128, secureRandom);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES"));

        byte[] buf = cipher.doFinal(bytes.getBytes("utf-8"));
        return parseByte2HexStr(buf);
    }

    /**
     * <p>
     * AES解密
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 11:28
     *
     * @param encryptBytes 加密字符串
     * @return 解密后字符串
     * @throws Exception
     */
    public static String decrypt(String encryptBytes) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(AESKEY.getBytes("UTF-8"));

        keyGenerator.init(128, secureRandom);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(parseHexStr2Byte(encryptBytes));

        return new String(decryptBytes, "UTF-8");
    }

    /**
     * <p>
     * 将二进制转换为十六进制
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 11:37
     *
     * @param buf 二进制字节数组
     * @return 十六进制字符串
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * <p>
     * 将十六进制转换为二进制
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 11:39
     *
     * @param hexStr 十六进制字符串
     * @return 二进制字节数组
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (null == hexStr || hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        String userid = "23108B5F1DE2AB499E42AB57E1D9D841A991D2B8C028A7E490AC648EF3DFE027";
        String jiemi = decrypt(userid);

        System.out.println("jiemi====" + jiemi);




    }

    public static Date getNextDay(Date newDate, Date oldDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        Calendar newdar = Calendar.getInstance();
        newdar.set(newdar.get(Calendar.YEAR), newdar.get(Calendar.MONTH), newdar.get(Calendar.DATE), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        return newdar.getTime();
    }

    private static boolean validateNum(String str) {
        String regx = "^(?:[0-9]{1,3}|1000)$";
        if (str.matches(regx)) {
            return true;
        }
        return false;
    }


}
