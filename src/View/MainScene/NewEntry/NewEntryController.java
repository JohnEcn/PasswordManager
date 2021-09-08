package View.MainScene.NewEntry;

import View.MainScene.MainSceneController;
import View.MainScene.NewEntry.EntryTypes.EntryType;
import View.MainScene.NewEntry.EntryTypes.WebNewEntryController;
import View.MainScene.NewEntry.EntryTypes.ccNewEntryController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Map;

import static View.MainScene.MainSceneController.loadMainScene;

public class NewEntryController {

    @FXML private AnchorPane newEntryContainer;
    @FXML private AnchorPane valuesAp;
    @FXML private TextField nameValue;
    @FXML private ImageView ccType;
    @FXML private Region ccBorderRegion;
    @FXML private ImageView webCredType;
    @FXML private Region webBorderRegion;
    @FXML private Label errorMessageLabel;

    private String currentTypeSelected = "webCredType";

    @FXML
    protected void initialize() throws IOException {
        setMouseClickTypeButton();

        Parent node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/webNewEntry.fxml"));
        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
        webBorderRegion.setStyle("-fx-border-color: #FF7E06;");
    }

    public static void loadNewEntryUI(VBox root) throws IOException
    {
        Parent node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/newEntry.fxml"));
        root.getChildren().clear();
        root.getChildren().add(node);
    }

    public void backToMainScene() throws IOException
    {
        loadMainScene(newEntryContainer.getScene());
    }

    private void setMouseClickTypeButton()
    {
        ccType.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent m)
            {
                try {setEntryType(m);}
                catch (IOException e) {}
            }
        });

        webCredType.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent m)
            {
                try {setEntryType(m);}
                catch (IOException e) {}
            }
        });

        /** Additional types can be added here */
    }

    public void setEntryType(MouseEvent m) throws IOException
    {
        ImageView iv = (ImageView)m.getSource();
        String entryType = iv.getId();
        if(entryType.equals(currentTypeSelected)){return;}

        webBorderRegion.setStyle("-fx-border-color: #C0C0C0;");
        ccBorderRegion.setStyle("-fx-border-color: #C0C0C0;");

        Parent node = null;
        if(entryType.equals("ccType"))
        {
           node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/ccNewEntry.fxml"));
           ccBorderRegion.setStyle("-fx-border-color: #FF7E06;");
           currentTypeSelected = "ccType";
        }
        else if(entryType.equals("webCredType"))
        {
            node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/webNewEntry.fxml"));
            webBorderRegion.setStyle("-fx-border-color: #FF7E06;");
            currentTypeSelected = "webCredType";
        }
        /** Additional types can be added here as else if statements */

        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
    }

    public void saveNewEntry() throws IOException
    {
        Map<String,String> data = null;
        EntryType entryController = null;

        if(currentTypeSelected.equals("webCredType"))
        {
            entryController = WebNewEntryController.getInstance();
        }
        else if(currentTypeSelected.equals("ccType"))
        {
            entryController = ccNewEntryController.getInstance();
        }
        /** Additional types of entries go here as else-if */
        try
        {
            //Collect the data
             data = entryController.collectData();
             data.put("name",nameValue.getText());
             if(nameValue.getText().equals("")){
                 throw new Exception("Name cannot be empty");
             }

            //Call the MainSceneController method to pass the data to ViewModel

            MainSceneController mainSceneController = MainSceneController.getInstance();
            mainSceneController.addNewEntry(data);
            this.backToMainScene();
            mainSceneController.displayFeedbackMessage("New Entry Added","orange");
        }
        catch(Exception e)
        {
            errorMessageLabel.setText(e.getMessage());
        }
    }
}
