package com.formssi.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.channel.client.Service;
import org.bcos.channel.handler.ChannelConnections;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Int256;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.CipherException;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.WalletUtils;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.protocol.http.HttpService;
import org.bcos.web3j.protocol.parity.Parity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

//import com.formssi.entity.IOU;
//import com.formssi.entity.Organ;
//import com.formssi.entity.Transaction;

import exception.initConfigException;
import utils.PropertiesUtil;
import wrapper.SuplInfo;
import wrapper.SuplInfo.AddTransactionEventResponse;
import wrapper.SuplInfo.InitIouLimitDataEventResponse;
import wrapper.SuplInfo.IouRecycleEventResponse;
import wrapper.SuplInfo.SetIouLimitEventResponse;
import wrapper.SuplInfo.UpdateTransStatusResultEventResponse;

@Controller
public class IOUService {
	static Logger logger = LoggerFactory.getLogger(IOUService.class);
	static private final List<String> IOU_STATELIST= Arrays.asList("U","C","P");
	static private final List<String> TRAN_LATESTSTATE_LIST= Arrays.asList("U","C");
	// 初始化交易参数
	private static BigInteger gasPrice = new BigInteger("99999999999");
	private static BigInteger gasLimit = new BigInteger("9999999999999");
	private static BigInteger initialWeiValue = new BigInteger("0");
	
	private static Web3j transactionWeb3 = null;
	private static Web3j observableWeb3 = null;
	private static AbstractApplicationContext transactionContext = null;
	private static AbstractApplicationContext observableContext = null;
	private static Credentials credentials = null;

//	private static List<SuplInfo> contractListOfTransaction = new ArrayList<>();
	@Autowired
	private static SuplInfo contractTransaction;
	
	private static List<SuplInfo> contractListOfObservable = new ArrayList<>();
	private static List<String> contractAddressList = new ArrayList<>();
	
	private static String subPath = null;
	//线程池
//	private static ThreadPoolTaskExecutor threadPool;
	
	public static void initObj() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, ExecutionException, IOException, CipherException{
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		subPath = path.substring(5, path.length());//  /C:/eclipse-workspace/HPCDemo2/bin/
		
		logger.info("开始测试...");
		logger.info("===================================================================");

		// 读取applicationContext.xml里的配置信息
		transactionContext = new ClassPathXmlApplicationContext("classpath:applicationContextOfTransaction.xml");
		observableContext = new ClassPathXmlApplicationContext("classpath:applicationContextOfObservable.xml");
		
		transactionWeb3 = buildWeb3j(transactionContext);
		observableWeb3 = buildWeb3j(observableContext);
		
//		threadPool = (ThreadPoolTaskExecutor)transactionContext.getBean("pool");
		
		try{
    		if(transactionWeb3 == null || observableWeb3 == null){
    			throw new initConfigException("初始化web3j失败");
        	}
    	} catch(initConfigException e){
    		e.printStackTrace();
    	}
        
		// 初始化交易签名私钥
		PropertiesUtil.readFile("wallet.properties");
        credentials = WalletUtils.loadCredentials(
                PropertiesUtil.readValue("wallet.password"),
                subPath+PropertiesUtil.readValue("wallet.path"));
        logger.info("credentials address:{}",credentials.getAddress());
    	
    	// 加载已部署的合约
    	PropertiesUtil.readFile("config.properties");
    	
    	String contractAddress = PropertiesUtil.readValue("contract.address.0");
    	contractTransaction=SuplInfo.load(contractAddress, transactionWeb3, credentials, gasPrice, gasLimit);
    	
    	//读取合约地址的。如有多个合约地址，使用如下代码
//    	for(int i = 0; i<10; i++){
//    		String contractAddress = PropertiesUtil.readValue("contract.address."+i);
////    		logger.info("contractAddress:{}",contractAddress);
//    		contractAddressList.add(contractAddress);
//    		contractTransaction=SuplInfo.load(contractAddress, transactionWeb3, credentials, gasPrice, gasLimit);
////    		contractListOfTransaction.add(UserInfo.load(contractAddress,transactionWeb3, credentials, gasPrice, gasLimit));
////    		contractListOfObservable.add(UserInfo.load(contractAddress,observableWeb3, credentials, gasPrice, gasLimit));
//    	}
    	logger.info("初始化结束");
    	
	}
	
	private static Web3j buildWeb3j(AbstractApplicationContext context) {
		
		// 获取一个Service的实例
		Service service = context.getBean(Service.class);
						
		context.close();
				
		ChannelConnections fromChannelConnections = service.getAllChannelConnections().get(service.getOrgID());
		List<String> list = fromChannelConnections.getConnectionsStr();
		for(String str:list){
			System.out.println("ConnectionsStr:"+str);
		}
		String[] split1 = list.get(0).split("@");
		String targetaddress = "";
		if (split1.length > 1) {
			targetaddress = split1[1];
		}
				
		HttpService httpService = new HttpService("http://"+targetaddress);
		Web3j Web3 = Parity.build(httpService);
		
		return Web3;
	}

	
	public static void queryBlockNumber() throws IOException{
		BigInteger blockNumber = transactionWeb3.ethBlockNumber().send().getBlockNumber();
		logger.info("blockNumber:{}",blockNumber.intValue());
	}
	///////////////////////////////一下内容为sdk的实际方法需要全部修改成新项目的方法////////////////////////////////////
	
//	public static int addTransaction(Transaction tran) throws InterruptedException, ExecutionException{
//		
//		if(!TRAN_LATESTSTATE_LIST.contains(tran.getLatestStatus())) {
//			logger.info("the value of Tran Status must be C or U.");
//			return -1;
//		}
//		TransactionReceipt receipt = contractTransaction.addTransaction(new Utf8String(tran.getConID()), new Utf8String (tran.getSaleOrg()), new Utf8String (tran.getBuyOrg()), 
//				new Utf8String (tran.getTransType()), new Int256 (tran.getAmount()), new Utf8String (tran.getConHash()), new Utf8String (tran.getLatestStatus()), new Utf8String (tran.getTransTime()), new Utf8String (tran.getUpdateTime())).get();
//		logger.info("addTransaction receipt transactionHash:{}",receipt.getTransactionHash());
//		//TODO AddTransactionResultEventResponse 事件添加
////		List<AddTransactionResultEventResponse> responses = contractTransaction.getAddTransactionResultEvents(receipt);
////		System.out.println(responses.size());
//		List<AddTransactionEventResponse> responses = contractTransaction.getAddTransactionEvents(receipt);
//		System.out.println(responses.size()+"   |   "+responses.get(0)._json.getValue());
////		logger.info("result responses.get(0)._code.getValue():{}",responses.get(0)._code.getValue());
////		int result = responses.get(0)._code.getValue().intValue();
////		return result;
//		return 1;
//	}
//	
//	
//	
//	public static int updateTransStatus(Transaction tran) throws InterruptedException, ExecutionException{
//		if(!TRAN_LATESTSTATE_LIST.contains(tran.getLatestStatus())) {
//			logger.info("the value of IOU Status must be C or U.");
//			return -1;
//		}
//		TransactionReceipt receipt = contractTransaction.updateTransStatus(new Utf8String(tran.getConID()), new Utf8String(tran.getLatestStatus())).get();
//		logger.info("updateTransStatus receipt transactionHash:{}",receipt.getTransactionHash());
//		List<UpdateTransStatusResultEventResponse> responses = contractTransaction.getUpdateTransStatusResultEvents(receipt);
//		int result = responses.get(0)._code.getValue().intValue();
//		return result;
//	}
//	
//	public static String setIouLimit(Organ org) throws InterruptedException, ExecutionException{
//		TransactionReceipt receipt = contractTransaction.setIouLimit(new Utf8String(org.getOrgID()), new Int256(org.getIouLimit())).get();
//		logger.info("setIouLimit receipt transactionHash:{}",receipt.getTransactionHash());
//		List<SetIouLimitEventResponse> responses = contractTransaction.getSetIouLimitEvents(receipt);
//		String result=responses.get(0)._json.toString(); 
//		return result;
//	}
//	
//	public static String initIouLimitData(Organ org) throws InterruptedException, ExecutionException{
//		TransactionReceipt receipt = contractTransaction.initIouLimitData(new Utf8String(org.getOrgID()), new Utf8String(org.getOrgName()),new Int256(org.getIouLimit()), new Utf8String(org.getCreateTime()), new Utf8String(org.getUpdateTime())).get();
//		logger.info("initIouLimitData receipt transactionHash:{}",receipt.getTransactionHash());
//		List<InitIouLimitDataEventResponse> responses = contractTransaction.getInitIouLimitDataEvents(receipt);
//		String result=responses.get(0)._json.toString(); 
//		return result;
//	}
//	
//	public static String queryTransByConId(String conID) throws InterruptedException, ExecutionException{
//		String receipt = contractTransaction.queryTransByConId(new Utf8String(conID)).get().getValue();
//		logger.info("queryTransByConId receipt transactionHash:{}",receipt);
//		return receipt;
//	}
//	
//	public static String getIouLimit(IOU iou) throws InterruptedException, ExecutionException{
//		String receipt = contractTransaction.getIouLimit(new Utf8String(iou.getFromOrg())).get().getValue();
//		logger.info("getIouLimit receipt transactionHash:{}",receipt);
//		return receipt;
//	}
//	
//	public static String getIouList(int pageNo,int pageSize) throws InterruptedException, ExecutionException{
//		String receipt = contractTransaction.getIouList(new Uint256(pageNo),new Uint256(pageSize)).get().getValue();
//		logger.info("getIouList receipt transactionHash:{}",receipt);
//		return receipt;
//	}
//	public static String queryTransList(int pageNo,int pageSize) throws InterruptedException, ExecutionException{
//		String receipt = contractTransaction.queryTransList(new Uint256(pageNo),new Uint256(pageSize)).get().getValue();
//		logger.info("queryTransList receipt transactionHash:{}",receipt);
//		return receipt;
//	}
//	public static String iouRecycle(IOU iou) throws InterruptedException, ExecutionException{
//		TransactionReceipt receipt = contractTransaction.iouRecycle(new Utf8String(iou.getIouId()),new Int256(iou.getPaidAmt())).get();
//		logger.info("iouRecycle receipt transactionHash:{}",receipt);
//		List<IouRecycleEventResponse> responses = contractTransaction.getIouRecycleEvents(receipt);
//		String result=responses.get(0)._json.toString(); 
//		return result;
//	}
	
}
