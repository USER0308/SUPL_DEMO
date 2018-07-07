package com.formssi.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import com.formssi.service.impl.IOUService;

public class WebInitializer implements WebApplicationInitializer {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onStartup(ServletContext servletContext) throws ServletException {
    	logger.info("容器启动初始化start...");

		try {
			IOUService.initObj();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("容器启动初始化end...");
    }
}