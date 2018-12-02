package com.party.game.common.utils;


import com.party.game.common.model.Parameter;
import com.party.game.common.model.UTF8PostMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具
 *
 * @author wenqiang.luo date:15-8-20
 */
public abstract class HttpUtils {

    /** Http默认交互编码 */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /** Http默认连接超时时间 */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

    /** Http默认ContentType类型 */
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    /** 日志记录对象 */
    protected static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /** 私有构造方法，禁用创建对象 */
    private HttpUtils() { }

    /**
     * 证书https请求
     * @return
     * @throws Exception
     */
    public static String certHttpsRequest(String url, Map<String, String> inputParameters,
                                          File certFile,String certPassword,
                                          String inputCharsetRequest,
                                          String inputCharsetResponse)throws Exception{

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(certFile,certPassword.toCharArray(),
                new TrustSelfSignedStrategy()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        // Http请求的放回结果
        String resultResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);

            //设置请求体编码方式
            httpPost.setHeader(HTTP.CONTENT_TYPE,
                    ContentType.create(DEFAULT_CONTENT_TYPE, inputCharsetRequest).toString());

            // 获取表单参数和值
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : inputParameters.entrySet()) {
                BasicNameValuePair parameter =
                        new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(parameter);
            }

            // 设置表单提交的编码方式
            UrlEncodedFormEntity urlEncodedFormEntity = null;
            try {
                urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs,
                        inputCharsetRequest);
            } catch (UnsupportedEncodingException uee) {
                logger.error("certHttps unsupported encoding error: " + inputCharsetRequest,
                        uee);
                throw uee;
            }
            httpPost.setEntity(urlEncodedFormEntity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            //获取返回状态行
            StatusLine responseStatus = response.getStatusLine();
            //如果请求成功并得到响应
            if ( HttpStatus.SC_OK == responseStatus.getStatusCode() ) {
                //获取返回信息
                HttpEntity responseHttpEntity = response.getEntity();
                //设置返回编码字符集
                Charset charsetResponse = null;
                if (inputCharsetResponse != null) {
                    charsetResponse = Charset.forName(inputCharsetResponse);
                } else {
                    ContentType contentTypeResponse = ContentType.getOrDefault(responseHttpEntity);
                    charsetResponse = contentTypeResponse.getCharset();
                }
                //获取返回字符串
                resultResponse = EntityUtils.toString(responseHttpEntity, charsetResponse);
            } else {
                logger.error("{} error {} {}",
                        responseStatus.getProtocolVersion().toString(),
                        responseStatus.getStatusCode(),
                        responseStatus.getReasonPhrase());
                throw new IOException(responseStatus.getReasonPhrase());
            }
            try {
                HttpEntity entity = response.getEntity();
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return resultResponse;
    }

    /**
     * 证书https请求，微信端退款使用
     * @return
     * @throws Exception
     */
    public static String certHttpsRequest(
            String url, String inputParameters,
            InputStream certFileInputStream,String certPassword)throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(certFileInputStream, certPassword.toCharArray());
        } finally {
            certFileInputStream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        // Http请求的放回结果
        String resultResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);

            //设置请求体编码方式
            httpPost.setHeader(HTTP.CONTENT_TYPE,
                    ContentType.create(DEFAULT_CONTENT_TYPE, "UTF-8").toString());
            //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
            StringEntity postEntity = new StringEntity(inputParameters, "UTF-8");
            httpPost.setEntity(postEntity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            //获取返回状态行
            StatusLine responseStatus = response.getStatusLine();
            //如果请求成功并得到响应
            if ( HttpStatus.SC_OK == responseStatus.getStatusCode() ) {
                //获取返回信息
                HttpEntity responseHttpEntity = response.getEntity();
                //获取返回字符串
                resultResponse = EntityUtils.toString(responseHttpEntity, "UTF-8");
            } else {
                logger.error("{} error {} {}",
                        responseStatus.getProtocolVersion().toString(),
                        responseStatus.getStatusCode(),
                        responseStatus.getReasonPhrase());
                throw new IOException(responseStatus.getReasonPhrase());
            }
            try {
                HttpEntity entity = response.getEntity();
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return resultResponse;
    }

   /* *//**
     * 微信端发送https请求
     * @param requestUrl url地址
     * @param outputStr 参数
     * @return 返回
     *//*
    public static String httpsRequest(String requestUrl, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("POST");

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out.");
        } catch (Exception e) {
            System.out.println("https request error:"+e);
        }
        return buffer.toString();
    }*/

    /**
     * Http Get默认发送请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUrl 请求完整url
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpGet(final String inputUrl) throws IOException, URISyntaxException {
        //采用默认编码方式进行Http Get发送
        return httpGet(inputUrl, DEFAULT_CHARSET, null);
    }

    /**
     * Http Get请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUrl 请求完整url
     * @param inputCharsetRequest Http Get请求字符集编码
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpGet(final String inputUrl,
                                 final String inputCharsetRequest) throws IOException, URISyntaxException {
        return httpGet(inputUrl, inputCharsetRequest, null);
    }

    /**
     * Http Get请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUrl 请求完整url
     * @param inputCharsetRequest Http Get请求字符集编码
     * @param inputCharsetResponse Http Get返回字符编码集
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpGet(String inputUrl, String inputCharsetRequest,
                                 String inputCharsetResponse) throws IOException, URISyntaxException {
        //参数断言检查
        AssertUtils.notNull(inputUrl, "http get url is null");

        //请求字符集为空，使用默认字符集发送Http Get请求
        if (inputCharsetRequest == null) {
            return httpGet(inputUrl, DEFAULT_CHARSET, inputCharsetResponse);
        }

        logger.debug("http get url: {}, charset: {}", inputUrl, inputCharsetRequest);

        //建立可关闭的Http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

       /*  HttpGet httpGet = new HttpGet(inputUrl);*/

        //把String转成URL
        URL url = new URL(inputUrl);
        URI uri = null;
        try {
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), new String(url.getQuery().getBytes(inputCharsetRequest)), null);
        } catch (URISyntaxException ue) {
            logger.error("url transformation uri error: {}",ue.getMessage());
            throw ue;
        }

        HttpGet httpGet = new HttpGet(uri);
        //设置请求体编码方式
        httpGet.setHeader(HTTP.CONTENT_TYPE,
                ContentType.create(DEFAULT_CONTENT_TYPE, inputCharsetRequest).toString());

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();

        httpGet.setConfig(requestConfig);

        String response = null; //请求返回值
        try {
            //发送Get请求
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //获取返回状态行
            StatusLine responseStatus = httpResponse.getStatusLine();
            //如果请求成功并得到响应
            if ( HttpStatus.SC_OK == responseStatus.getStatusCode() ) {
                //获取返回信息（包含在返回体中）
                HttpEntity responseHttpEntity = httpResponse.getEntity();

                //设置返回的编码字符集
                Charset charsetResponse = null;
                if (inputCharsetResponse != null) {
                    charsetResponse = Charset.forName(inputCharsetResponse);
                } else {
                    ContentType contentTypeResponse =
                            ContentType.getOrDefault(responseHttpEntity);
                    charsetResponse = contentTypeResponse.getCharset();
                }
                //返回字符串
                response = EntityUtils.toString(responseHttpEntity, charsetResponse);
            } else {
                logger.error("{} Error {} {}",
                        responseStatus.getProtocolVersion().toString(),
                        responseStatus.getStatusCode(),
                        responseStatus.getReasonPhrase());
                throw new IOException(responseStatus.getReasonPhrase());
            }
        } catch (IOException ioe) {
            logger.error("http get error: " + inputUrl, ioe);
            throw ioe;
        } finally {
            try {
                httpClient.close();
            } catch (IOException ioe) {
                logger.error("http client close error", ioe);
                throw ioe;
            }
        }

        logger.debug("Http Get Response: {}", response);
        return response;
    }

    /**
     * Http Post 请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputParameters post 参数
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPost(String inputUri, Map<String, String> inputParameters)
            throws IOException {
        //采用默认编码方式进行Http Get发送
        return httpPost(inputUri, inputParameters, DEFAULT_CHARSET, null);
    }

    /**
     * Http Post 请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputParameters post 参数
     * @param inputCharsetRequest Http Post交互字符集编码
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPost(String inputUri, Map<String, String> inputParameters,
                                  String inputCharsetRequest) throws IOException {
        return httpPost(inputUri, inputParameters, inputCharsetRequest, null);
    }

    /**
     * Http Post 请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputParameters post 参数
     * @param inputCharsetRequest Http Post交互字符集编码
     * @param inputCharsetResponse Http Post返回字符编码集
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPost(String inputUri, Map<String, String> inputParameters,
                                  String inputCharsetRequest, String inputCharsetResponse)
            throws IOException {
        //参数断言检查
        AssertUtils.notNull(inputUri, "http post uri is null");
        AssertUtils.notNull(inputUri, "http post parameters is null");

        //字符集为空，使用默认字符集发送Http Post请求
        if (inputCharsetRequest == null) {
            return httpPost(inputUri, inputParameters,
                    DEFAULT_CHARSET, inputCharsetResponse);
        }

        logger.info("http post uri: {}, charset: {}", inputUri, inputCharsetRequest);
        logger.info("http post parameters: {}", inputParameters.toString());

        // 建立可关闭的Http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(inputUri);

        //设置请求体编码方式
        httpPost.setHeader(HTTP.CONTENT_TYPE,
                ContentType.create(DEFAULT_CONTENT_TYPE, inputCharsetRequest).toString());

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);

        // 获取表单参数和值
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : inputParameters.entrySet()) {
            BasicNameValuePair parameter =
                    new BasicNameValuePair(entry.getKey(), entry.getValue());
            nameValuePairs.add(parameter);
        }

        // 设置表单提交的编码方式
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs,
                    inputCharsetRequest);
        } catch (UnsupportedEncodingException uee) {
            logger.error("http post unsupported encoding error: " + inputCharsetRequest,
                    uee);
            throw uee;
        }
        httpPost.setEntity(urlEncodedFormEntity);

        // Http请求的放回结果
        String response = null;
        try {
            //发送Post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //获取返回状态行
            StatusLine responseStatus = httpResponse.getStatusLine();
            //如果请求成功并得到响应
            if ( HttpStatus.SC_OK == responseStatus.getStatusCode() ) {
                //获取返回信息
                HttpEntity responseHttpEntity = httpResponse.getEntity();

                //设置返回编码字符集
                Charset charsetResponse = null;
                if (inputCharsetResponse != null) {
                    charsetResponse = Charset.forName(inputCharsetResponse);
                } else {
                    ContentType contentTypeResponse = ContentType.getOrDefault(responseHttpEntity);
                    charsetResponse = contentTypeResponse.getCharset();
                }
                //获取返回字符串
                response = EntityUtils.toString(responseHttpEntity, charsetResponse);
            } else {
                logger.error("{} error {} {}",
                        responseStatus.getProtocolVersion().toString(),
                        responseStatus.getStatusCode(),
                        responseStatus.getReasonPhrase());
                throw new IOException(responseStatus.getReasonPhrase());
            }
        } catch (IOException ioe) {
            logger.error("http post error: " + inputUri, ioe);
            throw ioe;
        } finally {
            try {
                httpClient.close(); // 无论是否成功，都要关闭Http客户端
            } catch (IOException ioe) {
                logger.error("http client close error", ioe);
                throw ioe;
            }
        }

        logger.debug("http post response: {}", response);

        return response;
    }

    /**
     * Http Post String 默认发送请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputPushData 字符流
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPostString(String inputUri, String inputPushData)
            throws IOException {
        //采用默认编码方式进行Http Get发送
        return httpPostString(inputUri, inputPushData, DEFAULT_CHARSET, null);
    }

    /**
     * Http Post String 请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputPushData 字符流
     * @param inputCharsetRequest Http Post交互字符集编码
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPostString(String inputUri, String inputPushData,
                                        String inputCharsetRequest) throws IOException {
        return httpPostString(inputUri, inputPushData,
                inputCharsetRequest, null);
    }

    /**
     * Http Post String 请求
     *
     * @author wenqiang.luo 2015-08-20 完成创建
     * @param inputUri 请求路径uri
     * @param inputPushData 字符流
     * @param inputCharsetRequest Http Post请求字符集编码
     * @param inputCharsetResponse Http Post返回字符编码集
     * @return 交互结果
     * @throws IOException 交互异常，一般有网络超时、对方服务器异常等
     */
    public static String httpPostString(String inputUri, String inputPushData,
                                        String inputCharsetRequest,
                                        String inputCharsetResponse) throws IOException {
        //参数断言检查
        AssertUtils.notNull(inputUri, "http post string uri is null");
        AssertUtils.notNull(inputUri, "http post string pushData is null");

        //字符集为空，使用默认字符集发送Http Post请求
        if (inputCharsetRequest == null) {
            return httpPostString(inputUri, inputPushData,
                    DEFAULT_CHARSET, inputCharsetResponse);
        }

        logger.debug("http post string uri: {}, charset: {}",
                inputUri, inputCharsetRequest);
        logger.debug("http post string push data: {}", inputPushData);

        // 建立可关闭的Http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(inputUri);

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);

        String response = null;
        StringEntity stringEntity = new StringEntity(inputPushData, inputCharsetRequest);
        httpPost.setEntity(stringEntity);
        try {
            //发送Post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //获取Http 返回状态行
            StatusLine responseStatus = httpResponse.getStatusLine();
            //如果请求成功并得到响应
            if ( HttpStatus.SC_OK == responseStatus.getStatusCode() ) {
                //获取返回信息（返回体）
                HttpEntity responseHttpEntity = httpResponse.getEntity();

                //设置返回字符编码集
                Charset charsetResponse = null;
                if (inputCharsetResponse != null) {
                    charsetResponse = Charset.forName(inputCharsetResponse);
                } else {
                    ContentType contentTypeResponse = ContentType.getOrDefault(responseHttpEntity);
                    charsetResponse = contentTypeResponse.getCharset();
                }
                //获取返回字符串
                response = EntityUtils.toString(responseHttpEntity, charsetResponse);
            } else {
                logger.error("{} error {} {}",
                        responseStatus.getProtocolVersion().toString(),
                        responseStatus.getStatusCode(),
                        responseStatus.getReasonPhrase());
                throw new IOException(responseStatus.getReasonPhrase());
            }
        } catch (IOException ioe) {
            logger.error("http post string error: " + inputUri, ioe);
            throw ioe;
        } finally {
            try {
                httpClient.close();
            } catch (IOException ioe) {
                logger.error("http client close error", ioe);
                throw ioe;
            }
        }

        logger.debug("http post string response: {}", response);

        return response;
    }

    public static String httpPostForQunar(String url, String requestParam) throws IOException {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new UTF8PostMethod(url);

        postMethod.addParameter("Connection", "Keep-Alive");
        postMethod.addParameter("Charset", "UTF-8");
        postMethod.addParameter("Content-Type", "application/x-www-form-urlencoded");
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);

        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("requestParam", requestParam));
        postMethod.setRequestBody(buildNameValuePair(params));

        int statusCode = client.executeMethod(postMethod);
        if (statusCode == HttpStatus.SC_OK) {
            response = postMethod.getResponseBodyAsString();
        } else {
            logger.error("Post Method StatusCode: {}", statusCode);
            throw new IOException("Http Post Exception Of Code: " + statusCode);
        }
        postMethod.releaseConnection();
        client = null;
        return response;
    }

    private static org.apache.commons.httpclient.NameValuePair[] buildNameValuePair(List<Parameter> list) {
        int length = list.size();
        org.apache.commons.httpclient.NameValuePair[] pais = new org.apache.commons.httpclient.NameValuePair[length];
        for (int i = 0; i < length; i++) {
            Parameter param = list.get(i);
            pais[i] = new org.apache.commons.httpclient.NameValuePair(param.getName(), param.getValue());
        }
        return pais;
    }

    public static String httpPostForOta(String url, String method, String requestParam) throws IOException {
        logger.info("--------------HttpPost----------------");
        logger.info("url: " + url);
        logger.info("method: " + method);
        logger.info("requestParam: " + requestParam);

        String response = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new UTF8PostMethod(url);

        postMethod.addParameter("Connection", "Keep-Alive");
        postMethod.addParameter("Charset", "UTF-8");
        postMethod.addParameter("Content-Type", "application/x-www-form-urlencoded");
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);

        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("method", method));
        params.add(new Parameter("requestParam", requestParam));
        postMethod.setRequestBody(buildNameValuePair(params));

        int statusCode = client.executeMethod(postMethod);
        if (statusCode == HttpStatus.SC_OK) {
            response = postMethod.getResponseBodyAsString();
        } else {
            logger.error("Post Method StatusCode: {}", statusCode);
            throw new IOException("Http Post Exception Of Code: " + statusCode);
        }
        postMethod.releaseConnection();
        client = null;

        logger.info("response: " + response);
        logger.info("--------------------------------------");
        return response;
    }

}
