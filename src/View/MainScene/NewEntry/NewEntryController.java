package View.MainScene.NewEntry;

import View.MainScene.MainSceneController;
import View.MainScene.NewEntry.EntryTypes.EntryType;
import View.Utilities.Utilities;
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
import java.net.URL;
import java.util.Map;

import static View.MainScene.MainSceneController.loadMainScene;

/**
 *  This class is responsible for displaying the UI for adding a new entry
 *  and also for collecting the new entry data and passing them to the MainSceneController.saveNewEntry()
 */

public class NewEntryController {

    @FXML private AnchorPane newEntryContainer;
    @FXML private AnchorPane valuesAp;
    @FXML private TextField nameValue;
    @FXML private ImageView ccType;
    @FXML private Region ccBorderRegion;
    @FXML private ImageView webCredType;
    @FXML private Region blockChainKeyBorderRegion;
    @FXML private ImageView blockchainKeyType;
    @FXML private Region webBorderRegion;
    @FXML private ImageView noteType;
    @FXML private Region noteBorderRegion;
    @FXML private Label errorMessageLabel;

    private String currentTypeSelected = "webCredType";
    private EntryType currentEntryController = null;
    @FXML
    protected void initialize() throws IOException
    {
        setMouseClickTypeButton();

        URL fxmlURL = NewEntryController.class.getResource("EntryTypes/webNewEntry.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlURL);
        Parent node = loader.load();
        currentEntryController = loader.getController();
        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
        webBorderRegion.setStyle("-fx-border-color: #FF7E06;");
    }

    public static void loadNewEntryUI(VBox root) throws IOException
    {
        URL fxmlURL = NewEntryController.class.getResource("newEntry.fxml");
        Parent node = FXMLLoader.load(fxmlURL);
        root.getChildren().clear();
        root.getChildren().add(node);
    }

    public void backToMainScene() throws IOException
    {
        loadMainScene(newEntryContainer.getScene());
    }

    private void setMouseClickTypeButton()
    {
        //This array holds the 'buttons' that when clicked a entry type is selected
        //To add a new button simply add an imageView in the array
        ImageView[] entryTypesButtons = {ccType , webCredType ,blockchainKeyType,noteType};

        for(int i = 0; i<entryTypesButtons.length; i++)
        {
            entryTypesButtons[i].setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent m)
                {
                    try {setEntryType(m);}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void setEntryType(MouseEvent m) throws IOException
    {
        ImageView iv = (ImageView)m.getSource();
        String entryType = iv.getId();
        if(entryType.equals(currentTypeSelected)){return;}
        Parent node = null;

        //To add a button for a new entry type,
        // (1) Add the entry type name
        // (2) add the fxml file that corresponds to the new entry and
        // (3) Add the button region of the new button
        String[] entryTypeName = {"ccType","webCredType","blockchainKeyType","noteType"};
        String[] entryTypeFXML = {"ccNewEntry","webNewEntry","blockchainKeyNewEntry","noteNewEntry"};
        Region[] buttonRegion =  {ccBorderRegion,webBorderRegion,blockChainKeyBorderRegion,noteBorderRegion};

        for(int i =0; i<buttonRegion.length; i++)
        {
            //Reset the borders of all buttons so they appear not selected
            buttonRegion[i].setStyle("-fx-border-color: #C0C0C0;");
        }

        for(int i =0; i<entryTypeName.length; i++)
        {
            //Load the correct fxml and set the border of the pressed button orange
            if(entryType.equals(entryTypeName[i]))
            {
                URL fxmlURL = getClass().getResource("EntryTypes/"+entryTypeFXML[i]+".fxml");
                FXMLLoader loader = new FXMLLoader(fxmlURL);
                node = loader.load();
                buttonRegion[i].setStyle("-fx-border-color: #FF7E06;");
                currentTypeSelected = entryTypeName[i];
                currentEntryController = loader.getController();
                break;
            }
        }
        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
    }

    public void saveNewEntry() throws IOException
    {
        Map<String,String> data = null;
        EntryType entryController = currentEntryController;

        try
        {
            //Collect the data
            data = entryController.collectData();
            data.put("name",nameValue.getText());
            if(nameValue.getText().equals(""))
            {
                throw new Exception("Name cannot be empty");
            }

            //Escape illegal characters from data collected
            for (Map.Entry<String, String> entry : data.entrySet())
            {
                data.replace(entry.getKey(),Utilities.escapeIllegalCharacters(entry.getValue()));
            }

            //Call the MainSceneController method to pass the data to ViewModel
            MainSceneController mainSceneController = MainSceneController.getInstance();
            mainSceneController.addNewEntry(data);
            this.backToMainScene();
            mainSceneController.displayFeedbackMessage("New Entry Added","orange");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            errorMessageLabel.setText(e.getMessage());
        }
    }
}
