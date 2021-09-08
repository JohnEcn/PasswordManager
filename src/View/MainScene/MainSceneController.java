package View.MainScene;

import Model.CustomExceptions.InvalidArgumentException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import View.MainScene.NewEntry.EntryTypes.ccNewEntryController;
import ViewModel.ViewModel;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static View.MainScene.NewEntry.NewEntryController.loadNewEntryUI;

public class MainSceneController {

    private static MainSceneController controller_instance = null;
    public static MainSceneController getInstance()
    {
        return controller_instance;
    }

    private final ViewModel vm = ViewModel.getInstance();
    private final BoxBlur boxBlur = new BoxBlur();

    @FXML private AnchorPane rootContainer;
    @FXML private VBox entriesPanel;
    @FXML private Label messageLabel;

    @FXML
    protected void initialize()
    {
        String vaultName = vm.getLoadedVaultName();;
        Label vaultNameLabel = (Label) rootContainer.lookup("#mainLabel");
        vaultNameLabel.setText(vaultName);

        boxBlur.setWidth(7);
        boxBlur.setHeight(2);
        boxBlur.setIterations(6);
        try
        {
            rootContainer.lookup("#sensDataCheckbox").setDisable(false);
            displayEntries();
        }
        catch (Exception e)
        {
            messageLabel.setText("Unexpected error occurred.");
        }

        controller_instance = this;
    }

    public static void loadMainScene(Scene scene) throws IOException
    {
        /** Loads this controller's Scene */
        Parent root = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/mainScene.fxml"));
        scene.setRoot(root);
    }

    public void onTopController(ActionEvent a)
    {
        try
        {
            CheckBox checkbox = (CheckBox) a.getSource();
            boolean checkBoxState = checkbox.isSelected();
            Stage stage = (Stage) rootContainer.getScene().getWindow();
            stage.setAlwaysOnTop(checkBoxState);
        }
        catch (Exception e)
        {
            messageLabel.setText("Unexpected error occurred.");
        }
    }

    public void hideSensitiveDataController(ActionEvent a)
    {
        try
        {
            CheckBox checkbox = (CheckBox) a.getSource();
            boolean checkBoxState = checkbox.isSelected();

            if(checkBoxState)
            {
                boxBlur.setWidth(7);
                boxBlur.setHeight(2);
                boxBlur.setIterations(6);
            }
            else
            {
                boxBlur.setWidth(0);
                boxBlur.setHeight(0);
                boxBlur.setIterations(0);
            }
            displayEntries();
        }
        catch (Exception e)
        {
            messageLabel.setText("Unexpected error occurred.");
        }
    }

    public void copyValueToClipBoard(MouseEvent m)
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        Label value = (Label) m.getSource();
        content.putString(value.getText());
        clipboard.setContent(content);

        /** Feedback that the value is copied */
        messageLabel.setText("Value copied..");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300));
        fadeIn.setNode(messageLabel);
        fadeIn.setFromValue(1);
        fadeIn.setToValue(0);
        fadeIn.setCycleCount(1);
        fadeIn.setDelay(Duration.millis(1300));
        fadeIn.setAutoReverse(false);
        fadeIn.playFromStart();
    }

    private void addMouseClickListener(Label label)
    {
        label.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent m)
            {
                copyValueToClipBoard(m);
            }
        });
    }

    public void newEntry()
    {
        try
        {
            loadNewEntryUI(entriesPanel);
            rootContainer.lookup("#sensDataCheckbox").setDisable(true);;
        }
        catch (IOException e)
        {
            messageLabel.setText("Unexpected error occurred.");
        }
    }

    public void displayEntries() throws IOException
    {
        ArrayList<Map<String,Object>> vaultContents = vm.getVaultContents();
        ArrayList<AnchorPane> rows = new ArrayList<AnchorPane>();
        if(vaultContents == null){return;}

        /** Creates anchorPane elements with the entry data.
         *  To add a new type of entry, add a new else if
         *  that calls the appropriate method for the entry type */
        for (int i = 0; i < vaultContents.size(); i++)
        {
            String entryType = (String) vaultContents.get(i).get("type");
            if(entryType.equals("webCredentials"))
            {
                AnchorPane newRow = getWebCredRow(vaultContents.get(i));
                rows.add(newRow);
            }
            else if(entryType.equals("DebitCard"))
            {
                AnchorPane newRow = getCreditCardRow(vaultContents.get(i));
                rows.add(newRow);
            }
        }
        /** ------------------------------------------------------- */

        entriesPanel.getChildren().clear();
        for(int i = 0; i < rows.size(); i++)
        {
            entriesPanel.getChildren().add(rows.get(i));
        }
    }

    public void addNewEntry(Map<String,String> data) throws InvalidArgumentException, NotUniqueEntryNameException {
        //Passing the data to the ViewModel
        vm.addToVault(data);
    }

    /** Methods that build the entry rows for each entry type */
    private AnchorPane getWebCredRow(Map<String,Object> data) throws IOException
    {
        AnchorPane newRow = FXMLLoader.load(getClass().getResource("/View/MainScene/EntryRow/wevCredRow.fxml"));

        Label entryNameLabel = (Label) newRow.lookup("#entryNameLabel");
        entryNameLabel.setText((String)data.get("name"));

        Label emailValue = (Label) newRow.lookup("#emailValue");
        emailValue.setText((String)data.get("email"));
        addMouseClickListener(emailValue);

        Label usernameValue = (Label) newRow.lookup("#usernameValue");
        usernameValue.setText((String)data.get("username"));
        addMouseClickListener(usernameValue);

        Label passwordValue = (Label) newRow.lookup("#passwordValue");
        passwordValue.setText((String)data.get("password"));
        passwordValue.setEffect(boxBlur);
        addMouseClickListener(passwordValue);

        Label urlValue = (Label) newRow.lookup("#urlValue");
        urlValue.setText((String)data.get("url"));
        addMouseClickListener(urlValue);

        return newRow;
    }

    private AnchorPane getCreditCardRow(Map<String,Object> data) throws IOException
    {
        AnchorPane newRow = FXMLLoader.load(getClass().getResource("/View/MainScene/EntryRow/creditCardRow.fxml"));

        Label entryNameLabel = (Label) newRow.lookup("#entryNameLabel");
        entryNameLabel.setText((String)data.get("name"));

        Label ccNumberValue = (Label) newRow.lookup("#ccNumberValue");
        String ccNum = (String) data.get("number");
        String formatedCcNum = ccNum.substring(0,4) + "-" + ccNum.substring(4, 8) + "-" + ccNum.substring(8, 12) + "-" + ccNum.substring(12, ccNum.length());
        ccNumberValue.setText(formatedCcNum);
        ccNumberValue.setEffect(boxBlur);
        addMouseClickListener(ccNumberValue);

        Label usernameValueLabel = (Label) newRow.lookup("#expDateValue");
        usernameValueLabel.setText((String)data.get("expireMonth") + "/" + (String)data.get("expireYear"));
        usernameValueLabel.setEffect(boxBlur);
        addMouseClickListener(usernameValueLabel);

        Label urlValueLabel = (Label) newRow.lookup("#ccv2Value");
        urlValueLabel.setText((String)data.get("ccv2"));
        urlValueLabel.setEffect(boxBlur);
        addMouseClickListener(urlValueLabel);

        Label ownersNameValue = (Label) newRow.lookup("#ownersNameValue");
        ownersNameValue.setText((String)data.get("ownersName"));
        ownersNameValue.setEffect(boxBlur);
        addMouseClickListener(ownersNameValue);

        return newRow;

    }

}
