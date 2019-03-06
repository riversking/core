package com.rivers.core.oath.component;

import cn.hutool.core.util.StrUtil;
import com.rivers.core.annotation.Inner;
import com.rivers.core.constant.SecurityConstants;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author riversking
 */
@Aspect
@Component
public class SecurityInnerAspect implements Ordered{

    private static final Logger log = LoggerFactory.getLogger(SecurityInnerAspect.class);


    private final HttpServletRequest request;

    public SecurityInnerAspect(HttpServletRequest request) {
        this.request = request;
    }

    @SneakyThrows
    @Around("@annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        String header = request.getHeader(SecurityConstants.FROM);
        log.warn("访问接口 HEADER {}", header);
        if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限", point.getSignature().getName());
            throw new AccessDeniedException("Access is denied");
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
