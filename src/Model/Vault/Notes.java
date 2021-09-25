package Model.Vault;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notes extends Element{

    private String note;
    private String creationDate;

    public Notes(String entryName, String note)
    {
        setName(entryName);
        setCreationDate();
        this.note = note;
    }

    private void setCreationDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        this.creationDate = formatter.format(date);
    }

    @Override
    public String validate()
    {
        String validationStatus = "";

        if(note.equals(""))
        {
            validationStatus = "Note is empty!";
        }
        else
        {
            validationStatus ="OK";
        }
        return validationStatus;
    }

    @Override
    public String toJson()
    {
        String json =  "{ " +
                "\"type\": \"note\"" +
                ",\"name\": " + "\"" + this.getName() + "\"" +
                ",\"note\": " + "\"" + this.note + "\"" +
                ",\"creationDate\": " + "\"" + this.creationDate + "\"" +
                " }";

        return validateJson(json);
    }
}
