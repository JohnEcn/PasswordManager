package View.MainScene.UserSettings.SettingType;

import View.MainScene.UserSettings.UserSettings;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class PasswordChange {

    @FXML private PasswordField oldPasswordTextField;
    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField ConfirmPasswordTextField;

    public void collectPasswordData()
    {
        String oldPass = oldPasswordTextField.getText();
        String newPass = newPasswordTextField.getText();
        String newPassConf = ConfirmPasswordTextField.getText();

        if(newPass.equals(newPassConf) && !newPass.equals(oldPass) && !newPass.equals(""))
        {
            requestPasswordChange(oldPass,newPass);
        }
        else
        {
            newPasswordTextField.setStyle("-fx-border-color:red");
            ConfirmPasswordTextField.setStyle("-fx-border-color:red");
            newPasswordTextField.setText("");
            ConfirmPasswordTextField.setText("");
        }
    }
    private void requestPasswordChange(String oldPass,String newPass)
    {
        UserSettings.getInstance().changePassword(oldPass,newPass);
    }
}
