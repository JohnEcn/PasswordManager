package ViewModel;

import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.InvalidArgumentException;
import Model.Vault.Vault;
import java.io.File;
import java.util.ArrayList;

public class ViewModelUtilities {

     /**
     *  Ensure that only one instance of ViewModelUtilities can exist.
     *  When a ViewModel instance is created ViewModelUtilities instance also gets created
     *  by the ViewModel
     */
    private static ViewModelUtilities single_instance = null;
    public static ViewModelUtilities getInstance(){
        if (single_instance == null)
        {
            single_instance = new ViewModelUtilities();
        }
        return single_instance;
    }
    private ViewModelUtilities(){
    }
    /** ---------------------------------------------------- */

    private Vault vault;

    public void setVault(Vault v)
    {
        this.vault = v;
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

    public void changePassword(String oldPassword,String newPassword) throws Exception
    {
        ViewModel vm = ViewModel.getInstance();
        try
        {
            //Attempt to authenticate user before changing password
            new Vault(vm.getLoadedVaultName(),oldPassword);
            vault.changeVaultPassword(newPassword);
        }
        catch (IncorrectSecretKeyException | InvalidArgumentException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new Exception("Unexpected error occurred.");
        }
        finally
        {
            //If changing password was not successful reload the initial vault instance
            vm.unlockVault(vm.getLoadedVaultName(),oldPassword);
        }
        vault.changeVaultPassword(newPassword);
    }

//    public void changeVaultName(){};
//
//    public void deleteVault(){};

}
