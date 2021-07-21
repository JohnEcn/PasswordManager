package Model.Vault;

public class WebCredentials extends  Element{

    private String username;
    private String email;
    private String password;
    private String url;

    public WebCredentials(String entryName, String username, String email, String password, String url)
    {
        setName(entryName);
        this.username = username;
        this.email = email;
        this.password = password;
        this.url = url;
    }

    //Set methods
    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    //Get methods
    public String getUsername()
    {
        return this.username;
    }
    public String getEmail()
    {
        return this.email;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getUrl()
    {
        return this.url;
    }

}
