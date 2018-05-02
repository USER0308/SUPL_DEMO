package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	
    
    /**
     * 配置文件对象
     */
    private static Properties props=new Properties();
    
    /**
     * 默认构造函数，用于sh运行，自动找到classpath下的config.properties。
     */
    
    public PropertiesUtil(){
    	
    }
    
    public static void readFile(String path) throws IOException{
    	/*String classPath = Thread.currentThread().getContextClassLoader().getResource("").toString();
    	String subClassPath = classPath.substring(5, path.length());
    	String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){
		}
		else {
		}*/
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
//        props = new Properties();
        props.load(in);
        //关闭资源
        in.close();
    }
    
    /**
     * 根据key值读取配置的值
     * @param key key值
     * @return key 键对应的值 
     * @throws IOException 
     */
    public static String readValue(String key) throws IOException {
       
        return  props.getProperty(key);
    }
    
    
    
    /**
     * 根据key值，value值写入配置
     * @param key key值; value value值
     * @return void 
     * @throws IOException 
     */
	public static void writeValue(String key, String value, String path) throws IOException {
    	
    	Properties writeProps = new Properties();
    	writeProps.setProperty(key, value);
    	FileOutputStream out = new FileOutputStream(path);
    	writeProps.store(out, null);
        //关闭资源
        out.close();
    }
	
	public static void writeValues(ArrayList<String[]> arrayList, String path) throws IOException {
    	
    	Properties writeProps = new Properties();
    	for(String string[]: arrayList){
        	writeProps.setProperty(string[0], string[1]);
    	}
    	FileOutputStream out = new FileOutputStream(path);
    	writeProps.store(out, null);
        //关闭资源
        out.close();
    }
    
    public static void setContractAdress(int i, String value){
    	
        props.setProperty("contract.address."+i, value);
    }
    
    public static void setProperty(String key, String value){
    	
        props.setProperty(key, value);
    }
    
    
    public static void store(String path) throws IOException{
    	
        OutputStream out = new FileOutputStream(PropertiesUtil.class.getResource(path).getFile());
        props.store(out, null);
        //关闭资源
        out.close();
    }
    
    /**
     * 读取properties的全部信息
     * @throws FileNotFoundException 配置文件没有找到
     * @throws IOException 关闭资源文件，或者加载配置文件错误
     * 
     */
    @SuppressWarnings("rawtypes")
	public Map<String,String> readAllProperties() throws FileNotFoundException,IOException  {
        //保存所有的键值
        Map<String,String> map=new HashMap<String,String>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }
  
}

