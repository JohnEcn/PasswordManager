package Model.Vault;

public class BlockchainKeys extends Element{

    private String publicKey;
    private String privateKey;

    public BlockchainKeys(String entryName, String publicKey, String privateKey)
    {
        setName(entryName);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    //Set methods
    public void setPublicKey(String publicKey)
    {
        this.publicKey = publicKey;
    }
    public void setPrivateKey(String privateKey)
    {
        this.privateKey = privateKey;
    }

    //Get methods
    public String getPublicKey()
    {
        return this.publicKey;
    }
    public String getPrivateKey()
    {
        return this.privateKey;
    }

    @Override
    public String toJson()
    {
        return "{ " +
                "\"type\": \"blockchainKeys\"" +
                ",\"name\": " + "\"" + this.getName() + "\"" +
                ",\"publicKey\": " + "\"" + this.publicKey + "\"" +
                ",\"privateKey\": " + "\"" + this.privateKey + "\"" +
                " }";
    }
}
