package tests.Vault;

import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import Model.Vault.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

/** Tests work only in order */
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
    @ValueSource(strings = {"secretKey","SecretKey!","asdad","0000-0000","secretKey!!"})
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
        Element debitCard1= new DebitCard("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
        Element webCredentials= new WebCredentials("Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
        Element blockchainKeys = new BlockchainKeys("Eth 1","0x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
        Element note = new Notes("Random Note","This is a random        note  \n random blahahbhahbhahbah !!");
        String correctJson = "[" +  debitCard1.toJson() + "," +  webCredentials.toJson() + "," + blockchainKeys.toJson() + "," + note.toJson() + "]";
        try
        {
            //Add 3 new valid entry to the vault (same with the ones created before)
            testVault.addElement("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
            testVault.addElement( "Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
            testVault.addElement( "Eth 1","0x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
            testVault.addElement("Random Note","This is a random        note  \n random blahahbhahbhahbah !!");

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
                testVault.addElement("DebitCard_1",5168213493333333L,(short) 2,(short)23,(short)112,"NIKOS NIKAS"); });
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
            testVault.removeElement("Eth 1");
            testVault.removeElement("Random Note");

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

    @Test
    @DisplayName("Edit Entry test")
    public void editEntryTest()
    {
        Element webCredentials= new WebCredentials("EditedName1","it001002","me@uni.gr","EditedPassword","random.uni.gr/whatever");
        Element webCredentials2 = new WebCredentials("Gmail","EditedUsername","me@uni.gr","1512dasgs1234","random.uni.gr/whatever");
        Element debitCard1= new DebitCard("EditedName2",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
        Element blockchainKeys = new BlockchainKeys("Edited Eth 1","2x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
        Element note = new Notes("Random Note Edited","This is a random   edited     note  \n random blahahbhahbhahbah !!");
        String ExpectedJsonAfterEdit = "[" +  webCredentials.toJson() + "," + webCredentials2.toJson() + "," + debitCard1.toJson() + "," + blockchainKeys.toJson() + "," + note.toJson() + "]";

        try
        {
            Vault editTestVault = new Vault("editTestvault","123");

            //Insert the elements
            editTestVault.addElement("uni_Login","it001002","me@uni.gr","123415test_1","random.uni.gr/whatever");
            editTestVault.addElement("Gmail","it1032182","me@uni.gr","EditedPassword","random.uni.gr/whatever");
            editTestVault.addElement("Revolut",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
            editTestVault.addElement("Eth 1","0x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
            editTestVault.addElement("Random Note","This is a random        note  \n random blahahbhahbhahbah !!");

            //Edit the elements
            editTestVault.editElement("Gmail","Gmail","EditedUsername","me@uni.gr","1512dasgs1234","random.uni.gr/whatever");
            editTestVault.editElement("EditedName2","Revolut",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
            editTestVault.editElement("EditedName1","uni_Login","it001002","me@uni.gr","EditedPassword","random.uni.gr/whatever");
            editTestVault.editElement("Edited Eth 1","Eth 1","2x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
            editTestVault.editElement("Random Note Edited","Random Note","This is a random   edited     note  \n random blahahbhahbhahbah !!");

            //Get the vault contents as Json
            String resultJson = editTestVault.getVaultElements();

            //Compare the 2 Json
            Assertions.assertEquals(ExpectedJsonAfterEdit,resultJson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            File testVaultFile = new File("editTestvault.vlt");
            testVaultFile.delete();
        }
    }

    @Test
    @DisplayName("change password test")
    public void changePasswordTest()
    {
        Element debitCard1= new DebitCard("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
        Element webCredentials= new WebCredentials("Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
        Element blockchainKeys = new BlockchainKeys("Eth 1","0x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");
        String expectedJson = "[" +  debitCard1.toJson() + "," +  webCredentials.toJson() + "," + blockchainKeys.toJson() + "]";

        try
        {
            //Create a vault and add elements
            Vault v = new Vault("test_vault1","123");
            v.addElement("DebitCard_1",5168213499062633L,(short) 12,(short)25,(short)233,"PETER JACKSON");
            v.addElement("Uni Services","it001002","me@uni.gr","testPassword123","random.uni.gr/whatever");
            v.addElement("Eth 1","0x17B86a7Ce9829B97AAB346fdbFC27dA0976d476e","88771d875fc0494bd736ab46b962e986d010381ccb4a629901bfc05578dbb2e3");

            //Change Password and reload the vault
            v.changeVaultPassword("changedPassword");
            Vault v1 = new Vault("test_vault1","changedPassword");
            String resultJson = v1.getVaultElements();

            Assertions.assertEquals(expectedJson,resultJson);
        }
        catch (Exception e)
        {
        }
        finally
        {
            File testVaultFile = new File("test_vault1.vlt");
            testVaultFile.delete();
        }
    }
}