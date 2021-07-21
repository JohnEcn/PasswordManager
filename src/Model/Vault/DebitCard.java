package Model.Vault;

public class DebitCard extends Element{

    private long number;
    private short expireMonth;
    private short expireYear;
    private short ccv2;

    public DebitCard(String entryName, long number, short expireMonth, short expireYear, short ccv2)
    {
        setName(entryName);
        this.number = number;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.ccv2 = ccv2;
    }
}
