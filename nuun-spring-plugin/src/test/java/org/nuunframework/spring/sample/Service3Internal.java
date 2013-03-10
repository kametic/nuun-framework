package org.nuunframework.spring.sample;

public class Service3Internal implements Service3
{

    @Override
    public String serve()
    {
        return this.getClass().getName();
    }

}
