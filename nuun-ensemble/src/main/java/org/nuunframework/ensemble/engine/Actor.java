package org.nuunframework.ensemble.engine;


public class Actor
{
    
    public final Role role;
    public Object instance;

    public Actor (Role role , Object instance)
    {
        this.role = role;
        this.instance = instance;
        
    }

}
