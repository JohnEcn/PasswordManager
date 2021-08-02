package Model.RWHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    private final SecretKeySpec key;
    private final IvParameterSpec iv;
    private static final String SALT = "2968e36f88942b4f";

    public Encryptor(String encryptionKey)
    {
        try
        {
            key = new SecretKeySpec(hashKey(encryptionKey),"AES");
            iv = new IvParameterSpec(SALT.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            //Should never happen
            throw new RuntimeException(e);
        }
    }

    private byte[] hashKey(String s)
    {
        try
        {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hashedKey = sha256.digest(s.getBytes("UTF-8"));
            return hashedKey;
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            //Should never happen
            throw new RuntimeException(e);
        }
    }

    public SealedObject encrypt(Serializable obj)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);

            SealedObject sealedObj = new SealedObject(obj,cipher);
            return sealedObj;
        }
        catch (Exception e)
        {
            //Should never happen
            throw new RuntimeException(e);
        }
    }

    public Object decrypt(Object sealedObj) throws BadPaddingException {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);

            SealedObject sealedObject = (SealedObject) sealedObj;
            Object decryptedObj = sealedObject.getObject(cipher);
            return decryptedObj;
        }
        catch (BadPaddingException e)
        {
            //Wrong decryption Key -> BadPaddingException
            throw e;
        }
        catch (Exception e)
        {
            //Should never happen
            throw new RuntimeException(e);
        }
    }
}
