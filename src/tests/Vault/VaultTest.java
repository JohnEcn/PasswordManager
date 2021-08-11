package tests.Vault;
import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import Model.Vault.DebitCard;
import Model.Vault.Element;
import Model.Vault.Vault;
import Model.Vault.WebCredentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

class VaultTest {

    private static Vault testVault;
    @BeforeAll
    public static void vaultInit()
    {
        //Create a new vault that will be used for tests
        try
        {
            testVault = new Vault("testVault","secretKey!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void cleanUp()
    {
        File testVaultFile = new File("testVault.vlt");
        testVaultFile.delete();
    }
    @ParameterizedTest
    @ValueSource(strings = {"secretKey","SecretKey!","","0000-0000","secretKey!!"})
    @DisplayName("Wrong password (encryption key) test")
    public void incorrectKeyTest(String key)
    {
        try
        {
            //Attempt to create new instance of 'testVault' with the wrong encryption key
            Assertions.assertThrows(IncorrectSecretKeyException.class , () -> {
                Vault wrongKeyVault = new Vault("testVault",key);});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add entry to the vault and try to retrieve this entry test")
    public void addEntryTest()
    {
        //Create objects and their Json representation to compare them with the vault output
        Element debitCard1= new DebitCard("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233);
        Element webCredentials= new WebCredentials("Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
        String correctJson = "[" +  debitCard1.toJson() + "," +  webCredentials.toJson() + "]";
        try
        {
            //Add 2 new valid entry to the vault (same with the ones created before)
            testVault.addElement("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233);
            testVault.addElement( "Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");

            //Create new instance of vault (new Login) and retrieve the entries
            Vault testVaultNew = new Vault("testVault","secretKey!");
            String testJson = testVaultNew.getVaultElements();
            Assertions.assertEquals(correctJson,testJson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Duplicate entry name test")
    public void duplicateNameTest()
    {
        try
        {
            //Attempt to add an entry to the vault with an existing name (debitCard1)
            Assertions.assertThrows(NotUniqueEntryNameException.class , () -> {
                testVault.addElement("DebitCard_1",5168213493333333L,(short) 2,(short)23,(short)112); });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Remove Entry test")
    public void removeEntryTest()
    {
        Element webCredentials= new WebCredentials("Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
        String correctJson = "[" +  webCredentials.toJson() + "]";
        try
        {
            testVault.removeElement("DebitCard_1");

            //Create new instance of vault (new Login) and retrieve the entries
            Vault testVaultNew = new Vault("testVault","secretKey!");
            String expectedJson = testVaultNew.getVaultElements();
            Assertions.assertEquals(correctJson,expectedJson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}