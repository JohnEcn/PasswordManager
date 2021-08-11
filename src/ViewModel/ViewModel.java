package ViewModel;
import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.Vault.Vault;

import java.nio.file.FileAlreadyExistsException;

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

    public void unlockVault(String vaultName,String vaultKey) throws Exception {

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
            try {
                this.vault = new Vault(vaultName, vaultKey);
                this.isVaultOpen = true;
            } catch (IncorrectSecretKeyException e) {
                this.vault = null;
                this.isVaultOpen = false;
                throw new FileAlreadyExistsException("Vault exists");
            } catch (Exception e) {
                this.vault = null;
                this.isVaultOpen = false;
                throw new Exception("Unexpected error occurred");
            }
        }
    }
}
