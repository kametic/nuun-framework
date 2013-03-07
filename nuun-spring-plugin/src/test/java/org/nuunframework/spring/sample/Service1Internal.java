package org.nuunframework.spring.sample;

public class Service1Internal implements Service1
{

    @Override
    public String serve()
    {
        return this.getClass().getName();
    }

}
