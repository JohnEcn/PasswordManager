package View.MainScene.NewEntry.EntryTypes;

import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Map;

/**
 *  This class is inherited by the entry type controllers
 *
 *  All subclasses must override the collectData() that is used for
 *  collecting the data during new entry addition and editing
 *
 *  All subclasses must override loadEditEntryUI() that displays the
 *  corresponding entry UI during entry editing *
*/

public abstract class EntryType {

    public abstract Map<String,String> collectData();

    public abstract void  loadEditEntryUI(AnchorPane root, Map<String,Object> data) throws IOException;
}
