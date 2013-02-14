package org.nuunframework.kernel.commons;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

public class AssertUtilsTest
{

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE})
    static @interface MetaAnno2 {}
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE})
    @MetaAnno2
    static @interface MetaAnno1 {}
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    @MetaAnno1
    static @interface Anno1 {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    @MetaAnno2
    static @interface Anno2 {}
    
    @Anno1
    static class Class1 {}

    @Anno2
    static class Class2 {}
    
    @Test
    public void testHasAnnotationDeep()
    {
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, MetaAnno2.class)).isTrue()  ;
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, MetaAnno1.class)).isTrue()  ;
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, Anno1.class)).isTrue()  ;

        assertThat(AssertUtils.hasAnnotationDeep(Class2.class, Anno2.class)).isTrue()  ;
        assertThat(AssertUtils.hasAnnotationDeep(Class2.class, MetaAnno2.class)).isTrue()  ;
        

        
    }

}
