package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DowloadFileUtil {
	
	public static void _downLoad(String filePath, HttpServletRequest request, HttpServletResponse response, boolean isOnLine) throws Exception {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
	        File f = new File(filePath);
	        if (!f.exists()) {
	            response.sendError(404, "File not found!");
	            return;
	        }
	        br = new BufferedInputStream(new FileInputStream(f));
	        byte[] buf = new byte[1024];
	        int len = 0;
	        
	        String userAgent = request.getHeader("User-Agent"); 
	        String fileName = null;
	        
	        // 针对IE或者以IE为内核的浏览器：  
	        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
	        	fileName = java.net.URLEncoder.encode(f.getName(), "UTF-8");  
	        } else {  
	            // 非IE浏览器的处理：  
	        	fileName = new String(f.getName().getBytes("UTF-8"), "ISO-8859-1");  
	        } 
	
	        response.reset(); // 非常重要
	        if (isOnLine) { // 在线打开方式
	            URL u = new URL("file:///" + filePath);
	            response.setContentType(u.openConnection().getContentType());
	            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
	            // 文件名应该编码成UTF-8
	        } else { // 纯下载方式
	            response.setContentType("application/x-msdownload");
	            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	        }
	        out = response.getOutputStream();
	        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(br != null) {
				br.close();
			}
			if(out != null) {
				out.close();
			}
		}
        
    }
	
	public static void downloadNet(HttpServletRequest request, HttpServletResponse response, String fileUrl) throws Exception {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        BufferedInputStream br = null;
        OutputStream out = null;
        
        try {
        	String fName = fileUrl.substring(fileUrl.lastIndexOf("/")+1); 
        	String userAgent = request.getHeader("User-Agent"); 
            String fileName = null;
            // 针对IE或者以IE为内核的浏览器：  
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
            	fileName = java.net.URLEncoder.encode(fName, "UTF-8");  
            } else {  
                // 非IE浏览器的处理：  
            	fileName = new String(fName.getBytes("UTF-8"), "ISO-8859-1");  
            } 
            
            response.reset(); // 非常重要
        	response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        	
        	URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            br = new BufferedInputStream(conn.getInputStream());
            out = response.getOutputStream();

            byte[] buffer = new byte[1204];
            while ((byteread = br.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                out.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(br != null) {
				br.close();
			}
			if(out != null) {
				out.close();
			}
        }
    }
	
	public static void downLoad(String filePath) throws Exception {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
	        File f = new File(filePath);
			if (!f.exists()) {
	            System.out.println("File not found!");
	            return;
	        }
	        br = new BufferedInputStream(new FileInputStream(f));
	        byte[] buf = new byte[1024];
	        int len = 0;
	        int start = filePath.lastIndexOf("/");
	        String fileName = filePath.substring(start);
	        System.out.println("--------"+fileName);
	        File FILE_FOR_WRITE = new File("D:/RSE/publickey/"+fileName);
	        out = new FileOutputStream(FILE_FOR_WRITE);
	        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(br != null) {
				br.close();
			}
			if(out != null) {
				out.close();
			}
		}
        
    }

}
