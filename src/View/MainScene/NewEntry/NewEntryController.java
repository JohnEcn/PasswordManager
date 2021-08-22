package View.MainScene.NewEntry;

import View.MainScene.MainSceneController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

import static View.MainScene.MainSceneController.loadMainScene;

public class NewEntryController {

    @FXML private AnchorPane newEntryContainer;
    @FXML private AnchorPane valuesAp;
    @FXML private ImageView ccType;
    @FXML private ImageView webCredType;
    private String currentTypeSelected = "webCredType";

    @FXML
    protected void initialize()
    {
        setMouseClickTypeButton();
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
    }

    public void setEntryType(MouseEvent m) throws IOException
    {
        ImageView iv = (ImageView)m.getSource();
        String entryType = iv.getId();
        if(entryType.equals(currentTypeSelected)){return;}

        Parent node = null;
        if(entryType.equals("ccType"))
        {
           node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/ccNewEntry.fxml"));
           currentTypeSelected = "ccType";
        }
        else if(entryType.equals("webCredType"))
        {
            node = FXMLLoader.load(MainSceneController.class.getResource( "../MainScene/NewEntry/EntryTypes/webNewEntry.fxml"));
            currentTypeSelected = "webCredType";
        }
        /** Additional types can be added here as else if statements */

        valuesAp.getChildren().clear();
        valuesAp.getChildren().add(node);
    }
}
