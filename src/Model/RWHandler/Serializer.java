package Model.RWHandler;

import Model.CustomExceptions.InvalidArgumentException;

import java.io.*;
import javax.crypto.BadPaddingException;
import javax.crypto.SealedObject;

public class Serializer {

    private final String fileName;
    private final Encryptor encryptor;

    public Serializer(String fileName)
    {
        this.fileName = fileName + ".vlt";
        this.encryptor = null;
    }

    //If a encryption key is passed , encryption/decryption will be used ;
    public Serializer(String fileName, String key) throws InvalidArgumentException
    {
        if(fileName.length() < 1 ||  key.length() < 1)
        {
            throw new InvalidArgumentException("Empty filename or password.");
        }
        if(fileName.length() > 15)
        {
            throw new InvalidArgumentException("Username must be 15 characters max.");
        }
        this.fileName = fileName + ".vlt";
        this.encryptor = new Encryptor(key);
    }

    public void serialize(Serializable serializableObj) throws IOException
    {
        Object objectForSerialization = serializableObj;
        if (encryptor != null)
        {
            //Optional encryption of object
            objectForSerialization = encryptor.encrypt(serializableObj);
        }

        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try
        {
            fileOut = new FileOutputStream(this.fileName);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(objectForSerialization);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new FileNotFoundException("File does not exist");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("General IO exception");
        }
        finally
        {
            try
            {
                out.close();
                fileOut.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    public Object deserialize() throws IOException, BadPaddingException
    {
        Object deserializedObj;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try
        {
            fileIn = new FileInputStream(this.fileName);
            in = new ObjectInputStream(fileIn);
            deserializedObj = in.readObject();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new FileNotFoundException("File does not exist");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IOException("General IO exception");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Unrecoverable Exception");
        }
        finally
        {
            try
            {
                in.close();
                fileIn.close();
            }
            catch (Exception e)
            {
            }
        }

        if (encryptor != null)
        {
            SealedObject sealedObj = (SealedObject) deserializedObj;
            try
            {
                return encryptor.decrypt(sealedObj);
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
                throw e;
            }
        }
        return deserializedObj;
    }
}

