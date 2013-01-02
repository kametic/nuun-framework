package org.nuunframework.kernel.commons.function.sample.resource;

public class Resource
{

    public final String  name;
    public final Integer salary;

    public Resource(String name, Integer salary)
    {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString()
    {
        return "Resource [name=" + name + ", salary=" + salary + "]";
    }
}