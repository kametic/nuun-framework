package org.nuunframework.kernel.commons.specification;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

public class AbstractSpecificationTest
{

    
    private S1 s1;
    private S2 s2;

    static class S1 extends AbstractSpecification<Object>
    {
        @Override
        public boolean isSatisfiedBy(Object candidate)
        {
            return false;
        }
    }
    
    static class S2 extends AbstractSpecification<Object>
    {
        @Override
        public boolean isSatisfiedBy(Object candidate)
        {
            return false;
        }
    }
    
    @Before
    public void init ()
    {
        s1 = new S1();
        s2 = new S2();
    }
    
    @Test
    public void testHashCode()
    {
        assertThat(s1.hashCode()).isNotEqualTo(s2.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertThat(s1.equals(s2)).isFalse();
    }

}
