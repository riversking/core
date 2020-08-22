package com.rivers.core.config;

import cn.hutool.core.collection.CollUtil;
import com.rivers.core.constant.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author v-wangyichuan
 */
@Configuration
@Log4j2
public class RequestInterceptorConfig implements RequestInterceptor {

    private static String TOKEN_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        log.info("fromHeader {}", fromHeader);
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            log.info("SecurityConstants.FROM_IN {}", SecurityConstants.FROM_IN);
            return;
        }
        template.header(TOKEN_HEADER, getHeaders(Objects.requireNonNull(getHttpServletRequest())).get(TOKEN_HEADER));
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
