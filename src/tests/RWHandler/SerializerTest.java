package tests.RWHandler;

import Model.RWHandler.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.*;

class SerializerTest {

    @Test
    @DisplayName("Serialization - deserialization test")
    public void serializeDeserializeTest()
    {
        Serializer serializer = new Serializer("SerializerTest","encryptionKey");
        String serializableTestObj = new String("Test object");

        serializer.serialize(serializableTestObj);
        String deserializedObj = (String) serializer.deserialize();

        Assertions.assertEquals(serializableTestObj,deserializedObj);

        File testFile = new File("SerializerTest");
        testFile.delete();
    }

    @Test
    @DisplayName("File for deserialization not found test")
    public void fileNotFoundTest()
    {
        Serializer serializer = new Serializer("notExistingFile","encryptionKey");
        Assertions.assertThrows(RuntimeException.class , () -> {serializer.deserialize();});
    }

    @Test
    @DisplayName("File's Header is corrupted test")
    public void corruptedFileTest()
    {
        //Create an file that contains an encrypted Object
        Serializer serializer = new Serializer("SerializerTest","encryptionKey");
        String serializableTestObj = new String("Test object");
        serializer.serialize(serializableTestObj);

        try
        {
            RandomAccessFile raf = new RandomAccessFile("SerializerTest", "rw");
            raf.seek(0);
            raf.write(00);
            raf.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Assertions.assertThrows(RuntimeException.class , () -> {serializer.deserialize();});

        File testFile = new File("SerializerTest");
        testFile.delete();
    }

    @Test
    @DisplayName("Wrong decryption key of the serialized object test")
    public void wrongDecryptionKeyTest()
    {
        Serializer serializer = new Serializer("SerializerTest","encryptionKey");
        String serializableTestObj = new String("Test object");
        serializer.serialize(serializableTestObj);

        Serializer wrongKeySerializer = new Serializer("SerializerTest","wrongKey");
        Assertions.assertThrows(RuntimeException.class , () -> {wrongKeySerializer.deserialize();});
    }
}