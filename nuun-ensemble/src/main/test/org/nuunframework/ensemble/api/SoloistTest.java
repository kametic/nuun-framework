package org.nuunframework.ensemble.api;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.nuunframework.ensemble.Ensemble;
import org.nuunframework.ensemble.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SoloistTest
{
    
    Logger logger = LoggerFactory.getLogger(SoloistTest.class);
    
    
    @Test
    public void internal_stack_should_be_filled () throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Swap1 swap1 = new Swap1();
        
        Method methodForConfigure = Types.methodFor(Ensemble.class, "play");        
        methodForConfigure.setAccessible(true);        
        methodForConfigure.invoke(swap1);

        logger.info("" + swap1.getRecording() );
        
        assertThat(swap1.getRecording()).hasSize(7);
    }

    
    
    
    
    static abstract class Swap<R> extends Soloist<R>
    {
        
        protected R swaper()
        {
            return actor1();
        }
        
    }

    static class Swap1 extends Swap<Swappable>
    {
        @Override
        protected void play()
        {
            swaper().setTmp(actor1().getA());
            swaper().setA(actor1().getB());
            swaper().setB(actor1().getTmp());
            swaper().setTmp(null);
        }
    }

//    static class Swap2 extends Swap
//    {
//        @Override
//        protected void play()
//        {
//            actor1().setTmp(actor1().getA());
//            actor1().setA(actor1().getB());
//            actor1().setB(actor1().getTmp());
//            actor1().setTmp(null);
//        }
//    }
    
    static class Swappable 
    {
        protected String a;
        protected String b;
        protected String tmp;
        
        public String getA()
        {
            return a;
        }
        public void setA(String a)
        {
            this.a = a;
        }
        public String getB()
        {
            return b;
        }
        public void setB(String b)
        {
            this.b = b;
        }
        public String getTmp()
        {
            return tmp;
        }
        public void setTmp(String tmp)
        {
            this.tmp = tmp;
        }
        
    }
    
//    static class SuperSwappable extends Swappable
//    {
//        private String tmp2;
//
//        public String getTmp2()
//        {
//            return tmp2;
//        }
//
//        public void setTmp2(String tmp2)
//        {
//            this.tmp2 = tmp2;
//        }
//        
//    }

}
