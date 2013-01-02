package org.nuunframework.kernel.scanner;


public class ClasspathScannerFactory
{
    public ClasspathScanner create(String... packageRoot)
    {
        return new ClasspathScannerInternal(packageRoot);
    }

    public ClasspathScanner create( boolean reachAbstractClass ,  String packageRoot,String... packageRoots )
    {
        return new ClasspathScannerInternal(reachAbstractClass , packageRoot ,packageRoots);
    }

}
