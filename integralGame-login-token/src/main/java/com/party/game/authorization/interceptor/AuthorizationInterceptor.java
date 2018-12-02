package com.party.game.authorization.interceptor;

import com.party.game.authorization.annotation.Authorization;
import com.party.game.authorization.manager.TokenManager;
import com.party.game.authorization.utils.Constant;
import com.party.game.common.redis.StringJedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，对请求进行身份验证
 *
 * @author ScienJus
 * @date 2015/7/30.
 * @see Authorization
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {


    //管理身份验证操作的对象
    private TokenManager manager;

    //鉴权信息的无用前缀，默认为空
    private String httpHeaderPrefix = "";

    //鉴权失败后返回的错误信息，默认为401 unauthorized
    private String unauthorizedErrorMessage = "401 unauthorized";

    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    @Autowired
    private StringJedis stringJedis;

    public void setManager(TokenManager manager) {
        this.manager = manager;
    }

    public void setHttpHeaderPrefix(String httpHeaderPrefix) {
        this.httpHeaderPrefix = httpHeaderPrefix;
    }

    public void setUnauthorizedErrorMessage(String unauthorizedErrorMessage) {
        this.unauthorizedErrorMessage = unauthorizedErrorMessage;
    }

    public void setUnauthorizedErrorCode(int unauthorizedErrorCode) {
        this.unauthorizedErrorCode = unauthorizedErrorCode;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //从header中得到token
        String token = request.getHeader(Constant.HTTP_HEADER_NAME);
        if (token != null && token.startsWith(httpHeaderPrefix) && token.length() > 0) {
            token = token.substring(httpHeaderPrefix.length());
            //验证token
            String key = manager.getKey(token);
            if (key != null) {
                //stringJedis.setValue(Constant.REQUEST_CURRENT_KEY, key);
                return true;
            }
        }
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null   //查看方法上是否有注解
                || handlerMethod.getBeanType().getAnnotation(Authorization.class) != null) {    //查看方法所在的Controller是否有注解
            response.setStatus(unauthorizedErrorCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            writer.write(unauthorizedErrorMessage);
            writer.close();
            return false;
        }
        //stringJedis.setValue(Constant.REQUEST_CURRENT_KEY, null);
        return true;
    }
}
