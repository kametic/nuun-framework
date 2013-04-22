/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.kernel.commons;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})    
    static @interface AnnoFrom {
        String value ();
        String v1();
        long v2();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    
    static @interface AnnoFromClone {
        String value ();
        String v1();
        long v2();
    }

    static @interface AnnoFromClone2 {
        String value ();
        String v1();
        short v2();
    }

    static @interface AnnoFromClone3 {
        String value ();
        String v1();
        long v2();
        short v3();
        String v4();
    }
    
    static @interface AnnoFromClone4 {
        String value ();        
        long v2();
    }

    @AnnoFromClone(value="clone" , v1 = "clone2",v2 = 3l)
    static class Annoted  {  }
    
    @Test
    public void testAnno2 () throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        AnnoFromClone annoFromClone = Annoted.class.getAnnotation(AnnoFromClone.class);
        
        assertThat(annoFromClone.v1()).isEqualTo("clone2");
        
        Method v1 = annoFromClone.getClass().getMethod("v1");
        Object invoke = v1.invoke(annoFromClone);
        assertThat(invoke).isEqualTo("clone2");
        assertThat( annoFromClone.getClass().getDeclaredMethods() ).hasSize(7);
        
        AnnoFrom annoFrom = AssertUtils.annotationProxyOf(AnnoFrom.class, annoFromClone);
        assertThat(annoFrom.v1()).isEqualTo("clone2");
    
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone.class)).isTrue();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone2.class)).isFalse();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone3.class)).isTrue();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone4.class)).isFalse();
        
    }
    
    

}
