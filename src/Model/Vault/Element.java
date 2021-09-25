package Model.Vault;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.Map;

public abstract class Element implements Serializable {

    private String entryName;
    private static final long serialVersionUID = -385061156853308922L;

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

    public String validateJson(String Json)
    {
        try
        {
            Gson gson = new Gson();
            gson.fromJson(Json, Map.class);
            return Json;
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
