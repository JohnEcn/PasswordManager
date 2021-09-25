package View.MainScene.UserSettings;

import View.MainScene.MainSceneController;
import ViewModel.ViewModelUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;

public class UserSettings
{
    private static UserSettings controller_instance = null;
    public static UserSettings getInstance()
    {
        return controller_instance;
    }

    public static void loadSettingsUI(VBox container) throws IOException
    {
        URL fxmlURL = UserSettings.class.getResource("settings.fxml");
        Parent node = FXMLLoader.load(fxmlURL);
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    @FXML private AnchorPane settingsContainer;
    @FXML private Button changePasswordBut;
    @FXML private Button changeNameBut;
    @FXML private Button exportCsvBut;
    @FXML private Button deleteVaultBut;
    @FXML private HBox rightSettingsContainer;
    @FXML private Label errorMessageLabel;
    private String currentSetting = "passwordChange";

    @FXML
    protected void initialize() throws IOException
    {
        loadSettingUI(changePasswordBut);
        controller_instance = this;

        /** Temporary disabled buttons */
        changeNameBut.setDisable(true);
        exportCsvBut.setDisable(true);
        deleteVaultBut.setDisable(true);
    }

    public void backToMainScene() throws IOException
    {
        MainSceneController.loadMainScene(settingsContainer.getScene());
    }
    public void loadSettingUI(Button btn) throws IOException
    {
        Button[] settingsButtons = {changePasswordBut,changeNameBut,exportCsvBut,deleteVaultBut};
        URL fxmlURL = UserSettings.class.getResource("SettingType/"+ this.currentSetting +".fxml");
        Parent root = FXMLLoader.load(fxmlURL);
        rightSettingsContainer.getChildren().clear();
        rightSettingsContainer.getChildren().add(root);

        for(int i = 0; i< settingsButtons.length; i++)
        {
            settingsButtons[i].setStyle("-fx-border-color: #636363;");
        }

        btn.setStyle("-fx-border-color: #FF7E06;");
    }

    public void changePassword(String oldPass,String newPass)
    {
        try
        {
            ViewModelUtilities.getInstance().changePassword(oldPass,newPass);
            backToMainScene();
            MainSceneController.getInstance().displayFeedbackMessage("Password changed","green");
        }
        catch (Exception e)
        {
            errorMessageLabel.setText(e.getMessage());
        }
    }

    /** Methods called by the buttons */
    public void changePasswordUI(ActionEvent a) throws IOException
    {
        this.currentSetting = "passwordChange";
        loadSettingUI(changePasswordBut);
    }
    public void changeNameUI(ActionEvent a) throws IOException
    {
        this.currentSetting = "changeName";
        loadSettingUI(changeNameBut);
    }
    public void exportCsvUI(ActionEvent a) throws IOException
    {
        this.currentSetting = "exportCsv";
        loadSettingUI(exportCsvBut);
    }
    public void deleteVaultUI(ActionEvent a) throws IOException
    {
        this.currentSetting = "deleteVault";
        loadSettingUI(deleteVaultBut);
    }

}
