package test;

import java.util.Arrays;
import java.util.List;

import org.bouncycastle.crypto.tls.UseSRTPData;
import org.springframework.beans.factory.annotation.Autowired;


public class myTest {
//	static Logger logger = LoggerFactory.getLogger(query.class);
	static private final List<String> TRAN_LATESTSTATE_LIST= Arrays.asList("U","C");

	
	
	public static void main(String[] args) throws Exception{
		
		
		
		
		
		
		
		
		
		
//		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
//		String subPath = path.substring(5, path.length());
//		System.out.println(path+" | "+subPath);
		
//		IOUService.init();
//		Transaction transaction = new Transaction();
//		transaction.setConID("12354");//从12345开始
//		transaction.setSaleOrg("org005");
//		transaction.setBuyOrg("org006");
//		transaction.setTransType("XX");
//		transaction.setAmount(new BigInteger("10"));
//		transaction.setConHash("0x483e58985e9a80a215944fdaabe8sdfge23g3df8ba90741f");
//		transaction.setLatestStatus("A");
//		transaction.setTransTime(Utils.getCurrentDate());
//		transaction.setUpdateTime(Utils.getCurrentDate());
//		System.out.println(IOUService.addTransaction(transaction));

//		IOUService.init();
//		IOU iou=new IOU();
//		iou.setIouId("12345");
//		iou.setIouStatus("P");
//		System.out.println(IOUService.updateTrans(iou));
		
//		IOUService.init();
//		Organ org=new Organ();
//		org.setOrgID("org006");
//		org.setOrgName("TestFactory1");
//		org.setIouLimit(new BigInteger("1000001"));
//		org.setCreateTime(Utils.getCurrentDate());
//		org.setUpdateTime("");
//		IOUService.initIouLimitData(org);
		
//		IOUService.init();
//		System.out.println(IOUService.queryTransByConId("12345"));
//		
//		IOUService.init();
//		IOU iou=new IOU();
//		iou.setFromOrg("org006");
//		System.out.println(IOUService.getIouLimit(iou));
		
		
//		IOUService.init();
//		Transaction tran=new Transaction();
//		tran.setConID("12346");
//		tran.setLatestStatus("U");
//		System.out.println(IOUService.updateTransStatus(tran));
		
//		IOUService.init();
//		Organ organ=new Organ();
//		organ.setOrgID("org005");
//		organ.setIouLimit(new BigInteger("1000001"));
//		System.out.println(IOUService.setIouLimit(organ));
		
//		IOUService.init();
//		String iouListString=IOUService.getIouList(2, 2);
//		JSONArray jsonArray=JSON.parseArray(iouListString);
//		for (Object object : jsonArray) {
//			IOU iou = (IOU) JSON.parseObject(object.toString(), IOU.class);
//			System.out.println(iou.toString());
//		}
		
//		IOUService.init();
//		String tranListString=IOUService.queryTransList(0, 10);
//		JSONArray jsonArray=JSON.parseArray(tranListString);
//		for (Object object : jsonArray) {
//			Transaction tran = (Transaction) JSON.parseObject(object.toString(), Transaction.class);
//			System.out.println(tran.toString());
//		}
		
		
//		IOUService.init();
//		IOU iou = new IOU();
//		iou.setIouId("12347");
//		iou.setFromOrg("org006");
//		iou.setAmount(new BigInteger("5"));
//		IOUService.getIouLimit(iou);
//		System.out.println(IOUService.iouRecycle(iou));
//		IOUService.getIouLimit(iou);
		
//		String filename="./src/files/getContractAddress.js";
//		String string="./src/files/getContractAddress111.js";
//		System.out.println(Utils.getFileSHA256Str(string));
//		String path=Thread.currentThread().getContextClassLoader().getResource("").getPath();
//		System.out.println(path);
		
//		File fileInput=new File("./src/files/mysql.user.txt");
//		BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileInput)); 
//        String path=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"files/mysql.txt";
		
//		String path="C:\\User\\mysql.txt";
//        System.out.println(path);
//        File file=new File("./src/files/mysql.txt");
//        file.createNewFile();
//        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
//        byte[] b = new byte[1024*8];  
//        int len;  
//        while((len = in.read(b)) != -1){  
//            out.write(b, 0, len);  
//        }  
//        out.flush();
//        
//		System.out.println("yes");
		
		
//		Transaction transaction=new Transaction();
//		transaction.setTransType("C");
//		System.out.println(transaction.getTransType());
//		
//		if(!TRAN_LATESTSTATE_LIST.contains(transaction.getLatestStatus())) {
//			System.out.println("the value of IOU Status must be C or U.");
//		}
//		
//		System.out.println(TRAN_LATESTSTATE_LIST.contains(transaction.getTransType()));
		
		
	}

}
