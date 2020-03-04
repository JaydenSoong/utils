package com.soong.utils.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* 自定义 Servlet 基类。
* 继承该类创建 Servlet 要求其中定义的方法与 Servlet 中的 service() 参数一致，抛出异常也是一致，且是 public 权限
* 并且在使用该类的子类时，请求参数中需要一个 method 参数，以确定调用哪一个方法。
* 使用由该 Servlet 派生的 Servlet 时，请求方法可定义一个字符串类型的返回值，以确定请求或转发到
* 具体页面。r:/xxx 表示重定向, f:/xxx 表示转发
*/
public abstract class BaseServlet extends HttpServlet {
   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // 0. 设置编码
       request.setCharacterEncoding("UTF-8");
       response.setCharacterEncoding("UTF-8");
       // 1. 获取请求参数
       String methodName = request.getParameter("method");
       // 1.1 没有请求参数，抛出异常
       if (methodName == null || methodName.trim().isEmpty()) {
           throw new RuntimeException("请添加 method 参数，值为需要调用的方法名。");
       }
       // 2.反射查找 Servlet 中请求调用的方法
       Method method = null;
       try {
           method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
       } catch (NoSuchMethodException e) {
           throw new RuntimeException("方法 method=" + methodName + " 不存在，" + methodName +
                   " 方法必须是 public 权限的，并且方法参数与抛出异常类型需与 service 方法一致。");
       }
       // 3. 反射调用 method 方法
       try {
           String returns = (String) method.invoke(this, request, response);
           // 如果方法返回的字符串为 null 或为 "" ，则什么也不需要做
           if (returns == null || returns.trim().isEmpty()) {
               return;
           }
           // 请求方法返回字符串格式为 r:/xxx 表示重定向, f:/xxx 表示转发
           if (returns.contains(":")) {
               String[] split = returns.split(":");
               if (split[0].equals("r")) {
                   response.sendRedirect(split[1]);
               } else if (split[0].equals("f")){
                   request.getRequestDispatcher(split[1]).forward(request, response);
               } else {
                   throw new RuntimeException(methodName + " 方法返回的操作 " + split[0] + " 当前版本还不支持。");
               }
           } else { // 如果没有指定转发或重定向，则默认为转发
               request.getRequestDispatcher(returns).forward(request, response);
           }
       } catch (IllegalAccessException | InvocationTargetException e) {
           throw new RuntimeException(e);
       }
   }
}
