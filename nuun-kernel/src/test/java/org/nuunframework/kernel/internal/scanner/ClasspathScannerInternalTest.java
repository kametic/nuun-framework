package org.nuunframework.kernel.internal.scanner;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.kernel.annotations.KernelModule;
import org.nuunframework.kernel.internal.scanner.ClasspathScanner.Callback;
import org.nuunframework.kernel.internal.scanner.ClasspathScanner.CallbackResources;
import org.nuunframework.kernel.internal.scanner.sample.Bean1;
import org.nuunframework.kernel.internal.scanner.sample.Bean2;
import org.nuunframework.kernel.internal.scanner.sample.Bean3;
import org.nuunframework.kernel.internal.scanner.sample.Bean6;
import org.nuunframework.kernel.internal.scanner.sample.ScanMarkerSample;
import org.nuunframework.kernel.internal.scanner.sample.ScanMarkerSample2;
import org.nuunframework.kernel.internal.scanner.sample.MyModule1;
import org.nuunframework.kernel.internal.scanner.sample.MyModule4;
import org.nuunframework.kernel.plugin.dummy7.Module7;




public class ClasspathScannerInternalTest
{
    
    private static ClasspathScannerInternal underTest , underTest2;

    static TestCallback cb;
    static TestCallbackResources cbr;
    
    @BeforeClass
    public static void init()
    {
        underTest = new ClasspathScannerInternal("","META-INF.properties,"+MyModule1.class.getPackage().getName());
        underTest2 = new ClasspathScannerInternal(true ,"", "META-INF.properties,"+MyModule1.class.getPackage().getName());
        
        cb = new TestCallback();
        cbr = new TestCallbackResources();
    }
    
    static class TestCallback implements Callback
    {
        public Collection<Class<?>> scanResult;

        @Override
        public void callback(Collection<Class<?>> scanResult)
        {
            this.scanResult = scanResult;
            
        }
    }
    static class TestCallbackResources implements CallbackResources
    {
        public Collection<String> scanResult;
        
        @Override
        public void callback(Collection<String> scanResult)
        {
            this.scanResult = scanResult;
            
        }
    }
    
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation ()
    {
        underTest.scanClasspathForAnnotation(ScanMarkerSample.class , cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
         
         assertThat(scanClasspathForAnnotation).isNotNull();
         assertThat(scanClasspathForAnnotation).hasSize(2);
         assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class);
    }
    
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation_name ()
    {
        underTest.scanClasspathForAnnotationRegex(".*ScanMarkerSample",cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation =cb.scanResult ; 
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(3);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class, Bean6.class);
    }

    @Test
    public void classpathscanner_should_retrieve_properties_tst ()
    {
//        underTest.setAdditionalClasspath( ClasspathHelper.forPackage("") );
        underTest.scanClasspathForResource("tst-.*\\.properties",cbr);
        underTest.doClasspathScan();
        Collection<String> scanClasspathForAnnotation = cbr.scanResult;
         
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
//	    assertThat(scanClasspathForAnnotation).containsOnly("tst-one.properties" , "tst-two.properties");
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype ()
    {
        underTest.scanClasspathForAnnotation( KernelModule.class ,cb);
        underTest.doClasspathScan();
        Collection<Class<? >> scanClasspathSubType = cb.scanResult;
        
        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(2);
        assertThat(scanClasspathSubType).containsOnly( MyModule1.class , Module7.class);
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype_abstract_included ()
    {
        underTest2.scanClasspathForAnnotation( KernelModule.class , cb);
        underTest2.doClasspathScan();
        Collection<Class<? >> scanClasspathSubType = cb.scanResult;
        
        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(3);
        assertThat(scanClasspathSubType).containsOnly( MyModule1.class , MyModule4.class , Module7.class);
    }
    
    @Test
    public void classpathscanner_should_ignore_Ignore_classtype_based ()
    {
        underTest.scanClasspathForAnnotation(ScanMarkerSample2.class,cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class );
    }

    @Test
    public void classpathscanner_should_ignore_Ignore_classnamed_based ()
    {
        underTest.scanClasspathForAnnotationRegex(".*MarkerSample2",cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
        
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
