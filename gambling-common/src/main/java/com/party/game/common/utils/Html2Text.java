package com.party.game.common.utils;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

/**
 * 富文本字符串（HTML）转为文本（Text）
 *
 * @author yifeng
 * @date 2018/6/26 0026
 * @time 下午 15:14
 */
public class Html2Text extends HTMLEditorKit.ParserCallback {

    private static Html2Text html2Text = new Html2Text();

    StringBuffer s;

    public Html2Text() {
    }

    public void parse(String str) throws IOException {

        InputStream iin = new ByteArrayInputStream(str.getBytes());
        Reader in = new InputStreamReader(iin);
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        delegator.parse(in, this, Boolean.TRUE);
        iin.close();
        in.close();
    }

    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static String getContent(String str) {
        try {
            html2Text.parse(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html2Text.getText();
    }
}
