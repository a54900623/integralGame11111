package com.party.game.common.utils.img;

import com.google.common.collect.Maps;
import com.party.game.common.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 图片压缩
 *
 * @author Juliana
 * @data 18/2/1 16:57 .
 */
public class ImgCompressUtil {

    /**
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws IOException {
//        Long maxSize = 500 * 1024L;
//        try {
//            //http://txzapp-10052192.image.myqcloud.com/1499838429002
////            CompressResult compress = ImgCompressUtil.compress("http://img.soogif.com/GKDDsLMXkTD9FqlFnTJlAHmWbJ9BMNsn.gif", 600, 500, null);
////
////            ImgCompressUtil.byte2File(compress.getBytes(), "D:/Juliana/", "2.gif");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * 根据byte数组，生成文件
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
//    public static void byte2File(byte[] bfile,String filePath,String fileName){
//        BufferedOutputStream bos=null;
//        FileOutputStream fos=null;
//        File file=null;
//        try{
//            File dir=new File(filePath);
//            if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
//                dir.mkdirs();
//            }
//            file=new File(filePath+fileName);
//            fos=new FileOutputStream(file);
//            bos=new BufferedOutputStream(fos);
//            bos.write(bfile);
//        }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        finally{
//            try{
//                if(bos != null){
//                    bos.close();
//                }
//                if(fos != null){
//                    fos.close();
//                }
//            }
//            catch(Exception e){
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
    /**
     * 压缩图片
     *
     * @param input    压缩输入参数
     * @return
     * @throws Exception
     */
    public static CompressResult compress(CompressInput input) throws Exception {
        String strUrl = input.getStrUrl();
        Integer maxWidth = input.getMaxWidth();
        Integer maxHeight = input.getMaxHeight();
        Long maxSize = input.getMaxSize();

        BufferedImage thumbnail = null;
        String formatName = StringUtils.isEmpty(input.getFormatName()) ? "jpg" : input.getFormatName();
        BufferedImage image = null;
        InputStream inStream = null;
        int resWidth = 0;  // 源图宽度
        int resHeight = 0; // 源图高度
        int retWidth = 0; // 压缩后图片宽度
        int retHeight = 0; // 压缩后图片高度
        byte[] originBytes = new byte[0];
        Map<String, Object> ret = Maps.newHashMap();
        String fName = input.getFileName();
        String suffix = ".jpg";
        if (StringUtils.isNotEmpty(strUrl)) {
            fName = strUrl.trim().substring(strUrl.lastIndexOf("/")+1);
            suffix = fName.indexOf(".") != -1 ? fName.substring(fName.lastIndexOf(".")): ".jpg";
            if (strUrl.trim().indexOf("http") == 0) { // 远程图
                URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                inStream = conn.getInputStream();// 通过输入流获取图片数据
                image = ImageIO.read(url); // inputStream转换为图片
            } else{ // 本地图
                inStream = new FileInputStream(strUrl);// 通过输入流获取图片数据
                image = ImageIO.read(new FileInputStream(strUrl)); // inputStream转换为图片
            }
        }
        if (null != input.getImgInput()) {
            inStream = input.getImgInput();
            image = ImageIO.read(inStream);
        }
        try {
            originBytes = ImgUtil.readInputStream(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        resWidth = image.getWidth();      // 源图宽度
        resHeight = image.getHeight();    // 源图高度
        float ratio = (float) (Math.round(resWidth / resHeight * 100) / 100);
        if (suffix.equals(".gif")) {
            CompressResult cr = new CompressResult();
            cr.setBytes(originBytes);
            cr.setHeight(resHeight);
            cr.setWidth(resWidth);
            cr.setRatio(ratio);
            cr.setName(fName);
            cr.setSize(new Long(originBytes.length));
            return cr;
        }
        Thumbnails.Builder<BufferedImage> of = Thumbnails.of(image);
        Boolean isCompress = false;
        if (null != maxSize && originBytes.length > maxSize) { // 超过最大限制大小
            Long scale = maxSize / originBytes.length;
            of = of.scale(1f).outputQuality(scale);
            isCompress = true;
        }else {
            if (null != maxWidth && resWidth > maxWidth && null == maxHeight) { // 超过最大宽度
                of = of.width(maxWidth);
                isCompress = true;
            }
            if (null != maxHeight && resHeight > maxHeight && null == maxWidth) {// 超过最大高度
                of = of.height(maxHeight);
                isCompress = true;
            }
            if ((null != maxHeight && null != maxWidth) && (resHeight > maxHeight || resWidth > maxWidth)) { // 若传入宽高两个参数
                of = of.size(maxWidth, maxHeight);
                isCompress = true;
            }
        }
        if(!isCompress){
            of.scale(1f);
        }
        thumbnail = of.asBufferedImage();
        retWidth = thumbnail.getWidth();
        retHeight = thumbnail.getHeight();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, formatName, os);
        byte[] bytes = os.toByteArray();
        CompressResult cr = new CompressResult();
        cr.setBytes(bytes);
        cr.setHeight(retHeight);
        cr.setWidth(retWidth);
        cr.setRatio(ratio);
        cr.setName(fName);
        cr.setSize(new Long(bytes.length));
        return cr;
    }
}

