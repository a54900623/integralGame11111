package com.party.game.common.utils;

import java.security.MessageDigest;

/**
 * @program: base
 * @description: md5加密
 * @author: Huaqunhao
 * @create: 2018-08-10 19:19
 **/
public class MD5Util {
    public static String getMD5(String plainText){
        String result = null;
        try{
            //首先进行实例化和初始化
            MessageDigest md = MessageDigest.getInstance("MD5");
            //得到一个操作系统默认的字节编码格式的字节数组
            byte[] btInput = plainText.getBytes();
            //对得到的字节数组进行处理
            md.update(btInput);
            //进行哈希计算并返回结果
            byte[] btResult = md.digest();
            //进行哈希计算后得到的数据的长度
            StringBuffer sb = new StringBuffer();
            for(byte b : btResult){
                int bt = b&0xff;
                if(bt<16){
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            result = sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 2.对文本进行32位MD5大写加密
     * @param plainText 要进行加密的文本
     * @return 加密后的内容
     */
    public static String textToMD5U32(String plainText){
        String result = getMD5(plainText);
        return result.toLowerCase();
    }
}
