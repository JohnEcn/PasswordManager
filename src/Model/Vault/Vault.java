package Model.Vault;
import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.InvalidArgumentException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import Model.RWHandler.Serializer;

import javax.crypto.BadPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Vault {

    private final String vaultName;
    private final String encryptionKey;
    private final Serializer IOHandler;
    private  ArrayList<Element> vaultElements = new ArrayList<Element>();

    public Vault(String vaultName, String encryptionKey) throws IncorrectSecretKeyException , InvalidArgumentException
    {
        this.vaultName = vaultName;
        this.encryptionKey = encryptionKey;
        this.IOHandler = new Serializer(this.vaultName,this.encryptionKey);
        this.retrieveVault();
    }

    private void saveVault()
    {
        try
        {
            IOHandler.serialize(vaultElements);
        }
        catch (IOException e)
        {
            // IOException handling here
        }
    }

    private void retrieveVault() throws IncorrectSecretKeyException
    {
        try
        {
            vaultElements = (ArrayList<Element>) this.IOHandler.deserialize();
        }
        catch (BadPaddingException e)
        {
            throw new IncorrectSecretKeyException("Wrong Password");
        }
        catch (IOException e)
        {
            if(e.getClass() == FileNotFoundException.class)
            {
                //Vault with this name does not exist
                this.saveVault();
            }
        }
    }

    //Debit card add
    public void addElement(String entryName, long number, short expireMonth, short expireYear, short ccv2 , String ownersName) throws NotUniqueEntryNameException, InvalidArgumentException
    {
        Element debitCard = new DebitCard(entryName,number,expireMonth,expireYear,ccv2,ownersName);
        this.insertionHandler(debitCard);
    }

    //Web credentials add
    public void addElement(String entryName, String username, String email, String password, String url) throws NotUniqueEntryNameException, InvalidArgumentException
    {
        Element webCredentials = new WebCredentials(entryName, username, email, password, url);
        this.insertionHandler(webCredentials);
    }

    private void insertionHandler(Element entry) throws NotUniqueEntryNameException, InvalidArgumentException
    {
        boolean nameValidity = this.checkNameUniqueness(entry.getName());
        String argumentsValidity = entry.validate();

        if(nameValidity && argumentsValidity.equals("OK"))
        {
            this.vaultElements.add(entry);
            this.saveVault();
        }
        else if(!argumentsValidity.equals("OK"))
        {
            throw new InvalidArgumentException(argumentsValidity);
        }
        else
        {
            throw new NotUniqueEntryNameException("Entry name has to be unique");
        }
    }

    private boolean checkNameUniqueness(String entryName)
    {
        for(int i=0; i<this.vaultElements.size(); i++)
        {   String s = this.vaultElements.get(i).getName();
            if(s.equals(entryName))
            {
                return false;
            }
        }
        return true;
    }

    public void removeElement(String entryUniqueName)
    {
        for(int i = 0;i<vaultElements.size(); i++)
        {
            if(entryUniqueName.equals(this.vaultElements.get(i).getName()))
            {
                this.vaultElements.remove(i);
                saveVault();
            }
        }
    }

    public String getVaultElements()
    {
        //Converts Elements array to Json and returns it
        String Json = "[";

        for(int i = 0;i<vaultElements.size(); i++)
        {
            Json += this.vaultElements.get(i).toJson() + ",";
        }
        return Json.substring(0,Json.length() - 1) + "]";
    }
}
