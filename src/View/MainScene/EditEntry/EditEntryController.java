package View.MainScene.EditEntry;

import View.MainScene.MainSceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Map;

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

    @FXML
    protected void initialize()
    {
        nameValue.setText((String) data.get("name"));
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
            System.out.println("Deleting...");
            try
            {
                //MainSceneController.getInstance().deleteEntry((String) data.get("name"));
                backToMainScene();
            }
            catch (Exception e)
            {
            }
        }
    }
}
