package View.MainScene;

import Model.CustomExceptions.InvalidArgumentException;
import Model.CustomExceptions.NotUniqueEntryNameException;
import View.MainScene.DisplayEntries.EntriesPanelHandler;
import View.MainScene.EditEntry.EditEntryController;
import View.MainScene.UserSettings.UserSettings;
import View.Utilities.Utilities;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static View.MainScene.NewEntry.NewEntryController.loadNewEntryUI;

/**
 *  This class is responsible for communicating with the ViewModel for saving, deleting
 *  editing and getting the vault entries.
 *
 *  Also this class displays the vault contents and initiates the addition of new entries
 *  and the editing of existing.
 */

public class MainSceneController {

    private static MainSceneController controller_instance = null;
    public static MainSceneController getInstance()
    {
        return controller_instance;
    }

    private final ViewModel vm = ViewModel.getInstance();
    private final BoxBlur boxBlur = new BoxBlur();
    private final FadeTransition fadeIn = new FadeTransition(Duration.millis(4500));

    @FXML private AnchorPane rootContainer;
    @FXML private VBox entriesPanel;
    @FXML private Label messageLabel;
    @FXML private TextField searchField;

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
            e.printStackTrace();
            messageLabel.setText("Unexpected error occurred.");
        }

        controller_instance = this;
    }

    public static void loadMainScene(Scene scene) throws IOException
    {
        if(controller_instance != null)
        {
            controller_instance.displayEntries();
            return;
        }
        /** Loads this controller's Scene */
        URL fxmlURL = MainSceneController.class.getResource("mainScene.fxml");
        Parent root = FXMLLoader.load(fxmlURL);
        scene.setRoot(root);
    }

    public void loadSettingsUI()
    {
        try
        {
            UserSettings.loadSettingsUI(entriesPanel);
        }
        catch (IOException e)
        {
            displayFeedbackMessage("Unexpected error occurred.", "red");
        }
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
            e.printStackTrace();
            displayFeedbackMessage("Unexpected error occurred.", "red");
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
            e.printStackTrace();
            displayFeedbackMessage("Unexpected error occurred.", "red");
        }
    }

    public void displayFeedbackMessage(String message, String color)
    {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: "+color+";");
        fadeIn.setNode(messageLabel);
        fadeIn.setFromValue(1);
        fadeIn.setToValue(0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
        fadeIn.playFromStart();
    }

    public void editEntry(Map<String,Object> data,VBox container)
    {
        try
        {
            /** Gets the entry data and entry type, then calls the method to display the edit entry UI */
            EditEntryController.loadEditEntryUI(data,container);
            rootContainer.lookup("#sensDataCheckbox").setDisable(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            displayFeedbackMessage("Unexpected error occurred.", "red");
        }
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
            e.printStackTrace();
            displayFeedbackMessage("Unexpected error occurred.", "red");
        }
    }

    public void displayEntries() throws IOException
    {
        ArrayList<Map<String,Object>> vaultContents = vm.getVaultContents();
        EntriesPanelHandler.getInstance(entriesPanel,boxBlur).displayEntries(vaultContents,searchField.getText());
    }

    public void deleteEntry(String entryUniqueName)
    {
        try
        {
            vm.removeElement(entryUniqueName);
            displayFeedbackMessage("Element Deleted", "orange");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            displayFeedbackMessage("Entry deletion failed.", "red");
        }

    }

    public void addNewEntry(Map<String,String> data) throws InvalidArgumentException, NotUniqueEntryNameException {
        //Passing the data to the ViewModel
        vm.addToVault(data);
    }

    public void saveEntryChanges(String entryName,Map<String,String> data) throws InvalidArgumentException, NotUniqueEntryNameException
    {
        vm.editVaultElement(entryName,data);
    }


}
