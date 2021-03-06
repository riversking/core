package com.rivers.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.google.common.collect.Lists;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ProtobufHttpMessageConverter extends AbstractHttpMessageConverter<Message> {


    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final MediaType PROTOBUF;
    private static final ConcurrentHashMap<Class<?>, Method> METHOD_CACHE;

    static {
        PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);
        METHOD_CACHE = new ConcurrentHashMap<>();
    }

    public ProtobufHttpMessageConverter() {
        super(PROTOBUF, MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Message.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(Message message) {
        return PROTOBUF;
    }

    @Override
    protected Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = PROTOBUF;
        }

        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }

        try {
            Message.Builder ex = getMessageBuilder(clazz);
            InputStreamReader reader;
            if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                reader = new InputStreamReader(inputMessage.getBody(), charset);
                JsonFormat.parser()
                        .ignoringUnknownFields()
                        .merge(reader, ex);
            } else {
                ex.mergeFrom(inputMessage.getBody());
            }
            return ex.build();
        } catch (Exception var7) {
            throw new HttpMessageNotReadableException("Could not read Protobuf message: " + var7.getMessage(), var7);
        }
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return super.canWrite(mediaType);
    }

    @Override
    protected void writeInternal(Message message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = this.getDefaultContentType(message);
        }

        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }

        OutputStreamWriter outputStreamWriter;
        if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
            outputStreamWriter = new OutputStreamWriter(outputMessage.getBody(), charset);
            String result =
                    JsonFormat.printer()
                            .includingDefaultValueFields()
                            .preservingProtoFieldNames()
                            .omittingInsignificantWhitespace()
                            .print(message);
            outputStreamWriter.write(formatJsonString(result));
            outputStreamWriter.flush();
        } else if (PROTOBUF.isCompatibleWith(contentType)) {
            FileCopyUtils.copy(message.toByteArray(), outputMessage.getBody());
        }

    }

    private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
        Method method = METHOD_CACHE.get(clazz);
        if (method == null) {
            method = clazz.getMethod("newBuilder");
            METHOD_CACHE.put(clazz, method);
        }
        return (Message.Builder) method.invoke(clazz, new Object[0]);
    }

    /*在response中需要保留的字段名*/
    private static final List<String> usableFieldNames = Lists.newArrayList("retCode");


    /**
     * 过滤掉响应中的一些空字段（或使用proto默认值的字段）
     * <p>1.枚举类型字段，响应中都是有意义的值，如果使用枚举默认值则删除</p>
     * <p>2.字符串/数组类型字段，为空则过滤掉</p>
     * <p>3.数字类型字段，除fieldNames定义的字段外值为0的则过滤掉</p>
     */
    private static String formatJsonString(String input) {
        PropertyFilter filter = (Object o, String name, Object value)
                -> {
            boolean usable = true;

            if (value instanceof JSONArray && ((JSONArray) value).isEmpty()) {
                usable = false;
            } else if (value instanceof Number
                    && !usableFieldNames.contains(name)
                    && ((Number) value).doubleValue() == NumberUtils.DOUBLE_ZERO) {
                usable = false;
            } else if (value instanceof String && StringUtils.isBlank((String) value)) {
                usable = false;
            }
            return usable;
        };

        return JSON.toJSONString(JSON.parseObject(input), filter);
    }
}
