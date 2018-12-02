package com.party.game.mobile.web.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 控制器异常拦截器
 * Created by yifeng
 *
 * @date 2017/1/3 0003
 * @time 16:26
 */
@Component(value = "exceptionHandler")
public class ExceptionHandler extends AbstractHandlerExceptionResolver {

    private static final String UNAUTHORIZED = "401 unauthorized";
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 异常拦截器
     *
     * @param httpServletRequest  请求参数
     * @param httpServletResponse 响应参数
     * @param o                   参数对象
     * @param e                   异常对象
     * @return
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String requestURI = httpServletRequest.getRequestURI();
        String className = ((HandlerMethod) o).getBeanType().getName();
        String parameterJson = JSONObject.toJSONString(httpServletRequest.getParameterMap());
        logger.error("异常详情——请求路径：" + requestURI + "；className名称：" + className + "；请求参数：" + parameterJson, e);
        if (e instanceof AuthorizationException) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(httpServletResponse.getOutputStream()));
                writer.write(UNAUTHORIZED);
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
