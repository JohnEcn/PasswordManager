package View.AuthScene;

import Model.CustomExceptions.IncorrectSecretKeyException;
import Model.CustomExceptions.InvalidArgumentException;
import View.MainScene.MainSceneController;
import ViewModel.ViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;

public class AuthSceneController implements Initializable {

    @FXML
    private final ViewModel vm = ViewModel.getInstance();
    @FXML private HBox usersHbox;
    @FXML private AnchorPane rootAp;

    /** FXML Resources */
    private final String FXMLPATH = "FxmlResources/";
    private final String AUTHSCENE = FXMLPATH + "authScene.fxml";
    private final String SELECTEDVAULT = FXMLPATH + "selectedVault.fxml";
    private final String USERVAULTBOX = FXMLPATH + "userVaultBox.fxml";
    private final String USERVAULTBOXMINI = FXMLPATH + "userVaultBoxMini.fxml";
    private final String NEWVAULT = FXMLPATH + "newVault.fxml";
    /** FXML Resources */

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            displayVaults();
            rootAp.lookup("#newVaultLabel").setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t)
                {
                    displayNewVault();
                }
            });
        }
        catch (Exception e)
        {
            displayGeneralError("Unexpected error occurred.");
        }
    }
    public void displayVaults() throws IOException
    {
        if(vm.isVaultUnlocked())
        {
            /** If a vault is unlocked this method should not be called */
            return;
        }

        ArrayList<String> vaultNames = this.vm.getExistingVaultNames();
        usersHbox.getChildren().clear();

        if(vaultNames.size() == 1)
        {
            /** If there is only one vault
                selectVault() should be called */
            selectVault(vaultNames.get(0));
            return;
        }

        for(int i = 0; i < vaultNames.size(); i++)
        {
            String labelName = "#username";
            String fxmlResource = USERVAULTBOX; //fxml filename

            if(vaultNames.size() > 4)
            {
                //if there are more than 4 vaults , different fxml resource will be loeaded
                //with smaller size elements
                labelName = "#usernameMini";
                fxmlResource = USERVAULTBOXMINI; //fxml filename
                usersHbox.setSpacing(14);
            }

            AnchorPane userVaultBox = (AnchorPane) FXMLLoader.load(getClass().getResource( fxmlResource));
            Label username = (Label) userVaultBox.lookup(labelName);

            //Make the imageView to not 'block' the clicks on it's parent element
            ImageView img = (ImageView) userVaultBox.lookup("#vaultImg");
            img.setPickOnBounds(false);

            //Event listener for each vault
            int k = i;
            Rectangle rect = (Rectangle) userVaultBox.lookup("#mainBox");
            rect.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t) {
                    selectVault(vaultNames.get(k));
                }
            });

            username.setText(vaultNames.get(i));
            usersHbox.getChildren().add(userVaultBox);
        }
    }
    public void selectVault(String vaultName)
    {
        try
        {
            AnchorPane userVaultBox = (AnchorPane) FXMLLoader.load(getClass().getResource(SELECTEDVAULT)); //fxml filename

            Label username = (Label) userVaultBox.lookup("#username");
            username.setText(vaultName);

            //Event listener for unlock button
            Button unlockButton = (Button) userVaultBox.lookup("#unlockButton");
            unlockButton.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t)
                {
                    unlockVault();
                }
            });

            //Event listener for back button (Left arrow)
            ImageView backImage = (ImageView) userVaultBox.lookup("#backButton");
            backImage.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t) {
                    try
                    {
                        displayVaults();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        /** Display error message */
                    }
                }
            });

            usersHbox.getChildren().clear();
            usersHbox.getChildren().add(userVaultBox);
        }
        catch (IOException e)
        {

        }
    }
    public void displayNewVault()
    {
        try
        {
            AnchorPane userVaultBox = (AnchorPane) FXMLLoader.load(getClass().getResource(NEWVAULT)); //fxml filename

            //Event listener for back button (Left arrow)
            ImageView backImage = (ImageView) userVaultBox.lookup("#backButton");
            backImage.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t) {
                    try
                    {
                        displayVaults();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        /** Display error message */
                    }
                }
            });

            //Event listener for new Vault button
            Button newVaultButton = (Button) userVaultBox.lookup("#newVaultButton");
            newVaultButton.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent t)
                {
                        newVault();
                }
            });

            usersHbox.getChildren().clear();
            usersHbox.getChildren().add(userVaultBox);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void unlockVault()
    {
        Label vaultNameLabel = (Label) usersHbox.lookup("#username");
        String vaultName= vaultNameLabel.getText();

        PasswordField passwordField = (PasswordField) usersHbox.lookup("#passwordField");
        String password = passwordField.getText();

        try
        {
            vm.unlockVault(vaultName,password);

            if(vm.isVaultUnlocked())
            {
                loadVaultContents();
            }
            else
            {
                displayGeneralError("Unexpected error occurred.");
            }
        }
        catch (Exception e)
        {
            if(e.getClass() == IncorrectSecretKeyException.class || e.getClass() == InvalidArgumentException.class)
            {
                Label errorLabel = (Label) usersHbox.lookup("#passErrorMessageLabel");
                errorLabel.setText("Incorrect password.");
            }
            else
            {
                displayGeneralError("Unexpected error occurred.");
            }
            e.printStackTrace();
        }
    }
    public void newVault()
    {
        TextField vaultNameLabel = (TextField) usersHbox.lookup("#usernameField");
        String vaultName= vaultNameLabel.getText();

        PasswordField passwordField = (PasswordField) usersHbox.lookup("#passwordField");
        String password = passwordField.getText();

        try
        {
            vm.newVault(vaultName,password);

            if(vm.isVaultUnlocked())
            {
                loadVaultContents();
            }
            else
            {
                displayGeneralError("Unexpected error occurred.");
            }
        }
        catch (Exception e)
        {
            Label errorLabel = (Label) usersHbox.lookup("#passErrorMessageLabel");
            e.printStackTrace();
            if(e.getClass() == FileAlreadyExistsException.class)
            {
                errorLabel.setText("This vault name already exists.");
            }
            else if(e.getClass() == InvalidArgumentException.class)
            {
                errorLabel.setText(e.getMessage());
            }
            else
            {
                displayGeneralError("Unexpected error occurred.");
            }
        }
    }
    private void loadVaultContents() throws IOException
    {
        MainSceneController.loadMainScene(rootAp.getScene());
    }
    private void displayGeneralError(String errorMsg)
    {
        Label generalErrorLabel = (Label) rootAp.lookup("#generalErrorMessageLabel");
        generalErrorLabel.setText(errorMsg);
    }
}
