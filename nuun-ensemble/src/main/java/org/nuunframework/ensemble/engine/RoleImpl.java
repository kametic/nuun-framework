package org.nuunframework.ensemble.engine;

public class RoleImpl implements Role
{
    
    private String name;

    public RoleImpl(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

}
