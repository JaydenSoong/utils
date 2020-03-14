package com.soong.utils.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义 Servlet 基类，用来完成请求方法的分发。
 * 继承该类创建 Servlet 要求其中定义的方法与 Servlet 中的 service() 参数一致，抛出异常也是一致，且是 public 权限
 * 由于继承该类的作用是适配多个方法，所以 urlPatterns 写成 “/xxx/*”
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * 复写 service 方法，完成功能的分发
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        //从请求 uri 中截取字符串，获取请求方法名
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        try {
            //从请求 Servlet 中查询方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //调用查询到的方法
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将 java 对象序列化为 json，并写入客户端
     * @param obj，需要序列化的 java 对象
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 直接将 java 对象序列化为 json，返回一个 json 格式的字符串
     * @param obj，需要序列化的 java 对象
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
