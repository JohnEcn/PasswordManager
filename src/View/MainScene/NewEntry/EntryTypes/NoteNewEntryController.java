package View.MainScene.NewEntry.EntryTypes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NoteNewEntryController extends EntryType{

    @FXML private TextArea notesValue;

    public  Map<String,String> collectData()
    {
        Map<String,String> noteData = new HashMap<>();
        String noteContent = notesValue.getText();
        noteData.put("type","note");
        noteData.put("note",noteContent);
        return noteData;
    }
    public  void  loadEditEntryUI(AnchorPane root, Map<String,Object> data) throws IOException
    {
        notesValue.setText((String) data.get("note"));
    }



}
