package com.party.game.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2017/11/15 0015.
 */
public class LogUtils {


    /**
     * 打印堆栈
     * @param t 异常
     * @return 堆栈输出
     */
    public static String getTrace(Throwable t){
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}
