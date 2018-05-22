package com.formssi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
import com.sun.jmx.snmp.Timestamp;

import rx.Observable;
import utils.RSAUtils;
import utils.Utils;
import wrapper.FileInfo.ResponseFileEventEventResponse;

@Service
public class FileServiceImpl implements FileService {

	private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	private static Map<String, String> fileAddrMap = new HashMap();

	// 注入Service依赖
	@Autowired
	private FileDao fileDao;

	@Override
	public ShareFile getById(String fileId) {
		return fileDao.queryById(fileId);
	}

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

	@Override
	public String dowloadFile(ShareFile shareFile) throws Exception {
		
		String fileAddrReturn = null;
		// TODO Auto-generated method stub
		//调区块链请求
		Observable<ResponseFileEventEventResponse> resObservable =  FileShareService.observeResRvent(shareFile.getUserId());
		FileReq fReq = new FileReq();
		fReq.setFileId(shareFile.getFileId());
		fReq.setUserId(shareFile.getUserId());	//请求人ID
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
				
				resObservable.subscribe((response) -> {
				logger.info("\n\n----------ResponseSucceedEvent---------");
				logger.info("" + response);
				logger.info(response.info.getValue());
				JSONObject resInfo = JSONObject.parseObject(response.info.getValue().toString());
	
				String PubKeyToSymkey = JSONObject.parseObject(resInfo.getString("response")).getString("_PubKeyToSymkey");
				String fileAddr = JSONObject.parseObject(resInfo.getString("response")).getString("_fileAddr");
				logger.info(fileAddr);
				// 不用加密做测试
				String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/files/keys/"
						+ resInfo.getString("userId") + "PRIKEY";// 私钥路径
				try {
					String privateKey = Utils.fileRead(basePath);// 读取获取私钥（base64格式）
					// //私钥解密PubKeyToSymkey（被加密的公共密钥）和fileAddr（加密地址）
					String dePubKeyToSymkey = new String(
							RSAUtils.decryptByPrivateKey(PubKeyToSymkey.getBytes(), privateKey));
					String deFileAddr = new String(RSAUtils.decryptByPrivateKey(fileAddr.getBytes(), privateKey));
					logger.info(deFileAddr);
					// 用地址去下载文件
//					DowloadFileUtil.downLoad(deFileAddr);
					String key = JSONObject.parseObject(resInfo.getString("response")).getString("_fileId") + shareFile.getUserId();
					fileAddrMap.put(key, deFileAddr);
					System.out.println("deFileAddr: " + deFileAddr);
				} catch (Exception e) {
	
					e.printStackTrace();
				}
			});
			}else{
				logger.info("未知错误!!!");
			}
			
			String key = shareFile.getFileId() + shareFile.getUserId();
			if(fileAddrMap.get(key) == null) {
				boolean b = deepCheckAddr(key, new Timestamp().getDateTime());
				if(b) {
					fileAddrReturn = fileAddrMap.get(key);
					logger.info(fileAddrReturn);
					System.out.println("fileAddrReturn: " + fileAddrReturn);
				}else {
					logger.info("下载超时");
					throw new Exception("下载超时");
				}
			}else {
				fileAddrReturn = fileAddrMap.get(key);
				logger.info(fileAddrReturn);
				System.out.println("fileAddrReturn: " + fileAddrReturn);
			}
		
		
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileAddrReturn;
	}
	
	private static boolean deepCheckAddr(String key, Long startTime) {
		if(fileAddrMap.get(key) == null) {
			Long currTime = new Timestamp().getDateTime();
			logger.info("deepCheckAddr 毫秒数: " + (currTime - startTime));
			//30秒为超时
			if(currTime - startTime > 30000) {
				return false;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return deepCheckAddr(key, startTime);
		}else {
			return true;
		}
	}

}
