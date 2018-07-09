package com.formssi.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bcos.channel.client.Service;
import org.bcos.channel.handler.ChannelConnections;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Int256;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.WalletUtils;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.protocol.http.HttpService;
import org.bcos.web3j.protocol.parity.Parity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.IouRecord;
import com.formssi.entity.Transaction;
import com.formssi.service.IIouLimitEntityService;
import com.formssi.service.IIouRecordService;
import com.formssi.service.ITransactionService;

import exception.initConfigException;
import utils.PropertiesUtil;
import utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;



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
	
	private static Web3j web3j = null;
	private static AbstractApplicationContext Context = null;
	private static Credentials credentials = null;
	
	@Autowired
	private static IIouLimitEntityService iouLimitEntityServiceImpl;
	@Autowired
	private static IIouRecordService iouRecordServiceImpl;
	@Autowired
	private static ITransactionService transactionServiceImpl;
	@Autowired
	private static SuplInfo contractTransaction;
	
	private static List<String> contractAddressList = new ArrayList<>();
	
	private static String subPath = null;
	
	public static void initObj() throws Exception{
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		subPath = path.substring(5, path.length());
		
		logger.info("开始测试...");
		logger.info("===================================================================");

		// 读取applicationContext.xml里的配置信息
		Context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		System.out.println("context is:" + Context);
		web3j = buildWeb3jByRPC(Context);
//		web3j = buildWeb3j(context);
		
		try{
    		if(web3j == null){
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
    	
    	for(int i = 0; i<1; i++){
    		String contractAddress = PropertiesUtil.readValue("contract.address."+i);
    		contractAddressList.add(contractAddress);
    		contractTransaction=SuplInfo.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
    	}
    	logger.info("初始化结束");
    	
	}
	
	private static Web3j buildWeb3jByRPC(AbstractApplicationContext context) {
		
		// 获取一个Service的实例
		System.out.println("context is:" + context);
		Service service = context.getBean(Service.class);
		System.out.println("service is:" + service);				
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
	
	private static Web3j buildWeb3jByAMOP(AbstractApplicationContext transactionContext) throws InterruptedException {
		Service service = transactionContext.getBean(Service.class);
		try {
			service.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(3000);
		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		//使用AMOP消息信道初始化web3j
		return Web3j.build(channelEthereumService);
	}

	public static Web3j buildWeb3j(AbstractApplicationContext context) throws Exception{
	//初始化 Service
	Service service = context.getBean(Service.class);
	service.run();
	Thread.sleep(3000);
	System.out.println("开始测试...");
	System.out.println("===================================================================");
	System.out.println("初始化 AOMP 的 ChannelEthereumService");
	ChannelEthereumService channelEthereumService = new ChannelEthereumService();
	channelEthereumService.setChannelService(service);
	//使用 AMOP 消息信道初始化 web3j
	Web3j web3 = Web3j.build(channelEthereumService);
	return web3;
	}
	
	public static void queryBlockNumber() throws IOException{
		BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
		logger.info("blockNumber:{}",blockNumber.intValue());
	}

//	@Service("addTransaction")
	
//	public static boolean checkPassword(String orgID,String password) {
//		boolean isSuccess = iouLimitEntityServiceImpl.checkPasswordByOrgID(password, orgID);
//		if(isSuccess) {
//			System.out.println("登录成功");
//			return true;
//		}else {
//			System.out.println("登录失败");
//			return false;
//		}
//	}
	
	public static String initIouLimitData(String orgID,String orgName,String password,int iouLimit) throws InterruptedException, ExecutionException {
//		IouLimitEntity iouLimitEntity = new IouLimitEntity();
	    long now=System.currentTimeMillis();
		String createTime = Utils.sdf(now);
//		iouLimitEntity.setOrgID(orgID);
//		iouLimitEntity.setOrgName(orgName);
//		iouLimitEntity.setPassword(password);
//		iouLimitEntity.setIouLimit(iouLimit);
//		iouLimitEntity.setCreateTime(createTime);
//		iouLimitEntity.setUpdateTime(createTime);
//		//检查一下本地有没有已经注册了的机构
//		
//		boolean isSuccess = iouLimitEntityServiceImpl.addIouLimitEntity(orgID,orgName,password,iouLimit);
//		if(!isSuccess) {
//			//创建失败
//			return "创建失败,已经有该机构";
//		}
		//区块链上新建
		TransactionReceipt receipt = contractTransaction.initIouLimitData( new Utf8String(orgID),
				new Utf8String(orgName),
				new Int256(iouLimit),
				new Utf8String(createTime),
				new Utf8String(createTime)
				).get();
		List<InitIouLimitDataEventResponse> responses = contractTransaction.getInitIouLimitDataEvents(receipt);
		String result=responses.get(0)._json.toString(); 
		return result;
	}
	public static String setIouLimit(String orgID,int amount) throws InterruptedException, ExecutionException {
		//设置数据库的limit
		long now=System.currentTimeMillis();
		String updateTime = Utils.sdf(now); // 获取当前时间
		//iouLimitEntityServiceImpl.setIouLimit(amount, updateTime, orgID);
		//更改区块链上的
		TransactionReceipt receipt = contractTransaction.setIouLimit(new Utf8String(orgID), new Int256(amount)).get();
		logger.info("setIouLimit receipt transactionHash:{}",receipt.getTransactionHash());
		List<SetIouLimitEventResponse> responses = contractTransaction.getSetIouLimitEvents(receipt);
		String result=responses.get(0)._json.toString(); 
		return result;
	}
	public static String getIouLimit(String orgID) throws InterruptedException, ExecutionException {
		// 直接从后端数据库查询
		int amount = iouLimitEntityServiceImpl.getIouLimit(orgID);
		//应该删掉以下的代码
//		String receipt = contractTransaction.getIouLimit(new Utf8String(orgID)).get().getValue();
//		logger.info("getIouLimit receipt transactionHash:{}",receipt);
		return ""+amount;
	}
	public static List<IouRecord> getIouList(int pageNo,int pageSize) throws InterruptedException, ExecutionException {
		// 直接从后端数据库查询
		return iouRecordServiceImpl.getIouRecordList(pageNo,pageSize);
		//应该删掉以下的代码
//		String receipt = contractTransaction.getIouList(new Uint256(pageNo),new Uint256(pageSize)).get().getValue();
//		logger.info("getIouList receipt transactionHash:{}",receipt);
//		return receipt;
	}
//	public static String addIouRecord(IouRecord iouRecord) {
//		TransactionReceipt receipt = contractTransaction.addIou(new Utf8String(iouRecord.getIouId()),
//				new Utf8String(iouRecord.getFromOrg()),
//				new Utf8String(iouRecord.getRecvOrg()),
//				new Utf8String(iouRecord.getTransTime()),
//				new Int256(iouRecord.getAmount()),
//				new Int256(iouRecord.getPaidAmt()),
//				new Utf8String(iouRecord.getIouStatus()),
//				new Utf8String(iouRecord.getTransTime()),
//				new Utf8String(iouRecord.getUpdateTime()))
//		logger.info("iouRecycle receipt transactionHash:{}",receipt);
//		List<IouAddIouEventResponse> responses = contractTransaction.getAddIouEvents(receipt);
//		String result=responses.get(0)._json.toString(); 
//		return result;
//	}
	public static String iouRecycle(String iouId,int amount) throws InterruptedException, ExecutionException {
		// 修改后端数据库
//		boolean isSuccess = iouLimitEntityServiceImpl.recycleIou(iouId,amount);
//		if(!isSuccess) {
//			System.out.println("回收白条失败");
//		}
		//更新区块链
		TransactionReceipt receipt = contractTransaction.iouRecycle(new Utf8String(iouId),new Int256(amount)).get();
		logger.info("iouRecycle receipt transactionHash:{}",receipt);
		List<IouRecycleEventResponse> responses = contractTransaction.getIouRecycleEvents(receipt);
		String result=responses.get(0)._json.toString(); 
		return result;
	}
	
	public static String addTransaction(String saleOrg,String buyOrg,String transType,long amount,String latestStatus,String conID,String conHash) throws InterruptedException, ExecutionException{
		             // 合同hash
	    long now=System.currentTimeMillis();
		String transTime = Utils.sdf(now);
//		String conID="conID"+transTime;               // 合同号
//	    String conHash="conHash";
//		Transaction transaction = new Transaction();
//		transaction.setConID(conID);
//		transaction.setSaleOrg(saleOrg);
//		transaction.setBuyOrg(buyOrg);
//		transaction.setTransType(transType);
//		transaction.setAmount(amount);
//		transaction.setLatestStatus(latestStatus);
//		transaction.setConHash(conHash);
//		transaction.setTransTime(transTime);
//		transaction.setUpdateTime(transTime);
//		//修改后端数据库
//		boolean isSuccess = transactionServiceImpl.addTransactionRecord(transaction);
//		if(!isSuccess) {
//			System.out.println("录入出错");
//			return "录入交易出错";
//		}
		//更新区块链
		logger.info("transaction's conId is "+conID);
		TransactionReceipt receipt = contractTransaction.addTransaction(new Utf8String(conID),
				new Utf8String(saleOrg),
				new Utf8String(buyOrg),
				new Utf8String(transType),
				new Int256(amount), 
				new Utf8String(conHash),
				new Utf8String(latestStatus),
				new Utf8String(transTime),
				new Utf8String(transTime)).get();
		logger.info("addTransaction receipt transactionHash:{}",receipt.getTransactionHash());
		logger.info("receipt's contract addr is ?"+receipt.getContractAddress());
		List<AddTransactionEventResponse> responses = contractTransaction.getAddTransactionEvents(receipt);
		logger.info("responses size is"+responses.size());
		String result=responses.get(0)._json.toString(); 
		return result;
	}
	
	public static int updateTransStatus(String conId, String status) throws InterruptedException, ExecutionException {
		//更新数据库
//		boolean isSuccess = transactionServiceImpl.updateTransactionStatusByConId(conId, status);
//		if(!isSuccess) {
//			System.out.println("更新交易出错");
//			return -1;
//		}
		//更新区块链
		if(!TRAN_LATESTSTATE_LIST.contains(status)) {
			logger.info("the value of IOU Status must be C or U.");
			return -1;
		}
		TransactionReceipt receipt = contractTransaction.updateTransStatus(new Utf8String(conId), new Utf8String(status)).get();
		logger.info("updateTransStatus receipt transactionHash:{}",receipt.getTransactionHash());
		List<UpdateTransStatusResultEventResponse> responses = contractTransaction.getUpdateTransStatusResultEvents(receipt);
		int result = responses.get(0)._code.getValue().intValue();
		return result;
	}
	
	public static Transaction queryTransactionByConId(String conId) throws InterruptedException, ExecutionException{
		// 直接从后端数据库查询
		Transaction transaction = transactionServiceImpl.getTransactionByConId(conId);
//		if(transaction==null) {
//			// 不存在该交易的话返回null	
//		}
		// transaction to json
		return transaction;
		//应该删掉以下的代码
//		Future<Utf8String> result = contractTransaction.queryTransByConId(new Utf8String(conId));
//		logger.info("queryTransaction result is:{}",result.get());
//		return result.get();
	}
	
	public static List<Transaction> queryTransList(int pageNo,int pageSize) throws InterruptedException, ExecutionException {
		// 直接从后端数据库查询
		List<Transaction> result = transactionServiceImpl.getAllTransaction();
		// transaction to json
		return result;
		//应该删掉以下的代码
//		String receipt = contractTransaction.queryTransList(new Uint256(pageNo),new Uint256(pageSize)).get().getValue();
//		logger.info("queryTransList receipt transactionHash:{}",receipt);
//		return receipt;
	}
	public static int getIouLength() throws InterruptedException, ExecutionException {
		// 直接从后端数据库查询   TODO
		List<IouRecord> result = iouRecordServiceImpl.getAllIouRecord();
		return result.size();
		//应该删掉以下的代码
//		Uint256 receipt = contractTransaction.getIouLength().get();
//		logger.info("getIouLength receipt transactionHash:{}",receipt);
//		return receipt.getValue().toString();
	}
	
	public static int getTransLength() throws InterruptedException, ExecutionException {
		// 直接从后端数据库查询  TODO
		List<Transaction> result = transactionServiceImpl.getAllTransaction();
		return result.size();
		//应该删掉以下的代码
//		Uint256 receipt = contractTransaction.getTransLength().get();
//		logger.info("getTransLength receipt transactionHash:{}",receipt);
//		return receipt.getValue().toString();
	}
	public static Utf8String getVersion() throws InterruptedException, ExecutionException{
		Future<Utf8String> result = contractTransaction.getVersion();
		System.out.println("result is null?");
		System.out.println(result==null);
		System.out.println("result is ");
		System.out.println(result.get().getValue());
		logger.info("version is ",result.get());
//		result.
		return result.get();
	}
	
	public static void checkParam() {
		System.out.println("web3j is null?" + web3j==null);
		System.out.println("contractTransaction is null?" + contractTransaction==null);
	}
}
