package com.formssi.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.formssi.service.FileShareService;
/**
 * 项目启动的时候（登录的时候）初始化FileShareService
 * @author Administrator
 *
 */
@Component
public class SysInitBean implements InitializingBean, ServletContextAware {

	
	@Override
	public void setServletContext(ServletContext arg0) {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new FileShareService();
		FileShareService.initObj();
		for(int i = 0;i<100;i++){
			FileShareService.observeReqEvent(i);
			FileShareService.observeResRvent(i);
		}
	}

}
