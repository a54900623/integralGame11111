package com.party.game.common.utils;

import com.party.game.common.model.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 系统工具类
 * @author yongkang.liao date:15-10-19
 */
public class SystemUtils {

    protected static Logger logger = LoggerFactory.getLogger(SystemUtils.class);

    public static boolean isChinaPhone(String phone){
        String pattern = "^((\\+?86)|(\\(\\+86\\)))?1\\d{10}$";
        boolean matches = Pattern.matches(pattern, phone);
        return matches;
    }

    public static boolean phoneContains86(String phone){
        String two = phone.substring(0,2);
        String three = phone.substring(0,3);
        if(SystemConstant.SMS_86.equals(two)){
            return true;
        }else if(SystemConstant.SMS_ADD_86.equals(three)){
            return true;
        }
        return false;
    }
    public static String getLinuxHostIp(){
        // 根据网卡取本机配置的IP
        String linuxHostIp = "";
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                if(ni.getName().equals("eth0")) {
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        String ip = ips.nextElement().getHostAddress();
                        if(ip.equals(SystemConstant.ALLOW_SEND_MESSAGE_IP)){
                            linuxHostIp = ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linuxHostIp;
    }

    public static boolean allowInvokeTaskIp(){
        String linuxHostIp = getLinuxHostIp();
        try{
            if(linuxHostIp.equals(SystemConstant.ALLOW_SEND_MESSAGE_IP)){
                return true;
            }
        }catch (Exception e){
            logger.error("获取Linux主机IP异常:" + e.getMessage());
            return false;
        }
        return false;
    }
}