package View.MainScene.NewEntry.EntryTypes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;

public class WebNewEntryController {

    @FXML
    TextField newValue1;

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
}
