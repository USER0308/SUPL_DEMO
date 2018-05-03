package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;  
import java.security.Key;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;  
  
public class KeyPairGenUtilTest {  
  
    /** 指定加密算法为RSA */  
    private static final String ALGORITHM = "RSA";  
    /** 密钥长度，用来初始化 */  
    private static final int KEYSIZE = 1024;  
    /** 指定公钥存放文件 */  
    private static String PUBLIC_KEY_FILE = "PublicKey";  
    /** 指定私钥存放文件 */  
    private static String PRIVATE_KEY_FILE = "PrivateKey";  
  
    public static void main(String[] args) throws Exception {  
        generateKeyPair();  
//        genKeyPair();
        testEncryptAndDecrypt();
        
        
    }  
  
    /** 
    * 生成密钥对 
    * @throws Exception 
    */  
    private static void generateKeyPair() throws Exception {  
  
        //     /** RSA算法要求有一个可信任的随机数源 */  
        //     SecureRandom secureRandom = new SecureRandom();  
        /** 为RSA算法创建一个KeyPairGenerator对象 */  
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);  
  
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */  
        //     keyPairGenerator.initialize(KEYSIZE, secureRandom);  
        keyPairGenerator.initialize(KEYSIZE);  
  
        /** 生成密匙对 */  
        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
  
        /** 得到公钥 */  
        Key publicKey = keyPair.getPublic();
        
        /** 得到私钥 */  
        Key privateKey = keyPair.getPrivate();
        
        ObjectOutputStream oos1 = null;
        ObjectOutputStream oos2 = null;
        try {  
            /** 用对象流将生成的密钥对象写入文件 */  
            oos1 = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));  
            oos2 = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));  
            oos1.writeObject(publicKey);  
            oos2.writeObject(privateKey);  
        } catch (Exception e) {  
            throw e;  
        } finally {  
            /** 清空缓存，关闭文件输出流 */  
            oos1.close();  
            oos2.close();  
        }  
    }  
  
//    private static void genKeyPair() throws NoSuchAlgorithmException {  
//          
//        /** RSA算法要求有一个可信任的随机数源 */  
//        SecureRandom secureRandom = new SecureRandom();  
//          
//        /** 为RSA算法创建一个KeyPairGenerator对象 */  
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);  
//  
//        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */  
//        keyPairGenerator.initialize(KEYSIZE, secureRandom);  
//        //keyPairGenerator.initialize(KEYSIZE);  
//  
//        /** 生成密匙对 */  
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
//  
//        /** 得到公钥 */  
//        Key publicKey = keyPair.getPublic();  
//  
//        /** 得到私钥 */  
//        Key privateKey = keyPair.getPrivate();  
//  
//        byte[] publicKeyBytes = publicKey.getEncoded();  
//        byte[] privateKeyBytes = privateKey.getEncoded();  
//  
//        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);  
//        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);  
//  
//        System.out.println("publicKeyBase64.length():" + publicKeyBase64.length());  
//        System.out.println("publicKeyBase64:" + publicKeyBase64);  
//  
//        System.out.println("privateKeyBase64.length():" + privateKeyBase64.length());  
//        System.out.println("privateKeyBase64:" + privateKeyBase64);
//    }  
    
    public static void testEncryptAndDecrypt() throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //读取私钥，进行加密
        PrivateKey privateKey = (PrivateKey) readKey(PRIVATE_KEY_FILE);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        //加密
        String sendInfo = "我的明文";
        byte[] results = cipher.doFinal(sendInfo.getBytes());
        
        //读取公钥，进行解密
        PublicKey publicKey = (PublicKey) readKey(PUBLIC_KEY_FILE);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //解密
        byte[] deciphered = cipher.doFinal(results);
        //得到明文
        String recvInfo = new String(deciphered);
        System.out.println(recvInfo);
    }
    
    public static void writeKey(String path, Key key) throws Exception {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.close();
    }
     
    public static Key readKey(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream bis = new ObjectInputStream(fis);
        Object object = bis.readObject();
        bis.close();
        return (Key) object;
    }
    
    
}  