package org.nuunframework.kernel.commons;


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
    
    // CLASS
    public static boolean isClass(Class<? extends Object> klazz)
    {
        return !isInterface(klazz);
    }

    public static void assertIsClass(Class<? extends Object> klazz)
    {
        assertionIllegalArgument(isClass(klazz), "Type " + klazz + " must not be an interface.");
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
