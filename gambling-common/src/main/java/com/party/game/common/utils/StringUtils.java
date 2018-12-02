package com.party.game.common.utils;

import com.google.common.collect.Lists;
import com.party.game.common.constant.Constant;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.oro.text.perl.Perl5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    private static final Pattern chinesePattern = compile("[\u4e00-\u9fa5]");
    private static final Pattern englishNumberPattern = compile("[a-zA-Z0-9]");

    static Logger logger = LoggerFactory.getLogger("com.party.game.common.utils.StringUtils");

    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param txt
     * @return
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String abbr2(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
                "$1$2");
        // 去掉不需要结素标记的HTML标记
        temp_result = temp_result
                .replaceAll(
                        "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                        "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
                "$2");
        // 用正则表达式取出标记
        Pattern p = compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = Lists.newArrayList();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 获得i18n字符串
     public static String getMessage(String code, Object[] args) {
     LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
     HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
     Locale localLocale = localLocaleResolver.resolveLocale(request);
     return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
     }
     */

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }


    /**
     * html转议
     *
     * @param content
     * @return
     * @descript
     * @author LJN
     * @date 2015年4月27日
     * @version 1.0
     */
    public static String htmltoString(String content) {
        if (content == null)
            return "";
        String html = content;
        html = html.replace("'", "&apos;");
        html = html.replaceAll("&", "&amp;");
        html = html.replace("\"", "&quot;"); // "
        html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
        html = html.replace(" ", "&nbsp;");// 替换空格
        html = html.replace("<", "&lt;");
        html = html.replaceAll(">", "&gt;");

        return html;
    }

    /**
     * html转议
     *
     * @param content
     * @return
     * @descript
     * @author LJN
     * @date 2015年4月27日
     * @version 1.0
     */
    public static String stringtohtml(String content) {
        if (content == null)
            return "";
        String html = content;
        html = html.replace("&apos;", "'");
        html = html.replaceAll("&amp;", "&");
        html = html.replace("&quot;", "\""); // "
        html = html.replace("&nbsp;&nbsp;", "\t");// 替换跳格
        html = html.replace("&nbsp;", " ");// 替换空格
        html = html.replace("&lt;", "<");
        html = html.replaceAll("&gt;", ">");

        return html;
    }

    /**
     * 替换html标签 除了（img、p、strong、span、br）标签，其他的都过滤掉
     *
     * @param html
     * @return
     */
    public static String removeHtmlTag(String html) {
        StringBuffer buffer = new StringBuffer();
        Perl5Util preg = new Perl5Util();
        preg.substitute(buffer, "s/<script[^>]*?>.*?<\\/script>//gmi", html);
        html = buffer.toString();
        buffer.setLength(0);
        preg.substitute(buffer, "s#<[/]*?(?!img|p|/p|strong|/strong|span|/span|br|tr|/tr|td|/td)[^<>]*?>##gmi", html);
        html = buffer.toString();
        buffer.setLength(0);
        preg.substitute(buffer, "s#<[/]*?(tr|/tr)[^<>]*?>#<br/>#gmi", html);
        html = buffer.toString();
        buffer.setLength(0);
        preg.substitute(buffer, "s#<[/]*?(td|/td)[^<>]*?>#&nbsp;#gmi", html);
        html = buffer.toString();
        buffer.setLength(0);
        preg.substitute(buffer, "s/([\r\n])[\\s]+//gmi", html);
        html = buffer.toString();
        buffer.setLength(0);
        return html;
    }

    /**
     * 编码base64
     *
     * @param content 内容
     * @return
     */
    public static String encodeBase64(String content) {
        try {
            return new String(Base64.getEncoder().encode(content.getBytes(Constant.UTF_8)));
        } catch (Exception e) {
            logger.error("编码异常", e);
        }
        return EmojiParser.parseToAliases(content);
    }


    /**
     * 解码base64
     *
     * @param content 内容
     * @return
     */
    public static String decodeBase64(String content) {
        try {
            return new String(Base64.getDecoder().decode(content.getBytes()), Constant.UTF_8);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().indexOf("Illegal base64 character") == -1) {
                logger.error("解码异常——" + content, e);
            }
        } catch (Exception e) {
            logger.error("解码异常", e);
        }
        return EmojiParser.parseToUnicode(content);
    }

    /**
     * 解码base64
     *
     * @param content 内容
     * @return
     */
    public static String decodeBase64New(String content) {
        if (isContainChinese(content)) {
            // 如果包含中文，尝试表情解码
            return EmojiParser.parseToUnicode(content);
        }
        return checkIsBase64(content);
    }

    /**
     * 检查是否为Base64加密过
     *
     * @param content 内容
     * @return
     */
    public static String checkIsBase64(String content) {
        try {
            // emoji转码
            String newContent = EmojiParser.parseToUnicode(content);
            String decodeContent = decodeBase64(newContent);
            String encodeContent = encodeBase64(decodeContent);
            if (content.equals(encodeContent)) {
                // 如果两个相等表示为已经base64加密过
                return decodeBase64(content);
            } else {
                return EmojiParser.parseToUnicode(content);
            }
        } catch (Exception e) {
            logger.error("检查是否为Base64加密过异常", e);
        }
        return content;
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Matcher m = chinesePattern.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否包含英文数字
     *
     * @param str 字符串
     * @return 结果（true/false）
     */
    public static boolean isContainEnglish(String str) {
        Matcher m = englishNumberPattern.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 计算当前String字符串所占的总Byte长度
     *
     * @param args 要截取的字符串
     * @return 返回值int型，字符串所占的字节长度，如果args为空或者“”则返回0
     * @throws UnsupportedEncodingException
     */
    public static int getStringByteLenths(String args) throws UnsupportedEncodingException {
        return args != null && args != "" ? args.getBytes("utf-8").length : 0;
    }

    /**
     * 获取与字符串每一个char对应的字节长度数组
     *
     * @param args 要计算的目标字符串
     * @return int[]
     * 数组类型，返回与字符串每一个char对应的字节长度数组
     * @throws UnsupportedEncodingException
     */
    public static int[] getByteLenArrays(String args) throws UnsupportedEncodingException {
        char[] strlen = args.toCharArray();
        int[] charlen = new int[strlen.length];
        for (int i = 0; i < strlen.length; i++) {
            charlen[i] = String.valueOf(strlen[i]).getBytes("utf-8").length;
        }
        return charlen;
    }

    /**
     * 按字节截取字符串 ，指定截取起始字节位置与截取字节长度
     *
     * @param orignal 要截取的字符串
     * @param start   开始的位置；
     * @param count   长度
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static String substringByte(String orignal, int start, int count) {
        orignal = orignal.trim();
        //如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal)) {
            return orignal;
        }

        //截取Byte长度必须>0
        if (count <= 0) {
            return orignal;
        }

        //截取的起始字节数必须比
        if (start < 0) {
            start = 0;
        }

        //目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();

        try {

            //截取字节起始字节位置大于目标String的Byte的length则返回空值
//			if (start >= getStringByteLenths(orignal)) return null;

            // int[] arrlen=getByteLenArrays(orignal);
            int len = 0;

            char c;

            //遍历String的每一个Char字符，计算当前总长度
            //如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {

                c = orignal.charAt(i);

                //当起始位置为0时候
                if (start == 0) {

                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len <= count) {
                        buff.append(c);
                    } else {
                        break;
                    }

                } else {

                    //截取字符串从非0位置开始
                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len >= start && len <= start + count) {
                        buff.append(c);
                    }
                    if (len > start + count) {
                        break;
                    }

                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //返回最终截取的字符结果;
        //创建String对象，传入目标char Buff对象
        return new String(buff);
    }

    /**
     * 截取指定长度字符串
     *
     * @param orignal 要截取的目标字符串
     * @param count   指定截取长度
     * @return 返回截取后的字符串
     */
    public static String substringByte(String orignal, int count) {
        return substringByte(orignal, 0, count);
    }

    public static void main(String[] args) {

        substringByte("第二届【粤企户外联盟】丝绸之路108公里戈壁沙漠徒步挑战赛", 81, 80);


        String encodeContent = "8J+Yr/CfmJfwn5iY8J+YpfCfmInwn5it5ZWm5ZWm5ZWm5ZWm5ZWm8J+RneaIkeadpee7meS9oOaUr+aMgeS4gOS4i+WVpvCflbbwn46A8J+ZgfCfmIbwn6SX8J+Yh/CfkZPwn5GU8J+RlfCfkZfwn46f8J+Og/Cfjobwn5Gc8J+bjfCfkaHwn5GZ4piu8J+RuA==";
        String chineseContent = "中国11111";
        String chineseWithEmojiContent = "fdsf65d:open_umbrella: :dash: :partly_sunny:️ :comet: :earth_africa: :palm_tree: :elephant: :snake: :tophat: :shirt: :information_desk_person: :woman: :horns_sign: :scream_cat: :dizzy_face: :angry: :yum: :pray: :dash: ";
        String englishContent = "aaaa456789dasdsafasdf";

        System.out.println("Base64解码前：" + encodeContent);
        encodeContent = decodeBase64New(encodeContent);
        System.out.println("Base64解码后：" + encodeContent);

        System.out.println("--------------------------------");

        System.out.println("中文不带表情解码前：" + chineseContent);
        chineseContent = decodeBase64New(chineseContent);
        System.out.println("中文不带表情解码后：" + chineseContent);

        System.out.println("--------------------------------");

        System.out.println("中文带表情解码前：" + chineseWithEmojiContent);
        chineseWithEmojiContent = decodeBase64New(chineseWithEmojiContent);
        System.out.println("中文带表情解码后：" + chineseWithEmojiContent);

        System.out.println("--------------------------------");

        System.out.println("数字英文解码前：" + englishContent);
        englishContent = decodeBase64New(englishContent);
        System.out.println("数字英文解码后：" + englishContent);
    }
}
