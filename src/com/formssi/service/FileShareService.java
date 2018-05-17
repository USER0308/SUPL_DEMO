package com.formssi.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.utils.DateUtils;
import org.bcos.channel.client.Service;
import org.bcos.channel.handler.ChannelConnections;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Int256;
import org.bcos.web3j.crypto.CipherException;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.WalletUtils;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.bcos.web3j.protocol.core.DefaultBlockParameterName;
import org.bcos.web3j.protocol.core.methods.response.EthBlock;
import org.bcos.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.bcos.web3j.protocol.core.methods.response.Log;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.protocol.http.HttpService;
import org.bcos.web3j.protocol.parity.Parity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.formssi.entity.Block;
import com.formssi.entity.FileReq;
import com.formssi.entity.FileRes;
import com.formssi.entity.ShareFile;
import com.formssi.entity.User;

import exception.initConfigException;
import rx.Observable;
import utils.PropertiesUtil;
import utils.RSAUtils;
import utils.Utils;
import wrapper.FileInfo;
import wrapper.FileInfo.NewFileEventResponse;
import wrapper.FileInfo.NewUserEventResponse;
import wrapper.FileInfo.RequestFileEventEventResponse;
import wrapper.FileInfo.ResponseFileEventEventResponse;

@Controller
public class FileShareService {
	static Logger logger = LoggerFactory.getLogger(FileShareService.class);
	// 初始化交易参数
	private static BigInteger gasPrice = new BigInteger("99999999999");
	private static BigInteger gasLimit = new BigInteger("9999999999999");
	private static BigInteger initialWeiValue = new BigInteger("0");

	private static Web3j transactionWeb3 = null;
	private static Web3j observableWeb3 = null;
	private static AbstractApplicationContext transactionContext = null;
	private static AbstractApplicationContext observableContext = null;
	private static Credentials credentials = null;

	public static List<FileInfo> contractListOfObservable = new ArrayList<>();
	private static List<String> contractAddressList = new ArrayList<>();

	private static String subPath = null;

	public static void initObj() throws InterruptedException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchProviderException, ExecutionException, IOException, CipherException {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		subPath = path.substring(5, path.length());// /C:/eclipse-workspace/HPCDemo2/bin/

		logger.info("开始测试...");
		logger.info("===================================================================");

		// 读取applicationContext.xml里的配置信息
		transactionContext = new ClassPathXmlApplicationContext("classpath:applicationContextOfTransaction.xml");
		observableContext = new ClassPathXmlApplicationContext("classpath:applicationContextOfObservable.xml");

		transactionWeb3 = buildWeb3j(transactionContext);
		observableWeb3 = buildWeb3j(observableContext);

		try {
			if (transactionWeb3 == null || observableWeb3 == null) {
				throw new initConfigException("初始化web3j失败");
			}
		} catch (initConfigException e) {
			e.printStackTrace();
		}

		// 初始化交易签名私钥
		PropertiesUtil.readFile("wallet.properties");
		credentials = WalletUtils.loadCredentials(PropertiesUtil.readValue("wallet.password"),
				subPath + PropertiesUtil.readValue("wallet.path"));
		logger.info("credentials address:{}", credentials.getAddress());

		// 加载已部署的合约
		PropertiesUtil.readFile("config.properties");

		// 读取合约地址的。如有多个合约地址，使用如下代码
		for (int i = 0; i < 100; i++) {
			String contractAddress = PropertiesUtil.readValue("contract.address." + i);
			contractAddressList.add(contractAddress);
			contractListOfObservable
					.add(FileInfo.load(contractAddress, transactionWeb3, credentials, gasPrice, gasLimit));
		}
		logger.info("初始化结束");

	}

	private static Web3j buildWeb3j(AbstractApplicationContext context) {

		// 获取一个Service的实例
		Service service = context.getBean(Service.class);

		context.close();

		ChannelConnections fromChannelConnections = service.getAllChannelConnections().get(service.getOrgID());
		List<String> list = fromChannelConnections.getConnectionsStr();
		for (String str : list) {
			System.out.println("ConnectionsStr:" + str);
		}
		String[] split1 = list.get(0).split("@");
		String targetaddress = "";
		if (split1.length > 1) {
			targetaddress = split1[1];
		}

		HttpService httpService = new HttpService("http://" + targetaddress);
		Web3j Web3 = Parity.build(httpService);

		return Web3;
	}

	private static int choiceContract(String _id) {
		if (_id.length() < 2) {
			_id = "00" + _id;
		} else if (_id.length() == 2) {
			_id = "0" + _id;
		} else {
		}
		String lastChar = _id.substring(_id.length() - 1, _id.length());
		int last2Char;
		if (lastChar.equals("X")) {
			last2Char = Integer.parseInt(_id.substring(_id.length() - 3, _id.length() - 1));
		} else {
			last2Char = Integer.parseInt(_id.substring(_id.length() - 2, _id.length()));
		}
		return last2Char;
	}

	public static void queryBlockNumber() throws IOException {
		BigInteger blockNumber = transactionWeb3.ethBlockNumber().send().getBlockNumber();
		logger.info("blockNumber:{}", blockNumber.intValue());
	}

	///////////////////////////////////////////////////////////////////////////////////////////

	// Y
	public static String addUser(User user) throws InterruptedException, ExecutionException {
		int contractId = choiceContract(user.getUserId());// 通过身份证号码来选择哪个合约
		System.out.println(contractListOfObservable.size());
		// FileInfo testList=contractListOfObservable.get(contractId);
		TransactionReceipt receipt = contractListOfObservable.get(contractId)
				.addUser(new Utf8String(user.getUserId()), new Utf8String(user.getPubKey()), new Int256(user.getRank()),
						new Utf8String(Integer.toString(user.getDepartment())), new Utf8String(user.getCreateTime()),
						new Utf8String(user.getUpdateTime()))
				.get();
		logger.info("addUser receipt transactionHash:{}", receipt.getTransactionHash());

		List<NewUserEventResponse> responses = contractListOfObservable.get(contractId).getNewUserEvents(receipt);
		String result = responses.get(0)._json.toString();
		return result;
	}

	// Y
	public static String UploadFile(ShareFile sf) throws InterruptedException, ExecutionException {
		int contractId = choiceContract(sf.getUserId());// 通过身份证号码来选择哪个合约

		StringBuffer strategy = new StringBuffer(Integer.toString(sf.getAllowRank()));
		strategy.append("," + sf.getAllowDep());

		TransactionReceipt receipt = contractListOfObservable.get(contractId)
				.UploadFile(new Utf8String(sf.getFileId()), new Utf8String(sf.getFileAddr()),
						new Utf8String(sf.getPubKeyToSymkey()), new Utf8String(strategy.toString()),
						new Utf8String(sf.getDescription()), new Utf8String(Utils.sdf(System.currentTimeMillis())),
						new Utf8String(sf.getUserId()), new Utf8String(Integer.toString(sf.getDepartment())))
				.get();
		logger.info("UploadFile receipt transactionHash:{}", receipt.getTransactionHash());

		List<NewFileEventResponse> responses = contractListOfObservable.get(contractId).getNewFileEvents(receipt);
		String result = responses.get(0)._json.toString();
		return result;
	}

	// Y
	public static String RequestFile(FileReq fReq) throws InterruptedException, ExecutionException {
		int contractId = choiceContract(fReq.getUserId());// 通过身份证号码来选择哪个合约

		TransactionReceipt receipt = contractListOfObservable.get(contractId)
				.RequestFile(new Utf8String(fReq.getUserId()), new Utf8String(fReq.getFileId()),
						new Utf8String(fReq.getRequestId()), new Utf8String(fReq.getRequestTime()))
				.get();
		logger.info("RequestFile receipt transactionHash:{}", receipt.getTransactionHash());

		List<RequestFileEventEventResponse> responses = contractListOfObservable.get(contractId)
				.getRequestFileEventEvents(receipt);
		logger.info("response size==>", responses.size());
		String state = responses.get(0).state.toString();
		String info = responses.get(0).info.toString();
		return state + "|-|" + info;
	}

	public static String ResponseFile(FileRes fRes, String userId) throws InterruptedException, ExecutionException {
		int contractId = choiceContract(userId);// 通过身份证号码来选择哪个合约

		TransactionReceipt receipt = contractListOfObservable.get(contractId)
				.ResponseFile(new Utf8String(fRes.getResponseId()), new Utf8String(fRes.getRequestId()),
						new Utf8String(fRes.getFileId()), new Utf8String(fRes.getPubKeyToSymkey()),
						new Utf8String(fRes.getFileAddr()))
				.get();
		logger.info("ResponseFile receipt transactionHash:{}", receipt.getTransactionHash());

		List<ResponseFileEventEventResponse> responses = contractListOfObservable.get(contractId)
				.getResponseFileEventEvents(receipt);
		String result = responses.get(0).info.toString();
		return result;
	}

	public static Observable<RequestFileEventEventResponse> observeReqEvent(int contractId) {

		Observable<RequestFileEventEventResponse> reqObservable = contractListOfObservable.get(contractId)
				.requestFileEventEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
		reqObservable.subscribe((response) -> {
			logger.info("\n\n----------RequestSucceedEvent---------");
			logger.info(response.info.getValue());
			JSONObject resInfo = JSONObject.parseObject(response.info.getValue().toString());

			if (response.state.getValue().equals("1")) {
				try {

					String PubKeyToSymkey = resInfo.getString("PubKeyToSymkey");
					String pubKey = resInfo.getString("pubKey");
					String fileAddr = resInfo.getString("fileAddr");
					// 私钥解密公共密钥PubKeyToSymkey和文件地址fileAddr
					// 并用公钥pubKey加密公共密钥和文件地址fileAddr
					String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
							+ "/files/keys/" + resInfo.getString("userId") + "PRIKEY";// 私钥路径
					System.out.println("---------basePath:" + basePath);

					String privateKey = Utils.fileRead(basePath);// 读取获取私钥（base64格式）
					byte[] dePubKeyToSymkey = RSAUtils.decryptByPrivateKey(PubKeyToSymkey.getBytes(), privateKey);
					String enPubKeyToSymkey = new String(
							Base64.getEncoder().encode(RSAUtils.encryptByPublicKey(dePubKeyToSymkey, pubKey)));

					byte[] deFileAddr = RSAUtils.decryptByPrivateKey(fileAddr.getBytes(), privateKey);
					String enFileAddr = new String(RSAUtils.encryptByPublicKey(deFileAddr, pubKey));
					// 不用加密做测试
					// String enPubKeyToSymkey = "test";
					// String enFileAddr = "D:/RSE/publickey/publickey.txt";

					contractListOfObservable.get(contractId)
							.ResponseFile(new Utf8String(resInfo.getString("_requestId").replace("REQ", "RES")),
									new Utf8String(resInfo.getString("_requestId")),
									new Utf8String(resInfo.getString("fileId")), new Utf8String(enPubKeyToSymkey),
									new Utf8String(enFileAddr))
							.get();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.info(response.toString());
			}
		});
		return reqObservable;
	}

	public static Observable<ResponseFileEventEventResponse> observeResRvent(String userId) {
		int contractId = choiceContract(userId);// 通过身份证号码来选择哪个合约
		Observable<ResponseFileEventEventResponse> resObservable = contractListOfObservable.get(contractId)
				.responseFileEventEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
//		resObservable.subscribe((response) -> {
//			logger.info("\n\n----------ResponseSucceedEvent---------");
//			logger.info("" + response);
//			logger.info(response.info.getValue());
//			JSONObject resInfo = JSONObject.parseObject(response.info.getValue().toString());
//
//			String PubKeyToSymkey = JSONObject.parseObject(resInfo.getString("response")).getString("_PubKeyToSymkey");
//			String fileAddr = JSONObject.parseObject(resInfo.getString("response")).getString("_fileAddr");
//			logger.info(fileAddr);
//			// 不用加密做测试
//			String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/files/keys/"
//					+ resInfo.getString("userId") + "PRIKEY";// 私钥路径
//			try {
//				String privateKey = Utils.fileRead(basePath);// 读取获取私钥（base64格式）
//				// //私钥解密PubKeyToSymkey（被加密的公共密钥）和fileAddr（加密地址）
//				String dePubKeyToSymkey = new String(
//						RSAUtils.decryptByPrivateKey(PubKeyToSymkey.getBytes(), privateKey));
//				String deFileAddr = new String(RSAUtils.decryptByPrivateKey(fileAddr.getBytes(), privateKey));
//				logger.info(deFileAddr);
//				// 用地址去下载文件
//				DowloadFileUtil.downLoad(deFileAddr);
//			} catch (Exception e) {
//
//				e.printStackTrace();
//			}
//		});
		return resObservable;
	}

	// public static String getUserByPage(ShareFile sFile) throws
	// InterruptedException, ExecutionException{
	//// int contractId = choiceContract(userId);//通过身份证号码来选择哪个合约
	//
	// TransactionReceipt receipt =
	// contractListOfObservable.get(0).getUserByPage(new Int256(),new
	// Int256()).get();
	// logger.info("ResponseFile receipt
	// transactionHash:{}",receipt.getTransactionHash());
	//
	// List<NewUserEventResponse> responses =
	// contractListOfObservable.get(contractId).getNewUserEvents(receipt);
	// String result = responses.get(0)._json.toString();
	// return result;
	// }

	// new Utf8String(fRes.getResponseId()),new
	// Utf8String(fRes.getRequestId()),new Utf8String(fRes.getFileId()),new
	// Utf8String(fRes.getPubKeyToSymkey()),new Utf8String(fRes.getFileAddr())
	
	public static BigInteger getBlockHigh() throws Exception{
		BigInteger blockNumber = transactionWeb3.ethBlockNumber().send().getBlockNumber();
		return blockNumber;
	}
	
	public static Block getBlockTransLogInfo(BigInteger blockNumber) throws Exception{
		Block returnBlock = new Block();
		
		try {
			EthBlock ethBlock = transactionWeb3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send();
			EthBlock.Block block = ethBlock.getBlock();
			
			returnBlock.setBlockNumber(String.valueOf(blockNumber));
		    BigInteger blockTime = block.getTimestamp();
			Date date = new Date(Long.valueOf(String.valueOf(blockTime)));
			String blockTimeStr = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
			System.out.println(blockTimeStr);
			returnBlock.setBlockTime(blockTimeStr);
			returnBlock.setPreBlockHash(block.getParentHash());
			returnBlock.setCurrBlockHash(block.getHash());
			String transInfos = "";
			
			List<EthBlock.TransactionResult> transactionResults = block.getTransactions();
			for (EthBlock.TransactionResult result : transactionResults) {
				EthGetTransactionReceipt ethGetTransactionReceipt = transactionWeb3.ethGetTransactionReceipt((String) result.get()).send();
				Optional<TransactionReceipt> opt = ethGetTransactionReceipt.getTransactionReceipt();
				if (opt.isPresent()) {
					TransactionReceipt receipt = opt.get();
					List<Log> logList = receipt.getLogs();
					for (Log log : logList) {
						String data = log.getData();
						System.out.println("data: " + data);
						String transInfo = hexStringToString(data.substring(130));
						System.out.println("transInfo: " + transInfo);
						transInfos += transInfo;
					}
				}
			}
			returnBlock.setTransInfo(transInfos.trim());
		} catch (Exception e) {
			System.out.println("查询区块信息异常");
			e.printStackTrace();
			throw new Exception("getBlockTransLogInfo, 查询区块信息异常");
		}
		
		return returnBlock;
	}
	
	 /** 
     * 16进制字符串转换为字符串 
     *  
     * @param s 
     * @return 
     */  
    public static String hexStringToString(String s) {  
        if (s == null || s.equals("")) {  
            return null;  
        }  
        s = s.replace(" ", "");  
        byte[] baKeyword = new byte[s.length() / 2];  
        for (int i = 0; i < baKeyword.length; i++) {  
            try {  
                baKeyword[i] = (byte) (0xff & Integer.parseInt(  
                        s.substring(i * 2, i * 2 + 2), 16));  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        try {  
            s = new String(baKeyword, "gbk");  
            new String();  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        return s;  
    }  

}
