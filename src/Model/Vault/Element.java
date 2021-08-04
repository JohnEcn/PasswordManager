package Model.Vault;

import java.io.Serializable;

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

    public String toJson()
    {
        return "{}";
    }
}
