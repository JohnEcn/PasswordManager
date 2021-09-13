package tests.RWHandler;

import Model.RWHandler.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class SerializerTest {

    @Test
    @DisplayName("Serialization - deserialization test")
    public void serializeDeserializeTest()
    {
        String serializableTestObj = new String("Test object");
        String deserializedObj = null;
        try
        {
            Serializer serializer = new Serializer("SerializerTest","encryptionKey");
            serializer.serialize(serializableTestObj);
            deserializedObj = (String) serializer.deserialize();
        }
        catch (Exception e)
        {
            //Should not happen
            e.printStackTrace();
        }
        finally
        {
            Assertions.assertEquals(serializableTestObj,deserializedObj);
            File testFile = new File("SerializerTest.vlt");
            testFile.delete();
        }
    }

    @Test
    @DisplayName("File for deserialization not found test")
    public void fileNotFoundTest()
    {
        try
        {
            Serializer serializer = new Serializer("notExistingFile","encryptionKey");
            Assertions.assertThrows(FileNotFoundException.class , () -> {serializer.deserialize();});
        }
        catch (Exception e)
        {
        }
    }

    @Test
    @DisplayName("Wrong decryption key of the serialized object test")
    public void wrongDecryptionKeyTest()
    {
        try
        {
            Serializer serializer = new Serializer("SerializerTest","encryptionKey");
            String serializableTestObj = new String("Test object");
            Serializer wrongKeySerializer = new Serializer("SerializerTest","wrongKey");
            serializer.serialize(serializableTestObj);
            Assertions.assertThrows(BadPaddingException.class , () -> {wrongKeySerializer.deserialize();});
        }
        catch (Exception e)
        {
        }
    }

    @Test
    @DisplayName("File's Header is corrupted test")
    public void corruptedFileTest()
    {
        try
        {
            //Create an file that contains an encrypted Object
            Serializer serializer = new Serializer("SerializerTest","encryptionKey");
            String serializableTestObj = new String("Test object");

            serializer.serialize(serializableTestObj);

            //Change a byte so the file gets corrupted
            RandomAccessFile raf = new RandomAccessFile("SerializerTest.vlt", "rw");
            raf.seek(5);
            raf.write(00);
            raf.close();

            //Attempt to deserialize the corrupted file
            Assertions.assertThrows(IOException.class , () -> {serializer.deserialize();});
        }
        catch (Exception e)
        {
        }
        finally
        {
            File testFile = new File("SerializerTest.vlt");
            testFile.delete();
        }
    }
}