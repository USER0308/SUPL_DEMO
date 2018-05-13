package com.formssi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bcos.web3j.protocol.core.DefaultBlockParameterName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.formssi.dao.FileDao;
import com.formssi.entity.FileReq;
import com.formssi.entity.ShareFile;
import com.formssi.service.FileService;
import com.formssi.service.FileShareService;

import utils.DowloadFileUtil;
//<<<<<<< HEAD
import utils.RSAUtils;
import utils.Utils;
//=======
import rx.Observable;
import wrapper.FileInfo.ResponseFileEventEventResponse;

@Service
public class FileServiceImpl implements FileService {

	private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	// 注入Service依赖
	@Autowired
	private FileDao fileDao;

	@Override
	public ShareFile getById(String fileId) {
		return fileDao.queryById(fileId);
	}
	
	private static Map<String, String> map = new HashMap<>();

	@Override
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void add(ShareFile shareFile,String upFileName) throws Exception {
		fileDao.add(shareFile);
    	String keyFilePath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/files/keys/";		//拼公钥的地址
		try {
			shareFile.setFileAddr(new String(RSAUtils.encryptByPublicKey(upFileName.getBytes(),Utils.fileRead(keyFilePath+shareFile.getUserId()+"PUBKEY"))));
			FileShareService.UploadFile(shareFile);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Upload File to blockchain filed!!");
			throw new Exception("add file to blockchain made a exception!");
		}
		
	}

	@Override
	public List<ShareFile> queryAll(ShareFile shareFile) {
		
		return fileDao.queryAll(shareFile);
	}

	/*@Override
	public void dowloadFile(ShareFile shareFile) {
		// TODO Auto-generated method stub
		//调区块链请求
		FileReq fReq = new FileReq();
		fReq.setFileId(shareFile.getFileId());
		ShareFile sFile=fileDao.queryById(shareFile.getFileId());
		fReq.setUserId(sFile.getUserId());	//根据fileId从数据库里面查  文件拥有者Id
		logger.info(shareFile.getUserId());
		fReq.setRequestId("REQ"+shareFile.getUserId()+System.currentTimeMillis());	//登录当前用户ID用来创建reqId
		fReq.setRequestTime(Utils.sdf(System.currentTimeMillis()));	//当前时间
		try {
			
			String reqFile = FileShareService.RequestFile(fReq);
			logger.info("--------reqFile:"+reqFile.substring(0, 1));
			//int a = Integer.parseInt(str);
			String state = reqFile.substring(0,1);
			String info = reqFile.substring(2);
			logger.info(state);
			logger.info(info);
			if (state.equals("0")) {
				logger.info("download failed!!!"+reqFile);
			}else if (state.equals("1")) {
				logger.info("策略验证已通过，正在请求文件下载!!!");
			}else{
				logger.info("未知错误!!!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void dowloadFile(ShareFile shareFile, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		//调区块链请求
		FileReq fReq = new FileReq();
		fReq.setFileId(shareFile.getFileId());
		ShareFile sFile=fileDao.queryById(shareFile.getFileId());
		fReq.setUserId(sFile.getUserId());	//根据fileId从数据库里面查  文件拥有者Id
		logger.info(shareFile.getUserId());
		fReq.setRequestId("REQ"+shareFile.getUserId()+System.currentTimeMillis());	//登录当前用户ID用来创建reqId
		fReq.setRequestTime(Utils.sdf(System.currentTimeMillis()));	//当前时间
		try {
			
			String reqFile = FileShareService.RequestFile(fReq);
			logger.info("--------reqFile:"+reqFile.substring(0, 1));
			//int a = Integer.parseInt(str);
			String state = reqFile.substring(0,1);
			String info = reqFile.substring(2);
			logger.info(state);
			logger.info(info);
			if (state.equals("0")) {
				logger.info("download failed!!!"+reqFile);
			}else if (state.equals("1")) {
				logger.info("策略验证已通过，正在请求文件下载!!!");
			}else{
				logger.info("未知错误!!!");
			}
			
			String key = shareFile.getFileId()+shareFile.getUserId();
			checkDowload(map.get(key), request, response);
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkDowload(String addr, HttpServletRequest request, HttpServletResponse response) throws InterruptedException{
		if(addr != null) {
			//用地址去下载文件
			try {
				DowloadFileUtil._downLoad(addr, request, response, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			Thread.sleep(1000);
			
			checkDowload(addr, request, response);
		}
	}
	
public static Observable<ResponseFileEventEventResponse> observeResRvent(int contractId) {
		
		Observable<ResponseFileEventEventResponse> resObservable = FileShareService.contractListOfObservable
				.get(contractId).responseFileEventEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
		resObservable.subscribe((response)->{
			logger.info("\n\n----------ResponseSucceedEvent---------");
			logger.info(""+response);
			logger.info(response.info.getValue());
			JSONObject resInfo = JSONObject.parseObject(response.info.getValue().toString());
			
			String PubKeyToSymkey = JSONObject.parseObject(resInfo.getString("response")).getString("_PubKeyToSymkey");
			String fileAddr = JSONObject.parseObject(resInfo.getString("response")).getString("_fileAddr");
			logger.info(fileAddr);
			//不用加密做测试
			String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/files/keys/"+resInfo.getString("userId")+"PRIKEY";//私钥路径
			try {
				String privateKey = Utils.fileRead(basePath);//读取获取私钥（base64格式）
//				//私钥解密PubKeyToSymkey（被加密的公共密钥）和fileAddr（加密地址）
				String dePubKeyToSymkey = new String(RSAUtils.decryptByPrivateKey(PubKeyToSymkey.getBytes(), privateKey));
				String deFileAddr = new String(RSAUtils.decryptByPrivateKey(fileAddr.getBytes(), privateKey));
				logger.info(deFileAddr);
				//用地址去下载文件
				//DowloadFileUtil.downLoad(deFileAddr);
				String key = JSONObject.parseObject(resInfo.getString("response")).getString("_fileId")+resInfo.getString("userId");
				map.put(key, deFileAddr);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
		return resObservable;
	}
}
