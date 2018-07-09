package com.formssi.filter;

/*
import org.springframework.stereotype.Component;
 
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 
 
/**
 * CorsFileter 功能描述：CORS过滤器
 *
 * @author RickyLee【l**@*.com.cn】
 * @date 2017/2/9 11:24
 */
/*
@Component
public class CorsFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "127.0.0.1:3000");
        //response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,Content-Type");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(servletRequest, servletResponse);
    }
 
    @Override
    public void destroy() {
 
    }
}
*/

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 该类用于跨域使用
 * @author Parallel
 *
 */
@Component
public class CorsFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 在服务器响应客户端的时候，带上Access-Control-Allow-Origin信息，允许特定的域名访问
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
        response.addHeader("Access-Control-Max-Age", "1800");//30 min
        
        // 
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);

	}

}