package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Token{
	
	public static void main(String[] args) {
		try {
			System.out.println(getToken());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private static final char[] hexCode = "0123456789abcdef".toCharArray();
    
    public static String getToken() throws Exception{
        return generateValue(UUID.randomUUID().toString());
    }
    
    public static String toHexString(byte[] data){
      if (data == null) {
        return null;
      }
      StringBuilder r = new StringBuilder(data.length * 2);
      byte[] arrayOfByte = data; int j = data.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
        r.append(hexCode[(b >> 4 & 0xF)]);
        r.append(hexCode[(b & 0xF)]);
      }
      return r.toString();
    }
    
    public static String generateValue(String param) throws Exception {
        try {
            MessageDigest algorithm;
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("generateValue error");
        }
    }
}