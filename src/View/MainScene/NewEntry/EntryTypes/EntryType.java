package View.MainScene.NewEntry.EntryTypes;

import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Map;

public abstract class EntryType {

    public static EntryType getInstance()
    {
       return null;
    }
    public Map<String,String> collectData()
    {
       return null;
    }
    public void loadEditEntryUI(AnchorPane root, Map<String,Object> data) throws IOException {}
}
