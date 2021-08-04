package Model.Vault;

import java.io.Serializable;
import java.util.regex.*;
import java.util.Calendar;

public abstract class Element implements Serializable {

    private String entryName;

    public void setName(String name)
    {
        this.entryName = name;
    }

    public String getName()
    {
        return this.entryName;
    }

    public String validate()
    {
        return "OK";
    }

}
