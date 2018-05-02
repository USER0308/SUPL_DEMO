package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256File {
    
//    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
//        boolean result = verifyChecksum("/home/eclipse-jee-indigo-SR2-linux-gtk-x86_64.tar.gz", "177750b65a21a9043105fd0820b85b58cf148ae4");
//        System.out.println("Does the file's checksum matches the expected one? " + result);
//    }
     
    /**
     * Verifies file's SHA256 checksum
     * @param Filepath and name of a file that is to be verified
     * @param testChecksum the expected checksum
     * @return true if the expeceted SHA256 checksum matches the file's SHA256 checksum; false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String fileHash(String file) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA256");
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

}
