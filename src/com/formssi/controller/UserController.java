package com.formssi.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.IouRecord;
import com.formssi.entity.Transaction;
import com.formssi.service.IIouLimitEntityService;
import com.formssi.service.IIouRecordService;
import com.formssi.service.ITransactionService;
import com.formssi.service.impl.IOUService;
//import com.formssi.entity.ReturnJson;
//import com.formssi.entity.User;
//import com.formssi.service.UserService;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
import com.sun.jmx.snmp.Timestamp;

import utils.Utils;
import utils.MoveFile;
//import utils.MD5Util;
//import utils.MySessionContext;
//import utils.Token;
//import utils.Utils;

@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private IIouLimitEntityService iouLimitEntityService;
	@Autowired
	private IIouRecordService iouRecordService;
	@Autowired
	private ITransactionService transactionService;
	
	private String basePath="/home/user0308/Tmp/tmp/";  //合同存储的根目录
	

	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "127.0.0.1:3000");//跨域访问
		String orgID=map.get("orgID");
		String orgName=map.get("orgName");
		String password=map.get("password");
		
		//登录操作checkPasswordByorgID
		int isSuccess=iouLimitEntityService.checkPasswordByorgID(password, orgID, orgName);
		
		JSONObject res=new JSONObject();
		if(isSuccess==1) {
			res.put("status", "1");
			HttpSession session=request.getSession();
			session.setAttribute("orgID", orgID);
		}
		else 
			res.put("status", isSuccess);
		return res.toJSONString();
	}

	@RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String register(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String orgID=map.get("orgID");
		String orgName=map.get("orgName");
		String password=map.get("password");
		int iouLimit = Integer.parseInt(map.get("iouLimit"));
		System.out.println("orgID is:"+orgID);
		System.out.println("orgName is:"+orgName);
		System.out.println("password is:"+password);
		//注册操作
//		IOUService.initObj();
//		IOUService.initIouLimitData(orgID, orgName, password, iouLimit);
		
		boolean isSuccess = iouLimitEntityService.addIouLimitEntity(orgID,orgName,password,iouLimit);
		
		
		HttpSession session=request.getSession();
		session.setAttribute("orgID", orgID);
		
		JSONObject res=new JSONObject();
		if(isSuccess)
			res.put("status", "1");
		else 
			res.put("status", "0");
		return res.toJSONString();
	}
	
	
	@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println(request.getParameter("username")+"   @@@@@");
		session = request.getSession();
		String orgID  = (String) session.getAttribute("orgID");
//		
		System.out.println("orgID is " + orgID);
		return "666";
	}

	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		
		/* 上传文件到用户的临时存放区，并名为temContract，即basePath/orgID/temContract
		 * 
		 * */

//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		//System.out.println(request.getParameter("username")+"   @@@@@");
		//System.out.println("file : "+request.getParameter("file"));
		session = request.getSession();
		String orgID  = (String) session.getAttribute("orgID");
//		String orgID = "user01";
		System.out.println("orgID: "+orgID);
		
        //获取表单(POST)数据
        ServletInputStream in = request.getInputStream();//此方法得到所有的提交信息，不仅仅只有内容
        //转换流
        InputStreamReader inReaser = new InputStreamReader(in);
        //缓冲流
        BufferedReader reader = new BufferedReader(inReaser);
        String str = null;
//        System.out.println(in.read());
//        System.out.println("##@!@#@@");
//        while ((str=reader.readLine()) != null){
//            System.out.println(str);
//        }
        System.out.println("    #######");
        
        
		//FileInputStream fis = new FileInputStream("src\\File\\Outfile.java");//读取文件
        
        File directory = new File("");//设定为当前文件夹 
        try{ 
            System.out.println(directory.getCanonicalPath());//获取标准的路径 
            System.out.println(directory.getAbsolutePath());//获取绝对路径
            System.out.println(directory.getPath());//获取绝对路径
            
        }catch(Exception e){} 
        
        
        String temPath = basePath+orgID;
        System.out.println("上传文件的文件夹位置为 "+temPath);
        File f = new File(temPath);
        
        if(!f.exists()){
            f.mkdirs();//创建目录
        }
        
		FileOutputStream fos = new FileOutputStream(temPath + "/" + "temContract");//保存文件
		int len;
		Byte[] b =new Byte[1024];
		while((len=in.read())!=-1){//判读文件内容是否存在
			//System.out.print((char)len);//打印文件
			fos.write(len);//写入文件
		}
		in.close();
		fos.close();	
		System.out.println("上传文件成功");
		return "666";
	}
	
	
	@RequestMapping(value="/download/{con_id}")
    public ResponseEntity<byte[]> download(@PathVariable("con_id") String con_id, HttpServletRequest request,
            Model model)throws Exception {
    	
    	/* 下载basePath/contract下的con_id文件
    	 * 
    	 * */
    	
    	
    	String filename = con_id;
       //下载文件路径
       String path = basePath+"contract";
       File file = new File(path + File.separator + filename);
       HttpHeaders headers = new HttpHeaders();  
       //下载显示的文件名，解决中文名称乱码问题  
       String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
       //通知浏览器以attachment（下载方式）打开图片
       headers.setContentDispositionFormData("attachment", downloadFielName); 
       //application/octet-stream ： 二进制流数据（最常见的文件下载）。
       headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
       return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
               headers, HttpStatus.CREATED);  
    }

    @RequestMapping(value = "/get_entity_iou_limit", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_entity_iou_limit(@RequestBody Map<String, String> map,HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "127.0.0.1:3000");//跨域访问
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		if(orgID==null) {
			orgID=map.get("orgID");
		}
		System.out.println(orgID);
		
		//获取ioulist数目
		int tem =iouLimitEntityService.getIouLimit(orgID);
		
		JSONObject res=new JSONObject();
		res.put("iouLimit", tem);
		res.put("orgID", orgID);
		return res.toJSONString();
	} 
	
	@RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		HttpSession session=request.getSession();
		session.invalidate();
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/ioulist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String ioulist(HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
//		int pageNum=Integer.parseInt(map.get("pageNum"));
//		int pageSize=Integer.parseInt(map.get("pageSize"));
		//String pageSize=request.getParameter("pageSize");
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//获取iou列表操作
		List<IouRecord> tem = iouRecordService.getAllIouRecord();
		
		JSONArray res=new JSONArray();
		for (int i=0;i<tem.size();i++) {
			JSONObject xxx = new JSONObject();
			xxx.put("iouId", tem.get(i).getIouId());
			xxx.put("fromOrg", tem.get(i).getFromOrg());
			xxx.put("recOrg", tem.get(i).getRecvOrg());
			xxx.put("transTime", tem.get(i).getTransTime());
			xxx.put("updateTime", tem.get(i).getUpdateTime());
			xxx.put("amount", tem.get(i).getAmount());
			xxx.put("paidAmt", tem.get(i).getPaidAmt());
			xxx.put("iouStatus", tem.get(i).getIouStatus());
			
			res.add(xxx);
		}
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_ioulist_num", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_ioulist_num(HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "127.0.0.1:3000");//跨域访问
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		System.out.println(orgID);
		
		//获取ioulist数目
		int tem =iouRecordService.getAllIouRecord().size();
		
		JSONObject res=new JSONObject();
		res.put("amount", tem);
		res.put("orgID", orgID);
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/recycle_iou", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String recycle_iou(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String iouId=map.get("iouId");
		//String amount=request.getParameter("amount");
		int amount=Integer.parseInt(map.get("amount"));
		System.out.println("iouId is "+iouId);
		System.out.println("amount is "+amount);
		//HttpSession session=request.getSession();
		//String orgID = (String) session.getAttribute("orgID");
		try {
			iouLimitEntityService.recycleIou(iouId, amount);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/update_iou_limit", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String update_iou_limit(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		int amount=Integer.parseInt(map.get("amount"));
		
		HttpSession session=request.getSession();
		String orgID = (String) session.getAttribute("orgID");
		if(orgID==null) {
			orgID = map.get("orgID");
		}
		//更新白条额度操作
		String transTime = Utils.sdf(System.currentTimeMillis());
		boolean isSuccess=false;
		try {
			isSuccess = iouLimitEntityService.setIouLimit(amount, transTime, orgID);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//(orgID,orgName,password,iouLimit);
		
		
		
		JSONObject res=new JSONObject();
		if(isSuccess)
			res.put("status", "1");
		else 
			res.put("status", "0");
		return res.toJSONString();
		
	}
	
	@RequestMapping(value = "/transactionlist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String transactionlist( HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
//		int pageNum=Integer.parseInt(map.get("pageNum"));
//		int pageSize=Integer.parseInt(map.get("pageSize"));
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//交易列表操作
		List<Transaction> tem = transactionService.getAllTransaction();
		
		JSONArray res=new JSONArray();
		for (int i=0;i<tem.size();i++) {
			JSONObject xxx = new JSONObject();
			xxx.put("conID", tem.get(i).getConID());
			xxx.put("saleOrg", tem.get(i).getSaleOrg());
			xxx.put("buyOrg", tem.get(i).getBuyOrg());
			xxx.put("transType", tem.get(i).getTransType());
			xxx.put("transTime", tem.get(i).getTransTime());
			xxx.put("updateTime", tem.get(i).getUpdateTime());
			xxx.put("amount", tem.get(i).getAmount());
			xxx.put("conHash", tem.get(i).getConHash());
			xxx.put("latestStatus", tem.get(i).getLatestStatus());
			
			
			res.add(xxx);
		}
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_transaction_num", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_transaction_num(HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//获取get_transaction_num数目
		int tem=transactionService.getAllTransaction().size();
		
		JSONObject res=new JSONObject();
		res.put("amount", tem);
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/add_transaction", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String add_transaction(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
		
		/*
		 * 添加交易时 要将用户临时存放区的文件移到basePath/contract下，并名为con_id
		 * 然后计算合同hash
		 * */
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String saleOrg=map.get("saleOrg");
		String buyOrg=map.get("buyOrg");
		String transType=map.get("transType");
		IouLimitEntity iouLimitEntityBuy = iouLimitEntityService.getIouLimitEntityByConId(buyOrg);
		IouLimitEntity iouLimitEntitySale = iouLimitEntityService.getIouLimitEntityByConId(saleOrg);
		if (iouLimitEntitySale == null) {
			System.out.println("出售方交易账号输入错误");
			return "998";
		}
		if (iouLimitEntityBuy == null) {
			System.out.println("购买方交易账号输入错误");
			return "998";
		}
		if(iouLimitEntityBuy.getOrgName().equals("upstream") == true) {
			System.out.println("很抱歉你不能发起购买交易");
			return "998";
		}
		if(iouLimitEntityBuy.getOrgName().equals("core") == true) {
			if (transType.equals("U") == true) {
				if (iouLimitEntitySale.getOrgName().equals("upstream") == false) {
					System.out.println("很抱歉,购买交易的对象错误");
					return "998";
				}
			}
			if (transType.equals("F") == true) {
				if (iouLimitEntitySale.getOrgName().equals("downstream") == false) {
					System.out.println("很抱歉,购买交易的对象错误");
					return "998";
				}
			}
		}
		if(iouLimitEntityBuy.getOrgName().equals("downstream") == true ) {
			if (transType.equals("U") == true) {
				if (iouLimitEntitySale.getOrgName().equals("core") == false ) {
					System.out.println("很抱歉,购买交易的对象错误");
					return "998";
				}
			}
			if (transType.equals("F") == true) {
				if (iouLimitEntitySale.getOrgName().equals("factory") == false) {
					System.out.println("很抱歉,购买交易的对象错误");
					return "998";
				}
			}
		}
		
		if(iouLimitEntityBuy.getOrgName().equals("factory") == true  ) {
			if (transType.equals("U") == true) {
				if (iouLimitEntitySale.getOrgName().equals("upstream") == false ) {
					System.out.println("很抱歉,购买交易的对象错误");
					return "998";
				}
			}
			else {
				System.out.println("很抱歉你不能购买成品");
			}
		}

		long amount=Integer.parseInt(map.get("amount"));
//		String conFile=map.get("conFile");
		
		HttpSession session=request.getSession();
		String orgID = (String) session.getAttribute("orgID");
		
		//添加交易操作
		try {
			// get conID,conHash
			long now=System.currentTimeMillis();
			String time = Utils.sdf(now);
			
			String folderUrl = basePath+buyOrg;
			System.out.println("folder is "+folderUrl);
	        File destFloder = new File(folderUrl);
	        //检查目标路径是否合法
	        if(destFloder.exists())
	        {
	            if(destFloder.isFile())
	            {
	            	System.out.println("目标路径是个文件，请检查目标路径！");
	                return "404";
	            }
	        }else
	        {
	            if(!destFloder.mkdirs())
	            {
	            	System.out.println("目标文件夹不存在，创建失败！");
	                return "404";
	            }
	        }
			
			String conID = "conID"+time;
			String filePath = basePath +buyOrg+"/" + "temContract";
			String conHash = utils.Utils.getFileSHA256Str(filePath);
			System.out.println("conID is:"+conID);
			System.out.println("conHash is:"+conHash);
			// move file
			String destPath = basePath + "contract/";
			String sourceFilePath = basePath + buyOrg +"/" + "temContract";
			boolean isSuccess = MoveFile.removeFile(sourceFilePath,destPath,conID);
			if(!isSuccess) {
				System.out.println("移动合同文件失败");
				JSONObject res=new JSONObject();
				res.put("status", "0");
				return res.toJSONString();
			}
			
			transactionService.addTransactionRecord(saleOrg, buyOrg, transType, amount, "P",conID,conHash);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_transaction/{con_id}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_transaction(@PathVariable("con_id") String con_id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println("Product Id  ff : " + con_id); 
		JSONObject res=new JSONObject();
		//根据合同获得交易信息
		Transaction tran = transactionService.getTransactionByConId(con_id);
		if(tran==null) {
			//不存在该交易
			res.put("status", "0");
			res.put("message", "不存在该交易");
			return res.toJSONString();
		}
		
		
		res.put("conID", tran.getConID());
		res.put("saleOrg", tran.getSaleOrg());
		res.put("buyOrg", tran.getBuyOrg());
		res.put("transType", tran.getTransType());
		res.put("transTime", tran.getTransTime());
		res.put("updateTime", tran.getUpdateTime());
		res.put("amount", tran.getAmount());
		res.put("conHash", tran.getConHash());
		res.put("latestStatus", tran.getLatestStatus());
		
		//te.fluentAdd(zz);
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/checkConId/{con_ID}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkConID(HttpServletRequest request, HttpServletResponse response,@PathVariable String con_ID) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		System.out.println("orgID 在文件校验中是:"+orgID);
		String filePath = basePath +orgID +"/" + "temContract";
		System.out.println("filePath 是:"+filePath);
		JSONObject zz=new JSONObject();
		try {
			String conHash = utils.Utils.getFileSHA256Str(filePath);
			System.out.println("计算出来的文件哈系是:"+conHash);
			Utf8String J_Transaction = IOUService.queryTransactionByConId(con_ID);
			String Str = J_Transaction.toString();
			JSONObject jsonObject1=JSON.parseObject(Str);
			String conHash_block = jsonObject1.getString("conHash");
			System.out.println("从区块链上读取出来的文件哈系是:"+conHash_block);
			if (conHash_block.equals(conHash) ==true) {
				System.out.println("合同哈希匹配成功");
				return zz.put("status", "1").toString();
			}else {
				System.out.println("合同哈希匹配不成功");
				return zz.put("status", "0").toString();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	@RequestMapping(value = "/update_trans_status", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String update_transaction_status(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String conId = map.get("conID");
		//更新交易状态操作
		String transTime = Utils.sdf(System.currentTimeMillis());
		boolean isSuccess = transactionService.updateTransactionStatusByConId(conId, "C");
		
		JSONObject res=new JSONObject();
		if(isSuccess)
			res.put("status", "1");
		else 
			res.put("status", "0");
		return res.toJSONString();
		
	}
//	@RequestMapping(value="/product/{productId}", produces = "application/json;charset=UTF-8") 
//	public String getProduct(@PathVariable("productId") String productId, HttpServletResponse response, HttpSession session){ 
//	    System.out.println("Product Id : " + productId); 
//	    return "hello"; 
//	} 
	

	
	@RequestMapping(value = "/product/{productId}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String test(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println("Product Id  ff : " + productId); 
		JSONObject zz=new JSONObject();
		zz.put("zzz", "xxxx");
		System.out.println(zz.toJSONString()); 
		JSONArray te=new JSONArray();
		te.add(zz);
		for(int i=0;i<2;i++) {
		JSONObject zzz =new JSONObject();
		zzz.put("ddd", "ddsss");
		te.add(zzz);
		}
		//te.fluentAdd(zz);
		return te.toJSONString();
	}

}
