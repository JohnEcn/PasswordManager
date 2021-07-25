package Model.RWHandler;
import java.io.*;
import javax.crypto.SealedObject;

public class Serializer {

    private String fileName;
    private Encryptor encryptor;

    public Serializer(String fileName) {
        this.fileName = fileName;
        this.encryptor = null;
    }

    //If a encryption key is provided , encryption/decryption will be used;
    public Serializer(String fileName, String key) {
        this.fileName = fileName;
        this.encryptor = new Encryptor(key);
    }

    public void serialize(Serializable serializableObj)
    {
        Object objectForSerialization = serializableObj;

        if (encryptor != null) {
            objectForSerialization = encryptor.encrypt(serializableObj);
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(this.fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(objectForSerialization);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object deserialize()
    {
        Object deserializedObj;

        try {
            FileInputStream fileIn = new FileInputStream(this.fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deserializedObj = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (encryptor != null) {
            SealedObject sealedObj = (SealedObject) deserializedObj;
            return encryptor.decrypt(sealedObj);
        }
        return deserializedObj;
    }
}

