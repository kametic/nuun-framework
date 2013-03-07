package org.nuunframework.spring.sample;

public class Service2Internal extends AbstractService2
{

    @Override
    public String serve2()
    {
        return Service2Internal.class.getName();
    }

}
