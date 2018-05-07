package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

public class Utils {
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	
	//获取字符串SHA256值
	public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }
	
	//获取文件SHA256值
	public static String getFileSHA256Str(String file) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fis.read(data)) != -1) {
            sha256.update(data, 0, read);
        };
        byte[] hashBytes = sha256.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String fileHash = sb.toString();
        return fileHash;
    }
	
	//判断非空
	public static boolean stringIsNull(String str) {
		if (null==str || "".equals(str)) {
			return true;
		}
		return false;
	}
	
   public static boolean isNullOrEmpty(Object obj) {  
        if (obj == null)  
            return true;  
  
        if (obj instanceof CharSequence)  
            return ((CharSequence) obj).length() == 0;  
  
        if (obj instanceof Collection)  
            return ((Collection) obj).isEmpty();  
  
        if (obj instanceof Map)  
            return ((Map) obj).isEmpty();  
  
        if (obj instanceof Object[]) {  
            Object[] object = (Object[]) obj;  
            if (object.length == 0) {  
                return true;  
            }  
            boolean empty = true;  
            for (int i = 0; i < object.length; i++) {  
                if (!isNullOrEmpty(object[i])) {  
                    empty = false;  
                    break;  
                }  
            }  
            return empty;  
        }  
        return false;  
    }
   
   /**
    * <p>
    * 读取文件
    * 读取公私钥文件用于后续的加解密
    * </p>
    * 
    * @param filePath文件路径
    * @return
    * @throws Exception
    * @author wangbin
    */
   public static String fileRead(String filePath) throws Exception {
       File file = new File(filePath);//定义一个file对象，用来初始化FileReader
       FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
       BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
       StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
       String s = "";
       while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
           sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
       }
       bReader.close();
       String str = sb.toString();
       return str;
   }

}
