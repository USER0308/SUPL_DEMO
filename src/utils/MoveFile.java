package utils;

import java.io.File;

public class MoveFile {
	 public static boolean removeFile(String fileName,String destinationFloderUrl,String destFileName)
	    {
	        File file = new File(fileName);
	        File destFloder = new File(destinationFloderUrl);
	        //检查目标路径是否合法
	        if(destFloder.exists())
	        {
	            if(destFloder.isFile())
	            {
	            	System.out.println("目标路径是个文件，请检查目标路径！");
	                return false;
	            }
	        }else
	        {
	            if(!destFloder.mkdirs())
	            {
	            	System.out.println("目标文件夹不存在，创建失败！");
	                return false;
	            }
	        }
	        //检查源文件是否合法
	        if(file.isFile() &&file.exists())
	        {
//	        	long now=System.currentTimeMillis();
//				String time = Utils.sdf(now);
	            String destinationFile = destinationFloderUrl+destFileName;
	            if(!file.renameTo(new File(destinationFile)))
	            {
	                System.out.println("移动文件失败！");
	                return false;
	            }
	        }else
	        {
	        	System.out.println("要备份的文件路径不正确，移动失败！");
	            return false;
	        }
	        System.out.println("已成功移动文件"+file.getName()+"到"+destinationFloderUrl);
	        return true;
	    }
	 
	 public static void main(String[] argv) {
		String basePath = "/home/user0308/Tmp/tmp/";
		String destPath = basePath + "contract/";
		String sourceFilePath = basePath + "/orgA/" + "temContract";
			
		removeFile(sourceFilePath,destPath,"new_file"); 
	 }
}
