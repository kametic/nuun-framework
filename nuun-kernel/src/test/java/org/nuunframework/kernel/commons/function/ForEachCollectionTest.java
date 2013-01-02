package org.nuunframework.kernel.commons.function;

import static org.fest.assertions.Assertions.assertThat;
import static org.nuunframework.kernel.commons.function.ForEachCollection.foreach;
import static org.nuunframework.kernel.commons.function.sample.resource.Blocks.print;
import static org.nuunframework.kernel.commons.function.sample.resource.Blocks.print_uppercase;
import static org.nuunframework.kernel.commons.function.sample.resource.Predicates.contains_letter;
import static org.nuunframework.kernel.commons.function.sample.resource.Predicates.ends_with;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.kernel.commons.function.sample.resource.Blocks;
import org.nuunframework.kernel.commons.function.sample.resource.Resource;

public class ForEachCollectionTest
{

    List<Resource> resources;

    @Before
    public void init()
    {
        resources = Arrays.asList( //
                resource("Jeanne Jemba", 100), //
                resource("Epo Jemba", 100), //
                resource("Adrien Lauer", 100), //
                resource("Emmanuel Vinel", 100), //
                resource("Marius Matéi", 100), //
                resource("Laurent Didier", 100), //
                resource("Yves Dautremay", 100) //
                );
    }

    @Test
    public void test()
    {
        // TODO rajouter select o dans foreach
        foreach(resources).select(contains_letter , "e")
           .forEachDo(ends_with , "Jemba", print_uppercase , print );
        // get(print_uppercase)
        // get(print)
        // getOtherwise()
        System.out.println("==================================");
        
        foreach(resources).select(contains_letter , "u").forEachDo(print);
        
        System.out.println("==================================");        
        foreach(resources).select(contains_letter , "y").forEachDo(print_uppercase);
        
        System.out.println("==================================");
        
        int number = foreach(resources).select().forEachDo(Blocks.count_elems).get();
        
        assertThat(number).isEqualTo(7);
        
    }

    private Resource resource(String name, Integer salary)
    {
        return new Resource(name, salary);
    }

}
