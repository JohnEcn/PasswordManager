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

    //Set methods
    public void setNumber(long number)
    {
        this.number = number;
    }
    public void setExpireMonth(short expireMonth)
    {
        this.expireMonth = expireMonth;
    }
    public void setExpireYear(short expireYear)
    {
        this.expireYear = expireYear;
    }
    public void setCcv2(short ccv2)
    {
        this.ccv2 = ccv2;
    }

    //Get methods
    public long getNumber()
    {
        return this.number;
    }
    public short getExpireMonth()
    {
        return this.expireMonth;
    }
    public short getExpireYear()
    {
        return this.expireYear;
    }
    public short getCcv2()
    {
        return this.ccv2;
    }
}
