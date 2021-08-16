package ViewModel;

import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.InvalidArgumentException;
import Model.Vault.Vault;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

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
    private boolean isVaultOpen = false;

    public void unlockVault(String vaultName,String vaultKey) throws Exception
    {

        if(!isVaultOpen) {
            try {
                this.vault = new Vault(vaultName, vaultKey);
                this.isVaultOpen = true;
            } catch (IncorrectSecretKeyException e) {
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

}
