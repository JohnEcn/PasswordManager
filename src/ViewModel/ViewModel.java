package ViewModel;

import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.InvalidArgumentException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import Model.Vault.Vault;
import com.google.gson.Gson;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Map;


public class ViewModel {

    /** Ensure that only one instance of ViewModel can exist */
    private static ViewModel single_instance = null;
    private ViewModel(){}
    public static ViewModel getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new ViewModel();
        }
        return single_instance;
    }
    /** ---------------------------------------------------- */

    private Vault vault;
    private String vaultName = "";
    private boolean isVaultOpen = false;

    public void unlockVault(String vaultName,String vaultKey) throws Exception
    {

        if(!isVaultOpen) {
            try {
                this.vault = new Vault(vaultName, vaultKey);
                this.isVaultOpen = true;
                this.vaultName = vaultName;
            } catch (IncorrectSecretKeyException | InvalidArgumentException e) {
                this.vault = null;
                this.isVaultOpen = false;
                throw e;
            } catch (Exception e) {
                this.vault = null;
                this.isVaultOpen = false;
                throw new Exception("Unexpected error occurred");
            }
        }
    }

    public void newVault(String vaultName,String vaultKey) throws Exception
    {
        if(!isVaultOpen)
        {
            try
            {
                this.vault = new Vault(vaultName, vaultKey);
                this.isVaultOpen = true;
                this.vaultName = vaultName;
            }
            catch (IncorrectSecretKeyException e) {
                this.vault = null;
                this.isVaultOpen = false;
                throw new FileAlreadyExistsException("Vault exists");
            }
            catch (InvalidArgumentException e)
            {
                this.vault = null;
                this.isVaultOpen = false;
                throw new InvalidArgumentException(e.getMessage());
            }
            catch (Exception e)
            {
                this.vault = null;
                this.isVaultOpen = false;
                throw new Exception("Unexpected error occurred");
            }
        }
    }

    public ArrayList<String> getExistingVaultNames()
    {
        File folder = new File("./");
        File[] vaultFiles = folder.listFiles();
        ArrayList<String> vaultNames = new ArrayList<String>();

        for (int i = 0; i < vaultFiles.length; i++)
        {
            /** Get the 4 last characters */
            String fileExtension = vaultFiles[i].getName().length() >= 5 ? vaultFiles[i].getName().substring(vaultFiles[i].getName().length() - 4) : "";

            if (!vaultFiles[i].isDirectory() && fileExtension.equals(".vlt"))
            {
                String filename = vaultFiles[i].getName();
                String fileWithoutExtension = filename.substring(0,filename.length()-4);
                vaultNames.add(fileWithoutExtension);
            }
        }
        return vaultNames;
    }

    public boolean isVaultUnlocked()
    {
        return isVaultOpen;
    }

    public String getLoadedVaultName()
    {
        return this.vaultName;
    }

    public ArrayList<Map<String,Object>> getVaultContents()
    {
        String content = vault.getVaultElements();
        if(!isVaultOpen || content.equals("]"))
        {
            return null;
        }

        Gson gson = new Gson();
        ArrayList<Map<String,Object>> vaultContents = new  ArrayList<Map<String,Object>>();
        return  (ArrayList<Map<String,Object>>) gson.fromJson(content, vaultContents.getClass());
    }

    public void addToVault(Map<String,String> data) throws InvalidArgumentException, NotUniqueEntryNameException
    {
        String newEntryType = data.get("type");

        if(newEntryType.equals("webCredentials"))
        {
            vault.addElement(data.get("name"),data.get("username"),data.get("email"),data.get("password"),data.get("url"));
        }
        else if(newEntryType.equals("DebitCard"))
        {
            try {
                //Remove the "-" from the number String and parse it as long type
                long number = Long.parseLong(data.get("number").replaceAll("-", ""));
                short expMonth = Short.parseShort(data.get("expireMonth"));
                short expYear = Short.parseShort(data.get("expireYear"));
                short ccv2 = Short.parseShort(data.get("ccv2"));
                vault.addElement(data.get("name"),number,expMonth,expYear,ccv2,data.get("ownersName"));
            }
            catch (Exception e)
            {
                if(e.getClass() == NumberFormatException.class)
                {
                    throw new InvalidArgumentException("Empty fields.");
                }
                else
                {
                    throw e;
                }
            }
        }
    }

    public void removeElement(String elementName)
    {
        vault.removeElement(elementName);
    }

}
