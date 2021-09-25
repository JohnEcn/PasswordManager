package View.MainScene.EditEntry;

import View.MainScene.MainSceneController;
import View.MainScene.NewEntry.EntryTypes.EntryType;
import View.MainScene.NewEntry.NewEntryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static View.MainScene.MainSceneController.loadMainScene;

/**
 *  This class is responsible for displaying the UI for editing and deleting an entry
 *  and also for collecting the new entry data and passing them to the MainSceneController.saveEntryChanges()
 */

public class EditEntryController {

    private static Map<String,Object> data;
    public static void loadEditEntryUI(Map<String,Object> entryData , VBox entriesPanel) throws IOException
    {
        EditEntryController.data = entryData;
        URL fxmlURL = EditEntryController.class.getResource("editEntry.fxml");
        Parent node = FXMLLoader.load(fxmlURL);
        entriesPanel.getChildren().clear();
        entriesPanel.getChildren().add(node);
    }

    @FXML private TextField nameValue;
    @FXML private AnchorPane valuesAp;
    @FXML private Label errorMessageLabel;
    private EntryType entryTypeController = null;

    @FXML
    protected void initialize() throws IOException
    {
        nameValue.setText((String) data.get("name"));
        loadEntryDataFields();
    }

    public void backToMainScene() throws IOException
    {
        loadMainScene(nameValue.getScene());
    }

    public void deleteEntry()
    {
        //Disable always on top before displaying the delete dialog
        Stage stage = (Stage) nameValue.getScene().getWindow();
        boolean onTopPropertyStatus = stage.alwaysOnTopProperty().get();
        stage.setAlwaysOnTop(false);

        /** Confirmation window */
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Delete confirmation");
        a.setHeaderText("You are about to delete " + (String) data.get("name") + ".\nAction cannot be reverted!");
        a.showAndWait();
        ButtonType result = a.getResult();
        String resultText = result.getText();

        if(resultText.equals("OK"))
        {
            try
            {
                MainSceneController.getInstance().deleteEntry((String) data.get("name"));
                backToMainScene();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //Restore the onTopProperty
        stage.setAlwaysOnTop(onTopPropertyStatus);
    }

    public void loadEntryDataFields() throws IOException
    {
        String entryType = (String) data.get("type");
        EntryType controller = null;
        Parent node = null;

        //Load the correct fxml of the entry type that is edited
        String[] entryTypeName = {"webCredentials","DebitCard","blockchainKeys","note"};
        String[] entryTypeFXML = {"webNewEntry","ccNewEntry","blockchainKeyNewEntry","noteNewEntry"};
        for(int i = 0; i< entryTypeName.length; i++)
        {
            if(entryType.equals(entryTypeName[i]))
            {
                URL fxmlURL = NewEntryController.class.getResource("EntryTypes/"+ entryTypeFXML[i] +".fxml");
                FXMLLoader loader = new FXMLLoader(fxmlURL);
                node = loader.load();
                controller = loader.getController();
                this.entryTypeController = controller;
                break;
            }
        }

        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
        controller.loadEditEntryUI(valuesAp,data);
    }

    public void saveChanges()
    {
        Map<String,String> newData = null;
        EntryType entryController = this.entryTypeController;
        boolean changedDataFlag  = false;

        try
        {
            //Collect the data
            newData = entryController.collectData();
            newData.put("name",nameValue.getText());

            //Check if the data is changed
            if(checkIfDataChanged(data,newData))
            {
                //No change in data
                backToMainScene();
                return;
            }

            if(nameValue.getText().equals("")){
                throw new Exception("Name cannot be empty");
            }

            //Call the MainSceneController method to pass the data to ViewModel
            MainSceneController mainSceneController = MainSceneController.getInstance();
            mainSceneController.saveEntryChanges((String) data.get("name"),newData);
            this.backToMainScene();
            mainSceneController.displayFeedbackMessage("Entry changed","orange");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            errorMessageLabel.setText(e.getMessage());
        }
    }

    public boolean checkIfDataChanged(Map<String,Object> oldData,Map<String,String> newData)
    {
        //Get the Map keys set and transform it to an array
        Set<String> s = newData.keySet();
        String [] keys = s.toArray(new String[s.size()]);

        //Check if each key has the same value in both 'oldData' and 'newData' Maps
        for(int i = 0; i<keys.length; i++)
        {
            String tempKey = keys[i];
            if(!oldData.get(tempKey).equals(newData.get(tempKey)))
            {
                return false;
            }
        }
        return true;
    }
}
