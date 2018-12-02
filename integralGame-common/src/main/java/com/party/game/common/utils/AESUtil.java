package com.party.game.common.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * AES工具算法
 *
 */
public class AESUtil {

    private static final String ALGORITHM = "AES";
    static Charset CHARSET = Charset.forName("utf-8");

    /**
     *  将content的内容以password的密钥加密
     * @param content   被加密码的内容
     * @param password  密钥
     * @return  加密后的秘串
     */
    public static String encrypt(String content, String password) {

        checkParam(content, password);

        try {
            SecretKeySpec secretKeySpec = getSecretKeySpec(password);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] bytes = encryptContent(content);
            byte[] data = cipher.doFinal(bytes);

            return Base64.encodeBase64URLSafeString(data);

        } catch (Exception var6) {
            return null;
        }
    }

    /**
     *  将content的内容以password的密钥解密
     * @param content   被解密码的内容
     * @param password  密钥
     * @return  原始数据
     */
    public static String decrypt(String content, String password) {

        checkParam(content, password);

        try {
            SecretKeySpec secretKeySpec = getSecretKeySpec(password);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] bytes = Base64.decodeBase64(content);
            byte[] data = cipher.doFinal(bytes);

            return decryptContent(data);

        } catch (Exception var5) {
            return null;
        }
    }

    /**
     *  检查参数
     * @param content   内容
     * @param password  密钥
     */
    private static void checkParam(String content, String password) {
        boolean contentIsNotBlank = StringUtils.isNotBlank(content);
        boolean passwordIsNotBlank = StringUtils.isNotBlank(password);
        Preconditions.checkArgument(contentIsNotBlank, "content is empty");
        Preconditions.checkArgument(passwordIsNotBlank, "password is empty");
    }


    /**
     * 生成加密后的key
     * @param password 密钥
     * @return 加密后的key
     * @throws Exception 加密异常
     */
    private static SecretKeySpec getSecretKeySpec(String password) throws Exception {
        //指定算法名称
        final String algorithm = "SHA1PRNG";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
        secureRandom.setSeed(password.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return new SecretKeySpec(enCodeFormat, ALGORITHM);
    }



    // 生成4个字节的网络字节序
    static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // 还原4个字节的网络字节序
    static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    // 随机生成16位字符串
    static String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 对明文进行算法运算
     * @param content 待加密内容
     * @return 运算后的字节
     */
    private static byte[] encryptContent(String content){
        ByteGroup byteCollector = new ByteGroup();
        //获取十六位随机字符的字节
        byte[] randomStrBytes = getRandomStr().getBytes(CHARSET);
        //待加密内容字节
        byte[] textBytes = content.getBytes(CHARSET);
        //根据待加密字节获取四位网络字节序
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);

        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);


        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        return unencrypted;
    }


    /**
     * 对明文进行算法还原
     * @param original 明文字节
     * @return 还原后的字符
     */
    private static String decryptContent(byte[] original){

        // 去除补位字符
        byte[] bytes = PKCS7Encoder.decode(original);
        //分离网络字符
        byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
        //算出加密内容长度
        int contentLength = recoverNetworkBytesOrder(networkOrder);
        //取出加密内容
        String content = new String(Arrays.copyOfRange(bytes, 20, 20 + contentLength), CHARSET);
        return content;
    }


}
