package View.MainScene.EditEntry;

import View.MainScene.MainSceneController;
import View.MainScene.NewEntry.EntryTypes.EntryType;
import View.MainScene.NewEntry.EntryTypes.WebNewEntryController;
import View.MainScene.NewEntry.EntryTypes.ccNewEntryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static View.MainScene.MainSceneController.loadMainScene;

public class EditEntryController {

    private static Map<String,Object> data;
    public static void loadEditEntryUI(Map<String,Object> entryData , VBox entriesPanel) throws IOException
    {
        EditEntryController.data = entryData;

        Parent node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/EditEntry/editEntry.fxml"));
        entriesPanel.getChildren().clear();
        entriesPanel.getChildren().add(node);
    }

    @FXML private TextField nameValue;
    @FXML private AnchorPane valuesAp;
    @FXML private Label errorMessageLabel;

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
    }

    public void loadEntryDataFields() throws IOException
    {
        String entryType = (String) data.get("type");
        EntryType controller = null;
        Parent node = null;

        if(entryType.equals("webCredentials"))
        {
            node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/webNewEntry.fxml"));
            controller = WebNewEntryController.getInstance();
        }
        else if(entryType.equals("DebitCard"))
        {
            node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/ccNewEntry.fxml"));
            controller = ccNewEntryController.getInstance();
        }

        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
        controller.loadEditEntryUI(valuesAp,data);
    }

    public void saveChanges()
    {
        Map<String,String> newData = null;
        EntryType entryController = null;
        String currentTypeSelected = (String) data.get("type");
        boolean changedDataFlag  = false;

        if(currentTypeSelected.equals("webCredentials"))
        {
            entryController = WebNewEntryController.getInstance();
        }
        else if(currentTypeSelected.equals("DebitCard"))
        {
            entryController = ccNewEntryController.getInstance();
        }
        /** Additional types of entries go here as else-if */
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
                System.out.println("no change");
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
