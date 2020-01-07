package com.webank.fabric.node.manager.common.config;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.pojo.response.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.response.ConstantCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * config common result.
 */
@EnableWebMvc
@Configuration
public class ResultConfig {
    @RestControllerAdvice("com.webank.fabric.node.manager.api")
    static class CommonResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
            if (body instanceof BaseResponse) {
                return body;
            }

            BaseResponse result = new BaseResponse(ConstantCode.SUCCESS, body);
            if(body instanceof String) {
                return JSON.toJSONString(result);
            }

            return result;
        }
    }
}
