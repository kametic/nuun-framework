package org.nuunframework.kernel.commons.function;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class LoopInfoTest
{

    @Test
    public void testFirst()
    {
        int underTest = LoopInfo.FIRST;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    @Test
    public void testAntepenultimate()
    {
        int underTest = LoopInfo.ANTEPENULTIMATE;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    
    @Test
    public void testPenultimate()
    {
        int underTest = LoopInfo.PENULTIMATE;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    
    @Test
    public void testLast()
    {
        int underTest = LoopInfo.LAST;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isTrue();
    }

    @Test
    public void testFirstAndLast()
    {
        int underTest = 0;
        underTest |= LoopInfo.FIRST;
        underTest |= LoopInfo.LAST;
        

        Assertions.assertThat(LoopInfo.isFirst(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isTrue();
    }
    
    
    
    
    

}
