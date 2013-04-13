package org.nuunframework.ensemble.sample;

import java.util.Collection;

public class Person
{

    private String  firstname;
    private String  lastname;
    private Address address;
    private Person spouse;
    private Collection<Person> children;

    Person()
    {        
    }
    
    public Person(String firstname, String lastname, Address address)
    {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    
    public Person getSpouse()
    {
        return spouse;
    }

    
    public void setSpouse(Person spouse)
    {
        this.spouse = spouse;
    }

    
    public Collection<Person> getChildren()
    {
        return children;
    }

    
    public void setChildren(Collection<Person> children)
    {
        this.children = children;
    }
    
    
    

}
