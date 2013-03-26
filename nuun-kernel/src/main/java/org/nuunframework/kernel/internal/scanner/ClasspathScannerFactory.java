package org.nuunframework.kernel.internal.scanner;

import java.net.URL;
import java.util.Set;


public class ClasspathScannerFactory
{
    public ClasspathScanner create(Set<URL> additionalClasspath , String... packageRoot)
    {
        ClasspathScannerInternal classpathScannerInternal = new ClasspathScannerInternal(packageRoot);
        classpathScannerInternal.setAdditionalClasspath(additionalClasspath);
        return classpathScannerInternal;
    }

    public ClasspathScanner create(String... packageRoot)
    {
        return new ClasspathScannerInternal(packageRoot);
    }

    public ClasspathScanner create( boolean reachAbstractClass ,  String packageRoot,String... packageRoots )
    {
        return new ClasspathScannerInternal(reachAbstractClass , packageRoot ,packageRoots);
    }

}
