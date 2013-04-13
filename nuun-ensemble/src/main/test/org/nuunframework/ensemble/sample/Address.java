package org.nuunframework.ensemble.sample;


public class Address
{
    
    private String line1;
    private String line2;
    private String town;
    private String country;
    private String code;
    
    Address()
    {        
    }
    
    public Address(String line1, String line2, String town, String country, String code)
    {
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.country = country;
        this.code = code;
    }

    public String getLine1()
    {
        return line1;
    }
    
    public String getLine2()
    {
        return line2;
    }
    
    public String getTown()
    {
        return town;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    
    public String getCode()
    {
        return code;
    }
    
}
