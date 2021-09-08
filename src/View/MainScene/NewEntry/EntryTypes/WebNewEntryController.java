package View.MainScene.NewEntry.EntryTypes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebNewEntryController extends EntryType {

    @FXML private TextField newValue1; //email
    @FXML private TextField newValue2; //username
    @FXML private TextField newValue3; //password
    @FXML private TextField newValue4; //url

    private static WebNewEntryController controller_instance = null;
    public static WebNewEntryController getInstance()
    {
        return controller_instance;
    }

    @FXML
    protected void initialize() throws IOException
    {
        /** Listener to detect out of focus textField */
        newValue1.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    validateMail();
                }
            }
        });

        controller_instance = this;
    }
    public void validateMail()
    {
        if(!newValue1.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
        {
            newValue1.setStyle("-fx-background-color: #ffcccc;");
        }
        else
        {
            newValue1.setStyle("-fx-background-color: #d9d9d9;");
        }
    }
    public Map<String,String> collectData()
    {
        Map<String,String> data = new HashMap<String,String>();

        data.put("email",newValue1.getText());
        data.put("username",newValue2.getText());
        data.put("password",newValue3.getText());
        data.put("url",newValue4.getText());

        return  data;
    }
}
