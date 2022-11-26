package com.zgnba.clos.config.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (!StrUtil.hasEmpty(value)) {
                    value = HtmlUtil.filter(value);
                }
                values[i] = value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        // 保证输入数据有序用LinkedHashMap
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if (!StrUtil.hasEmpty(value)) {
                        value = HtmlUtil.filter(value);
                    }
                    values[i] = value;
                }
                map.put(key, values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 1.将io数据读进StringBuffer
        InputStream in = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        // 为了阅读高效性, 添加缓冲流
        BufferedReader buffer = new BufferedReader(reader);
        String line = buffer.readLine();
        StringBuffer body = new StringBuffer();
        while (line != null) {
            body.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();

        // 2.根据StringBuffer进行转义
        Map<String, Object> entries = JSONUtil.parseObj(body.toString());
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (String key : entries.keySet()) {
            Object value = entries.get(key);
            if (value instanceof String) {
                if (!StrUtil.hasEmpty(value.toString())) {
                    map.put(key, HtmlUtil.filter(value.toString()));
                }
            } else {
                map.put(key, value);
            }
        }

        // 3.将转义好的字符串转化为json格式的io流返回
        String json = JSONUtil.toJsonStr(map);
        ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}
        };
    }
}



