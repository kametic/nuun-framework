package org.nuunframework.kernel.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    // public static boolean hasAnnotationDeep(Field field , Class<? extends Annotation> klass)
    // {
    // if (field.isAnnotationPresent(klass))
    // return true;
    // else
    // return hasAnnotationDeep(field., klass)
    //
    // }
    public static boolean hasAnnotationDeep(Class<?> memberDeclaringClass, Class<? extends Annotation> klass)
    {

        if (memberDeclaringClass.equals(klass))
        {
            return true;
        }

        for (Annotation anno : memberDeclaringClass.getAnnotations())
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (!annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeep(annoClass, klass))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isEquivalent(Class<? extends Annotation> original, Class<? extends Annotation> copy)
    {
        for (Method originalMethod : original.getDeclaredMethods())
        {
            if (originalMethod.getParameterTypes().length == 0)
            {

                String name = originalMethod.getName();
                try
                {
                    Method cloneMethod = null;
                    if ((cloneMethod = copy.getDeclaredMethod(name)) != null)
                    {
                        if (originalMethod.getReturnType() != cloneMethod.getReturnType())
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (SecurityException e)
                {
                    return false;
                }
                catch (NoSuchMethodException e)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static <A extends Annotation> A annotationProxyOf(Class<A> annotationModelType, Annotation annotationClone)
    {
        return AnnotationCopy.of(annotationModelType, annotationClone);

    }

    public static class AnnotationCopy implements InvocationHandler
    {

        private Class<? extends Annotation> annotationModelType;
        private Annotation                  annotationClone;

        AnnotationCopy(Class<? extends Annotation> annotationModelType, Annotation annotationClone)
        {
            this.annotationModelType = annotationModelType;
            this.annotationClone = annotationClone;

        }

        @SuppressWarnings("unchecked")
        public static <A extends Annotation> A of(Class<A> annotationModelType, Annotation annotationClone)
        {
            return (A) Proxy.newProxyInstance(annotationModelType.getClassLoader(), new Class[] {
                annotationModelType
            }, new AnnotationCopy(annotationModelType, annotationClone));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            String name = method.getName();
            Method method2 = annotationClone.getClass().getMethod(name);

            return method2.invoke(annotationClone);
        }
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
