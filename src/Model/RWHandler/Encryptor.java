package Model.RWHandler;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.MessageDigest;

public class Encryptor {

    private SecretKeySpec key;
    private IvParameterSpec iv;
    private static final String SALT = "random_salt12345";

    public Encryptor(String encryptionKey) throws Exception
    {
        key = new SecretKeySpec(hashKey(encryptionKey),"AES");
        iv = new IvParameterSpec(SALT.getBytes("UTF-8"));
    }
    private byte[] hashKey(String s) throws Exception
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hashedKey = sha256.digest(s.getBytes("UTF-8"));
        return hashedKey;
    }
    public SealedObject encrypt(Serializable obj) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);

        SealedObject sealedObj = new SealedObject(obj,cipher);
        return sealedObj;
    }
    public Object decrypt(SealedObject sealedObj) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);

        Object decryptedObj = sealedObj.getObject(cipher);
        return decryptedObj;
    }





}
