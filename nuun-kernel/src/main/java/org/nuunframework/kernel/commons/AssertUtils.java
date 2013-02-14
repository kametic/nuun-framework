package org.nuunframework.kernel.commons;

import java.lang.annotation.Annotation;


public class AssertUtils
{

    // INTERFACE
    public static boolean isInterface(Class<? extends Object> klazz)
    {
        return klazz.isInterface();
    }

    public static void assertInterface(Class<? extends Object> klazz)
    {
        assertionIllegalArgument(isInterface(klazz), "Type " + klazz + " must be an interface.");
    }
    
    // CLASS //
    public static boolean isClass(Class<? extends Object> klazz)
    {
        return !isInterface(klazz);
    }

    public static void assertIsClass(Class<? extends Object> klazz)
    {
        assertionIllegalArgument(isClass(klazz), "Type " + klazz + " must not be an interface.");
    }
    
    // ANNOTATION //
    
    
    
    
//    public static boolean hasAnnotationDeep(Field field , Class<? extends Annotation> klass)
//    {
//        if (field.isAnnotationPresent(klass)) 
//            return true;
//        else
//            return hasAnnotationDeep(field., klass)
//        
//    }
    public static boolean hasAnnotationDeep(Class<?> memberDeclaringClass , Class<? extends Annotation> klass) 
    {
        
        if (memberDeclaringClass.equals(klass) )
        {
            return true;
        }
        
        for ( Annotation anno :memberDeclaringClass.getAnnotations() )
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (! annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeep(annoClass, klass))
            {
                return true;
            }
        }
            
        
        return false;
    }

    public static void assertionIllegalArgument(boolean asserted, String message)
    {
        if (!asserted)
            throw new IllegalArgumentException(message);
    }

    public static void assertionNullPointer(boolean asserted, String message)
    {
        if (!asserted)
            throw new NullPointerException(message);
    }

    public static void assertLegal(Object underAssertion, String message)
    {
        assertionIllegalArgument(underAssertion != null, message);
    }

    public static void assertNotNull(Object underAssertion, String message)
    {
        assertionNullPointer(underAssertion != null, message);
    }

}
