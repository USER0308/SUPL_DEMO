package com.formssi.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.formssi.entity.ReturnJson;
import com.sun.jmx.snmp.Timestamp;

import utils.Token;

/**
 * 登录认证的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handler执行完成之后调用这个方法
     */
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception exc)
            throws Exception {

    }

    /**
     * Handler执行之后，ModelAndView返回之前调用这个方法
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * Handler执行之前调用这个方法
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
    	
        ReturnJson returnJson = new ReturnJson();
        
        //说明是登录请求，无需拦截
        if(request.getRequestURI().indexOf("login") != -1) {
        	returnJson.setSuccess(true);
            returnJson.setMessage("登录请求成功");
            
            return true;
        }
        
        String token = request.getHeader("token");

        if(token == null){
        	
        	returnJson.setSuccess(false);
            returnJson.setMessage("登录验证失败，缺少token");
            
            dealJsonReturn(request, response, returnJson.toJSON());
            
            return false;
        }
        
        HttpSession session = request.getSession();
        Map<String, Long> map = (Map<String, Long>)session.getAttribute("token");
        
        if(map == null) {
        	returnJson.setSuccess(false);
            returnJson.setMessage("登录验证失败，已登出");
            
            dealJsonReturn(request, response, returnJson.toJSON());
            
            return false;
        }
        
        Long dateTime = map.get(token);
        Long before5 = new Timestamp().getDateTime() - 300000;//5分钟为超时时间
        if(dateTime < before5) {
        	returnJson.setSuccess(false);
            returnJson.setMessage("登录验证失败，已超时");
            
            dealJsonReturn(request, response, returnJson.toJSON());
            
            return false;
        }
        
        returnJson.setSuccess(true);
        returnJson.setMessage("登录验证成功");
        
        //更新token的时间
        String newToken = Token.getToken();
        Map<String, Long> newMap = new HashMap<>();
        newMap.put(newToken, new Timestamp().getDateTime());
        session.setAttribute("token", newMap);
        //更新Token，防止重复提交
        response.setHeader("token", newToken);
        
        dealJsonReturn(request, response, returnJson.toJSON()); 
        
        return true;
    }
    
    /**
     * 处理json返回
     * @param request
     * @param response
     * @param json
     */
    public void dealJsonReturn(HttpServletRequest request, HttpServletResponse response, String json){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException ex) {
            logger.error("response error",ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
