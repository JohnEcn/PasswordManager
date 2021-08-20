package Model.Vault;

import java.util.Calendar;

public class DebitCard extends Element{

    private long number;
    private short expireMonth;
    private short expireYear;
    private short ccv2;
    private String ownersName;

    public DebitCard(String entryName, long number, short expireMonth, short expireYear, short ccv2 , String ownersName)
    {
        setName(entryName);
        this.number = number;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.ccv2 = ccv2;
        this.ownersName = ownersName;
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
    public void setOwnersName(String ownersName )
    {
        this.ownersName = ownersName;
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
    public String getOwnersName()
    {
        return this.ownersName;
    }

    @Override
    public String validate()
    {
        String validationStatus = "";

        //Match a 16 digit number
        boolean number = String.valueOf(this.number).matches("(^[0-9]{16}$)");

        //Match a number between 1 and 12
        boolean expireM = String.valueOf(this.expireMonth).matches("(^0?[1-9]$)|(^1[0-2]$)");

        //Match a number between this year and 5 years in future (ex. 21-26)
        int year =  Calendar.getInstance().get(Calendar.YEAR) % 100;
        boolean expireY = this.expireYear >= year && this.expireYear <= year + 5;

        //Match a 1 to 3 digit number
        boolean ccv2 = String.valueOf(this.ccv2).matches("(^\\d{1,3}$)");

        //Match a valid name with a space between
        boolean ownersName = String.valueOf(this.ownersName).matches("^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)");

        //Construct the string by concatenating the present errors
        validationStatus = number && expireM && expireY && ccv2 && ownersName? "OK" : "";
        validationStatus += number ? ""  : "-Incorrect 16 digit number";
        validationStatus += expireM ? "" : "-Incorrect expire month";
        validationStatus += expireY ? "" : "-Incorrect expire year";
        validationStatus += ccv2 ? ""    : "-Incorrect 3 digit ccv2";
        validationStatus += ownersName ? ""    : "-Invalid owner's name";

        return validationStatus;
    }

    @Override
    public String toJson()
    {
        return "{ " +
                "\"type\": \"DebitCard\"" +
                ", \"name\": " + "\"" + this.getName() + "\"" +
                ", \"number\": " +"\"" +  this.number+ "\""  +
                ", \"expireMonth\": " +"\"" +   this.expireMonth + "\""  +
                ", \"expireYear\": " +"\"" +   this.expireYear + "\""  +
                ", \"ccv2\":" + "\"" +   this.ccv2 + "\""  +
                ", \"ownersName\":"+  this.ownersName +
                " }";
    }
}
