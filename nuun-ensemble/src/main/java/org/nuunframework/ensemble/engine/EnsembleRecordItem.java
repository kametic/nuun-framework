package org.nuunframework.ensemble.engine;

import java.lang.reflect.Method;

public class EnsembleRecordItem
{

    public final Method     method;
    public final String     roleName;
    public final Object[]   arguments;
    public final Class<?>   callerClass;
    public final Class<?>   methodReturnType;

    public EnsembleRecordItem(Class<?> callerClass, Method method, Class<?> methodReturnType, String roleName, Object[] arguments)
    {
        this.callerClass = callerClass;
        this.method = method;
        this.methodReturnType = methodReturnType;
        this.roleName = roleName;
        this.arguments = arguments;
    }

    @Override
    public String toString()
    {// @f:off
            return String.format("EnsembleRecordItem [\n" + 
            		            "     caller=%s, \n" +
            		             "    method=%s, \n" +
            		             "    method Ret Type=%s\n" +
            		             "    roleName=%s\n" +
            		             "    args=%s\n" +
            "]\n", callerClass.getSimpleName() , method, methodReturnType , roleName, display( arguments ));// @f:on
    }

    String display(Object... args)
    {
        StringBuilder b = new StringBuilder();
        for (Object arg : args)
        {
            b.append(arg).append(", ");
        }

        return b.toString();
    }

}
