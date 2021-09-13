package tests.RWHandler;

import Model.RWHandler.Encryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.SealedObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncryptorTest {

    @Test
    @DisplayName("The object after decryption must be equal to the original object")
    public void encryptDecryptTest()
    {
        Encryptor testSubj = new Encryptor("EncryptionTestKey");
        String originalObj = new String("testObject");

        SealedObject encryptedObj = testSubj.encrypt(originalObj);
        Object decryptedObj = null;
        try
        {
            decryptedObj = testSubj.decrypt(encryptedObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String afterDecryptionObj = (String) decryptedObj;
        Assertions.assertEquals(originalObj,afterDecryptionObj);
    }

    @Test
    @DisplayName("Wrong decryption key test")
    public void incorrectDecryptionKey()
    {
        Encryptor testSubj = new Encryptor("EncryptionTestKey");
        String originalObj = new String("testObject");
        SealedObject encryptedObj = testSubj.encrypt(originalObj);

        //New Encryptor instance with different key;
        Encryptor newTestSubj = new Encryptor("WrongEncryptionKey");
        try
        {
            Assertions.assertThrows(BadPaddingException.class , () -> {newTestSubj.decrypt(encryptedObj);});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}