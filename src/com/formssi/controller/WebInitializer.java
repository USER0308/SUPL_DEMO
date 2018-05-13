package com.formssi.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.bcos.web3j.crypto.CipherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import com.formssi.service.FileShareService;
import com.formssi.service.impl.FileServiceImpl;

public class WebInitializer implements WebApplicationInitializer {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onStartup(ServletContext servletContext) throws ServletException {
    	logger.info("容器启动初始化...");

        new FileShareService();
		try {
			FileShareService.initObj();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CipherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<100;i++){
			FileShareService.observeReqEvent(i);
			FileServiceImpl.observeResRvent(i);
		}
    }
}