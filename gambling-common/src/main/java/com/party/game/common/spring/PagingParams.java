package com.party.game.common.spring;

import com.party.game.common.paging.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/** Spring MVC 分页处理
 * @author wei.li
 * @version 2016/8/1
 */
@ControllerAdvice
public class PagingParams extends HandlerInterceptorAdapter implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return Page.class.isAssignableFrom(parameter.getParameterType());
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        int pageNo = Page.DEFAULT_PAGE_NO;
        int pageSize = Page.DEFAULT_PAGE_SIZE;

        String no = webRequest.getParameter("pageNo");
        if (StringUtils.isNumeric(no)) {
            pageNo = Integer.parseInt(no);
        }
        String size = webRequest.getParameter("pageSize");
        if (StringUtils.isNumeric(size)) {
            pageSize = Integer.parseInt(size);
        }
        return new Page(pageNo, pageSize);
    }

}
