package org.nuunframework.kernel.commons.function.sample.resource;

import org.nuunframework.kernel.commons.function.Block;
import org.nuunframework.kernel.commons.function.BlockContext;
import org.nuunframework.kernel.commons.function.LoopInfo;

public enum Blocks implements Block<Resource> 
{
    print
    {
        public void evaluate(BlockContext context,Resource resource)
        {
            System.out.println("Name = " + resource.name);
            System.out.println("Salary = " + resource.salary);
        }

    }
    ,
    print_uppercase
    {
        public void evaluate(BlockContext context,Resource resource)
        {
            System.out.println("Name = " + resource.name.toUpperCase());
            System.out.println("Salary = " + resource.salary);
        }
    }
    ,
    count_elems
    {
        public void evaluate(BlockContext context ,Resource resource)
        {
            
            if (LoopInfo.isFirst(context.loopInfo()))
            {
                context.map().put(this, 1);
            }
            else
            {
                int count = (Integer) context.map().get(this);
                count++;
                context.map().put(this, count);
                
            }
            System.out.println(  context.map().get("count") + " > " + this  + " "  + resource + " "+ LoopInfo.toString( context.loopInfo()));
        }
    }
    
    
    
    
}