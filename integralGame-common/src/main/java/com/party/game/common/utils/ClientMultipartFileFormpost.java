package com.party.game.common.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;

/**
 * @author yongkang.liao 2015/10/18.
 */
public class ClientMultipartFileFormpost {
    public String handleFileUpload(MultipartHttpServletRequest request) throws IOException {
        Map<String, MultipartFile> filesMap = request.getFileMap();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://img.jingqu.cn/upload");

//            httppost.setHeader("Content-Type", "jpg");
              httppost.addHeader("Content-Type", "png");
//            httppost.addHeader("Content-Type", "image/jpeg");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setMode(HttpMultipartMode.STRICT);

            for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
                String uploadedFile = entry.getKey();
                MultipartFile file = entry.getValue();

                String mimeType = file.getContentType();
                String filename = file.getOriginalFilename();
                byte[] bytes = file.getBytes();

                ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
                httppost.setEntity(byteArrayEntity);

            }

            Header[] headers = httppost.getAllHeaders();
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }


        return "You successfully uploaded !";
    }
}
