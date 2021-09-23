package View.MainScene.DisplayEntries;

import View.MainScene.MainSceneController;
import View.Utilities.Utilities;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 *  This class is responsible for populating the entriesContainer with the vault entries
 *  The MainSceneController calls the displayEntries() method and this class does all the job
 *  of displaying the vault content, filtering the displayed entries using the searchQuery and adding
 *  the various listener events where is needed.
 */

public class EntriesPanelHandler
{
    /** Ensure that only one instance of EntriesPanelHandler can exist */
    private static EntriesPanelHandler single_instance = null;
    private EntriesPanelHandler(VBox entriesContainer,BoxBlur boxBlur)
    {
        this.entriesContainer = entriesContainer;
        this.boxBlur = boxBlur;
    }
    public static EntriesPanelHandler getInstance(VBox entriesContainer,BoxBlur boxBlur)
    {
        single_instance = new EntriesPanelHandler(entriesContainer,boxBlur);
        return single_instance;
    }
    public static EntriesPanelHandler getInstance()
    {
        return single_instance;
    }
    /** ---------------------------------------------------- */

    private VBox entriesContainer;
    private  BoxBlur boxBlur;

    /** Method that populates the entriesContainer with the entry rows */
    public void displayEntries(ArrayList<Map<String,Object>> vaultContents,String searchQuery) throws IOException
    {
        vaultContents = filterEntries(vaultContents,searchQuery);
        ArrayList<AnchorPane> rows = new ArrayList<AnchorPane>();
        if(vaultContents == null || vaultContents.size()==0)
        {
            //Displaying empty vault message
            entriesContainer.getChildren().clear();
            Label label = new Label();
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(884,343);
            label.setText("No entries added yet...");
            label.setId("emptyVaultLabel");
            label.setPrefWidth(345);
            label.setLayoutX(270.0);
            label.setLayoutY(163.0);
            anchorPane.getChildren().add(label);
            entriesContainer.getChildren().add(anchorPane);
            return;
        }

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
            else if(entryType.equals("blockchainKeys"))
            {
                AnchorPane newRow = getBlockChainKeysRow(vaultContents.get(i));
                rows.add(newRow);
            }
        }
        /** ------------------------------------------------------- */

        entriesContainer.getChildren().clear();
        for(int i = 0; i < rows.size(); i++)
        {
            entriesContainer.getChildren().add(rows.get(i));
        }
    }

    /** Gets an arraylist of entries and returns an arraylist containing the entries that matchthe searchQuey */
    private ArrayList<Map<String,Object>> filterEntries(ArrayList<Map<String,Object>> entries,String searchQuery)
    {

        ArrayList<String> entryNames = new ArrayList<>();
        ArrayList<Map<String,Object>> results = new ArrayList<>();
        if(entries == null){return results;}

        //Get only the entry names in an arraylist
        for(int i=0; i<entries.size(); i++)
        {
            entryNames.add((String)entries.get(i).get("name"));
        }

        //Search the entry names for matches using the searchQuery
        String[] searchResults = Utilities.searchFunction(searchQuery,entryNames.toArray(new String[entryNames.size()]));

        //All entries that match go  ArrayList 'results'
        for(int i=0; i<entries.size(); i++)
        {
            for(int k=0; k<searchResults.length; k++)
            {
                if(searchResults[k].equalsIgnoreCase((String) entries.get(i).get("name")))
                {
                    results.add(entries.get(i));
                }
            }
        }
        return results;
    }

    /** Methods that build the entry rows for each entry type */
    private AnchorPane getWebCredRow(Map<String,Object> data) throws IOException
    {
        URL fxmlURL = MainSceneController.class.getResource("EntryRow/wevCredRow.fxml");
        AnchorPane newRow = FXMLLoader.load(fxmlURL);
        ArrayList <Label> valueLabels = new ArrayList<>();

        Label entryNameLabel = (Label) newRow.lookup("#entryNameLabel");
        entryNameLabel.setText((String)data.get("name"));
        editEntryClickListener(entryNameLabel,data);
        entryNameLabel.setTooltip(new Tooltip("Edit"));

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
        URL fxmlURL = MainSceneController.class.getResource("EntryRow/creditCardRow.fxml");
        AnchorPane newRow = FXMLLoader.load(fxmlURL);

        Label entryNameLabel = (Label) newRow.lookup("#entryNameLabel");
        entryNameLabel.setText((String)data.get("name"));
        editEntryClickListener(entryNameLabel,data);
        entryNameLabel.setTooltip(new Tooltip("Edit"));

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

    private AnchorPane getBlockChainKeysRow(Map<String,Object> data) throws IOException
    {
        URL fxmlURL = MainSceneController.class.getResource("EntryRow/blockchainKeysRow.fxml");
        AnchorPane newRow = FXMLLoader.load(fxmlURL);

        Label entryNameLabel = (Label) newRow.lookup("#entryNameLabel");
        entryNameLabel.setText((String)data.get("name"));
        editEntryClickListener(entryNameLabel,data);
        entryNameLabel.setTooltip(new Tooltip("Edit"));

        Label publicKey = (Label) newRow.lookup("#publicKeyValue");
        publicKey.setText((String)data.get("publicKey"));
        addMouseClickListener(publicKey);

        Label privateKey = (Label) newRow.lookup("#privateKeyValue");
        privateKey.setText((String)data.get("privateKey"));
        privateKey.setEffect(boxBlur);
        addMouseClickListener(privateKey);

        return newRow;
    }

    /** Click listeners used for the entry rows */
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

    private void copyValueToClipBoard(MouseEvent m)
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        Label value = (Label) m.getSource();
        content.putString(value.getText());
        clipboard.setContent(content);

        // Feedback that the value is copied
        MainSceneController.getInstance().displayFeedbackMessage("Value copied..","black");
    }

    private void editEntryClickListener(Label label, Map<String,Object> data)
    {
        label.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent m)
            {
                MainSceneController mainSceneController = MainSceneController.getInstance();
                mainSceneController.editEntry(data,entriesContainer);
            }
        });
    }



}
