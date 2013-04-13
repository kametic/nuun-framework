package org.nuunframework.ensemble.sample;

import java.util.Collection;
import java.util.Set;

public class PersonRepresentation
{

    private String      fullname;
    private String      fullAddress;
    private String      spouseName;
    private Set<String> childrenName;
    private String      childrenFirstName;

    public String getFullname()
    {
        return fullname;
    }

    public void fillFullname(String firstName, String lastName)
    {
        this.fullname = firstName + " " + lastName;
    }

    public String getSpousename()
    {
        return spouseName;
    }

    public void fillSpouseName(String firstName, String lastName)
    {
        this.spouseName = firstName + " " + lastName;
    }

    public String getChildrenFirstName()
    {
        return childrenFirstName;
    }

    public void fillChildrenFirstName(Collection<Person> children)
    {
        childrenFirstName = "";
        if (children != null)
            for (Person person : children)
            {
                childrenFirstName += person.getFirstname() + " ";
            }

        childrenFirstName.trim();
    }

    public void fillFullAddress(String line1, String line2, String town, String country, String code)
    {
        this.fullAddress = line1 + "\n" + line2 + "\n" + town + "\n" + country + "\n" + code;
    }

    public String getFullAddress()
    {
        return this.fullAddress;
    }

    @Override
    public String toString()
    {
        return String.format("PersonRepresentation [getFullname()=%s, getFullAddress()=%s,getSpousename()=%s,getChildrenFirstname()=%s]", getFullname(), getFullAddress() , getSpousename() , getChildrenFirstName());
    }

}
