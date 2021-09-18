package View.MainScene.NewEntry.EntryTypes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockchainKeyNewEntryController extends EntryType {

    @FXML TextField publicKeyNewValue;
    @FXML TextField privateKeyNewValue;

    private static BlockchainKeyNewEntryController controller_instance = null;
    public static BlockchainKeyNewEntryController getInstance()
    {
        return controller_instance;
    }

    @FXML
    protected void initialize() throws IOException
    {
        /** Listener to detect out of focus textField to generate QR code  */
        publicKeyNewValue.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    //generateQR();
                }
            }
        });
        controller_instance = this;
    }

    public Map<String,String> collectData()
    {
        Map<String,String> data = new HashMap<String,String>();

        data.put("publicKey",publicKeyNewValue.getText());
        data.put("privateKey",privateKeyNewValue.getText());
        data.put("type","blockchainKeys");
        return  data;
    }

    /**Method used by EditEntryController to load data for edit */
    public void loadEditEntryUI(AnchorPane root, Map<String,Object> data) throws IOException
    {
        publicKeyNewValue.setText((String) data.get("publicKey"));
        privateKeyNewValue.setText((String) data.get("privateKey"));
    }
}
