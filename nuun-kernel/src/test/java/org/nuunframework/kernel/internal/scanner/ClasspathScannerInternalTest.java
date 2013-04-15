package org.nuunframework.kernel.internal.scanner;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.annotations.KernelModule;
import org.nuunframework.kernel.internal.scanner.ClasspathScannerInternal;
import org.nuunframework.kernel.internal.scanner.sample.Bean1;
import org.nuunframework.kernel.internal.scanner.sample.Bean2;
import org.nuunframework.kernel.internal.scanner.sample.Bean3;
import org.nuunframework.kernel.internal.scanner.sample.MarkerSample;
import org.nuunframework.kernel.internal.scanner.sample.MarkerSample2;
import org.nuunframework.kernel.internal.scanner.sample.MyModule1;
import org.nuunframework.kernel.internal.scanner.sample.MyModule4;
import org.reflections.util.ClasspathHelper;




public class ClasspathScannerInternalTest
{
    
    private static ClasspathScannerInternal underTest , underTest2;


    @BeforeClass
    public static void init()
    {
        
        
        underTest = new ClasspathScannerInternal(ClasspathScannerInternalTest.class.getPackage().getName());
        underTest2 = new ClasspathScannerInternal(true , ClasspathScannerInternalTest.class.getPackage().getName());
    }
    
    
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation ()
    {
         Collection<Class<?>> scanClasspathForAnnotation = underTest.scanClasspathForAnnotation(MarkerSample.class);
         
         assertThat(scanClasspathForAnnotation).isNotNull();
         assertThat(scanClasspathForAnnotation).hasSize(2);
         assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class);
    }
    
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation_name ()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanClasspathForAnnotationRegex(".*MarkerSample");
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class);
    }

    @Test
    public void classpathscanner_should_retrieve_properties_tst ()
    {
        underTest.setAdditionalClasspath( ClasspathHelper.forPackage("") );
        Collection<String> scanClasspathForAnnotation = underTest.scanClasspathForResource("tst-.*\\.properties");
        
	//        assertThat(scanClasspathForAnnotation).isNotNull();
	//        assertThat(scanClasspathForAnnotation).hasSize(2);
	//        assertThat(scanClasspathForAnnotation).containsOnly("tst-one.properties" , "tst-two.properties");
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype ()
    {
        Collection<Class<? >> scanClasspathSubType = underTest.scanClasspathForAnnotation( KernelModule.class );
        
        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(1);
        assertThat(scanClasspathSubType).containsOnly( MyModule1.class );
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype_abstract_included ()
    {
        Collection<Class<? >> scanClasspathSubType = underTest2.scanClasspathForAnnotation( KernelModule.class );
        
        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(2);
        assertThat(scanClasspathSubType).containsOnly( MyModule1.class , MyModule4.class );
    }
    
    @Test
    public void classpathscanner_should_ignore_Ignore_classtype_based ()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanClasspathForAnnotation(MarkerSample2.class);
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class );
    }

    @Test
    public void classpathscanner_should_ignore_Ignore_classnamed_based ()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanClasspathForAnnotationRegex(".*MarkerSample2");
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class );
    }
    
    @Test
    public void testAdd() {
     
        int i;
        int j;
        int result;
     
        Given: // Two numbers 1 and 4
        {
            i = 1;
            j = 4;
        }
     
        When: // I add the two numbers
        {
            result = i + j;
        }
     
        Then: // The result should be 5
        {
            assertThat(result).isEqualTo(5);
        }
     
    }

}
