package com.party.game.common.utils;

import java.io.*;

/**
 * Bean工具
 * @author wenqiang.luo date:15-9-9
 */
public class BeanUtils {

    /** 私有构造函数 */
    private BeanUtils() {

    }

    public static <T> T cloneInDepth(T object) {
        T result = null;
        try {
            //将对象写到流里
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(object);

            //从流里读出来
            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(byteInput);

            result = (T) objectInput.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
